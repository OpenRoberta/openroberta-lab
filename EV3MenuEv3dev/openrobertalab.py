#!/usr/bin/env python
import atexit
import dbus
import dbus.service
import fcntl
import gobject
import json
import logging
import os
import random
import socket
import struct
import subprocess
import sys
import time
import thread
import threading
import urllib2
from dbus.mainloop.glib import DBusGMainLoop
from roberta.ev3 import Hal
from roberta.__version__ import version

logging.basicConfig(level=logging.DEBUG)

logger = logging.getLogger('openroberta-service')
gobject.threads_init()

# helpers
def getHwAddr(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    info = fcntl.ioctl(s.fileno(), 0x8927,  struct.pack('256s', ifname[:15]))
    return ':'.join(['%02x' % ord(char) for char in info[18:24]])

def generateToken():
    chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    return ''.join(random.choice(chars) for _ in range(8))

def getBatteryVoltage():
    with open('/sys/devices/platform/legoev3-battery/power_supply/legoev3-battery/voltage_now', 'r') as bv:
        return "{0:.3f}".format(float(bv.read()) / 1000000.0)


class Service(dbus.service.Object):
    """OpenRobertab-Lab dbus service
    
    The status state machines is a follows:
    
    +-> disconnected
    |   |
    |    v
    +- connected
    |    |
    |    v
    +- registered
    |    ^
    |    v
    +- executing
    
    """

    def __init__(self, path):
        # needs /etc/dbus-1/system.d/openroberta.conf
        bus_name = dbus.service.BusName('org.openroberta.lab', bus=dbus.SystemBus())
        dbus.service.Object.__init__(self, bus_name, path)
        logger.info('object registered')
        self.status('disconnected')
        self.hal = Hal(None, None)
        self.hal.clearDisplay()
        self.thread = None

    @dbus.service.method('org.openroberta.lab', in_signature='s', out_signature='s')
    def connect(self, address):
        logger.info('connect(%s)' % address)
        if self.thread:
            logger.info('disconnect() old thread')
            self.thread.running = False;
        # start thread, connecting to address
        self.thread = Connector(address, self)
        self.thread.daemon = True
        self.thread.start()
        self.status('connected')
        return self.thread.params['token']

    @dbus.service.method('org.openroberta.lab')
    def disconnect(self):
        logger.info('disconnect()')
        self.thread.running = False
        self.status('disconnected')
        # end thread, can take up to 15 seconds (the timeout to return)
        # hence we don't join(), when connecting again we create a new thread
        # anyway
        #self.thread.join()
        #self.status('disconnected')
        self.thread = None

    @dbus.service.signal('org.openroberta.lab', signature='s')
    def status(self, status):
        logger.info('status changed: %s' % status)

class HardAbort(threading.Thread):
    """ Test for a 10s back key press and terminate the daemon"""

    def __init__(self, service):
        threading.Thread.__init__(self)
        self.service = service
        self.running = True
        self.long_press = 0

    def run(self):
        while self.running:
            if self.service.hal.isKeyPressed('back'):
                logger.info('back: %d', self.long_press)
                # if pressed for one sec, hard exit
                if self.long_press > 10:
                    logger.info('--- hard abort ---')
                    thread.interrupt_main()
                    self.running = False
                else:
                    self.long_press += 1
            else:
                self.long_press = 0
            time.sleep(0.1)

class Connector(threading.Thread):
    """OpenRobertab-Lab network IO thread"""

    def __init__(self, address, service):
        threading.Thread.__init__(self)
        self.address = address
        self.service = service
        self.params = {
          'macaddr': '00:00:00:00:00:00',
          'firmwarename': 'ev3dev',
          'menuversion': version.split('-')[0],
        }
        self.updateConfiguration();
        
        self.registered = False
        self.running = True
        logger.info('thread created')

    def updateConfiguration(self):
        # or /etc/os-release
        with open('/proc/version', 'r') as ver:
            self.params['firmwareversion'] = ver.read()

        for iface in [b'wlan', b'usb', b'eth']:
            for ix in range(10):
                try:
                    self.params['macaddr'] = getHwAddr(iface + str(ix))
                    break
                except IOError:
                    pass
        self.params['token'] = generateToken()
    
    def run(self):
        logger.info('network thread started')
        # network related locals
        headers = {
          'Content-Type': 'application/json'
        }
        timeout = 15 # seconds
        
        logger.info('target: %s' % self.address)
        while self.running:
            if self.registered:
                self.params['cmd'] = 'push'
                timeout = 15
            else:
                self.params['cmd'] = 'register'
                timeout = 330
            self.params['brickname'] = socket.gethostname()
            self.params['battery'] = getBatteryVoltage()

            try:
                # TODO: what about /api/v1/pushcmd
                logger.info('sending: %s' % self.params['cmd'])
                req = urllib2.Request('%s/pushcmd' % self.address, headers=headers)
                response = urllib2.urlopen(req, json.dumps(self.params), timeout=timeout)
                reply = json.loads(response.read())
                logger.info('response: %s' % json.dumps(reply))
                cmd = reply['cmd']
                if cmd == 'repeat':
                    if not self.registered:
                        self.service.status('registered')
                        self.service.hal.playFile(2)
                    self.registered = True
                elif cmd == 'abort':
                    break
                elif cmd == 'download':
                    self.service.hal.clearDisplay()
                    self.service.status('executing')
                    # TODO: url is not part of reply :/
                    # TODO: we should receive a digest for the download (md5sum) so that
                    #   we can verify the download
                    req = urllib2.Request('%s/download' % self.address, headers=headers)
                    response = urllib2.urlopen(req, json.dumps(self.params), timeout=timeout)
                    logger.info('response: %s' % json.dumps(reply))
                    hdr = response.info().getheader('Content-Disposition')
                    # TODO: save to /home/user
                    filename = '/tmp/%s' % hdr.split('=')[1] if hdr else 'unknown'
                    with open(filename, 'w') as prog:
                        prog.write(response.read().decode('utf-8'))
                    logger.info('code downloaded to: %s' % filename)
                    # new process
                    #res = subprocess.call(["python", filename], env={"PYTHONPATH":"$PYTONPATH:."})
                    #logger.info('execution result: %d' % res)
                    # eval from file, see http://bugs.python.org/issue14049
                    # NOTE: all the globals in the generated code will override gloabls we use here!
                    # NOTE: we don't have to keep pinging the server while running
                    # the code - robot is busy until we send push request again
                    # it would be nice though if we could cancel the running program
                    with open(filename) as f:
                        try:
                            code = compile(f.read(), filename, 'exec')
                            hard_abort = HardAbort(self.service)
                            hard_abort.daemon = True
                            hard_abort.start()
                            exec(code, globals(), globals())
                            hard_abort.running = False
                            logger.info('execution finished')
                        except:
                            logger.exception("Ooops:")
                        self.service.hal.clearDisplay()
                        self.service.hal.stopAllMotors()
                    self.service.status('registered')
                elif cmd == 'update':
                    # FIXME:
                    # fetch new files (menu/hal)
                    # then restart:
                    # os.execv(__file__, sys.argv)
                    # check if we need to close files (logger?)
                    pass
                else:
                    logger.warning('unhandled command: %s' % cmd)
            except urllib2.HTTPError as e:
                # [Errno 111] Connection refused>
                logger.error("HTTPError(%s): %s" % (e.code, e.reason))
                break
            except urllib2.URLError as e:
                # [Errno 111] Connection refused>
                logger.error("URLError: %s" % e.reason)
                break
            except socket.timeout:
                pass
            except:
                logger.exception("Ooops:")
        logger.info('network thread stopped')
        # don't play if we we just canceled a registration
        if self.registered:
            self.service.hal.playFile(3)

service = None

def cleanup():
    if service:
        service.hal.clearDisplay()
        service.hal.stopAllMotors()
    os.system('setterm -cursor on')
    logger.info('--- done ---')
    logging.shutdown()
    
def main():
    logger.info('--- starting ---')
    logger.info('running on tty: %s' % os.ttyname(sys.stdin.fileno()))   
    os.system('setterm -cursor off')
    
    atexit.register(cleanup)

    DBusGMainLoop(set_as_default=True)
    loop = gobject.MainLoop()
    service = Service('/org/openroberta/Lab1')
    logger.info('loop running')
    loop.run()

if __name__ == "__main__":
    main()

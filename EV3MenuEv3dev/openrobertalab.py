#!/usr/bin/env python
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
        # start thread, connecting to address
        self.thread = Connector(address, self)
        self.thread.daemon = True
        self.thread.start()
        self.status('connected')
        return self.thread.params['token']

    @dbus.service.method('org.openroberta.lab')
    def disconnect(self):
        logger.info('disconnect()')
        # end thread, can take up to 15 seconds (the timeout to return)
        self.thread.running = False;
        self.thread.join()
        self.thread = None
        self.status('disconnected')

    @dbus.service.signal('org.openroberta.lab', signature='s')
    def status(self, status):
        logger.info('status changed: %s' % status)

class Connector(threading.Thread):
    """OpenRobertab-Lab network IO thread"""

    def __init__(self, address, service):
        threading.Thread.__init__(self)
        self.address = address
        self.service = service
        self.params = {
          'macaddr': '70:1e:bb:88:89:bc',
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
    
        for iface in [b'wlan0', b'eth0']:
            try:
                self.params['macaddr'] = getHwAddr(iface)
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
                    self.service.status('registered')
                    if not self.registered:
                        self.service.hal.playFile(2)
                    self.registered = True
                elif cmd == 'abort':
                    break
                elif cmd == 'download':
                    self.service.hal.clearDisplay()
                    self.service.status('executing ...')
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
                    with open(filename) as f:
                        try:
                            code = compile(f.read(), filename, 'exec')
                            exec(code, globals(), globals())
                            logger.info('execution finished')
                            self.service.hal.clearDisplay()
                        except:
                            logger.exception("Ooops:")
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
        self.service.status('disconnected')
        self.service.hal.playFile(3)


def main():
    logger.info('--- starting ---')
    logger.info('running on tty: %s' % os.ttyname(sys.stdin.fileno()))   
    os.system('setterm -cursor off')

    DBusGMainLoop(set_as_default=True)
    loop = gobject.MainLoop()
    service = Service('/org/openroberta/Lab1')
    logger.info('loop running')
    loop.run()

    os.system('setterm -cursor on')
    logger.info('--- done ---')
    logging.shutdown()

if __name__ == "__main__":
    main()

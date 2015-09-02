#!/usr/bin/python

import fcntl
import json
import logging
import os
import random
import socket
import struct
import subprocess
import sys
import urllib2

# add cwd for hal
sys.path.append(os.getcwd())
from roberta.ev3 import Hal

logging.basicConfig(filename='/var/log/robertalab.log', level=logging.DEBUG)

def getHwAddr(ifname):
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    info = fcntl.ioctl(s.fileno(), 0x8927,  struct.pack('256s', ifname[:15]))
    return ':'.join(['%02x' % ord(char) for char in info[18:24]])

def generateToken():
    chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    return ''.join(random.choice(chars) for _ in range(8))

def updateConfiguration(params):
    # or /etc/os-release
    with open('/proc/version', 'r') as ver:
        params['firmwareversion'] = ver.read()

    for iface in ['wlan0', 'eth0']:
        try:
            params['macaddr'] = getHwAddr(iface)
            break
        except IOError:
            pass
    params['token'] = generateToken()

def getBatteryVoltage():
    with open('/sys/devices/platform/legoev3-battery/power_supply/legoev3-battery/voltage_now', 'r') as bv:
        return "{0:.3f}".format(float(bv.read()) / 1000000.0)

def drawUI(hal, params):
    hal.clearDisplay()
    hal.drawText('Token: %s' % params['token'], 0, 0, hal.font_x)
    hal.drawText('press and hold "back" to exit', 0, 9)


def main():
    logger = logging.getLogger('robertalab')
    logger.info('--- starting ---');
    params = {
      'macaddr': '70:1e:bb:88:89:bc',
      'firmwarename': 'ev3dev',
      'menuversion': '1.2.0',         # FIXME: take from build
    }
    headers = {
      'Content-Type': 'application/json'
    }

    try:
        with open('.robertalab.json') as cfg_file:
            cfg = json.load(cfg_file)
    except IOError as e:
        cfg = {
          'target': 'http://lab.open-roberta.org/', # address and port of server
        }

    os.system('setterm -cursor off');

    hal = Hal(None, None)
    updateConfiguration(params)
    drawUI(hal, params)

    registered = False
    while not hal.isKeyPressed('back'):
        try:
            if registered:
                params['cmd'] = 'push'
                timeout = 15
            else:
                params['cmd'] = 'register'
                timeout = 330
            params['brickname'] = socket.gethostname()
            params['battery'] = getBatteryVoltage()

            # TODO: what about /api/v1/pushcmd
            logger.info('sending: %s' % params['cmd'])
            req = urllib2.Request('%s/pushcmd' % cfg['target'], headers=headers)
            response = urllib2.urlopen(req, json.dumps(params), timeout=timeout)
            reply = json.loads(response.read())
            logger.info('response: %s' % json.dumps(reply))
            cmd = reply['cmd']
            if cmd == 'repeat':
                hal.drawText('registered', 0, 2)
                if not registered: 
                    hal.playFile(2);
                registered = True
            elif cmd == 'abort':
                break
            elif cmd == 'download':
                hal.drawText('executing ...', 0, 3)
                # TODO: url is not part of reply :/
                # TODO: we should receive a digest for the download (md5sum) so that
                #   we can verify the download
                req = urllib2.Request('%s/download' % cfg['target'], headers=headers)
                response = urllib2.urlopen(req, json.dumps(params), timeout=timeout)
                logger.info('response: %s' % json.dumps(reply))
                hdr = response.info().getheader('Content-Disposition');
                filename = '/tmp/%s' % hdr.split('=')[1] if hdr else 'unknown'
                with open(filename, 'w') as prog:
                    prog.write(response.read())
                logger.info('code downloaded to: %s' % filename)
                # new process
                #res = subprocess.call(["python", filename], env={"PYTHONPATH":"$PYTONPATH:."})
                #logger.info('execution result: %d' % res)
                # eval from file, see http://bugs.python.org/issue14049
                # NOTE: all the globals in the generated code will override gloabls we use here!
                try:
                    execfile(filename, globals(), globals())
                    logger.info('execution finished')
                except:
                    logger.exception("Ooops:")
                drawUI(hal, params)
                hal.drawText('registered', 0, 2)
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
            break;
        except socket.timeout:
            pass
        except:
            logger.exception("Ooops:")
    os.system('setterm -cursor on');
    logger.info('--- done ---');

if __name__ == "__main__":
    main()

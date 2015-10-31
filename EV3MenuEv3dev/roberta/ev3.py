from __future__ import absolute_import
from PIL import Image,ImageDraw,ImageFont
from bluetooth import *
import dbus
import ev3dev
import glob
import logging
import math
import os
import random
import time

from roberta.BlocklyMethods import BlocklyMethods
from roberta.StaticData import IMAGES

logger = logging.getLogger('ev3')


class Hal(object):

    def __init__(self, brickConfiguration, usedSensors):
        self.cfg = brickConfiguration
        self.usedSensors = usedSensors
        dir = os.path.dirname(__file__)
        self.font_s = ImageFont.load(os.path.join(dir, 'ter-u12n_unicode.pil'))
        self.font_x = ImageFont.load(os.path.join(dir, 'ter-u18n_unicode.pil'))
        self.lcd = ev3dev.LCD()
        self.led = ev3dev.led
        self.key = ev3dev.button
        self.sound = ev3dev.sound
        (self.font_w, self.font_h)=self.lcd.draw.textsize('X', font=self.font_s)
        self.timers = {}
        self.images = {}
        self.sys_bus = None
        self.bt_server = None
        self.bt_connections = []

    @staticmethod
    def makeLargeMotor(port, regulated, direction, side):
        m = ev3dev.large_motor(port)
        m.speed_regulation_enabled = regulated
        if direction is 'backward':
            m.polarity = 'inversed'
        else:
            m.polarity = 'normal'
        m.cfg_side = side
        m.last_position = m.position
        return m

    @staticmethod
    def makeMediumMotor(port, regulated, direction, side):
        m = ev3dev.medium_motor(port)
        m.speed_regulation_enabled = regulated
        if direction is 'backward':
            m.polarity = 'inversed'
        else:
            m.polarity = 'normal'
        m.cfg_side = side
        m.last_position = m.position
        return m

    # control
    def waitFor(self, ms):
        time.sleep (ms / 1000.0)

    def busyWait(self):
        '''Used as interrupptible busy wait.'''
        time.sleep (0.0)

    # lcd
    def drawText(self, msg, x, y, font=None):
        font = font or self.font_s
        self.lcd.draw.text((x*self.font_w, y*self.font_h), msg, font=font)
        self.lcd.update()

    def drawPicture(self,picture,x,y):
        if not picture in self.images:
            self.images[picture]=Image.frombytes('1',(178,128),
                IMAGES[picture], "raw", '1;IR', 0, 1)
        self.lcd.img.paste(self.images[picture], (x,y))
        self.lcd.update()

    def clearDisplay(self):
        self.lcd.clear()
        self.lcd.update()

    # led
    def ledOn(self, color, mode):
        # color: green, red, orange - LED.COLOR.{RED,GREEN,AMBER}
        # mode: on, flash, double_flash
        if mode is 'on':
            if color is 'green':
                self.led.green_on()
            elif color is 'red':
                self.led.red_on()
            elif color is 'orange':
                self.led.orange_on()

            # TODO: we also have amber_on(), yellow_on() and
            #                    mix_colors(float red, float green)
        elif mode in ['flash', 'double_flash']:
            wait_time = 1000
            if mode is 'double_flash':
                wait_time = 2000
            if color in ['green', 'orange']:
                self.led.green_left.flash(500,500)
                self.led.green_right.flash(500,500)
                self.waitFor(wait_time)
                self.ledOff()
            if color in ['red', 'orange']:
                self.led.red_left.flash(500,500)
                self.led.red_right.flash(500,500)
                self.waitFor(wait_time)
                self.ledOff()

    def ledOff(self):
        self.led.all_off()

    def resetLED(self):
        self.lefOff();

    # key
    def isKeyPressed(self, key):
        if key in ['any', '*']:
            # TODO: https://github.com/ev3dev/ev3dev-lang/issues/108
            for key in ['up', 'down', 'left', 'right', 'enter', 'back']:
                if getattr(self.key, key).pressed:
                  return True
            else:
                return False
        else:
            # remap some keys
            keys = {
              'escape':  'back',
              'backspace': 'back',
            }
            if key in keys:
                key = keys[key]
            # throws attribute error on wrong keys
            return getattr(self.key, key).pressed

    def isKeyPressedAndReleased(self, key):
        return False

    # tones
    def playTone(self, frequency, duration):
        frequency = frequency if frequency >= 100 else 0
        self.sound.tone(frequency, duration)

    def playFile(self,systemSound):
        # systemSound is a enum for preset beeps:
        # http://www.lejos.org/ev3/docs/lejos/hardware/Audio.html#systemSound-int-
        # https://sourceforge.net/p/lejos/ev3/code/ci/master/tree/ev3classes/src/lejos/remote/nxt/RemoteNXTAudio.java#l20
        C2=523
        if systemSound == 0:
            self.playTone(600, 200)
        elif systemSound == 1:
            self.playTone(600, 150)
            self.waitFor(200)
            self.playTone(600, 150)
            self.waitFor(150)
        elif systemSound == 2: # C major arpeggio
            for i in range(4, 7):
                self.playTone(C2 * i / 4, 100);
                self.waitFor(100);
        elif systemSound == 3:
            for i in range(7, 4, -1):
                self.playTone(C2 * i / 4, 100);
                self.waitFor(100);
        elif systemSound == 4:
            self.playTone(100, 500);
            self.waitFor(500);

    def setVolume(self, volume):
        self.sound.volume = volume

    def getVolume(self):
        return self.sound.volume

    # actors
    # http://www.ev3dev.org/docs/drivers/tacho-motor-class/
    def rotateRegulatedMotor(self, port, speed_pct, mode, value):
        # mode: degree, rotations, distance
        speed_pct *= 10.0
        m = self.cfg['actors'][port]
        if mode is 'degree':
            m.run_to_rel_pos(speed_regulation_enabled='on', position_sp=value, speed_sp=int(speed_pct))
            while (m.state):
                self.busyWait()
        elif mode is 'rotations':
            value *= m.count_per_rot
            m.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(value), speed_sp=int(speed_pct))
            while (m.state):
                self.busyWait()

    def rotateUnregulatedMotor(self, port, speed_pct, mode, value):
        speed_pct *= 10.0
        m = self.cfg['actors'][port];
        if mode is 'rotations':
            value *= m.count_per_rot
        if speed_pct >= 0:
            value = m.position + value
            m.run_forever(speed_regulation_enabled='off', duty_cycle_sp=int(speed_pct))
            while (m.position < value):
                self.busyWait()
        else:
            value = m.position - value
            m.run_forever(speed_regulation_enabled='off', duty_cycle_sp=int(speed_pct))
            while (m.position > value):
                self.busyWait()
        m.stop()

    def turnOnRegulatedMotor(self, port, value):
        value *= 10.0
        self.cfg['actors'][port].run_forever(speed_regulation_enabled='on', speed_sp=int(value))

    def turnOnUnregulatedMotor(self, port, value):
        value *= 10.0
        self.cfg['actors'][port].run_forever(speed_regulation_enabled='off', duty_cycle_sp=int(value))

    def setRegulatedMotorSpeed(self, port, power):
        self.cfg['actors'][port].speed_sp = power * 10.0

    def setUnregulatedMotorSpeed(self, port, power):
        self.cfg['actors'][port].duty_cycle_sp = power * 10.0

    def getRegulatedMotorSpeed(self, port):
        return self.cfg['actors'][port].speed / 10.0

    def getUnregulatedMotorSpeed(self, port):
        return self.cfg['actors'][port].duty_cycle  / 10.0

    def stopMotor(self, port, mode='float'):
        # mode: float, nonfloat
        # stop_commands: ['brake', 'coast', 'hold']
        m = self.cfg['actors'][port];
        if mode is 'float':
            m.stop_command='coast'
        elif mode is 'nonfloat':
            m.stop_command='brake'
        self.cfg['actors'][port].stop()

    def stopMotors(self, left_port, right_port):
        self.stopMotor(left_port)
        self.stopMotor(right_port)

    def stopAllMotors(self):
        #[m for m in [Motor(port) for port in ['outA', 'outB', 'outC', 'outD']] if m.connected]
        for file in glob.glob('/sys/class/tacho-motor/motor*/command'):
            with open(file, 'w') as f:
                f.write('stop')

    def regulatedDrive(self, left_port, right_port, reverse, direction, speed_pct):
        # direction: forward, backward
        # reverse: always false for now
        speed_pct *= 10.0
        if direction is 'backward':
            speed_pct = -speed_pct
        self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=int(speed_pct))
        self.cfg['actors'][right_port].run_forever(speed_regulation_enabled='on', speed_sp=int(speed_pct))

    def driveDistance(self, left_port, right_port, reverse, direction, speed_pct, distance):
        # direction: forward, backward
        # reverse: always false for now
        speed_pct *= 10.0
        ml = self.cfg['actors'][left_port];
        mr = self.cfg['actors'][right_port];
        circ = math.pi * self.cfg['wheel-diameter']
        dc = distance / circ
        if direction is 'backward':
            dc = -dc
        ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * ml.count_per_rot), speed_sp=int(speed_pct))
        mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * mr.count_per_rot), speed_sp=int(speed_pct))
        logger.info("driving: %s, %s" % (ml.state,mr.state))
        while (ml.state or mr.state):
            self.busyWait()

    def rotateDirectionRegulated(self, left_port, right_port, reverse, direction, speed_pct):
        # direction: left, right
        # reverse: always false for now
        speed_pct *= 10.0
        if direction is 'left':
            self.cfg['actors'][right_port].run_forever(speed_regulation_enabled='on', speed_sp=int(speed_pct))
            self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=int(-speed_pct))
        else:
            self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=int(speed_pct))
            self.cfg['actors'][right_port].run_forever(speed_regulation_enabled='on', speed_sp=int(-speed_pct))

    def rotateDirectionAngle(self, left_port, right_port, reverse, direction, speed_pct, angle):
        # direction: left, right
        # reverse: always false for now
        speed_pct *= 10.0
        ml = self.cfg['actors'][left_port];
        mr = self.cfg['actors'][right_port];
        circ = math.pi * self.cfg['track-width']
        distance = angle * circ / 360.0
        circ = math.pi * self.cfg['wheel-diameter']
        dc = distance / circ
        logger.info("doing %lf rotations" % dc)
        if direction is 'left':
            mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * mr.count_per_rot), speed_sp=int(speed_pct))
            ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(-dc * ml.count_per_rot), speed_sp=int(speed_pct))
        else:
            ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * ml.count_per_rot), speed_sp=int(speed_pct))
            mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(-dc * mr.count_per_rot), speed_sp=int(speed_pct))
        logger.info("turning: %s, %s" % (ml.state,mr.state))
        while (ml.state or mr.state):
            self.busyWait()

    # sensors
    # touch sensor
    def isPressed(self, port):
        return self.cfg['sensors'][port].value()

    # ultrasonic sensor
    def getUltraSonicSensorDistance(self, port):
        self.cfg['sensors'][port].mode='US-DIST-CM'
        return self.cfg['sensors'][port].value()

    def getUltraSonicSensorPresence(self, port):
        self.cfg['sensors'][port].mode='US-SI-CM'
        return self.cfg['sensors'][port].value()

    # gyro
    # http://www.ev3dev.org/docs/sensors/lego-ev3-gyro-sensor/
    def resetGyroSensor(self, port):
        # change mode to reset for GYRO-ANG and GYRO-G&A
        self.cfg['sensors'][port].mode='GYRO-RATE'
        self.cfg['sensors'][port].mode='GYRO-ANG'

    def getGyroSensorValue(self, port, mode):
        # mode = rate, angle
        if mode is 'angle':
            self.cfg['sensors'][port].mode='GYRO-ANG'
        elif mode is 'rate':
            self.cfg['sensors'][port].mode='GYRO-RATE'
        return self.cfg['sensors'][port].value()
        pass

    # color
    # http://www.ev3dev.org/docs/sensors/lego-ev3-color-sensor/
    def getColorSensorAmbient(self, port):
        self.cfg['sensors'][port].mode='COL-AMBIENT'
        return self.cfg['sensors'][port].value()

    def getColorSensorColour(self, port):
        self.cfg['sensors'][port].mode='COL-COLOR'
        return self.cfg['sensors'][port].value()

    def getColorSensorRed(self, port):
        self.cfg['sensors'][port].mode='COL-REFLECT'
        return self.cfg['sensors'][port].value()

    def getColorSensorRgb(self, port):
        self.cfg['sensors'][port].mode='RGB-RAW'
        return self.cfg['sensors'][port].value()

    # infrared
    # http://www.ev3dev.org/docs/sensors/lego-ev3-infrared-sensor/
    def getInfraredSensorSeek(self, port):
        self.cfg['sensors'][port].mode='IR-SEEK'
        return self.cfg['sensors'][port].value()

    def getInfraredSensorDistance(self, port):
        self.cfg['sensors'][port].mode='IR-PROX'
        return self.cfg['sensors'][port].value()

    # timer
    def getTimerValue(self, timer):
        if timer in self.timers:
            return time.clock() - self.timers[timer]
        else:
            timers[timer] = time.clock()

    def resetTimer(self, timer):
        del self.timers[timer]


    def resetMotorTacho(self, actorPort):
        self.cfg['actors'][actorPort].last_position = self.cfg['actors'][actorPort].position

    def getMotorTachoValue(self, actorPort, mode):
       	m = self.cfg['actors'][actorPort]
       	tachoCount = m.position - m.last_position

       	if mode == 'degree':
            return tachoCount * 360.0 / m.count_per_rot

        elif mode in ['rotation', 'distance']:

            rotations = float( tachoCount / m.count_per_rot)

            if mode == 'rotation':
                return rotations;
            else:
            	distance = round (math.pi * self.cfg['wheel-diameter'] * rotations )
            	logger.info('distance: [%lf]' % distance)
                return distance
        else:
        	raise ValueError('incorrect MotorTachoMode: %s' % mode)

    # communication
    def establishConnectionTo(self, host):
        # host can also be a name, resolving it is slow though and requires the
        # device to be visible
        if not is_valid_address(host):
            nearby_devices = discover_devices()
            for bdaddr in nearby_devices:
                if host == lookup_name(bdaddr):
                    host = bdaddr
                    break
        if is_valid_address(host):
            con = sock=BluetoothSocket(RFCOMM)
            con.connect((host,post))
            self.bt_connections.append(con)
            return len(self.bt_connections) - 1
        else:
            return -1

    def waitForConnection(self):
        # enable visibility
        if not self.sys_bus:
            self.sys_bus = dbus.SystemBus()
        # do only once (since we turn off the timeout)
        # alternatively set DiscoverableTimeout = 0 in /etc/bluetooth/main.conf
        # and run hciconfig hci0 piscan, from robertalab initscript
        hci0 = self.sys_bus.get_object('org.bluez', '/org/bluez/hci0')
        props = dbus.Interface(hci0, 'org.freedesktop.DBus.Properties')
        props.Set('org.bluez.Adapter1', 'DiscoverableTimeout', dbus.UInt32(0))
        props.Set('org.bluez.Adapter1', 'Discoverable', True)

        if not self.bt_server:
            self.bt_server = sock=BluetoothSocket(RFCOMM)
            self.bt_server.bind(("",PORT_ANY))
            self.bt_server.listen(1)

        con,info = self.bt_server.accept()
        self.bt_connections.append(con)
        return len(self.bt_connections) - 1

    def readMessage(self, con_ix):
        message = "NO MESSAGE";
        if con_ix < len(self.bt_connections) and self.bt_connections[con_ix]:
            logger.info('reading msg')
            message = self.bt_connections[con_ix].recv(1024)
            logger.info('received msg [%s]' % message)
        return message

    def sendMessage(self, con_ix, message):
        if con_ix < len(self.bt_connections) and self.bt_connections[con_ix]:
            logger.info('sending msg [%s]' % message)
            self.bt_connection[con_ix].send(message)
            logger.info('sent msg')

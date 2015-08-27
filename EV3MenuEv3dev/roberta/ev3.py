from __future__ import absolute_import
from PIL import Image,ImageDraw,ImageFont
import ev3dev
import math
import random
import time

class BlocklyMethods:
    GOLDEN_RATIO = (1 + math.sqrt(5)) / 2
    
    @staticmethod
    def isEven(number):
        return (number % 2) == 0

    @staticmethod
    def isOdd(number):
        return (number % 2) == 1

    @staticmethod
    def isPrime(number):
        for i in xrange(2, math.sqrt(number)):
            remainder = number % i
            if remainder == 0:
                return False
        return True

    @staticmethod
    def isWhole(number):
        return number % 1 == 0

    @staticmethod
    def isPositive(number):
        return number > 0

    @staticmethod
    def isNegative(number):
        return number < 0

    @staticmethod
    def isDivisibleBy(number, divisor):
        return number % divisor == 0

    @staticmethod
    def remainderOf(divident, divisor):
        return divident % divisor;

    @staticmethod
    def clamp(x, min_val, max_val):
        return min(max(x, min_val), max_val);

    @staticmethod
    def randInt(min_val, max_val):
        if min_val < max_val:
            return random.randint(min_val, max_val)
        else:
            return random.randint(max_val, min_val)

    @staticmethod
    def randDouble():
        return random.random()


class Hal(object):

    def __init__(self, brickConfiguration, usedSensors):
        self.cfg = brickConfiguration
        self.usedSensors = usedSensors
        self.font_s = ImageFont.load('roberta/ter-u12n_unicode.pil')
        self.font_x = ImageFont.load('roberta/ter-u18n_unicode.pil')
        self.lcd = ev3dev.LCD()
        self.led = ev3dev.led
        self.key = ev3dev.button
        self.sound = ev3dev.sound
        (self.font_w, self.font_h)=self.lcd.draw.textsize('X', font=self.font_s)

    @staticmethod
    def makeLargeMotor(port, regulated, direction, side):
      m = ev3dev.large_motor(port)
      m.speed_regulation_enabled = regulated
      if direction is 'backward':
        m.polarity = 'inversed'
      else:
        m.polarity = 'normal'
      m.cfg_side = side
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
      return m

    # control
    def waitFor(self, ms):
        time.sleep (ms / 1000.0)

    # lcd
    def drawText(self, msg, x, y, font=None):
        font = font or self.font_s
        self.lcd.draw.text((x*self.font_w, y*self.font_h), msg, font=font)
        self.lcd.update()

    def clearDisplay(self):
        self.lcd.clear()

    # led
    def ledOn(self, color, mode):
        # color: green, red, orange - LED.COLOR.{RED,GREEN,AMBER}
        # mode: on, flash, double_flash
        if mode is 'on':
            if color is 'green': 
                self.led.green_on()
            elif color is 'red':
                self.led.red_on()
            elif color in 'amber':
                self.led.amber_on()
            # TODO: we also have orange_on(), yellow_on() and 
            #                    mix_colors(float red, float green)
        elif mode in ['flash', 'double_flash']:
            if color in ['green', 'orange']: 
                self.led.left_green.flash(500,500)
                self.led.right_green.flash(500,500)
                self.WaitFor(1000)
                self.ledOff()
            if color in ['red', 'orange']:
                self.led.left_red.flash(500,500)
                self.led.right_red.flash(500,500)
                self.WaitFor(2000)
                self.ledOff()
    
    def ledOff(self):
        self.led.all_off()
    
    def resetLED(self):
        self.lefOff();

    # key
    def isKeyPressed(self, key):
        if key in ['any', '*']:
          return self.key.process_all()
        else:
          # remap some keys
          keys = {
            'escape':  'back',
            'backspace': 'back',
          }
          if key in keys:
            key = keys[key]
          # throws attribute error on wrong keys
          return getattr(self.key, key).process()
    
    def isKeyPressedAndReleased(self, key):
        return False

    # tones
    def playTone(self, frequency, duration):
        frequency = frequency if frequency >= 100 else 0
        self.sound.tone(frequency, duration)

    def setVolume(self, volume):
        self.sound.volume = volume

    def getVolume(self):
        return self.sound.volume

    # actors
    # http://www.ev3dev.org/docs/drivers/tacho-motor-class/
    def rotateRegulatedMotor(self, port, speed_pct, mode, value):
      # mode: degree, rotations, distance
      m = self.cfg['actors'][port] 
      if mode is 'degree':
        m.run_to_rel_pos(speed_regulation_enabled='on', position_sp=value, speed_sp=speed_pct)
      elif mode is 'rotations':
        value *= m.count_per_rot
        m.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(value), speed_sp=speed_pct)
    
    def rotateUnregulatedMotor(self, port, speed_pct, mode, value):    
      m = self.cfg['actors'][port];
      if mode is 'rotations':
        value *= m.count_per_rot
      if speed_pct >= 0:
        value = m.position + value
        m.run_forever(speed_regulation_enabled='off', duty_cycle_sp=speed_pct)
        while (m.position < value):
          pass
      else:
        value = m.position - value
        m.run_forever(speed_regulation_enabled='off', duty_cycle_sp=speed_pct)
        while (m.position > value):
          pass
      m.stop()
    
    def turnOnRegulatedMotor(self, port, value):
      self.cfg['actors'][port].run_forever(speed_regulation_enabled='on', speed_sp=value)
    
    def turnOnUnregulatedMotor(self, port, value):
      self.cfg['actors'][port].run_forever(speed_regulation_enabled='off', duty_cycle_sp=value)
    
    def setRegulatedMotorSpeed(self, port, power):
      self.cfg['actors'][port].speed_sp = power
    
    def setUnregulatedMotorSpeed(self, port, power):
      self.cfg['actors'][port].duty_cycle_sp = power
    
    def getRegulatedMotorSpeed(self, port):
      return self.cfg['actors'][port].speed
    
    def getUnregulatedMotorSpeed(self, port):
      return self.cfg['actors'][port].duty_cycle
    
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
    
    def regulatedDrive(self, left_port, right_port, reverse, direction, speed_pct):
      # direction: forward, backward
      # reverse: always false for now
      if direction is 'backward':
        speed_pct = -speed_pct
      self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=speed_pct)
      self.cfg['actors'][righ_port].run_forever(speed_regulation_enabled='on', speed_sp=speed_pct)

    def driveDistance(self, left_port, right_port, reverse, direction, speed_pct, distance):
      # direction: forward, backward
      # reverse: always false for now
      ml = self.cfg['actors'][left_port];
      mr = self.cfg['actors'][right_port];
      circ = math.pi * self.cfg['wheel-diameter']
      dc = distance / circ
      if direction is 'backward':
        dc = -dc
      ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * ml.count_per_rot), speed_sp=speed_pct)
      mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * mr.count_per_rot), speed_sp=speed_pct)
    
    def rotateDirectionRegulated(self, left_port, right_port, reverse, direction, speed_pct):
      # direction: left, right
      # reverse: always false for now
      if direction is 'left':
        self.cfg['actors'][right_port].run_forever(speed_regulation_enabled='on', speed_sp=speed_pct)
        self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=-speed_pct)
      else:
        self.cfg['actors'][left_port].run_forever(speed_regulation_enabled='on', speed_sp=speed_pct)
        self.cfg['actors'][right_port].run_forever(speed_regulation_enabled='on', speed_sp=-speed_pct)
    
    def rotateDirectionAngle(self, left_port, right_port, reverse, direction, speed_pct, angle):
      # direction: left, right
      # reverse: always false for now
      ml = self.cfg['actors'][left_port];
      mr = self.cfg['actors'][right_port];
      circ = math.pi * self.cfg['track-width']
      distance = angle * circ / 360.0
      circ = math.pi * self.cfg['wheel-diameter']
      dc = distance / circ
      print "doing %lf rotations" % dc
      if direction is 'left':
        mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * mr.count_per_rot), speed_sp=speed_pct)
        ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(-dc * ml.count_per_rot), speed_sp=speed_pct)
      else:
        ml.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(dc * ml.count_per_rot), speed_sp=speed_pct)
        mr.run_to_rel_pos(speed_regulation_enabled='on', position_sp=int(-dc * mr.count_per_rot), speed_sp=speed_pct)
    
    # sensors
    # touch sensor
    def isPressed(self, port):
      return self.cfg['sensors'][port].volue()

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

    def getColorSensorColor(self, port):
      self.cfg['sensors'][port].mode='COL-COLOR'
      return self.cfg['sensors'][port].color

    def getColorSensorRed(self, port):
      self.cfg['sensors'][port].mode='COL-REFLECT'
      return self.cfg['sensors'][port].reflect

    def getColorSensorRgb(self, port):
      self.cfg['sensors'][port].mode='RGB-RAW'
      return self.cfg['sensors'][port].rgb

    # infrared
    # http://www.ev3dev.org/docs/sensors/lego-ev3-infrared-sensor/
    def getInfraredSensorSeek(self, port):
      self.cfg['sensors'][port].mode='IR-SEEK'
      return self.cfg['sensors'][port].value()

    def getInfraredSensorDistance(self, port):
      self.cfg['sensors'][port].mode='IR-PROX'
      return self.cfg['sensors'][port].value()

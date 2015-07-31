from __future__ import absolute_import
from ev3.ev3dev import *
from PIL import Image,ImageDraw,ImageFont
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
        self.lcd = Lcd()
        self.led = LED()
        self.key = Key()
        self.tone = Tone()
        (self.font_w, self.font_h)=self.lcd.draw.textsize('X', font=self.font_s)
    
    # control
    def waitFor(self, ms):
        time.sleep (ms / 1000.0)

    # lcd
    def drawText(self, msg, x, y, font=None):
        font = font or self.font_s
        self.lcd.draw.text((x*self.font_w, y*self.font_h), msg, font=font)
        self.lcd.update()

    def clearDisplay(self):
        self.lcd.reset()

    # led
    def ledOn(self, color, mode):
        # color: green, red, orange - LED.COLOR.{RED,GREEN,AMBER}
        # mode: on, flash, double_flash
        if mode is 0:
            self.led.left.color=color
            self.led.right.color=color
            self.led.left.on()
            self.led.right.on()
        elif mode is 1:
            self.led.left.blink(color=color)
            self.led.right.blink(color=color)
        elif mode is 2:
            self.led.left.blink(color=color)
            self.led.right.blink(color=color)
    
    def ledOff(self):
        self.led.left.off()
        self.led.right.off()
    
    def resetLED(self):
        self.lefOff();

    # key
    def isKeyPressed(self, key):
        # key: is a string, we can't use the constants like self.Key.enter
        # TODO: https://github.com/topikachu/python-ev3/issues/59
        if key in Key.CODE.enum_dict.keys():
          return getattr(self.key, key.lower())
        elif key is "*":
          return True in [getattr(self.key, k.lower()) for k in Key.CODE.enum_dict.keys()]
        else:
          return False
    
    def isKeyPressedAndReleased(self, key):
        return False

    # tones
    def playTone(self, frequency, duration):
        frequency = frequency if frequency >= 100 else 0
        self.tone.play(frequency, duration)

    # http://stackoverflow.com/questions/10739390/how-to-programmatically-change-volume-in-ubuntu
    def setVolume(self, volume):
        # FIXME
        pass

    def getVolume(self):
        # FIXME
        return 100

    # actors
    
    # sensors
    # touch sensor
    def isPressed(self, port):
      return self.cfg['sensors'][port].is_pushed

    # ultrasonic sensor
    def getUltraSonicSensorDistance(self, port):
      return self.cfg['sensors'][port].dist_cm

    def getUltraSonicSensorPresence(self, port):
      pass

    # gyro
    def resetGyroSensor(self, port):
      pass

    def getGyroSensorValue(self, port, mode):
      # mode = rate, angle
      #return self.cfg['sensors'][port].ang
      #return self.cfg['sensors'][port].rate
      pass
    
    # color
    def getColorSensorAmbient(self, port):
      return self.cfg['sensors'][port].ambient

    def getColorSensorColor(self, port):
      return self.cfg['sensors'][port].color

    def getColorSensorRed(self, port):
      return self.cfg['sensors'][port].reflect

    def getColorSensorRgb(self, port):
      return self.cfg['sensors'][port].rgb

    # infrared
    def getInfraredSensorSeek(self, port):
      #return self.cfg['sensors'][port].seek
      pass

    def getInfraredSensorDistance(self, port):
      return self.cfg['sensors'][port].prox


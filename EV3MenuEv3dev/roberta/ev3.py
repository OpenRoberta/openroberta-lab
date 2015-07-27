from __future__ import absolute_import
from ev3.ev3dev import *
from PIL import Image,ImageDraw,ImageFont
import time

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

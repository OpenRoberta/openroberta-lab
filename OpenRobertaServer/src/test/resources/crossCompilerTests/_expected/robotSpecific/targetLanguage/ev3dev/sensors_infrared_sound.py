#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from ev3dev import ev3 as ev3dev
import math
import os
import time

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

_brickConfiguration = {
    'wheel-diameter': 5.6,
    'track-width': 18.0,
    'actors': {
    },
    'sensors': {
        '4':Hal.makeInfraredSensor(ev3dev.INPUT_4),
        '2':Hal.makeSoundSensor(ev3dev.INPUT_2),
    },
}
hal = Hal(_brickConfiguration)

___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = 'white'
___connectionVar = None
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = ['white', 'white']
___connectionList = [___connectionVar, ___connectionVar]
def ____sensors():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText(str(hal.getInfraredSensorDistance('4')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getInfraredSensorSeek('4')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getSoundLevel('2')), ___numberVar, ___numberVar)

def ____waitUntil():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    while True:
        if hal.getInfraredSensorDistance('4') < 30:
            break
        hal.waitFor(15)

def run():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    ____sensors()

def main():
    try:
        run()
    except Exception as e:
        hal.drawText('Fehler im EV3', 0, 0)
        hal.drawText(e.__class__.__name__, 0, 1)
        hal.drawText(str(e), 0, 2)
        hal.drawText('Press any key', 0, 4)
        while not hal.isKeyPressed('any'): hal.waitFor(500)
        raise

if __name__ == "__main__":
    main()
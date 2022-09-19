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
        '2':Hal.makeCompassSensor(ev3dev.INPUT_2),
        '3':Hal.makeHTColorSensorV2(ev3dev.INPUT_3),
        '4':Hal.makeIRSeekerSensor(ev3dev.INPUT_4),
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

    hal.drawText(str(hal.getHiTecCompassSensorValue('2', 'angle')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecCompassSensorValue('2', 'compass')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecIRSeekerSensorValue('4', 'AC')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecIRSeekerSensorValue('4', 'DC')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecColorSensorV2Colour('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecColorSensorV2Light('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecColorSensorV2Ambient('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getHiTecColorSensorV2Rgb('3')), ___numberVar, ___numberVar)

def ____waitUntil():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    while True:
        if hal.getHiTecCompassSensorValue('2', 'angle') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecCompassSensorValue('2', 'compass') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecIRSeekerSensorValue('4', 'AC') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecIRSeekerSensorValue('4', 'DC') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecColorSensorV2Colour('3') == 'red':
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecColorSensorV2Light('3') < 50:
            break
        hal.waitFor(15)
    while True:
        if hal.getHiTecColorSensorV2Ambient('3') < 50:
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
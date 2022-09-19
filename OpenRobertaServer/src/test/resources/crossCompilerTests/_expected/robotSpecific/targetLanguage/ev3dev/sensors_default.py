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
        'B':Hal.makeLargeMotor(ev3dev.OUTPUT_B, 'on', 'foreward'),
    },
    'sensors': {
        '1':Hal.makeTouchSensor(ev3dev.INPUT_1),
        '2':Hal.makeGyroSensor(ev3dev.INPUT_2),
        '3':Hal.makeColorSensor(ev3dev.INPUT_3),
        '4':Hal.makeUltrasonicSensor(ev3dev.INPUT_4),
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
    hal.drawText(str(hal.isPressed('1')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getUltraSonicSensorDistance('4')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getUltraSonicSensorPresence('4')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getColorSensorColour('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getColorSensorRed('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getColorSensorAmbient('3')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getColorSensorRgb('3')), ___numberVar, ___numberVar)
    hal.resetMotorTacho('B')
    hal.drawText(str(hal.getMotorTachoValue('B', 'degree')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getMotorTachoValue('B', 'rotation')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getMotorTachoValue('B', 'distance')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('enter')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('up')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('down')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('left')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('right')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('escape')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.isKeyPressed('any')), ___numberVar, ___numberVar)
    hal.resetGyroSensor('2')
    hal.drawText(str(hal.getGyroSensorValue('2', 'angle')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getGyroSensorValue('2', 'rate')), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getTimerValue(1)), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getTimerValue(2)), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getTimerValue(3)), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getTimerValue(4)), ___numberVar, ___numberVar)
    hal.drawText(str(hal.getTimerValue(5)), ___numberVar, ___numberVar)
    hal.resetTimer(1)
    hal.resetTimer(2)
    hal.resetTimer(3)
    hal.resetTimer(4)
    hal.resetTimer(5)

def ____waitUntil():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    while True:
        if hal.isPressed('1') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.getUltraSonicSensorDistance('4') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getUltraSonicSensorPresence('4') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.getColorSensorColour('3') == 'red':
            break
        hal.waitFor(15)
    while True:
        if hal.getColorSensorRed('3') < 50:
            break
        hal.waitFor(15)
    while True:
        if hal.getColorSensorAmbient('3') < 50:
            break
        hal.waitFor(15)
    while True:
        if hal.getMotorTachoValue('B', 'degree') > 180:
            break
        hal.waitFor(15)
    while True:
        if hal.getMotorTachoValue('B', 'rotation') > 2:
            break
        hal.waitFor(15)
    while True:
        if hal.getMotorTachoValue('B', 'distance') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('enter') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('up') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('down') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('left') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('right') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('escape') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.isKeyPressed('any') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.getTimerValue(1) > 500:
            break
        hal.waitFor(15)
    while True:
        if hal.getTimerValue(2) > 500:
            break
        hal.waitFor(15)
    while True:
        if hal.getTimerValue(3) > 500:
            break
        hal.waitFor(15)
    while True:
        if hal.getTimerValue(4) > 500:
            break
        hal.waitFor(15)
    while True:
        if hal.getTimerValue(5) > 500:
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
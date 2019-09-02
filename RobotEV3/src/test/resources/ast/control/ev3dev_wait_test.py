#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from ev3dev import ev3 as ev3dev
import math
import os

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

_brickConfiguration = {
    'wheel-diameter': 5.6,
    'track-width': 18.0,
    'actors': {
    },
    'sensors': {
        '1':Hal.makeTouchSensor(ev3dev.INPUT_1),
        '2':Hal.makeGyroSensor(ev3dev.INPUT_2),
        '3':Hal.makeColorSensor(ev3dev.INPUT_3),
        '4':Hal.makeUltrasonicSensor(ev3dev.INPUT_4),
    },
}
hal = Hal(_brickConfiguration)

def run():
    while True:
        if ( 0 == 0 ) and True:
            break
        hal.waitFor(15)
    hal.waitFor(500)
    while True:
        if hal.isPressed('1') == True:
            break
        hal.waitFor(15)
    while True:
        if hal.getGyroSensorValue('2', 'angle') > 90:
            break
        hal.waitFor(15)
    while True:
        if hal.getGyroSensorValue('2', 'rate') > 90:
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
        if hal.getUltraSonicSensorDistance('4') < 30:
            break
        hal.waitFor(15)
    while True:
        if hal.getUltraSonicSensorPresence('4') == True:
            break
        hal.waitFor(15)

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

#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from roberta.BlocklyMethods import BlocklyMethods
from ev3dev import ev3 as ev3dev
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

_brickConfiguration = {
    'wheel-diameter': 5.6,
    'track-width': 18.0,
    'actors': {
        'B':Hal.makeLargeMotor(ev3dev.OUTPUT_B, 'on', 'foreward'),
        'C':Hal.makeLargeMotor(ev3dev.OUTPUT_C, 'on', 'foreward'),
    },
    'sensors': {
    },
}
hal = Hal(_brickConfiguration)

def run():
    for k0 in range(0, 10, 1):
        hal.driveDistance('C', 'B', False, 'foreward', 30, 20)

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
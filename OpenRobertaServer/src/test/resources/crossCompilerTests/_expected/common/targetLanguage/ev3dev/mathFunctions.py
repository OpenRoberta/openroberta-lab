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
    },
}
hal = Hal(_brickConfiguration)

___x = 0
def run():
    global ___x
    ___x = ___x + math.sqrt(4)
    ___x = ___x + math.fabs(-2)
    ___x = ___x + ( - (-4) )
    ___x = ___x + math.log(math.exp(2))
    ___x = ___x + math.log10(100)
    ___x = ___x + math.pow(10, 2)
    ___x = ___x + ( 5 % 3 )
    ___x = ___x + math.sin(math.pi / float(2))
    ___x = ___x + math.cos(0)
    ___x = ___x + math.tan(0)
    ___x = ___x + math.asin(0)
    ___x = ___x + math.acos(1)
    ___x = ___x + math.atan(0)
    ___x = ___x + math.floor(float(42.8))
    ___x = ___x + math.sin(min(max(2, 1), 100))
    # expected: 170

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
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

___number = 0
___boolean2 = True
___string = ""
def run():
    global ___number, ___boolean2, ___string
    # Variable Test START
    ___number = 0 + 5
    ___number = 3 + float(0.999999999999)
    ___string = "abc"
    ___string = "123"
    ___string = "\u00B3\u00BD\u00B9]"
    ___boolean2 = not True
    # Variable Test START END

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
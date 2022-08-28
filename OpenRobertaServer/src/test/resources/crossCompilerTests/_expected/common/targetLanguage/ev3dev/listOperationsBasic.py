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

___initialEmptyNumbers = []
___initialEmptyBoolean = []
___initialEmptyStrings = []
___number = 3
___bool = True
___string = "c"
___item = 0
def run():
    global ___initialEmptyNumbers, ___initialEmptyBoolean, ___initialEmptyStrings, ___number, ___bool, ___string, ___item
    # Basis List Operations START
    if not ___initialEmptyNumbers:
        ___initialEmptyNumbers = [1, 2]
        ___item = len( ___initialEmptyNumbers)
        ___item = ___initialEmptyNumbers.index(1)
        ___item = ___initialEmptyNumbers.index(5)
        ___initialEmptyNumbers[0] = 2
    if not ___initialEmptyBoolean:
        ___initialEmptyBoolean = [True, False]
        ___item = len( ___initialEmptyBoolean)
        ___item = ___initialEmptyBoolean.index(___bool)
        ___item = ___initialEmptyBoolean.index(None)
        ___initialEmptyBoolean.insert(-1, True)
    if not ___initialEmptyStrings:
        ___initialEmptyStrings = ["a", "b"]
        ___item = len( ___initialEmptyStrings)
        ___item = ___initialEmptyStrings.index("a")
        ___initialEmptyStrings[-1 -2] = "c"
        ___initialEmptyStrings.insert(-1 -1, "d")
    # Basis List Operations END

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
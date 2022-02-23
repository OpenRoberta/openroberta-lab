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

___result = False
def run():
    global ___result
    # Math power -- Start --
    ___result = 1 == math.pow(2, 0)
    if not True == ___result:
        print("Assertion failed: ", "pos-1", True, "EQ", ___result)
    ___result = 2 == math.pow(2, 1)
    if not True == ___result:
        print("Assertion failed: ", "pos-2", True, "EQ", ___result)
    ___result = 4 == math.pow(2, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-3", True, "EQ", ___result)
    ___result = 8 == math.pow(2, 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-4", True, "EQ", ___result)
    ___result = -4 == ( - (math.pow(2, 2)) )
    if not True == ___result:
        print("Assertion failed: ", "pos-5", True, "EQ", ___result)
    ___result = 4 == math.pow(-2, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-6", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) * math.pow(2, 3) ) == math.pow(2, 2 + 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-7", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) * math.pow(3, 2) ) == math.pow(2 * 3, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-8", True, "EQ", ___result)
    ___result = math.pow(math.pow(2, 2), 3) == math.pow(2, 2 * 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-9", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) / float(math.pow(3, 2)) ) == math.pow(2 / float(3), 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-10", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) / float(math.pow(2, 3)) ) == math.pow(2, 2 - 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-11", True, "EQ", ___result)
    print("Math Power Test: success" if ( True == ___result ) else "Basic Math Test: FAIL")
    # Math power -- End --

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
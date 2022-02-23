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
    # logik_boolean_op-- Start
    ___x = ___x + 1 if ( True ) else ___x + 1000
    ___x = ___x + 1000 if ( False ) else ___x + 1
    if not 2 == ___x:
        print("Assertion failed: ", "pos-1", 2, "EQ", ___x)
    ___x = ___x + 1 if ( True if ( True ) else False ) else ___x + 1000
    ___x = ___x + 1 if ( False if ( False ) else True ) else ___x + 1000
    if not 4 == ___x:
        print("Assertion failed: ", "pos-2", 4, "EQ", ___x)
    ___x = ___x + 1 if ( True if ( True if ( True ) else False ) else False ) else ___x + 1000
    if not 5 == ___x:
        print("Assertion failed: ", "pos-3", 5, "EQ", ___x)
    ___x = ___x + 1 if ( False if ( True if ( 1 == 2 ) else False ) else True ) else ___x + 1000
    print("Logic Ternary Op Test: success" if ( 6 == ___x ) else "Logic Ternary Op Test: FAIL")
    # Logic Ternary Op -- End

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
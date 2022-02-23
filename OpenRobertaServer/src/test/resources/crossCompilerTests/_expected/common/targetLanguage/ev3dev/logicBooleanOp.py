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
    # Logic Boolean Operators -- Start
    if True and True:
        ___x += 1
    if True and False:
        ___x += 1000
    if False and True:
        ___x += 1000
    if False and False:
        ___x += 1000
    if not 1 == ___x:
        print("Assertion failed: ", "pos-1", 1, "EQ", ___x)
    if not (True and True):
        ___x += 1000
    if not (True and False):
        ___x += 1
    if not (False and True):
        ___x += 1
    if not (False and False):
        ___x += 1
    if not 4 == ___x:
        print("Assertion failed: ", "pos-2", 4, "EQ", ___x)
    if True or True:
        ___x += 1
    if True or False:
        ___x += 1
    if False or True:
        ___x += 1
    if False or False:
        ___x += 1000
    if not 7 == ___x:
        print("Assertion failed: ", "pos-3", 7, "EQ", ___x)
    if not (True or True):
        ___x += 1000
    if not (True or False):
        ___x += 1000
    if not (False or True):
        ___x += 1000
    if not (False or False):
        ___x += 1
    if not 8 == ___x:
        print("Assertion failed: ", "pos-4", 8, "EQ", ___x)
    if ( True and True ) and ( True and True ):
        ___x += 1
    if ( True and False ) or ( False and True ):
        ___x += 1000
    if not (True or True) and not (True or True):
        ___x += 1000
    if not (True and False) or not (True and False):
        ___x += 1
    print("Logic Boolean operators Test: success" if ( 10 == ___x ) else "Logic Boolean operators Test: FAIL")
    # Logic Boolean Operators -- End

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
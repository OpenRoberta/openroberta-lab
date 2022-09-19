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

___n1 = 0
___b = False
___n2 = 1
___n3 = 4
def ____number():
    global ___n1, ___b, ___n2, ___n3
    ___n1 = ___n2 + ___n3

def ____breakFunct():
    global ___n1, ___b, ___n2, ___n3
    if 5 == ___n1: return None
    ___n1 = ___n1 + 1000

def ____retBool():
    global ___n1, ___b, ___n2, ___n3
    ___n1 = ___n1
    return ___b

def ____retNumber():
    global ___n1, ___b, ___n2, ___n3
    ___n1 = ___n1
    return ___n1

def ____retNumber2(___x):
    global ___n1, ___b, ___n2, ___n3
    ___x = ___x / float(2)
    return ___x

def run():
    global ___n1, ___b, ___n2, ___n3
    # Basic Functions START
    ____number()
    ____breakFunct()
    if not 5 == ___n1:
        print("Assertion failed: ", "pos-1", 5, "EQ", ___n1)
    ___n1 = ____retNumber()
    ___b = ____retBool()
    ___n1 = ____retNumber2(10)
    # Basic Functions END

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
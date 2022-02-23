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
___y = 1
def run():
    global ___x, ___y
    # Control Flow Loop -- Start
    if not 0 == ___x:
        print("Assertion failed: ", "pos-0", 0, "EQ", ___x)
    for ___k0 in range(int(0), int(5), int(1)):
        ___x = ___x + 1
    if not 5 == ___x:
        print("Assertion failed: ", "pos-1", 5, "EQ", ___x)
    while not (___x == 10):
        ___x = ___x + 1
    if not 10 == ___x:
        print("Assertion failed: ", "pos-2", 10, "EQ", ___x)
    while ___x < 15:
        ___x = ___x + 1
    if not 15 == ___x:
        print("Assertion failed: ", "pos-3", 15, "EQ", ___x)
    for ___i in range(int(1), int(6), int(1)):
        ___x = ___x + 1
    if not 20 == ___x:
        print("Assertion failed: ", "pos-4", 20, "EQ", ___x)
    for ___j in range(int(2), int(5), int(3)):
        ___x = ___x + 1
    if not 21 == ___x:
        print("Assertion failed: ", "pos-5", 21, "EQ", ___x)
    for ___k in range(int(2), int(6), int(3)):
        ___x = ___x + 1
    if not 23 == ___x:
        print("Assertion failed: ", "pos-6", 23, "EQ", ___x)
    for ___o in range(int(2), int(7), int(3)):
        ___x = ___x + 1
    if not 25 == ___x:
        print("Assertion failed: ", "pos-7", 25, "EQ", ___x)
    for ___p in range(int(10), int(9), int(-1)):
        ___x = ___x + 1
    if not 25 == ___x:
        print("Assertion failed: ", "pos-8", 25, "EQ", ___x)
    for ___m in range(int(1), int(5), int(___y)):
        ___y = ___y + 1
        ___x = ___x + 1
    if not 27 == ___x:
        print("Assertion failed: ", "pos-9", 27, "EQ", ___x)
    while True:
        if ___x < 30:
            ___x = ___x + 1
            if True:
                continue
            ___x = ___x + 1000
        elif ___x >= 30:
            break
    if not 30 == ___x:
        print("Assertion failed: ", "pos-10", 30, "EQ", ___x)
    print("Control Flow Loops: success" if ( ___x == 30 ) else "Control Flow Loops: FAIL")
    # Control Flow Loop -- End

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
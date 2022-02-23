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

___text = "start:"
___eight = " eight"
___number = 0
def run():
    global ___text, ___eight, ___number
    # String Concat -- Start --
    ___text += str(" one")
    if not "start: one" == ___text:
        print("Assertion failed: ", "POS-1", "start: one", "EQ", ___text)
    ___text += str("".join(str(arg) for arg in [" two", " three"]))
    if not "start: one two three" == ___text:
        print("Assertion failed: ", "POS-2", "start: one two three", "EQ", ___text)
    ___text += str("".join(str(arg) for arg in [4, 5]))
    if not "start: one two three45" == ___text:
        print("Assertion failed: ", "POS-3", "start: one two three45", "EQ", ___text)
    ___text += str("".join(str(arg) for arg in [6, " seven"]))
    if not "start: one two three456 seven" == ___text:
        print("Assertion failed: ", "POS-4", "start: one two three456 seven", "EQ", ___text)
    ___text = "".join(str(arg) for arg in ["".join(str(arg) for arg in [___text, ___eight]), " nine"])
    if not "start: one two three456 seven eight nine" == ___text:
        print("Assertion failed: ", "POS-5", "start: one two three456 seven eight nine", "EQ", ___text)
    ___text = "".join(str(arg) for arg in ["".join(str(arg) for arg in [___text, "ten"]), "".join(str(arg) for arg in [" eleven", " twelve"])])
    print("String Concat SUCCESS" if ( "start: one two three456 seven eight nine ten eleven twelve" == ___text ) else "String Concat FAIL")
    ___number = float(___text)
    ___number = ord(___text[0])
    ___text = str(10)
    ___text = chr((int)(30))
    # String Concat -- End --

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
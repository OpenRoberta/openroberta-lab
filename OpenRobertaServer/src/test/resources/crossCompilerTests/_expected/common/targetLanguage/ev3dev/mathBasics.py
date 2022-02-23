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

___ergebnis = 0
def run():
    global ___ergebnis
    # Math basics START
    ___ergebnis = ___ergebnis + 1
    ___ergebnis = ___ergebnis - 3
    ___ergebnis = ___ergebnis * -1
    ___ergebnis = ___ergebnis / float(2)
    if not 1 == ___ergebnis:
        print("Assertion failed: ", "pos-1", 1, "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( float(0.1) - float(0.1) )
    ___ergebnis = ___ergebnis + ( 5 * 2 )
    ___ergebnis = ___ergebnis + ( 3 / float(2) )
    if not float(12.5) == ___ergebnis:
        print("Assertion failed: ", "pos-2", float(12.5), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis * ( 1 + 2 )
    ___ergebnis = ___ergebnis * ( 1 - 2 )
    ___ergebnis = ___ergebnis * ( 1 / float(2) )
    if not float(-18.75) == ___ergebnis:
        print("Assertion failed: ", "pos-3", float(-18.75), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis / float(( float(0.1) + float(0.1) ))
    ___ergebnis = ___ergebnis / float(( float(0.1) - float(0.2) ))
    ___ergebnis = ___ergebnis / float(( float(0.1) * float(0.1) ))
    if not float(1e-7) > math.fabs(93750 - ___ergebnis):
        print("Assertion failed: ", "pos-4", float(1e-7), "GT", math.fabs(93750 - ___ergebnis))
    ___ergebnis = ___ergebnis - ( float(1.535345) + float(0.999999999999999) )
    ___ergebnis = ___ergebnis - ( float(0.1111111111111111) + float(0.9999999999999999) )
    ___ergebnis = ___ergebnis - ( 435 + float(0.14543) )
    if not float(1e-7) > math.fabs(float(93311.208113889) - ___ergebnis):
        print("Assertion failed: ", "pos-5", float(1e-7), "GT", math.fabs(float(93311.208113889) - ___ergebnis))
    # Math basics END

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
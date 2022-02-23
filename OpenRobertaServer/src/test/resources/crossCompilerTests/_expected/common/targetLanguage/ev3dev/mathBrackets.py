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
    # Grundrechenarten Basics  --START--
    ___ergebnis = 2 + ( ( 3 * 4 ) / float(5) )
    if not float(4.4) == ___ergebnis:
        print("Assertion failed: ", "POS-1", float(4.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( 3 + 4 ) * 5 ) )
    if not float(74.4) == ___ergebnis:
        print("Assertion failed: ", "POS-2", float(74.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( 3 * ( 4 + 5 ) ) )
    if not float(128.4) == ___ergebnis:
        print("Assertion failed: ", "POS-3", float(128.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 * 4 ) - 5 ) * 6 ) )
    if not float(172.4) == ___ergebnis:
        print("Assertion failed: ", "POS-4", float(172.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) * 5 ) * 6 ) )
    if not float(592.4) == ___ergebnis:
        print("Assertion failed: ", "POS-5", float(592.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( 6 * ( ( 3 + 4 ) * 5 ) ) )
    if not float(1012.4) == ___ergebnis:
        print("Assertion failed: ", "POS-7", float(1012.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 + 4 ) / float(( 5 - 6 )) ) - ( ( 7 * 8 ) + ( 9 + 10 ) ) ) )
    if not float(932.4) == ___ergebnis:
        print("Assertion failed: ", "POS-13", float(932.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) + ( 5 * 6 ) ) * ( ( 7 * 8 ) + ( 9 - 10 ) ) ) )
    print("SUCCESS" if ( ___ergebnis == float(5002.4) ) else "FAIL")
    # Grundrechenarten Basics  --END--

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
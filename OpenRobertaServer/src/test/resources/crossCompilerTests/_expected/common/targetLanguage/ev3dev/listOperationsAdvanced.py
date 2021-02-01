#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from ev3dev import ev3 as ev3dev
import math
import os

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

___nl = []
___bl = []
___sl = []
___nl3 = [1, 2, 9]
___bl3 = [True, True, False]
___sl3 = ["a", "b", "c"]
___n = 0
___b = True
___s = ""
def run():
    global ___nl, ___bl, ___sl, ___nl3, ___bl3, ___sl3, ___n, ___b, ___s
    # Basis List Operations START
    if not ___nl:
        ___nl = [3, 4, 5, 6, 7, 8]
        ___nl3.insert(-1 -1, ___nl.pop(0))
    if not ___bl:
        ___bl = [True, False, True]
        ___bl = [___bl[0] == ___bl[-1], ___bl[1] == ___bl[-1 -1], ___bl[-1] == ___bl[0]]
    if not ___sl:
        ___sl = ["d", "e", "f"]
    ___n = len( ___nl)
    ___n = len( ___nl[0:])
    ___n = len( ___nl[0:]) + len( ___nl[1:3])
    ___n = ___sl.index("b")
    ___n = [5] * 5[-1]
    ___s = ["copy"] * 5[-1 -5]
    while not not not ___sl:
        ___sl3[-1] = ___sl.pop(0)
    while not (len( ___nl3) <= 9):
        ___nl3.insert(-1 -1, ___nl.pop(0))
    ___nl3[2:-1 -5][0] = ___nl3.index(___n)
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
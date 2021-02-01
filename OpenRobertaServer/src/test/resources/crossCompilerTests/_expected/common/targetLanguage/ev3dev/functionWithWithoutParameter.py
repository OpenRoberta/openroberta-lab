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

___two = 3
___summe = 3
___liste00 = [0, 0]
def get3():
    global ___two, ___summe, ___liste00
    return 3

def getList():
    global ___two, ___summe, ___liste00
    return [1, 2, 3]

def getParmPlus6(___x1):
    global ___two, ___summe, ___liste00
    return ___x1 + 6

def getListUpd1To8(___x2):
    global ___two, ___summe, ___liste00
    ___x2[1] = 8
    return ___x2

def getString():
    global ___two, ___summe, ___liste00
    return "".join(str(arg) for arg in ["++", "--"])

def getStringAppPP(___x3):
    global ___two, ___summe, ___liste00
    return "".join(str(arg) for arg in [___x3, "++"])

def run():
    global ___two, ___summe, ___liste00
    ___two = getList()[1]
    ___summe = ___two + get3()
    # 5
    if getString() == "++--":
        ___summe += 4
    # 9
    ___summe += getListUpd1To8(___liste00)[1]
    # 17
    ___summe += getParmPlus6(1)
    # 24
    if getStringAppPP("--") == "--++":
        ___summe += 11
    # 35

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
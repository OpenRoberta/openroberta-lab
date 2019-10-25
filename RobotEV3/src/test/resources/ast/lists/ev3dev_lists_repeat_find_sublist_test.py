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

___item = [0, 0, 0]
___item2 = [True, True, True]
___item3 = ["", "", ""]
___item4 = ['white', 'white', 'white']
___item5 = [None, None, None]
___item6 = 0
___item7 = True
def run():
    global ___item, ___item2, ___item3, ___item4, ___item5, ___item6, ___item7
    ___item = [0] * 5
    ___item2 = [True] * 5
    ___item3 = [""] * 5
    ___item4 = ['white'] * 5
    ___item5 = [hal.waitForConnection()] * 5
    ___item = ___item[0:0]
    ___item2 = ___item2[0:0]
    ___item3 = ___item3[0:0]
    ___item4 = ___item4[0:0]
    ___item5 = ___item5[0:0]
    ___item6 = ___item.index(0)
    ___item6 = ___item2.index(True)
    ___item6 = ___item3.index("")
    ___item6 = ___item4.index('white')
    ___item6 = ___item5.index(hal.waitForConnection())
    ___item6 = (len(___item) - 1) - ___item[::-1].index(0)
    ___item6 = (len(___item2) - 1) - ___item2[::-1].index(True)
    ___item6 = (len(___item3) - 1) - ___item3[::-1].index("")
    ___item6 = (len(___item4) - 1) - ___item4[::-1].index('white')
    ___item6 = (len(___item5) - 1) - ___item5[::-1].index(hal.waitForConnection())

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

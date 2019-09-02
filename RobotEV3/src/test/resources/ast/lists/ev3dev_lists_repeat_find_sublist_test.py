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

item = [0, 0, 0]
item2 = [True, True, True]
item3 = ["", "", ""]
item4 = ['white', 'white', 'white']
item5 = [None, None, None]
item6 = 0
item7 = True
def run():
    global item, item2, item7, item4, item3, item6, item5
    item = [0] * 5
    item2 = [True] * 5
    item3 = [""] * 5
    item4 = ['white'] * 5
    item5 = [hal.waitForConnection()] * 5
    item = item[0:0]
    item2 = item2[0:0]
    item3 = item3[0:0]
    item4 = item4[0:0]
    item5 = item5[0:0]
    item6 = item.index(0)
    item6 = item2.index(True)
    item6 = item3.index("")
    item6 = item4.index('white')
    item6 = item5.index(hal.waitForConnection())
    item6 = (len(item) - 1) - item[::-1].index(0)
    item6 = (len(item2) - 1) - item2[::-1].index(True)
    item6 = (len(item3) - 1) - item3[::-1].index("")
    item6 = (len(item4) - 1) - item4[::-1].index('white')
    item6 = (len(item5) - 1) - item5[::-1].index(hal.waitForConnection())

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

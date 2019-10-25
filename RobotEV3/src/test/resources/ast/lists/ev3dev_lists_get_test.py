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
___item3 = ["1", "2", "3"]
___item4 = ['white', 'white', 'white']
___item5 = [None, None, None]
___item6 = 0
___item7 = True
___item8 = "123"
___item9 = 'white'
___item10 = None
def run():
    global ___item, ___item2, ___item3, ___item4, ___item5, ___item6, ___item7, ___item8, ___item9, ___item10
    ___item6 = ___item[0]
    ___item6 = ___item[-1 -0]
    ___item6 = ___item[0]
    ___item6 = ___item[-1]
    ___item6 = ___item[0]
    ___item6 = ___item.pop(0)
    ___item6 = ___item.pop(-1 -0)
    ___item6 = ___item.pop(0)
    ___item6 = ___item.pop(-1)
    ___item6 = ___item.pop(0)
    ___item.pop(0)
    ___item.pop(-1 -0)
    ___item.pop(0)
    ___item.pop(-1)
    ___item.pop(0)
    ___item7 = ___item2[0]
    ___item7 = ___item2[-1 -0]
    ___item7 = ___item2[0]
    ___item7 = ___item2[-1]
    ___item7 = ___item2[0]
    ___item7 = ___item2.pop(0)
    ___item7 = ___item2.pop(-1 -0)
    ___item7 = ___item2.pop(0)
    ___item7 = ___item2.pop(-1)
    ___item7 = ___item2.pop(0)
    ___item2.pop(0)
    ___item2.pop(-1 -0)
    ___item2.pop(0)
    ___item2.pop(-1)
    ___item2.pop(0)
    ___item8 = ___item3[0]
    ___item8 = ___item3[-1 -0]
    ___item8 = ___item3[0]
    ___item8 = ___item3[-1]
    ___item8 = ___item3[0]
    ___item8 = ___item3.pop(0)
    ___item8 = ___item3.pop(-1 -0)
    ___item8 = ___item3.pop(0)
    ___item8 = ___item3.pop(-1)
    ___item8 = ___item3.pop(0)
    ___item3.pop(0)
    ___item3.pop(-1 -0)
    ___item3.pop(0)
    ___item3.pop(-1)
    ___item3.pop(0)
    ___item9 = ___item4[0]
    ___item9 = ___item4[-1 -0]
    ___item9 = ___item4[0]
    ___item9 = ___item4[-1]
    ___item9 = ___item4[0]
    ___item9 = ___item4.pop(0)
    ___item9 = ___item4.pop(-1 -0)
    ___item9 = ___item4.pop(0)
    ___item9 = ___item4.pop(-1)
    ___item9 = ___item4.pop(0)
    ___item4.pop(0)
    ___item4.pop(-1 -0)
    ___item4.pop(0)
    ___item4.pop(-1)
    ___item4.pop(0)
    ___item10 = ___item5[0]
    ___item10 = ___item5[-1 -0]
    ___item10 = ___item5[0]
    ___item10 = ___item5[-1]
    ___item10 = ___item5[0]
    ___item10 = ___item5.pop(0)
    ___item10 = ___item5.pop(-1 -0)
    ___item10 = ___item5.pop(0)
    ___item10 = ___item5.pop(-1)
    ___item10 = ___item5.pop(0)
    ___item5.pop(0)
    ___item5.pop(-1 -0)
    ___item5.pop(0)
    ___item5.pop(-1)
    ___item5.pop(0)

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

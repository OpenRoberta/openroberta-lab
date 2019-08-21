#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from ev3dev import ev3 as ev3dev
import math

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
item3 = ["1", "2", "3"]
item4 = ['white', 'white', 'white']
item5 = [None, None, None]
item6 = 0
item7 = True
item8 = "123"
item9 = 'white'
item10 = None
def run():
    global item, item2, item8, item7, item9, item4, item3, item10, item6, item5
    item6 = item[0]
    item6 = item[-1 -0]
    item6 = item[0]
    item6 = item[-1]
    item6 = item[0]
    item6 = item.pop(0)
    item6 = item.pop(-1 -0)
    item6 = item.pop(0)
    item6 = item.pop(-1)
    item6 = item.pop(0)
    item.pop(0)
    item.pop(-1 -0)
    item.pop(0)
    item.pop(-1)
    item.pop(0)
    item7 = item2[0]
    item7 = item2[-1 -0]
    item7 = item2[0]
    item7 = item2[-1]
    item7 = item2[0]
    item7 = item2.pop(0)
    item7 = item2.pop(-1 -0)
    item7 = item2.pop(0)
    item7 = item2.pop(-1)
    item7 = item2.pop(0)
    item2.pop(0)
    item2.pop(-1 -0)
    item2.pop(0)
    item2.pop(-1)
    item2.pop(0)
    item8 = item3[0]
    item8 = item3[-1 -0]
    item8 = item3[0]
    item8 = item3[-1]
    item8 = item3[0]
    item8 = item3.pop(0)
    item8 = item3.pop(-1 -0)
    item8 = item3.pop(0)
    item8 = item3.pop(-1)
    item8 = item3.pop(0)
    item3.pop(0)
    item3.pop(-1 -0)
    item3.pop(0)
    item3.pop(-1)
    item3.pop(0)
    item9 = item4[0]
    item9 = item4[-1 -0]
    item9 = item4[0]
    item9 = item4[-1]
    item9 = item4[0]
    item9 = item4.pop(0)
    item9 = item4.pop(-1 -0)
    item9 = item4.pop(0)
    item9 = item4.pop(-1)
    item9 = item4.pop(0)
    item4.pop(0)
    item4.pop(-1 -0)
    item4.pop(0)
    item4.pop(-1)
    item4.pop(0)
    item10 = item5[0]
    item10 = item5[-1 -0]
    item10 = item5[0]
    item10 = item5[-1]
    item10 = item5[0]
    item10 = item5.pop(0)
    item10 = item5.pop(-1 -0)
    item10 = item5.pop(0)
    item10 = item5.pop(-1)
    item10 = item5.pop(0)
    item5.pop(0)
    item5.pop(-1 -0)
    item5.pop(0)
    item5.pop(-1)
    item5.pop(0)

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
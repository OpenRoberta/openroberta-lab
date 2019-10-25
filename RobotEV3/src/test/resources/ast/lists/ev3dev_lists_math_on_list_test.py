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
    ___item6 = len(___item)
    ___item6 = len(___item2)
    ___item6 = len(___item3)
    ___item6 = len(___item4)
    ___item6 = len(___item5)
    ___item7 = not ___item
    ___item7 = not ___item2
    ___item7 = not ___item3
    ___item7 = not ___item4
    ___item7 = not ___item5
    ___item6 = sum(___item)
    ___item6 = min(___item)
    ___item6 = max(___item)
    ___item6 = float(sum(___item)) / len(___item)
    ___item6 = _median(___item)
    ___item6 = _standard_deviation(___item)
    ___item6 = ___item[0]

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

def _median(l):
    l = sorted(l)
    l_len = len(l)
    if l_len < 1:
        return None
    if l_len % 2 == 0:
        return ( l[int( (l_len-1) / 2)] + l[int( (l_len+1) / 2)] ) / 2.0
    else:
        return l[int( (l_len-1) / 2)]

def _randInt(min_val, max_val):
    val = int.from_bytes(os.urandom(4), byteorder='big')
    if min_val < max_val:
        return min_val + (val % ((max_val - min_val) + 1))
    else:
        return max_val + (val % ((min_val - max_val) + 1))

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))

if __name__ == "__main__":
    main()

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

___input = [1, 2, 3, 4, 3]
___input2 = []
___result = 0
def run():
    global ___input, ___input2, ___result
    ___result = len( ___input)
    ___result = ___result + len( ___input2)
    if not ___input:
        ___result = ___result + 1
    else:
        ___result = ___result + 2
    if not ___input2:
        ___result = ___result + 1
    else:
        ___result = ___result + 2
    # 8
    ___result = ___result + ___input.index(3)
    ___result = ___result + (len(___input) - 1) - ___input[::-1].index(3)
    # 14
    ___result = ___result + ___input[1]
    ___result = ___result + ___input[-1 -1]
    ___result = ___result + ___input[0]
    ___result = ___result + ___input[-1]
    # 24
    ___result = ___result + ___input.pop(1)
    ___result = ___result + ___input.pop(-1 -1)
    ___result = ___result + ___input.pop(0)
    ___result = ___result + ___input.pop(-1)
    ___result = ___result + len( ___input)
    # 35
    ___input.insert(0, 1)
    ___input.insert(-1 -1, 2)
    ___input.insert(0, 0)
    ___input.insert(-1, 4)
    ___result = ___result + len( ___input)
    # 40
    ___input.pop(1)
    ___input.pop(-1 -1)
    ___input.pop(0)
    ___input.pop(-1)
    ___result = ___result + len( ___input)
    ___result = ___result + ___input[-1]
    # 42
    ___input.insert(0, 1)
    ___input.insert(-1 -1, 2)
    ___input.insert(0, 0)
    ___input.insert(-1, 4)
    ___input[2] = 3
    ___input[1] = 2
    ___input[-1 -1] = 4
    ___input[0] = 1
    ___input[-1] = 5
    ___result = ___result + sum(___input)
    # 57
    ___result = ___result + sum(___input[1:3])
    ___result = ___result + sum(___input[1:-1 -1])
    ___result = ___result + sum(___input[1:])
    # 89
    ___result = ___result + sum(___input[-1 -3:4])
    ___result = ___result + sum(___input[-1 -4:-1 -3])
    ___result = ___result + sum(___input[-1 -3:])
    # 120
    ___result = ___result + sum(___input[0:3])
    ___result = ___result + sum(___input[0:-1 -3])
    ___result = ___result + sum(___input[0:])
    # 148
    ___result = ___result + min(___input)
    ___result = ___result + max(___input)
    # 154
    ___result = ___result + float(sum(___input))/len(___input)
    ___result = ___result + _median(___input)
    ___result = ___result + _standard_deviation(___input)
    # 161.414...
    # 161.414 - sim, 161.5 - board, OK

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
        return (l[int((l_len - 1) / 2)] + l[int((l_len + 1) / 2)] ) / 2.0
    else:
        return l[int((l_len - 1) / 2)]

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))
if __name__ == "__main__":
    main()
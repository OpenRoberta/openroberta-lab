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

___r1 = 0
___r2 = 0
___b1 = True
___r3 = 0
___sim = True
def run():
    global ___r1, ___r2, ___b1, ___r3, ___sim
    ___r1 = math.sqrt(( 20 - ( 2 * ( 4 / float(2) ) ) ) + math.pow(3, 2))
    ___b1 = ___b1 and not (___r1 % 2) == 0
    ___b1 = ___b1 and (___r1 % 2) == 1
    ___b1 = ___b1 and _isPrime(___r1)
    ___b1 = ___b1 and (___r1 % 1) == 0
    ___b1 = ___b1 and ___r1 > 0
    ___b1 = ___b1 and not ___r1 < 0
    ___b1 = ___b1 and (___r1 % 5) == 0
    ___b1 = ___b1 and not (___r1 % 3) == 0
    ___r1 += 1
    ___b1 = ___b1 and (___r1 % 2) == 0
    ___r2 = math.sqrt(20)
    ___b1 = ___b1 and not (___r2 % 1) == 0
    ___b1 = ___b1 and ( round(___r2) == 4 )
    ___b1 = ___b1 and ( math.ceil(___r2) == 5 )
    ___b1 = ___b1 and ( math.floor(___r2) == 4 )
    ___b1 = ___b1 and ( ___r1 > ___r2 )
    ___b1 = ___b1 and ( ___r1 >= ___r2 )
    ___b1 = ___b1
    ___b1 = ( ___b1 and ( ___r2 < ___r1 ) ) and ( ___r1 <= ___r1 )
    ___b1 = ___b1 and ( ( ___r1 % 4 ) == 2 )
    ___b1 = ___b1 and ( 29 == ( min(max(math.pow(3, 2), 1), 20) + ( min(max(9, 3 * 4), 18) + min(max(3 * 3, 5), 8) ) ) )
    ___b1 = ___b1 and ( 11 > ( _randDouble() * _randInt(1, 10) ) )
    # if b1 is true, the test succeeded, otherwise it failed :-)

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

def _isPrime(number):
    if(number == 0 or number == 1):
        return False
    for i in range(2, int(math.floor(math.sqrt(number))) + 1):
        remainder = number % i
        if remainder == 0:
            return False
    return True

def _randInt(min_val, max_val):
    val = int.from_bytes(os.urandom(4), byteorder='big')
    if min_val < max_val:
        return min_val + (val % ((max_val - min_val) + 1))
    else:
        return max_val + (val % ((min_val - max_val) + 1))

def _randDouble():
    return 1.0*int.from_bytes(os.urandom(4), byteorder='big') / 0xffffffff

if __name__ == "__main__":
    main()
#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from ev3dev import ev3 as ev3dev
import math
import os
import time


def _randInt(min_val, max_val):
    val = int.from_bytes(os.urandom(4), byteorder='big')
    if min_val < max_val:
        return min_val + (val % ((max_val - min_val) + 1))
    else:
        return max_val + (val % ((min_val - max_val) + 1))

def _randDouble():
    return 1.0*int.from_bytes(os.urandom(4), byteorder='big') / 0xffffffff

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

___item = 0

def ____plusOperations(___item2):
    global ___item
    ___item2 = ( 1 * 2 ) + ( 3 + 4 )
    ___item2 = min(max(( ( 6 + 5 ) % ( 5 ) ), 1), 100)
    ___item2 = ( ( math.sqrt(6) + math.sin(5) ) % ( 5 ) )
    ___item2 = ( ( 6 + math.pi ) % ( round(float(7.8)) ) )
    ___item2 = ( ( 6 + _randInt(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item2 = ( ( _randDouble() + 5 ) % ( 5 ) )

def ____multiplicationOperations(___item4):
    global ___item
    ___item4 = ( 1 * 2 ) * ( 3 + 4 )
    ___item4 = min(max(( ( 6 * 5 ) % ( 5 ) ), 1), 100)
    ___item4 = ( ( math.sqrt(6) * math.sin(5) ) % ( 5 ) )
    ___item4 = ( ( 6 * math.pi ) % ( round(float(7.8)) ) )
    ___item4 = ( ( 6 * _randInt(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item4 = ( ( _randDouble() * 5 ) % ( 5 ) )

def ____exponentOperations(___item6):
    global ___item
    ___item6 = math.pow(1 * 2, 3 + 4)
    ___item6 = min(max(( ( math.pow(6, 5) ) % ( 5 ) ), 1), 100)
    ___item6 = ( ( math.pow(math.sqrt(6), math.sin(5)) ) % ( 5 ) )
    ___item6 = ( ( math.pow(6, math.pi) ) % ( round(float(7.8)) ) )
    ___item6 = ( ( math.pow(6, _randInt(10 - 1, 100 - 1)) ) % ( 5 ) )
    ___item6 = ( ( math.pow(_randDouble(), 5) ) % ( 5 ) )

def ____minusOperations(___item3):
    global ___item
    ___item3 = ( 1 * 2 ) - ( 3 + 4 )
    ___item3 = min(max(( ( 6 - 5 ) % ( 5 ) ), 1), 100)
    ___item3 = ( ( math.sqrt(6) - math.sin(5) ) % ( 5 ) )
    ___item3 = ( ( 6 - math.pi ) % ( round(float(7.8)) ) )
    ___item3 = ( ( 6 - _randInt(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item3 = ( ( _randDouble() - 5 ) % ( 5 ) )

def ____divisionOperations(___item5):
    global ___item
    ___item5 = ( 1 * 2 ) / float(( 3 + 4 ))
    ___item5 = min(max(( ( 6 / float(5) ) % ( 5 ) ), 1), 100)
    ___item5 = ( ( math.sqrt(6) / float(math.sin(5)) ) % ( 5 ) )
    ___item5 = ( ( 6 / float(math.pi) ) % ( round(float(7.8)) ) )
    ___item5 = ( ( 6 / float(_randInt(10 - 1, 100 - 1)) ) % ( 5 ) )
    ___item5 = ( ( _randDouble() / float(5) ) % ( 5 ) )


def run():
    global ___item
    ____plusOperations(___item)
    ____minusOperations(___item)
    ____multiplicationOperations(___item)
    ____divisionOperations(___item)
    ____exponentOperations(___item)

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
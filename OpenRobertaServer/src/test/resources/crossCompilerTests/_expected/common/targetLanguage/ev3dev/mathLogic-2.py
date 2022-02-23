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
    ___r3 = math.pi / float(2) if ( ___sim ) else 90
    ___b1 = ___b1 and ( math.sin(___r3) == 1 )
    ___b1 = ___b1 and ( math.cos(0) == 1 )
    ___b1 = ___b1 and ( math.tan(0) == 0 )
    ___b1 = ___b1 and ( math.asin(1) == ___r3 )
    ___b1 = ___b1 and ( math.acos(1) == 0 )
    ___b1 = ___b1 and ( math.atan(0) == 0 )
    ___b1 = ___b1 and ( ( math.e > float(2.6) ) and ( math.e < float(2.8) ) )
    ___b1 = ___b1 and ( ( ( math.sqrt(2) * math.sqrt(0.5) ) >= float(0.999) ) and ( ( math.sqrt(2) * math.sqrt(0.5) ) <= float(1.001) ) )
    # if b1 is true, the test succeeded, otherwise it failed

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
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
        'B':Hal.makeLargeMotor(ev3dev.OUTPUT_B, 'on', 'foreward'),
    },
    'sensors': {
        '2':Hal.makeGyroSensor(ev3dev.INPUT_2),
        '3':Hal.makeColorSensor(ev3dev.INPUT_3),
        '4':Hal.makeUltrasonicSensor(ev3dev.INPUT_4),
    },
}
hal = Hal(_brickConfiguration)

___num = 0
___boolT = True
___str = ""
___color = 'white'
___boolF = False
___conn = None
___listN = [0, 0, 0]
___listN2 = [0, 0, 0]
___listConn = [None, None, None]
___listColor = ['white', 'white', 'white']
def run():
    global ___num, ___boolT, ___str, ___color, ___boolF, ___conn, ___listN, ___listN2, ___listConn, ___listColor
    ___num = ( math.exp(2) + math.sin(90) ) - ( _randInt(1, 10) * math.ceil(float(2.3)) )
    ___num = ( ( ( sum(___listN) + ___listN[0] ) + ___listN.index(0) ) + ___listN[0] ) - ___listN2.pop(1)
    ___boolT = ( ( ( (10 % 2) == 0 and (7 % 2) == 1 ) or ( _isPrime(11) and (8 % 1) == 0 ) ) or ( not ___listN and 5 > 0 ) ) or ( - (3) < 0 and (10 % 5) == 0 )
    ___str = "".join(str(arg) for arg in [___listColor[0], "Hello", chr((int)(65))])
    ___listN2 = ___listN[0:3]
    ___color = 'blue'
    ___color = 'green'
    ___num = hal.getRegulatedMotorSpeed('B')
    ___boolT = hal.getUltraSonicSensorPresence('4')
    ___listN2 = hal.getColorSensorRgb('3')
    ___boolT = hal.isKeyPressed('up')
    ___num = hal.getGyroSensorValue('2', 'angle')
    ___conn = hal.establishConnectionTo("hola")
    ___str = hal.readMessage(___conn)
    ___conn = hal.waitForConnection()

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

if __name__ == "__main__":
    main()
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

___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = 'white'
___connectionVar = None
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = ['white', 'white']
___connectionList = [___connectionVar, ___connectionVar]
def ____math():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText(str(0), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar + ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar - ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar * ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar / float(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.pow(___numberVar, ___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.sqrt(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.fabs(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(- (___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.log(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.log10(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.exp(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.pow(10, ___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.sin(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.cos(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.tan(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.asin(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.acos(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.atan(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.pi), ___numberVar, ___numberVar)
    hal.drawText(str(math.e), ___numberVar, ___numberVar)
    hal.drawText(str((1 + 5 ** 0.5) / 2), ___numberVar, ___numberVar)
    hal.drawText(str(math.sqrt(2)), ___numberVar, ___numberVar)
    hal.drawText(str(math.sqrt(0.5)), ___numberVar, ___numberVar)
    hal.drawText(str(float('inf')), ___numberVar, ___numberVar)
    hal.drawText(str((___numberVar % 2) == 0), ___numberVar, ___numberVar)
    hal.drawText(str((___numberVar % 2) == 1), ___numberVar, ___numberVar)
    hal.drawText(str(_isPrime(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str((___numberVar % 1) == 0), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar > 0), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar < 0), ___numberVar, ___numberVar)
    hal.drawText(str((___numberVar % ___numberVar) == 0), ___numberVar, ___numberVar)
    ___numberVar += ___numberVar
    hal.drawText(str(round(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.ceil(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(math.floor(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(sum(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(min(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(max(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(float(sum(___numberList))/len(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(_median(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(_standard_deviation(___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[0]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar % ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(min(max(___numberVar, ___numberVar), ___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(_randInt(___numberVar, ___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(_randDouble()), ___numberVar, ___numberVar)

def ____lists():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    ___numberList = []
    ___numberList = [0, 0, 0]
    ___numberList = [___numberVar] * ___numberVar
    hal.drawText(str(len( ___numberList)), ___numberVar, ___numberVar)
    hal.drawText(str(not ___numberList), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList.index(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str((len(___numberList) - 1) - ___numberList[::-1].index(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[-1 -___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[0]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[-1]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList.pop(___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList.pop(-1 -___numberVar)), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList.pop(0)), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList.pop(-1)), ___numberVar, ___numberVar)
    ___numberList.pop(___numberVar)
    ___numberList.pop(-1 -___numberVar)
    ___numberList.pop(0)
    ___numberList.pop(-1)
    ___numberList[___numberVar] = ___numberVar
    ___numberList[-1 -___numberVar] = ___numberVar
    ___numberList[0] = ___numberVar
    ___numberList[-1] = ___numberVar
    ___numberList.insert(___numberVar, ___numberVar)
    ___numberList.insert(-1 -___numberVar, ___numberVar)
    ___numberList.insert(0, ___numberVar)
    ___numberList.insert(-1, ___numberVar)
    hal.drawText(str(___numberList[___numberVar:___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[___numberVar:-1 -___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[___numberVar:]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[0:___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[0:-1 -___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[0:]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[-1 -___numberVar:___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[-1 -___numberVar:-1 -___numberVar]), ___numberVar, ___numberVar)
    hal.drawText(str(___numberList[-1 -___numberVar:]), ___numberVar, ___numberVar)

def run():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    ____math()
    ____lists()

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

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))
if __name__ == "__main__":
    main()
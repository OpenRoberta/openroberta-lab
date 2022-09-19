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
def ____control():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    if ___booleanVar:
        pass
    elif ___booleanVar:
        pass
    if ___booleanVar:
        pass
    elif ___booleanVar:
        pass
    while True:
        pass
    for ___k0 in range(int(0), int(___numberVar), int(1)):
        pass
    for ___i in range(int(___numberVar), int(___numberVar), int(___numberVar)):
        pass
    while True:
        break
    while True:
        continue
    hal.waitFor(___numberVar)
    while ___booleanVar:
        pass
    while not ___booleanVar:
        pass
    for ___item in ___numberList:
        pass
    for ___item2 in ___booleanList:
        pass
    for ___item3 in ___stringList:
        pass
    for ___item4 in ___colourList:
        pass
    for ___item5 in ___connectionList:
        pass
    while True:
        if ___booleanVar:
            break
        if ___booleanVar:
            break
        hal.waitFor(15)
    while True:
        if ___booleanVar:
            break
        hal.waitFor(15)

def ____logic():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText(str(___numberVar == ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar != ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar < ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar <= ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar > ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar >= ___numberVar), ___numberVar, ___numberVar)
    hal.drawText(str(___booleanVar and ___booleanVar), ___numberVar, ___numberVar)
    hal.drawText(str(___booleanVar or ___booleanVar), ___numberVar, ___numberVar)
    hal.drawText(str(not ___booleanVar), ___numberVar, ___numberVar)
    hal.drawText(str(True), ___numberVar, ___numberVar)
    hal.drawText(str(False), ___numberVar, ___numberVar)
    hal.drawText(str(None), ___numberVar, ___numberVar)
    hal.drawText(str(___numberVar if ( ___booleanVar ) else ___numberVar), ___numberVar, ___numberVar)

def run():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    ____control()
    ____logic()

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
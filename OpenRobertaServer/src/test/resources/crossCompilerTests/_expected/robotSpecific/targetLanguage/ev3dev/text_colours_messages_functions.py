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
def ____text():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText("", ___numberVar, ___numberVar)
    #
    hal.drawText(str("".join(str(arg) for arg in [___stringVar, ___stringVar])), ___numberVar, ___numberVar)
    ___stringVar += str(___stringVar)

def ____colours():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText(str('black'), ___numberVar, ___numberVar)
    hal.drawText(str('blue'), ___numberVar, ___numberVar)
    hal.drawText(str('green'), ___numberVar, ___numberVar)
    hal.drawText(str('brown'), ___numberVar, ___numberVar)
    hal.drawText(str('none'), ___numberVar, ___numberVar)
    hal.drawText(str('red'), ___numberVar, ___numberVar)
    hal.drawText(str('yellow'), ___numberVar, ___numberVar)
    hal.drawText(str('white'), ___numberVar, ___numberVar)

def ____messages():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    hal.drawText(str(hal.establishConnectionTo(str(___stringVar))), ___numberVar, ___numberVar)
    hal.sendMessage(___connectionVar, str(___stringVar))
    hal.drawText(str(hal.readMessage(___connectionVar)), ___numberVar, ___numberVar)
    hal.drawText(str(hal.waitForConnection()), ___numberVar, ___numberVar)

def ____function_parameter(___x, ___x2, ___x3, ___x4, ___x5, ___x6, ___x7, ___x8, ___x9, ___x10):
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    if ___booleanVar: return None

def ____function_return_numberVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___numberVar

def ____function_return_booleanVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___booleanVar

def ____function_return_stringVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___stringVar

def ____function_return_colourVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___colourVar

def ____function_return_connectionVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___connectionVar

def ____function_return_numberList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___numberList

def ____function_return_booleanList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___booleanList

def ____function_return_stringList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___stringList

def ____function_return_colourList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___colourList

def ____function_return_connectionList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    return ___connectionList

def run():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList
    ____text()
    ____colours()
    ____messages()
    ____function_parameter(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___connectionVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___connectionList)
    hal.drawText(str(____function_return_numberVar()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_booleanVar()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_stringVar()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_colourVar()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_connectionVar()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_numberList()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_booleanList()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_stringList()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_colourList()), ___numberVar, ___numberVar)
    hal.drawText(str(____function_return_connectionList()), ___numberVar, ___numberVar)

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
#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = 0xff0000
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = [0xff0000, 0xff0000]
def ____text():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say("")
    #
    h.say(str("".join(str(arg) for arg in [___stringVar, ___stringVar])))
    ___stringVar += str(___stringVar)

def ____colours():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say(str(0xffffff))
    h.say(str(0xcccccc))
    h.say(str(0xc0c0c0))
    h.say(str(0x999999))
    h.say(str(0x666666))
    h.say(str(0x333333))
    h.say(str(0x000000))
    h.say(str(0xffcccc))
    h.say(str(0xff6666))
    h.say(str(0xff0000))
    h.say(str(0xcc0000))
    h.say(str(0x990000))
    h.say(str(0x660000))
    h.say(str(0x330000))
    h.say(str(0xffcc99))
    h.say(str(0xff9966))
    h.say(str(0xff9900))
    h.say(str(0xcc6600))
    h.say(str(0x993300))
    h.say(str(0x663300))
    h.say(str(0xffff99))
    h.say(str(0xffff66))
    h.say(str(0xffcc66))
    h.say(str(0xffcc33))
    h.say(str(0xcc9933))
    h.say(str(0x996633))
    h.say(str(0x663333))
    h.say(str(0xffffcc))
    h.say(str(0xffff33))
    h.say(str(0xffff00))
    h.say(str(0xffcc00))
    h.say(str(0x999900))
    h.say(str(0x666600))
    h.say(str(0x333300))
    h.say(str(0x99ff99))
    h.say(str(0x66ff99))
    h.say(str(0x33ff33))
    h.say(str(0x33cc00))
    h.say(str(0x009900))
    h.say(str(0x006600))
    h.say(str(0x003300))
    h.say(str(0x99ffff))
    h.say(str(0x33ffff))
    h.say(str(0x66cccc))
    h.say(str(0x00cccc))
    h.say(str(0x339999))
    h.say(str(0x336666))
    h.say(str(0x003333))
    h.say(str(0xccffff))
    h.say(str(0x66ffff))
    h.say(str(0x33ccff))
    h.say(str(0x3366ff))
    h.say(str(0x3333ff))
    h.say(str(0x000099))
    h.say(str(0x000066))
    h.say(str(0xccccff))
    h.say(str(0x9999ff))
    h.say(str(0x6666cc))
    h.say(str(0xcc33cc))
    h.say(str(0x6600cc))
    h.say(str(0x333399))
    h.say(str(0x330099))
    h.say(str(0xffccff))
    h.say(str(0xff99ff))
    h.say(str(0xcc66cc))
    h.say(str(0xcc33cc))
    h.say(str(0x993399))
    h.say(str(0x663366))
    h.say(str(0x330033))
    h.say(str(int("{:02x}{:02x}{:02x}".format(min(max(100, 0), 255), min(max(120, 0), 255), min(max(140, 0), 255)), 16)))

def ____function_parameters(___x, ___x2, ___x3, ___x4, ___x5, ___x6, ___x7, ___x8):
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    if ___booleanVar: return None

def ____function_return_numberVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___numberVar

def ____function_return_booleanVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___booleanVar

def ____function_return_stringVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___stringVar

def ____function_return_colourVar():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___colourVar

def ____function_return_numberList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___numberList

def ____function_return_booleanList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___booleanList

def ____function_return_stringList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___stringList

def ____function_return_colourList():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    return ___colourList


def run():
    h.setAutonomousLife('ON')
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ____text()
    ____colours()
    ____function_parameters(___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList)
    h.say(str(____function_return_numberVar()))
    h.say(str(____function_return_booleanVar()))
    h.say(str(____function_return_stringVar()))
    h.say(str(____function_return_colourVar()))
    h.say(str(____function_return_numberList()))
    h.say(str(____function_return_booleanList()))
    h.say(str(____function_return_stringList()))
    h.say(str(____function_return_colourList()))

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
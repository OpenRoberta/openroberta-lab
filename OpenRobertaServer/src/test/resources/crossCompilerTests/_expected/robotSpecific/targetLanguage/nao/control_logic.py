#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass



___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = 0xff0000
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = [0xff0000, 0xff0000]
def ____control():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
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
        h.wait(___numberVar)
    while ___booleanVar:
        break
    while not ___booleanVar:
        continue
    for ___i in range(int(___numberVar), int(___numberVar), int(___numberVar)):
        pass
    for ___item in ___numberList:
        pass
    for ___item2 in ___booleanList:
        pass
    for ___item3 in ___stringList:
        pass
    for ___item4 in ___colourList:
        pass
    while True:
        if ___booleanVar:
            break
        if ___booleanVar:
            break
        h.wait(15)
    while True:
        if ___booleanVar:
            break
        h.wait(15)

def ____logic():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say(str(___numberVar == ___numberVar))
    h.say(str(___numberVar != ___numberVar))
    h.say(str(___numberVar < ___numberVar))
    h.say(str(___numberVar <= ___numberVar))
    h.say(str(___numberVar > ___numberVar))
    h.say(str(___numberVar >= ___numberVar))
    h.say(str(___booleanVar and ___booleanVar))
    h.say(str(___booleanVar or ___booleanVar))
    h.say(str(not ___booleanVar))
    h.say(str(not ___booleanVar))
    h.say(str(True))
    h.say(str(False))
    h.say(str(None))
    h.say(str(___numberVar if ( ___booleanVar ) else ___numberVar))


def run():
    h.setAutonomousLife('ON')
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ____control()
    ____logic()

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
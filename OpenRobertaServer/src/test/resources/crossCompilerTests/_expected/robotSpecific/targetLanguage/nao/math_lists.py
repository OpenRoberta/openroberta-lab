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
def ____math2():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    h.say(str(0))
    h.say(str(___numberVar + ___numberVar))
    h.say(str(___numberVar - ___numberVar))
    h.say(str(___numberVar * ___numberVar))
    h.say(str(___numberVar / float(___numberVar)))
    h.say(str(math.pow(___numberVar, ___numberVar)))
    h.say(str(math.sqrt(___numberVar)))
    h.say(str(math.fabs(___numberVar)))
    h.say(str(- (___numberVar)))
    h.say(str(math.log(___numberVar)))
    h.say(str(math.log10(___numberVar)))
    h.say(str(math.exp(___numberVar)))
    h.say(str(math.pow(10, ___numberVar)))
    h.say(str(math.sin(___numberVar)))
    h.say(str(math.cos(___numberVar)))
    h.say(str(math.tan(___numberVar)))
    h.say(str(math.asin(___numberVar)))
    h.say(str(math.acos(___numberVar)))
    h.say(str(math.atan(___numberVar)))
    h.say(str(math.pi))
    h.say(str(math.e))
    h.say(str((1 + 5 ** 0.5) / 2))
    h.say(str(math.sqrt(2)))
    h.say(str(math.sqrt(0.5)))
    h.say(str(float('inf')))
    h.say(str((___numberVar % 2) == 0))
    h.say(str((___numberVar % 2) == 1))
    h.say(str(_isPrime(___numberVar)))
    h.say(str((___numberVar % 1) == 0))
    h.say(str(___numberVar > 0))
    h.say(str(___numberVar < 0))
    h.say(str((___numberVar % ___numberVar) == 0))
    ___numberVar += ___numberVar
    h.say(str(round(___numberVar)))
    h.say(str(math.ceil(___numberVar)))
    h.say(str(math.floor(___numberVar)))
    h.say(str(sum(___numberList)))
    h.say(str(min(___numberList)))
    h.say(str(max(___numberList)))
    h.say(str(float(sum(___numberList))/len(___numberList)))
    h.say(str(_median(___numberList)))
    h.say(str(_standard_deviation(___numberList)))
    h.say(str(___numberList[0]))
    h.say(str(___numberVar % ___numberVar))
    h.say(str(min(max(___numberVar, ___numberVar), ___numberVar)))
    h.say(str(random.randint(___numberVar, ___numberVar)))
    h.say(str(random.random()))

def ____lists():
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ___numberList = []
    ___numberList = [0, 0]
    ___numberList = [___numberVar] * 5
    h.say(str(len( ___numberList)))
    h.say(str(not ___numberList))
    h.say(str(___numberList.index(___numberVar)))
    h.say(str((len(___numberList) - 1) - ___numberList[::-1].index(___numberVar)))
    h.say(str(___numberList[___numberVar]))
    h.say(str(___numberList[-1 -___numberVar]))
    h.say(str(___numberList[0]))
    h.say(str(___numberList[-1]))
    h.say(str(___numberList.pop(___numberVar)))
    h.say(str(___numberList.pop(-1 -___numberVar)))
    h.say(str(___numberList.pop(0)))
    h.say(str(___numberList.pop(-1)))
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
    h.say(str(___numberList[___numberVar:___numberVar]))
    h.say(str(___numberList[___numberVar:-1 -___numberVar]))
    h.say(str(___numberList[___numberVar:]))
    h.say(str(___numberList[-1 -___numberVar:___numberVar]))
    h.say(str(___numberList[-1 -___numberVar:-1 -___numberVar]))
    h.say(str(___numberList[-1 -___numberVar:]))
    h.say(str(___numberList[0:___numberVar]))
    h.say(str(___numberList[0:-1 -___numberVar]))
    h.say(str(___numberList[0:]))


def run():
    h.setAutonomousLife('ON')
    global ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___numberList, ___booleanList, ___stringList, ___colourList
    ____math2()
    ____lists()

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

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

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))
if __name__ == "__main__":
    main()
#!/usr/bin/env python3
import math, random, time, requests, threading, sys, io
sys.stdout = io.StringIO()
sys.stderr = io.StringIO()
ROBOTINOIP = "127.0.0.1:80"
PARAMS = {'sid':'robertaProgram'}
MAXSPEED = 0.5
MAXROTATION = 0.57

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

___booleanFalse = None
___booleanTrue = None
___number = None
___numberList = []
___string = None
___numberlist2 = []
def ____lists():
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList, ___string, ___numberlist2
    ___numberList = [0] * 5
    if not ___numberList:
        pass
    ___number = ___numberList.index(0)
    ___number = (len(___numberList) - 1) - ___numberList[::-1].index(0)
    ___number = ___numberList[0]
    ___numberList[0] = 0
    ___numberList = ___numberList[0:2]

def ____math():
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList, ___string, ___numberlist2
    ___number = 0 + 1
    ___number = 0 - 1
    ___number = 0 * 1
    ___number = 0 / float(1)
    ___number = math.pow(0, 1)
    ___number = math.sqrt(1)
    ___number = math.pow(1, 2)
    ___number = - (1)
    ___number = math.log(1)
    ___number = math.log10(1)
    ___number = math.exp(1)
    ___number = math.pow(10, 1)
    ___number = math.sin(1)
    ___number = math.cos(1)
    ___number = math.tan(1)
    ___number = math.asin(1)
    ___number = math.acos(1)
    ___number = math.atan(1)
    if (0 % 2) == 0:
        pass
    if (0 % 2) == 1:
        pass
    if _isPrime(0):
        pass
    if (0 % 1) == 0:
        pass
    if (0 % 1) == 0:
        pass
    if 0 > 0:
        pass
    if 0 < 0:
        pass
    if (0 % 0) == 0:
        pass
    ___number += 1
    ___number = round(math.atan(1))
    ___number = math.ceil(math.atan(1))
    ___number = math.floor(math.atan(1))
    ___number = sum(___numberList)
    ___number = min(___numberList)
    ___number = max(___numberList)
    ___number = float(sum(___numberList))/len(___numberList)
    ___number = _median(___numberList)
    ___number = _standard_deviation(___numberList)
    ___number = ___numberList[0]
    ___number = 1 % 2
    ___number = min(max(1002, 1), 100)
    ___number = random.randint(1, 100)
    ___number = random.random()
    ___string = str(___number)
    ___string = chr((int)(___number))


def run(RV):
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList, ___string, ___numberlist2
    time.sleep(1)
    ___booleanFalse = False
    ___booleanTrue = True
    ___number = 0
    ___numberList = [0, 0, 0]
    ___string = ""
    ___numberlist2 = []

    ____math()
    ____lists()

def step(RV):
    pass

def main(RV):
    try:
        run(RV)
    except Exception as e:
        print(e)
        raise

def start(RV):
    motorDaemon2 = threading.Thread(target=main, daemon=True, args=(RV,), name='mainProgram')
    motorDaemon2.start()

def stop(RV):
    pass

def cleanup(RV):
    pass


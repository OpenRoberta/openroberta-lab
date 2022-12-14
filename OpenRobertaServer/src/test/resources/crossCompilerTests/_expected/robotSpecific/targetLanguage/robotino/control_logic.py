#!/usr/bin/env python3
import math, random, time, requests, threading, sys, io
sys.stdout = io.StringIO()
sys.stderr = io.StringIO()
ROBOTINOIP = "127.0.0.1:80"
PARAMS = {'sid':'robertaProgram'}
MAXSPEED = 0.5
MAXROTATION = 0.57

def isBumped():
    BUMPER_URL = "http://" + ROBOTINOIP + "/data/bumper"
    r = requests.get(url = BUMPER_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data["value"]
    else:
        return -1


___booleanFalse = None
___booleanTrue = None
___number = None
___numberList = []
def ____logic():
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList
    if ___number == ___number:
        pass
    if ___number != ___number:
        pass
    if ___number < ___number:
        pass
    if ___number <= ___number:
        pass
    if ___number > ___number:
        pass
    if ___number >= ___number:
        pass
    if not ___booleanFalse:
        pass
    if True:
        pass
    if False:
        pass
    if None == ___numberList:
        pass
    if ___booleanFalse if ( ___booleanFalse ) else ___booleanTrue:
        pass

def ____control():
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList
    if ___booleanFalse:
        pass
    if ___booleanTrue:
        pass
    while True:
        pass
    for ___k0 in range(int(0), int(___number), int(1)):
        pass
    while not ___booleanFalse:
        pass
    for ___i in range(int(1), int(10), int(1)):
        break
    for ___Element in ___numberList:
        continue
    while True:
        if ___booleanTrue:
            break
        time.sleep(0.2)
    time.sleep(500/1000)
    while True:
        if isBumped() == True:
            break
        time.sleep(0.2)


def run(RV):
    global ___booleanFalse, ___booleanTrue, ___number, ___numberList
    time.sleep(1)
    ___booleanFalse = False
    ___booleanTrue = True
    ___number = 0
    ___numberList = [0, 0, 0]

    ____control()
    ____logic()

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


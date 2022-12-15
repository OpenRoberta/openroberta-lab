#!/usr/bin/env python3
import math, random, time, requests, threading, sys, io
sys.stdout = io.StringIO()
sys.stderr = io.StringIO()
ROBOTINOIP = "127.0.0.1:80"
PARAMS = {'sid':'robertaProgram'}
MAXSPEED = 0.5
MAXROTATION = 0.57

def getAnalogPin(pos):
    ANALOGPIN_URL = "http://" + ROBOTINOIP + "/data/analoginputarray"
    r = requests.get(url = ANALOGPIN_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data[pos-1]
    else:
        return -1

def getCameraLine(RV):
    value = RV.readFloatVector(5)
    if value[0]:
        return (value[1]/640) -0.5
    else:
        return -1

def getColourBlob(RV, inputs):
    RV.writeFloatVector(6, inputs)
    time.sleep(0.001)
    value = RV.readFloatVector(6)
    if value[3] <= 0:
        value = [-1,-1,0,0]
    else:
        value[0] = (value[0]/640) -0.5
        value[1] = (value[1]/480) -0.5
    return value

def getDigitalPin(pos):
    DIGITALPIN_URL = "http://" + ROBOTINOIP + "/data/digitalinputarray"
    r = requests.get(url = DIGITALPIN_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data[pos-1]
    else:
        return -1

def getDistance(port):
    DISTANCES_URL = "http://" + ROBOTINOIP + "/data/distancesensorarray"
    r = requests.get(url = DISTANCES_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data[port-1] * 100
    else:
        return -1

def getMarkerInformation(RV, id):
    RV.writeFloat(3,id)
    time.sleep(0.001)
    value = RV.readFloatVector(4)
    if not value[0]:
        value = [False, -1,-1,-1,-1,-1,-1]
    else:
        for i in range (1,4):
            value[i] = value[i] * 100
    return value[1:4]

def getMarkers(RV):
    markers = RV.readFloatVector(3)
    if len(markers) == 0:
        return [-1]
    return markers

def getOdometry(val):
    ODOMETRY_URL = "http://" + ROBOTINOIP + "/data/odometry"
    r = requests.get(url = ODOMETRY_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        #data: [x,y,rot,vx,vy,omega,seq]
        if val == 'x':
            return data[0]
        elif val == 'y':
            return data[1]
        elif val == 'rot':
            return data[2]
        else:
            return data
    else:
        return -1

def isBumped():
    BUMPER_URL = "http://" + ROBOTINOIP + "/data/bumper"
    r = requests.get(url = BUMPER_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data["value"]
    else:
        return -1

def resetOdometry(RV, x, y, z):
    RV.writeFloatVector(1, [x, y, z, 1])
    time.sleep(0.1)
    RV.writeFloatVector(1, [])

_timer1 = None
_timer2 = None
_timer3 = None
_timer4 = None
_timer5 = None

___Element5 = None
___Element6 = None
___Element7 = None
___Element8 = None
___Element9 = None
___Element10 = None
___Element11 = None
___Element12 = None
___Element13 = None
___Element14 = None
___Element15 = None
___Element16 = None
___Element = None
___Element17 = None
___Element18 = None
___Element19 = None
___Element20 = None
___Element2 = []


def run(RV):
    global _timer1, _timer2, _timer3, _timer4, _timer5, ___Element5, ___Element6, ___Element7, ___Element8, ___Element9, ___Element10, ___Element11, ___Element12, ___Element13, ___Element14, ___Element15, ___Element16, ___Element, ___Element17, ___Element18, ___Element19, ___Element20, ___Element2
    time.sleep(1)
    resetOdometry(RV, 0, 0, 0)
    RV.writeFloat(4, 100)
    time.sleep(0.05)
    _timer1 = time.time()
    _timer2 = time.time()
    _timer3 = time.time()
    _timer4 = time.time()
    _timer5 = time.time()
    ___Element5 = getCameraLine(RV)
    ___Element6 = getDigitalPin(1)
    ___Element7 = getDigitalPin(3)
    ___Element8 = getDigitalPin(4)
    ___Element9 = getDigitalPin(2)
    ___Element10 = getAnalogPin(1)
    ___Element11 = getDigitalPin(6)
    ___Element12 = getOdometry('x') * 100
    ___Element13 = getOdometry('y') * 100
    ___Element14 = getOdometry('rot') * (180 / math.pi)
    ___Element15 = getDistance(1)
    ___Element16 = isBumped()
    ___Element = ((time.time() - _timer1)/1000)
    ___Element17 = ((time.time() - _timer2)/1000)
    ___Element18 = ((time.time() - _timer3)/1000)
    ___Element19 = ((time.time() - _timer4)/1000)
    ___Element20 = ((time.time() - _timer5)/1000)
    ___Element2 = getColourBlob(RV, [40, 56, 42, 100, 53, 100])

    resetOdometry(RV, 0, RV.readFloatVector(1)[1], RV.readFloatVector(1)[2])
    resetOdometry(RV, RV.readFloatVector(1)[0], 0, RV.readFloatVector(1)[2])
    resetOdometry(RV, RV.readFloatVector(1)[0], RV.readFloatVector(1)[1], 0)
    resetOdometry(RV, 0, 0, 0)
    ___Element2 = getMarkers(RV)
    time.sleep(500/1000)
    ___Element2 = getMarkerInformation(RV, 0)
    RV.writeFloat(4, 100)
    time.sleep(0.005)

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


#!/usr/bin/env python3
import math, random, time, requests, threading, sys, io
sys.stdout = io.StringIO()
sys.stderr = io.StringIO()
ROBOTINOIP = "127.0.0.1:80"
PARAMS = {'sid':'robertaProgram'}
MAXSPEED = 0.5
MAXROTATION = 0.57
_digitalPinValues = [0 for i in range(8)]
currentSpeed = [0, 0, 0]

def driveForDistance(RV, x, y, distance):
    angle = math.atan2(y, x)
    angle += RV.readFloatVector(1)[2] * math.pi / 180
    targetX = RV.readFloatVector(1)[0] / 10 + distance * math.cos(angle)
    targetY = RV.readFloatVector(1)[1] / 10 + distance * math.sin(angle)
    resultingSpeed = math.sqrt(math.pow(x, 2) + math.pow(y, 2))
    driveToPosition(RV, targetX, targetY, resultingSpeed)

def driveToPosition(RV, x, y, speed):
    global currentSpeed
    speed = abs(speed)
    RV.writeFloatVector(2, [x * 10, y * 10, RV.readFloatVector(1)[2], 1])
    time.sleep(0.5)
    while RV.readFloatVector(2)[3] != 1 and not isBumped():
        returnedSpeedX = RV.readFloatVector(2)[0] * 10
        returnedSpeedY = RV.readFloatVector(2)[1] * 10
        speedX = (returnedSpeedX * speed / 100)
        speedY = (returnedSpeedY * speed / 100)
        if abs(speedX) < 0.005:
            speedX = toSlowCheck(returnedSpeedX, speedX)
        if abs(speedY) < 0.005:
            speedY = toSlowCheck(returnedSpeedY, speedY)

        currentSpeed = [speedX, speedY, 0]
        time.sleep(0.05)
    currentSpeed = [0, 0, 0]

def toSlowCheck(rawSpeed, percentSpeed):
    if abs(rawSpeed) < 0.005:
        return rawSpeed
    elif percentSpeed < 0:
        return -0.005
    else:
        return 0.005

def isBumped():
    BUMPER_URL = "http://" + ROBOTINOIP + "/data/bumper"
    r = requests.get(url = BUMPER_URL, params = PARAMS)
    if r.status_code == requests.codes.ok:
        data = r.json()
        return data["value"]
    else:
        return -1

def setSpeedOmnidrivePercent(x, y, z):
    global currentSpeed
    max = MAXSPEED
    for index, speed in enumerate([x, y, z]):
        if (index == 2):
            max = MAXROTATION
        if (speed > 100):
            currentSpeed[index] = max
        elif (speed < -100):
            currentSpeed[index] = -max
        else:
            currentSpeed[index] = speed / 100 * max

def postVel():
    global currentSpeed                 
    OMNIDRIVE_URL = "http://" + ROBOTINOIP + "/data/omnidrive"
    r = requests.post(url = OMNIDRIVE_URL, params = PARAMS, json = currentSpeed )

def resetOdometry(RV, x, y, z):
    RV.writeFloatVector(1, [x, y, z, 1])
    time.sleep(0.1)
    RV.writeFloatVector(1, [])

def setDigitalPin(pos, value):
    global _digitalPinValues
    _digitalPinValues[pos-1] = int(value)
    DIGITALPIN_URL = "http://" + ROBOTINOIP + "/data/digitaloutputarray"
    r = requests.post(url = DIGITALPIN_URL, params = PARAMS, json = _digitalPinValues )

def turnForDegrees(RV, speed, degrees):
    if degrees < 0:
        speed = speed * -1
    distance = abs(degrees)
    lastOrientation = RV.readFloatVector(1)[2]
    setSpeedOmnidrivePercent(0, 0, speed)
    while distance > 0:
        orientation = RV.readFloatVector(1)[2]
        traveledDegrees = abs(orientation - lastOrientation)
        if ((speed > 0 and lastOrientation > orientation) or (speed < 0 and lastOrientation < orientation)) and traveledDegrees > 300:
            traveledDegrees = 360 - traveledDegrees
        distance -= traveledDegrees
        lastOrientation = orientation
        time.sleep(0.05)
    setSpeedOmnidrivePercent(0, 0, 0)


def run(RV):
    time.sleep(1)
    resetOdometry(RV, 0, 0, 0)

    driveForDistance(RV, 30, 0, 30)
    time.sleep(500/1000)
    turnForDegrees(RV, -30, 20)
    driveToPosition(RV, 10, 10, 30)
    setSpeedOmnidrivePercent(30, 0, 0)
    setSpeedOmnidrivePercent(0, 0, 0)
    print("Hallo")
    setDigitalPin(1, True)

def step(RV):
    postVel()

def main(RV):
    try:
        run(RV)
    except Exception as e:
        print(e)
        raise
    finally:
        global _digitalPinValues
        setSpeedOmnidrivePercent(0,0,0)
        _digitalPinValues = [0 for i in range(8)]
        setDigitalPin(1, False)


def start(RV):
    motorDaemon2 = threading.Thread(target=main, daemon=True, args=(RV,), name='mainProgram')
    motorDaemon2.start()

def stop(RV):
    pass

def cleanup(RV):
    pass


import Ed
Ed.EdisonVersion = Ed.V2
Ed.DistanceUnits = Ed.CM
Ed.Tempo = Ed.TEMPO_SLOW
obstacleDetectionOn = False
Ed.LineTrackerLed(Ed.ON)
Ed.ReadClapSensor()
Ed.ReadLineState()
Ed.TimeWait(250, Ed.TIME_MILLISECONDS)

___numberVar = 0
___booleanVar = True
___numberList = Ed.List(3, [0,0,0])

def ____sensors():
    global ___numberVar, ___booleanVar, ___numberList
    ___booleanVar = (Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE)
    ___booleanVar = (Ed.ReadKeypad() == Ed.KEYPAD_ROUND)
    ___booleanVar = _obstacleDetection(Ed.OBSTACLE_LEFT)
    ___booleanVar = _obstacleDetection(Ed.OBSTACLE_RIGHT)
    ___booleanVar = _obstacleDetection(Ed.OBSTACLE_AHEAD)
    ___numberVar = _irSeek(1)
    ___numberVar = Ed.ReadLeftLightLevel() / 10
    ___numberVar = Ed.ReadRightLightLevel() / 10
    ___numberVar = Ed.ReadLineTracker() / 10
    ___booleanVar = (Ed.ReadLineState() == Ed.LINE_ON_BLACK)
    ___booleanVar = (Ed.ReadClapSensor() == Ed.CLAP_DETECTED)
    Ed.ReadObstacleDetection()
    Ed.ReadKeypad()
    Ed.ReadClapSensor()
    Ed.ReadRemote()
    Ed.ReadIRData()

def ____sensorWaitUntil():
    global ___numberVar, ___booleanVar, ___numberList
    while True:
        if ((Ed.ReadKeypad() == Ed.KEYPAD_TRIANGLE)) == True:
            break
        pass
    while True:
        if ((Ed.ReadKeypad() == Ed.KEYPAD_ROUND)) == True:
            break
        pass
    while True:
        if (_obstacleDetection(Ed.OBSTACLE_LEFT)) == True:
            break
        pass
    while True:
        if (_obstacleDetection(Ed.OBSTACLE_RIGHT)) == True:
            break
        pass
    while True:
        if (_obstacleDetection(Ed.OBSTACLE_AHEAD)) == True:
            break
        pass
    while True:
        if (_irSeek(1)) < 30:
            break
        pass
    while True:
        if (Ed.ReadLeftLightLevel() / 10) < 30:
            break
        pass
    while True:
        if (Ed.ReadRightLightLevel() / 10) < 30:
            break
        pass
    while True:
        if (Ed.ReadLineTracker() / 10) < 30:
            break
        pass
    while True:
        if ((Ed.ReadLineState() == Ed.LINE_ON_BLACK)) == True:
            break
        pass
    while True:
        if ((Ed.ReadClapSensor() == Ed.CLAP_DETECTED)) == True:
            break
        pass

____sensors()
____sensorWaitUntil()


def _irSeek(mode):
    global obstacleDetectionOn
    if (obstacleDetectionOn == True):
        Ed.ObstacleDetectionBeam(Ed.OFF)
        obstacleDetectionOn = False
    if (mode == 0): 
        return Ed.ReadIRData()
    elif (mode == 1): 
        return Ed.ReadRemote()

def _obstacleDetection(mode):
    global obstacleDetectionOn
    if (obstacleDetectionOn == False):
        Ed.ObstacleDetectionBeam(Ed.ON)
        obstacleDetectionOn = True
    return Ed.ReadObstacleDetection() == mode

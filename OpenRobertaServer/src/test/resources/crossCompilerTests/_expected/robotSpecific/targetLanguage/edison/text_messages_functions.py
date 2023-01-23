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

def ____text():
    global ___numberVar, ___booleanVar, ___numberList
    #

def ____messages():
    global ___numberVar, ___booleanVar, ___numberList
    _irSend(___numberVar)
    _irSend(_irSeek(0))

def ____function_parameters(___x, ___x2, ___x3):
    global ___numberVar, ___booleanVar, ___numberList
    if ___booleanVar: return

def ____function_return_numberVar():
    global ___numberVar, ___booleanVar, ___numberList
    return ___numberVar

def ____function_return_booleanVar():
    global ___numberVar, ___booleanVar, ___numberList
    return ___booleanVar

def ____function_return_numberList():
    global ___numberVar, ___booleanVar, ___numberList
    return ___numberList

____text()
____messages()
____function_parameters(___numberVar, ___booleanVar, ___numberList)
___numberVar = ____function_return_numberVar()
___booleanVar = ____function_return_booleanVar()
___numberList = ____function_return_numberList()


def _irSeek(mode):
    global obstacleDetectionOn
    if (obstacleDetectionOn == True):
        Ed.ObstacleDetectionBeam(Ed.OFF)
        obstacleDetectionOn = False
    if (mode == 0): 
        return Ed.ReadIRData()
    elif (mode == 1): 
        return Ed.ReadRemote()

def _irSend(payload):
    global obstacleDetectionOn
    if (obstacleDetectionOn == True):
        Ed.ObstacleDetectionBeam(Ed.OFF)
        obstacleDetectionOn = False
    Ed.SendIRData(payload)

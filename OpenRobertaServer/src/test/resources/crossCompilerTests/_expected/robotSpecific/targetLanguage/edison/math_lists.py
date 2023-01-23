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

def ____math():
    global ___numberVar, ___booleanVar, ___numberList
    ___numberVar = 0
    ___numberVar = ___numberVar + ___numberVar
    ___numberVar = ___numberVar - ___numberVar
    ___numberVar = ___numberVar * ___numberVar
    ___numberVar = ___numberVar / ___numberVar
    ___numberVar = _pow(___numberVar, ___numberVar)
    ___numberVar = _abs(___numberVar)
    ___numberVar = - (___numberVar)
    ___numberVar = _pow(10, ___numberVar)
    ___booleanVar = (___numberVar % 2) == 0
    ___booleanVar = (___numberVar % 2) == 1
    ___booleanVar = _isPrime(___numberVar)
    ___booleanVar = ___numberVar > 0
    ___booleanVar = ___numberVar < 0
    ___booleanVar = (___numberVar % ___numberVar) == 0
    ___numberVar += ___numberVar
    ___numberVar = ((___numberVar+5)/10)*10
    ___numberVar = ((___numberVar/10)+1)*10
    ___numberVar = (___numberVar/10)
    ___numberVar = sum(___numberList)
    ___numberVar = min(___numberList)
    ___numberVar = max(___numberList)
    ___numberVar = sum(___numberList) / len(___numberList)
    ___numberVar = ___numberVar % ___numberVar

def ____lists():
    global ___numberVar, ___booleanVar, ___numberList
    ___numberVar = len( ___numberList)
    ___numberVar = ___numberList[___numberVar]
    ___numberList[___numberVar] = ___numberVar

____math()
____lists()


def _abs(num):
    if (num < 0): 
        return -num
    else: 
        return num

def max(list):
    listMax = list[0]
    listLength = len(list)
    for i in range(listLength):
        if list[i] > listMax:
            listMax = list[i]
    return listMax

def min(list):
    listMin = list[0]
    listLength = len(list)
    for i in range(listLength):
        if list[i] < listMin:
            listMin = list[i]
    return listMin

def _pow(base, exp):
    result = 1
    b = base
    for _ in range(exp):
        result *= b
    return result

def _isPrime(num):
    if num <= 1: 
        return False
    newNum = num - 2
    for x in range(newNum):
        y = (x + 2)
        if (num % y) == 0: 
            return False
    return True

def sum(list):
    listSum = 0
    listLength = len(list)
    for i in range(listLength): 
        listSum = (listSum + list[i])
    return listSum

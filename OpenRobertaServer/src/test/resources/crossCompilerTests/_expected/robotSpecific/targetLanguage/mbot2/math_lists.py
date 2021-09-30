import cyberpi
import time
import math, random

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

___numberVar = 0
___booleanVar = True
___stringVar = ""
___colorVar = (204, 0, 0)
___numberList = [0, 0, 0]
___booleanList = [True, True, True]
___stringList = ["", "", ""]
___colorList = [(204, 0, 0), (204, 0, 0), (204, 0, 0)]
def lists():
    global ___numberVar, ___booleanVar, ___stringVar, ___colorVar, ___numberList, ___booleanList, ___stringList, ___colorList
    ___numberList = []
    ___booleanList = []
    ___stringList = []
    ___colorList = []
    ___numberList = [0, 0]
    ___booleanList = [True, True]
    ___stringList = ["", ""]
    ___colorList = [(204, 0, 0), (204, 0, 0)]
    cyberpi.console.println([___numberVar] * 5)
    cyberpi.console.println(len( ___numberList))
    cyberpi.console.println(not ___numberList)
    cyberpi.console.println(___numberList.index(___numberVar))
    cyberpi.console.println((len(___numberList) - 1) - ___numberList[::-1].index(___numberVar))
    cyberpi.console.println(___numberList[___numberVar])
    cyberpi.console.println(___numberList[-1 -___numberVar])
    cyberpi.console.println(___numberList[0])
    cyberpi.console.println(___numberList[-1])
    cyberpi.console.println(___numberList.pop(___numberVar))
    cyberpi.console.println(___numberList.pop(-1 -___numberVar))
    cyberpi.console.println(___numberList.pop(0))
    cyberpi.console.println(___numberList.pop(-1))
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
    cyberpi.console.println(___numberList[___numberVar:___numberVar])
    cyberpi.console.println(___numberList[___numberVar:-1 -___numberVar])
    cyberpi.console.println(___numberList[___numberVar:])
    cyberpi.console.println(___numberList[-1 -___numberVar:___numberVar])
    cyberpi.console.println(___numberList[-1 -___numberVar:-1 -___numberVar])
    cyberpi.console.println(___numberList[-1 -___numberVar:])
    cyberpi.console.println(___numberList[0:___numberVar])
    cyberpi.console.println(___numberList[0:-1 -___numberVar])
    cyberpi.console.println(___numberList[0:])

def math():
    global ___numberVar, ___booleanVar, ___stringVar, ___colorVar, ___numberList, ___booleanList, ___stringList, ___colorList
    cyberpi.console.println(0)
    cyberpi.console.println(___numberVar + ___numberVar)
    cyberpi.console.println(___numberVar - ___numberVar)
    cyberpi.console.println(___numberVar * ___numberVar)
    cyberpi.console.println(___numberVar / float(___numberVar))
    cyberpi.console.println(math.pow(___numberVar, ___numberVar))
    cyberpi.console.println(math.sqrt(___numberVar))
    cyberpi.console.println(math.pow(___numberVar, 2))
    cyberpi.console.println(math.fabs(___numberVar))
    cyberpi.console.println(- (___numberVar))
    cyberpi.console.println(math.log(___numberVar))
    cyberpi.console.println(math.log10(___numberVar))
    cyberpi.console.println(math.exp(___numberVar))
    cyberpi.console.println(math.pow(10, ___numberVar))
    cyberpi.console.println(math.sin(___numberVar))
    cyberpi.console.println(math.cos(___numberVar))
    cyberpi.console.println(math.tan(___numberVar))
    cyberpi.console.println(math.asin(___numberVar))
    cyberpi.console.println(math.acos(___numberVar))
    cyberpi.console.println(math.atan(___numberVar))
    cyberpi.console.println(math.pi)
    cyberpi.console.println(math.e)
    cyberpi.console.println((1 + 5 ** 0.5) / 2)
    cyberpi.console.println(___numberVar)
    cyberpi.console.println(math.sqrt(0.5))
    cyberpi.console.println(float('inf'))
    cyberpi.console.println((___numberVar % 2) == 0)
    cyberpi.console.println((___numberVar % 2) == 1)
    cyberpi.console.println(_isPrime(___numberVar))
    cyberpi.console.println((___numberVar % 1) == 0)
    cyberpi.console.println(___numberVar > 0)
    cyberpi.console.println(___numberVar < 0)
    cyberpi.console.println((___numberVar % ___numberVar) == 0)
    ___numberVar += ___numberVar
    cyberpi.console.println(round(___numberVar))
    cyberpi.console.println(math.ceil(___numberVar))
    cyberpi.console.println(math.floor(___numberVar))
    cyberpi.console.println(sum(___numberList))
    cyberpi.console.println(min(___numberList))
    cyberpi.console.println(max(___numberList))
    cyberpi.console.println(float(sum(___numberList))/len(___numberList))
    cyberpi.console.println(_median(___numberList))
    cyberpi.console.println(_standard_deviation(___numberList))
    cyberpi.console.println(___numberList[0])
    cyberpi.console.println(___numberVar % ___numberVar)
    cyberpi.console.println(min(max(___numberVar, ___numberVar), ___numberVar))
    cyberpi.console.println(random.randint(___numberVar, ___numberVar))
    cyberpi.console.println(random.random())
    cyberpi.console.println(str(___numberVar))
    cyberpi.console.println(chr((int)(___numberVar)))

def run():
    global ___numberVar, ___booleanVar, ___stringVar, ___colorVar, ___numberList, ___booleanList, ___stringList, ___colorList
    math()
    lists()

def main():
    try:
        run()
    except Exception as e:
        raise
main()
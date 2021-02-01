import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___numberVar = 0
___booleanVar = True
___stringVar = ""
___imageVar = microbit.Image.HEART
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___imageList = [microbit.Image.HEART, microbit.Image.HEART]
def math2():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.scroll(str(0))
    microbit.display.scroll(str(___numberVar + ___numberVar))
    microbit.display.scroll(str(___numberVar - ___numberVar))
    microbit.display.scroll(str(___numberVar * ___numberVar))
    microbit.display.scroll(str(___numberVar / float(___numberVar)))
    microbit.display.scroll(str(math.pow(___numberVar, ___numberVar)))
    microbit.display.scroll(str(math.sqrt(___numberVar)))
    microbit.display.scroll(str(math.fabs(___numberVar)))
    microbit.display.scroll(str(- (___numberVar)))
    microbit.display.scroll(str(math.log(___numberVar)))
    microbit.display.scroll(str(math.log10(___numberVar)))
    microbit.display.scroll(str(math.exp(___numberVar)))
    microbit.display.scroll(str(math.pow(10, ___numberVar)))
    microbit.display.scroll(str(math.sin(___numberVar)))
    microbit.display.scroll(str(math.cos(___numberVar)))
    microbit.display.scroll(str(math.tan(___numberVar)))
    microbit.display.scroll(str(math.asin(___numberVar)))
    microbit.display.scroll(str(math.acos(___numberVar)))
    microbit.display.scroll(str(math.atan(___numberVar)))
    microbit.display.scroll(str(math.pi))
    microbit.display.scroll(str(math.e))
    microbit.display.scroll(str((1 + 5 ** 0.5) / 2))
    microbit.display.scroll(str(math.sqrt(2)))
    microbit.display.scroll(str(math.sqrt(0.5)))
    microbit.display.scroll(str((___numberVar % 2) == 0))
    microbit.display.scroll(str((___numberVar % 2) == 1))
    microbit.display.scroll(str(_isPrime(___numberVar)))
    microbit.display.scroll(str((___numberVar % 1) == 0))
    microbit.display.scroll(str(___numberVar > 0))
    microbit.display.scroll(str(___numberVar < 0))
    microbit.display.scroll(str((___numberVar % ___numberVar) == 0))
    ___numberVar += ___numberVar
    microbit.display.scroll(str(round(___numberVar)))
    microbit.display.scroll(str(math.ceil(___numberVar)))
    microbit.display.scroll(str(math.floor(___numberVar)))
    microbit.display.scroll(str(sum(___numberList)))
    microbit.display.scroll(str(min(___numberList)))
    microbit.display.scroll(str(max(___numberList)))
    microbit.display.scroll(str(float(sum(___numberList))/len(___numberList)))
    microbit.display.scroll(str(_median(___numberList)))
    microbit.display.scroll(str(_standard_deviation(___numberList)))
    microbit.display.scroll(str(___numberList[0]))
    microbit.display.scroll(str(___numberVar % ___numberVar))
    microbit.display.scroll(str(min(max(___numberVar, ___numberVar), ___numberVar)))
    microbit.display.scroll(str(random.randint(___numberVar, ___numberVar)))
    microbit.display.scroll(str(random.random()))

def lists():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    ___numberList = []
    ___numberList = [___numberVar, ___numberVar, ___numberVar]
    microbit.display.scroll(str(len( ___numberList)))
    microbit.display.scroll(str(not ___numberList))
    microbit.display.scroll(str(___numberList.index(___numberVar)))
    microbit.display.scroll(str((len(___numberList) - 1) - ___numberList[::-1].index(___numberVar)))
    microbit.display.scroll(str(___numberList[___numberVar]))
    microbit.display.scroll(str(___numberList[-1 -___numberVar]))
    microbit.display.scroll(str(___numberList[0]))
    microbit.display.scroll(str(___numberList[-1]))
    microbit.display.scroll(str(___numberList.pop(___numberVar)))
    microbit.display.scroll(str(___numberList.pop(-1 -___numberVar)))
    microbit.display.scroll(str(___numberList.pop(0)))
    microbit.display.scroll(str(___numberList.pop(-1)))
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
    ___numberList = ___numberList[___numberVar:___numberVar]
    ___numberList = ___numberList[___numberVar:-1 -___numberVar]
    ___numberList = ___numberList[___numberVar:]
    ___numberList = ___numberList[-1 -___numberVar:___numberVar]
    ___numberList = ___numberList[-1 -___numberVar:-1 -___numberVar]
    ___numberList = ___numberList[-1 -___numberVar:]
    ___numberList = ___numberList[0:___numberVar]
    ___numberList = ___numberList[0:-1 -___numberVar]
    ___numberList = ___numberList[0:]

def run():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    math2()
    lists()

def main():
    try:
        run()
    except Exception as e:
        raise

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
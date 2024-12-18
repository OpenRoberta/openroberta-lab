import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()

___numberVar = 0
___booleanVar = True
___stringVar = ""
___colourVar = (255, 0, 0)
___imageVar = calliopemini.Image.HEART
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___colourList = [(255, 0, 0), (255, 0, 0)]
___imageList = [calliopemini.Image.HEART, calliopemini.Image.HEART]
___item2 = [0, 0, 0]

def ____lists():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___imageList, ___item2
    calliopemini.display.scroll(str(len( ___numberList)))
    calliopemini.display.scroll(str(not ___numberList))
    calliopemini.display.scroll(str(___numberList.index(___numberVar)))
    calliopemini.display.scroll(str((len(___numberList) - 1) - ___numberList[::-1].index(___numberVar)))
    calliopemini.display.scroll(str(___numberList[___numberVar]))
    calliopemini.display.scroll(str(___numberList[-1 -___numberVar]))
    calliopemini.display.scroll(str(___numberList[0]))
    calliopemini.display.scroll(str(___numberList[-1]))
    calliopemini.display.scroll(str(___numberList.pop(___numberVar)))
    calliopemini.display.scroll(str(___numberList.pop(-1 -___numberVar)))
    calliopemini.display.scroll(str(___numberList.pop(0)))
    calliopemini.display.scroll(str(___numberList.pop(-1)))
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
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___colourVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___colourList, ___imageList, ___item2
    ____lists()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
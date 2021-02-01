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
def control():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    if ___booleanVar:
        pass
    elif ___booleanVar:
        pass
    if ___booleanVar:
        pass
    elif ___booleanVar:
        pass
    while True:
        pass
    for ___k0 in range(int(0), int(___numberVar), int(1)):
        pass
    for ___i in range(int(___numberVar), int(___numberVar), int(___numberVar)):
        pass
    while True:
        break
    while True:
        continue
    microbit.sleep(___numberVar)
    while ___booleanVar:
        pass
    while not ___booleanVar:
        pass
    for ___item in ___numberList:
        pass
    for ___item2 in ___booleanList:
        pass
    for ___item3 in ___stringList:
        pass
    for ___item4 in ___imageList:
        pass
    while True:
        if ___booleanVar:
            break
        if ___booleanVar:
            break
    while True:
        if ___booleanVar:
            break

def logic():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.scroll(str(___numberVar == ___numberVar))
    microbit.display.scroll(str(___numberVar != ___numberVar))
    microbit.display.scroll(str(___numberVar < ___numberVar))
    microbit.display.scroll(str(___numberVar <= ___numberVar))
    microbit.display.scroll(str(___numberVar > ___numberVar))
    microbit.display.scroll(str(___numberVar > ___numberVar))
    microbit.display.scroll(str(___booleanVar and ___booleanVar))
    microbit.display.scroll(str(___booleanVar or ___booleanVar))
    microbit.display.scroll(str(not ___booleanVar))
    microbit.display.scroll(str(True))
    microbit.display.scroll(str(False))
    microbit.display.scroll(str(___numberVar if ( ___booleanVar ) else ___numberVar))

def run():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    control()
    logic()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
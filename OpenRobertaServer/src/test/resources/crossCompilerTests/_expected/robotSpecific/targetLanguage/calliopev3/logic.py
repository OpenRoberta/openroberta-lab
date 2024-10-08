import calliopemini
import random
import math


def ____logic():
    global timer1, ___numberVar, ___booleanVar
    calliopemini.display.scroll(str(___numberVar == ___numberVar))
    calliopemini.display.scroll(str(___numberVar != ___numberVar))
    calliopemini.display.scroll(str(___numberVar < ___numberVar))
    calliopemini.display.scroll(str(___numberVar <= ___numberVar))
    calliopemini.display.scroll(str(___numberVar > ___numberVar))
    calliopemini.display.scroll(str(___numberVar >= ___numberVar))
    calliopemini.display.scroll(str(___booleanVar and ___booleanVar))
    calliopemini.display.scroll(str(___booleanVar or ___booleanVar))
    calliopemini.display.scroll(str(not ___booleanVar))
    calliopemini.display.scroll(str(True))
    calliopemini.display.scroll(str(False))
    calliopemini.display.scroll(str(___numberVar if ( ___booleanVar ) else ___numberVar))

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___numberVar = 0
___booleanVar = True

def run():
    global timer1, ___numberVar, ___booleanVar
    ____logic()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
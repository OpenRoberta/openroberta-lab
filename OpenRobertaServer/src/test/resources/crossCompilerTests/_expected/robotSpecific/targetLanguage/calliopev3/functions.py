import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()

___Element = 0

def ____macheEtwas():
    global timer1, ___Element
    calliopemini.display.scroll("Hallo")

def ____macheEtwas2():
    global timer1, ___Element
    calliopemini.display.scroll("Hallo")
    return 0

def run():
    global timer1, ___Element
    ____macheEtwas()
    ___Element = ____macheEtwas2()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
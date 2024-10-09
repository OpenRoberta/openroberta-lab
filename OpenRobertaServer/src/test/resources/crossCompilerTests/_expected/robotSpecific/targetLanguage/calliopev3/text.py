import calliopemini
import random
import math


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___Element = ["", "", ""]
___Element2 = ""

def run():
    global timer1, ___Element, ___Element2
    calliopemini.display.scroll("test")
    # test
    calliopemini.display.scroll(str("".join(str(arg) for arg in ["test", "test"])))
    calliopemini.display.scroll("Hallo")
    ___Element2 += str("test")
    calliopemini.display.scroll(str(ord("test"[0])))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___number = 0
___boolean2 = True
___string = ""
def run():
    global timer1, ___number, ___boolean2, ___string
    # Variable Test START
    ___number = 0 + 5
    ___number = 3 + float(0.999999999999)
    ___string = "abc"
    ___string = "123"
    ___string = "\u00B3\u00BD\u00B9]"
    ___boolean2 = not True
    # Variable Test START END

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
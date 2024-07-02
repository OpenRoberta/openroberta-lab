import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___n1 = 0
___b = False
___n2 = 1
___n3 = 4
def ____number():
    global timer1, ___n1, ___b, ___n2, ___n3
    ___n1 = ___n2 + ___n3

def ____breakFunct():
    global timer1, ___n1, ___b, ___n2, ___n3
    if 5 == ___n1: return None
    ___n1 = ___n1 + 1000

def ____retBool():
    global timer1, ___n1, ___b, ___n2, ___n3
    ___n1 = ___n1
    return ___b

def ____retNumber():
    global timer1, ___n1, ___b, ___n2, ___n3
    ___n1 = ___n1
    return ___n1

def ____retNumber2(___x):
    global timer1, ___n1, ___b, ___n2, ___n3
    ___x = ___x / float(2)
    return ___x

def run():
    global timer1, ___n1, ___b, ___n2, ___n3
    # Basic Functions START
    ____number()
    ____breakFunct()
    if not 5 == ___n1:
        print("Assertion failed: ", "pos-1", 5, "EQ", ___n1)
    ___n1 = ____retNumber()
    ___b = ____retBool()
    ___n1 = ____retNumber2(10)
    # Basic Functions END

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
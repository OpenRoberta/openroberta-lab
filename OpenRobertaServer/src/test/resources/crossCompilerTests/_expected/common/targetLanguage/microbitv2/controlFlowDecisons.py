import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___X = 0

def run():
    global timer1, ___X
    if not 0 == ___X:
        print("Assertion failed: ", "pos-0", 0, "EQ", ___X)
    if True:
        ___X += 1
    if not 1 == ___X:
        print("Assertion failed: ", "pos-1", 1, "EQ", ___X)
    if False:
        ___X += 1000
    if not 1 == ___X:
        print("Assertion failed: ", "pos-2", 1, "EQ", ___X)
    if True:
        if True:
            ___X += 1
        ___X += 1
    if not 3 == ___X:
        print("Assertion failed: ", "pos-3", 3, "EQ", ___X)
    if True:
        if False:
            ___X += 1000
        ___X += 1
    if not 4 == ___X:
        print("Assertion failed: ", "pos-4", 4, "EQ", ___X)
    if False:
        if False:
            ___X += 1000
        ___X += 1000
    if not 4 == ___X:
        print("Assertion failed: ", "pos-5", 4, "EQ", ___X)
    if False:
        if True:
            ___X += 1000
        ___X += 1000
    if not 4 == ___X:
        print("Assertion failed: ", "pos-6", 4, "EQ", ___X)
    if True:
        if True:
            if False:
                ___X += 1000
            ___X += 1
        ___X += 1
    if not 6 == ___X:
        print("Assertion failed: ", "pos-7", 6, "EQ", ___X)
    if True:
        ___X += 1
    elif False:
        ___X += 1000
    if not 7 == ___X:
        print("Assertion failed: ", "pos-8", 7, "EQ", ___X)
    if False:
        ___X += 1000
    elif True:
        ___X += 1
    if not 8 == ___X:
        print("Assertion failed: ", "pos-9", 8, "EQ", ___X)
    if True:
        ___X += 1
    else:
        ___X += 1000
    if not 9 == ___X:
        print("Assertion failed: ", "pos-10", 9, "EQ", ___X)
    if False:
        ___X += 1000
    else:
        ___X += 1
    if not 10 == ___X:
        print("Assertion failed: ", "pos-11", 10, "EQ", ___X)
    if True:
        ___X += 1
    elif True:
        ___X += 1000
    else:
        ___X += 1000
    if not 11 == ___X:
        print("Assertion failed: ", "pos-12", 11, "EQ", ___X)
    if False:
        ___X += 1000
    elif False:
        ___X += 1000
    else:
        ___X += 1
    if not 12 == ___X:
        print("Assertion failed: ", "pos-13", 12, "EQ", ___X)
    if True:
        ___X += 1
    elif False:
        ___X += 1000
    else:
        ___X += 1000
    if not 14 == ___X:
        print("Assertion failed: ", "pos-14", 14, "EQ", ___X)
    if False:
        ___X += 1000
    elif True:
        ___X += 1
    else:
        ___X += 1000
    if not 14 == ___X:
        print("Assertion failed: ", "pos-15", 14, "EQ", ___X)
    print("Control Flow Test: success" if ( 14 == ___X ) else "Control Flow Test: FAIL")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
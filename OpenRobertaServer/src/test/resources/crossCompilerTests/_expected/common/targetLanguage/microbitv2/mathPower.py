import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___result = False

def run():
    global timer1, ___result
    # Math power -- Start --
    ___result = 1 == math.pow(2, 0)
    if not True == ___result:
        print("Assertion failed: ", "pos-1", True, "EQ", ___result)
    ___result = 2 == math.pow(2, 1)
    if not True == ___result:
        print("Assertion failed: ", "pos-2", True, "EQ", ___result)
    ___result = 4 == math.pow(2, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-3", True, "EQ", ___result)
    ___result = 8 == math.pow(2, 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-4", True, "EQ", ___result)
    ___result = -4 == ( - (math.pow(2, 2)) )
    if not True == ___result:
        print("Assertion failed: ", "pos-5", True, "EQ", ___result)
    ___result = 4 == math.pow(-2, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-6", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) * math.pow(2, 3) ) == math.pow(2, 2 + 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-7", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) * math.pow(3, 2) ) == math.pow(2 * 3, 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-8", True, "EQ", ___result)
    ___result = math.pow(math.pow(2, 2), 3) == math.pow(2, 2 * 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-9", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) / float(math.pow(3, 2)) ) == math.pow(2 / float(3), 2)
    if not True == ___result:
        print("Assertion failed: ", "pos-10", True, "EQ", ___result)
    ___result = ( math.pow(2, 2) / float(math.pow(2, 3)) ) == math.pow(2, 2 - 3)
    if not True == ___result:
        print("Assertion failed: ", "pos-11", True, "EQ", ___result)
    print("Math Power Test: success" if ( True == ___result ) else "Basic Math Test: FAIL")
    # Math power -- End --

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
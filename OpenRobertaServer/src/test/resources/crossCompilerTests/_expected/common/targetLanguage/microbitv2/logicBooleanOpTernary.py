import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___x = 0

def run():
    global timer1, ___x
    # logik_boolean_op-- Start
    ___x = ___x + 1 if ( True ) else ___x + 1000
    ___x = ___x + 1000 if ( False ) else ___x + 1
    if not 2 == ___x:
        print("Assertion failed: ", "pos-1", 2, "EQ", ___x)
    ___x = ___x + 1 if ( True if ( True ) else False ) else ___x + 1000
    ___x = ___x + 1 if ( False if ( False ) else True ) else ___x + 1000
    if not 4 == ___x:
        print("Assertion failed: ", "pos-2", 4, "EQ", ___x)
    ___x = ___x + 1 if ( True if ( True if ( True ) else False ) else False ) else ___x + 1000
    if not 5 == ___x:
        print("Assertion failed: ", "pos-3", 5, "EQ", ___x)
    ___x = ___x + 1 if ( False if ( True if ( 1 == 2 ) else False ) else True ) else ___x + 1000
    print("Logic Ternary Op Test: success" if ( 6 == ___x ) else "Logic Ternary Op Test: FAIL")
    # Logic Ternary Op -- End

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
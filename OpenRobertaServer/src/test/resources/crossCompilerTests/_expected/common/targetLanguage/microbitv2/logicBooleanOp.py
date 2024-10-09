import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___x = 0

def run():
    global timer1, ___x
    # Logic Boolean Operators -- Start
    if True and True:
        ___x += 1
    if True and False:
        ___x += 1000
    if False and True:
        ___x += 1000
    if False and False:
        ___x += 1000
    if not 1 == ___x:
        print("Assertion failed: ", "pos-1", 1, "EQ", ___x)
    if not (True and True):
        ___x += 1000
    if not (True and False):
        ___x += 1
    if not (False and True):
        ___x += 1
    if not (False and False):
        ___x += 1
    if not 4 == ___x:
        print("Assertion failed: ", "pos-2", 4, "EQ", ___x)
    if True or True:
        ___x += 1
    if True or False:
        ___x += 1
    if False or True:
        ___x += 1
    if False or False:
        ___x += 1000
    if not 7 == ___x:
        print("Assertion failed: ", "pos-3", 7, "EQ", ___x)
    if not (True or True):
        ___x += 1000
    if not (True or False):
        ___x += 1000
    if not (False or True):
        ___x += 1000
    if not (False or False):
        ___x += 1
    if not 8 == ___x:
        print("Assertion failed: ", "pos-4", 8, "EQ", ___x)
    if ( True and True ) and ( True and True ):
        ___x += 1
    if ( True and False ) or ( False and True ):
        ___x += 1000
    if not (True or True) and not (True or True):
        ___x += 1000
    if not (True and False) or not (True and False):
        ___x += 1
    print("Logic Boolean operators Test: success" if ( 10 == ___x ) else "Logic Boolean operators Test: FAIL")
    # Logic Boolean Operators -- End

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
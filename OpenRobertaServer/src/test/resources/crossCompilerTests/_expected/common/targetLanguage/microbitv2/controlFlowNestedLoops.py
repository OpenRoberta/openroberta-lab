import microbit
import random
import math


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___X = 0

def run():
    global timer1, ___X
    # Control Flow Nested Loop --Start
    while True:
        while not (___X >= 20):
            for ___i in range(int(1), int(11), int(1)):
                for ___k0 in range(int(0), int(2), int(1)):
                    if (___i % 2) == 0:
                        continue
                    ___X += 1
        break
    if not 20 == ___X:
        print("Assertion failed: ", "pos-1", 20, "EQ", ___X)
    for ___j in range(int(1), int(4), int(3)):
        ___X += 1
        if not 21 == ___X:
            print("Assertion failed: ", " = X", 21, "EQ", ___X)
        for ___k in range(int(1), int(5), int(3)):
            ___X += 1
    if not 23 == ___X:
        print("Assertion failed: ", "pos-2", 23, "EQ", ___X)
    while True:
        while True:
            if 23 == ___X:
                while True:
                    if (___X % 2) == 0:
                        continue
                    ___X += 1
                    break
                if (___X % 2) == 0:
                    break
                ___X += 1000
            break
        break
    print("Control Flow Nested Loops Test:success" if ( 24 == ___X ) else "ntrol Flow Nested Loops Test: FAIL")
    while True:
        try:
            while True:
                if True:
                    raise BreakOutOfALoop
                    break
                if True:
                    break
        except BreakOutOfALoop:
            break
        except ContinueLoop:
            continue
    # Control Flow Nested Loop -- End

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
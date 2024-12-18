import microbit
import random
import math


____n1 = 0
____n3 = 0
____h1n1 = 0
____h1n2 = 0
____n2 = 0
____n4 = 0
____b_h1n1 = 1
____w_n1_h1n1 = 1
____w_n3_h1n1 = 2
____b_h1n2 = 2
____w_n1_h1n2 = 3
____w_n3_h1n2 = 4
____b_n2 = 0
____w_h1n1_n2 = 7
____w_h1n2_n2 = 6
____b_n4 = 0
____w_h1n1_n4 = 8
____w_h1n2_n4 = 5


def ____nnStep():
    global ____h1n1, ____h1n2, ____n2, ____n4
    ____h1n1 = ____b_h1n1 + ____n1 * ____w_n1_h1n1 + ____n3 * ____w_n3_h1n1
    ____h1n2 = ____b_h1n2 + ____n1 * ____w_n1_h1n2 + ____n3 * ____w_n3_h1n2
    ____n2 = ____b_n2 + ____h1n1 * ____w_h1n1_n2 + ____h1n2 * ____w_h1n2_n2
    ____n4 = ____b_n4 + ____h1n1 * ____w_h1n1_n4 + ____h1n2 * ____w_h1n2_n4

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___n = 0

def ____runNN():
    global timer1, ___n, ____n1, ____n3, ____h1n1, ____h1n2, ____b_h1n1, ____w_n1_h1n1, ____w_n3_h1n1, ____b_h1n2, ____w_n1_h1n2, ____w_n3_h1n2, ____b_n2, ____w_h1n1_n2, ____w_h1n2_n2, ____b_n4, ____w_h1n1_n4, ____w_h1n2_n4
    ____n1 = 2
    ____n3 = 4
    ____w_n1_h1n1 = ____w_h1n2_n2
    ____w_n3_h1n1 = ____w_h1n2_n4
    ____b_n2 = ____b_h1n1
    ____b_n4 = ____b_h1n2
    ____nnStep()
    ___n = ____n2

def run():
    global timer1, ___n, ____n1, ____n3, ____h1n1, ____h1n2, ____b_h1n1, ____w_n1_h1n1, ____w_n3_h1n1, ____b_h1n2, ____w_n1_h1n2, ____w_n3_h1n2, ____b_n2, ____w_h1n1_n2, ____w_h1n2_n2, ____b_n4, ____w_h1n1_n4, ____w_h1n2_n4
    ____runNN()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
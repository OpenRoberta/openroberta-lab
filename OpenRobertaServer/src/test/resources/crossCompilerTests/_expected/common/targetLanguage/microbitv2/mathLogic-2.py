import microbit
import random
import math


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___r1 = 0
___r2 = 0
___b1 = True
___r3 = 0
___sim = True

def run():
    global timer1, ___r1, ___r2, ___b1, ___r3, ___sim
    ___r3 = math.pi / float(2) if ( ___sim ) else 90
    ___b1 = ___b1 and ( math.sin(___r3) == 1 )
    ___b1 = ___b1 and ( math.cos(0) == 1 )
    ___b1 = ___b1 and ( math.tan(0) == 0 )
    ___b1 = ___b1 and ( math.asin(1) == ___r3 )
    ___b1 = ___b1 and ( math.acos(1) == 0 )
    ___b1 = ___b1 and ( math.atan(0) == 0 )
    ___b1 = ___b1 and ( ( math.e > float(2.6) ) and ( math.e < float(2.8) ) )
    ___b1 = ___b1 and ( ( ( math.sqrt(2) * math.sqrt(0.5) ) >= float(0.999) ) and ( ( math.sqrt(2) * math.sqrt(0.5) ) <= float(1.001) ) )
    # if b1 is true, the test succeeded, otherwise it failed

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
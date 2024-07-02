import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___x = 0
def run():
    global timer1, ___x
    ___x = ___x + math.sqrt(4)
    ___x = ___x + math.fabs(-2)
    ___x = ___x + ( - (-4) )
    ___x = ___x + math.log(math.exp(2))
    ___x = ___x + math.log10(100)
    ___x = ___x + math.pow(10, 2)
    ___x = ___x + ( ( 5 ) % ( 3 ) )
    ___x = ___x + math.sin(math.pi / float(2))
    ___x = ___x + math.cos(0)
    ___x = ___x + math.tan(0)
    ___x = ___x + math.asin(0)
    ___x = ___x + math.acos(1)
    ___x = ___x + math.atan(0)
    ___x = ___x + math.floor(float(42.8))
    ___x = ___x + math.sin(min(max(2, 1), 100))
    # expected: 170

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
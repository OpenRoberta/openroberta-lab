import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = 0
def run():
    global timer1, ___item
    ___item = math.pi
    ___item = math.e
    ___item = (1 + 5 ** 0.5) / 2
    ___item = math.sqrt(2)
    ___item = math.sqrt(0.5)
    ___item = math.sqrt(12)
    ___item = math.fabs(12)
    ___item = - (12)
    ___item = math.log(12)
    ___item = math.log10(12)
    ___item = math.exp(12)
    ___item = math.pow(10, 12)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

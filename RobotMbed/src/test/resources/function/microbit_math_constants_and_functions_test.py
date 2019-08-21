import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = 0
def run():
    global timer1, item
    item = math.pi
    item = math.e
    item = (1 + 5 ** 0.5) / 2
    item = math.sqrt(2)
    item = math.sqrt(0.5)
    item = math.sqrt(12)
    item = math.fabs(12)
    item = - (12)
    item = math.log(12)
    item = math.log10(12)
    item = math.exp(12)
    item = math.pow(10, 12)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
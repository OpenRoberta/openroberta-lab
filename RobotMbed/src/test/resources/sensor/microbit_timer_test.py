import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = 0
def run():
    global timer1, item
    item = ( microbit.running_time() - timer1 )
    timer1 = microbit.running_time()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
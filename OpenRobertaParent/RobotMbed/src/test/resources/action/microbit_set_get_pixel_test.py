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
    microbit.display.set_pixel(0, 0, 5)
    item = microbit.display.get_pixel(0, 0)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
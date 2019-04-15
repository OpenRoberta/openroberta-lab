import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = "a"
def run():
    global timer1, item
    item = "".join(str(arg) for arg in ["sadf", "sdf"])

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
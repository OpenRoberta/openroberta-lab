import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = 0
item2 = True
def run():
    global timer1, item, item2
    item = min(max(item, 1), 100)
    item2 = (item % 2) == 0
    item2 = (item % 2) == 1
    # following function is not implemented yet
    item2 = false # not implemented yet
    item2 = (item % 1) == 0
    item2 = item > 0
    item2 = item < 0
    item2 = item % item
    item = random.randint(1, 100)
    item = random.random()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
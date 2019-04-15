import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = [0, 0, 0]
item2 = 0
item3 = True
def run():
    global timer1, item, item2, item3
    item = item[0:0]
    item2 = len( item)
    item3 = not item
    item = [0] * 5
    item[0] = 0
    item.insert(0, 0)
    item[-0] = 0
    item.insert(-0, 0)
    item[0] = 0
    item.insert(0, 0)
    item[-1] = 0
    item.insert(-1, 0)
    item[0] = 0
    item.insert(0, 0)
    item2 = item[0]
    item2 = item[-0]
    item2 = item[0]
    item2 = item[-1]
    item2 = item[0]
    item2 = item.pop(0)
    item2 = item.pop(-0)
    item2 = item.pop(0)
    item2 = item.pop(-1)
    item2 = item.pop(0)
    item.pop(0)
    item.pop(-0)
    item.pop(0)
    item.pop(-1)
    item.pop(0)
    item2 = item.index(0)
    item2 = (len(item) - 1) - item[::-1].index(0)
    item = item[0:-0]
    item = item[0:-1]
    item = item[-0:0]
    item = item[-0:-0]
    item = item[-0:-1]
    item = item[0:0]
    item = item[0:-0]
    item = item[0:-1]

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

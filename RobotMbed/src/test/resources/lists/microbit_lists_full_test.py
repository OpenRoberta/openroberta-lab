import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = [0, 0, 0]
___item2 = 0
___item3 = True
def run():
    global timer1, ___item, ___item2, ___item3
    ___item = ___item[0:0]
    ___item2 = len( ___item)
    ___item3 = not ___item
    ___item = [0] * 5
    ___item[0] = 0
    ___item.insert(0, 0)
    ___item[-1 -0] = 0
    ___item.insert(-1 -0, 0)
    ___item[0] = 0
    ___item.insert(0, 0)
    ___item[-1] = 0
    ___item.insert(-1, 0)
    ___item[0] = 0
    ___item.insert(0, 0)
    ___item2 = ___item[0]
    ___item2 = ___item[-1 -0]
    ___item2 = ___item[0]
    ___item2 = ___item[-1]
    ___item2 = ___item[0]
    ___item2 = ___item.pop(0)
    ___item2 = ___item.pop(-1 -0)
    ___item2 = ___item.pop(0)
    ___item2 = ___item.pop(-1)
    ___item2 = ___item.pop(0)
    ___item.pop(0)
    ___item.pop(-1 -0)
    ___item.pop(0)
    ___item.pop(-1)
    ___item.pop(0)
    ___item2 = ___item.index(0)
    ___item2 = (len(___item) - 1) - ___item[::-1].index(0)
    ___item = ___item[0:-1 -0]
    ___item = ___item[0:]
    ___item = ___item[-1 -0:0]
    ___item = ___item[-1 -0:-1 -0]
    ___item = ___item[-1 -0:]
    ___item = ___item[0:0]
    ___item = ___item[0:-1 -0]
    ___item = ___item[0:]

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

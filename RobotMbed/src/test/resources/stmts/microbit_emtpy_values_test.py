import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = None
item2 = None
def run():
    global timer1, item, item2
    while not True:
        pass
    item2 = None
    microbit.display.show("")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
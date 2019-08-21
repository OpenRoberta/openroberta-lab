import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    while True:
        if True:
            break
    microbit.sleep(500)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
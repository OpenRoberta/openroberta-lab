import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.show(microbit.Image.HAPPY)
    microbit.sleep(1000)
    microbit.display.show(microbit.Image.HEART)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
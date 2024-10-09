import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___n = 0

def run():
    global timer1, ___n
    ___n = ___n

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
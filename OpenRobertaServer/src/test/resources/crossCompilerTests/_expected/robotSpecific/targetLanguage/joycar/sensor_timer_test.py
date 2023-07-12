import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    # This program tests the timer of the robot
    while True:
        if ( microbit.running_time() - timer1 ) > 10000:
            music.pitch(261, 2000, microbit.pin16)
            microbit.display.scroll("10 seconds over")
            timer1 = microbit.running_time()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
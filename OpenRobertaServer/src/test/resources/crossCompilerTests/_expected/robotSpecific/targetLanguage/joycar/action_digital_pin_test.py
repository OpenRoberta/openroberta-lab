import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.off()
    print("A to start true on Pin 11")
    while True:
        if microbit.button_a.is_pressed():
            break
    microbit.pin12.write_digital(1)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
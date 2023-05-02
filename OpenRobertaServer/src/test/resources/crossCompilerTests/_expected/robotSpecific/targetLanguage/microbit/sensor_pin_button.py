import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("Press A")
    while True:
        if microbit.button_a.is_pressed() == True:
            break
    microbit.display.scroll("Press B")
    while True:
        if microbit.button_b.is_pressed() == True:
            break
    microbit.display.scroll("Press Pin 0")
    while True:
        if microbit.pin0.is_touched() == True:
            break
    microbit.display.scroll("Press Pin 1")
    while True:
        if microbit.pin1.is_touched() == True:
            break
    microbit.display.scroll("Press Pin 2")
    while True:
        if microbit.pin2.is_touched() == True:
            break
    while True:
        if microbit.button_a.is_pressed():
            microbit.display.scroll("A")
        elif microbit.button_b.is_pressed():
            microbit.display.scroll("B")
        elif microbit.pin0.is_touched():
            microbit.display.scroll("P0")
        elif microbit.pin1.is_touched():
            microbit.display.scroll("P1")
        elif microbit.pin2.is_touched():
            microbit.display.scroll("P2")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
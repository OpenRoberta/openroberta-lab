import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("press logo to start")
    while True:
        if microbit.pin_logo.is_touched() == True:
            break
    while True:
        if microbit.pin_logo.is_touched():
            microbit.display.scroll("logo")
        elif microbit.button_a.is_pressed():
            microbit.display.scroll("capacitive mode")
            microbit.pin0.set_touch_mode(microbit.pin0.CAPACITIVE)
            microbit.pin1.set_touch_mode(microbit.pin1.CAPACITIVE)
            microbit.pin2.set_touch_mode(microbit.pin2.CAPACITIVE)
        elif microbit.button_b.is_pressed():
            microbit.pin0.set_touch_mode(microbit.pin0.RESISTIVE)
            microbit.pin1.set_touch_mode(microbit.pin1.RESISTIVE)
            microbit.pin2.set_touch_mode(microbit.pin2.RESISTIVE)
            microbit.display.scroll("resistive mode")
        elif microbit.pin0.is_touched():
            microbit.display.scroll("pin 0")
        elif microbit.pin1.is_touched():
            microbit.display.scroll("pin 1")
        elif microbit.pin2.is_touched():
            microbit.display.scroll("pin 2")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
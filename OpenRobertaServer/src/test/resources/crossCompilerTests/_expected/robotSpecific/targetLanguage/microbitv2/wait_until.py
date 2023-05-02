import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("gesture upright")
    while True:
        if "up" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("freely falling")
    while True:
        if "freefall" == microbit.accelerometer.current_gesture():
            break
    microbit.display.scroll("Button A")
    while True:
        if microbit.button_a.is_pressed() == True:
            break
    microbit.display.scroll("Button B")
    while True:
        if microbit.button_b.is_pressed() == True:
            break
    microbit.display.scroll("Pin 0")
    while True:
        if microbit.pin0.is_touched() == True:
            break
    microbit.display.scroll("angle smaller than 30")
    while True:
        if microbit.compass.heading() < 30:
            break
    microbit.display.scroll("millig g x accelerometer bigger than 30")
    while True:
        if microbit.accelerometer.get_x() > 30:
            break
    microbit.display.scroll("light sensor smaller than 30")
    while True:
        if round(microbit.display.read_light_level() / 2.55) < 30:
            break
    microbit.display.scroll("Pin digital value != 0")
    while True:
        if microbit.pin0.read_digital() != 0:
            break
    microbit.display.scroll("Pin analog smaller than 20")
    while True:
        if microbit.pin1.read_analog() < 20:
            break
    microbit.display.scroll("temperature higher than 10")
    while True:
        if microbit.temperature() > 10:
            break

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("press A to go to the next sensor")
    microbit.display.scroll("Temperature")
    while not microbit.button_a.is_pressed():
        microbit.display.scroll(str(microbit.temperature()))
    microbit.display.scroll("light sensor")
    while not microbit.button_a.is_pressed():
        microbit.display.scroll(str(round(microbit.display.read_light_level() / 2.55)))
    microbit.display.scroll("Timer:")
    while not microbit.button_a.is_pressed():
        microbit.display.scroll(str(( microbit.running_time() - timer1 )))
    timer1 = microbit.running_time()
    microbit.display.scroll("Timer Reset")
    while not microbit.button_a.is_pressed():
        microbit.display.scroll(str(( microbit.running_time() - timer1 )))
    microbit.display.scroll("Done")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
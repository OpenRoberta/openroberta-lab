import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    # This program tests the buttons on the microbit, press button A, B and the logo to see the display turn on
    while True:
        if microbit.button_a.is_pressed():
            microbit.display.show(microbit.Image('00900:09090:09990:09090:09090'))
            microbit.sleep(500)
            microbit.display.clear()
        if microbit.button_b.is_pressed():
            microbit.display.show(microbit.Image('09900:09090:09900:09090:09900'))
            microbit.sleep(500)
            microbit.display.clear()
        if microbit.pin_logo.is_touched():
            microbit.display.show(microbit.Image('09990:90009:99099:90009:09990'))
            microbit.sleep(500)
            microbit.display.clear()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
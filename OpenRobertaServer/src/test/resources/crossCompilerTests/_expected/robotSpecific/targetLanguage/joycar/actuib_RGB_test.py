import microbit
import random
import math
import neopixel

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass
np = neopixel.NeoPixel(microbit.pin0, 8)

timer1 = microbit.running_time()

def run():
    global timer1
    for ___k0 in range(int(0), int(10), int(1)):
        led_set_colour(1, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(1, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(0, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(0, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(2, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(2, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(3, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(3, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(4, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(4, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(5, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(5, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(7, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(7, (0, 0, 0))
        microbit.sleep(500)
        led_set_colour(6, (255, 255, 255))
        microbit.sleep(500)
        led_set_colour(6, (0, 0, 0))
        microbit.sleep(500)

def main():
    try:
        run()
    except Exception as e:
        raise

def led_set_colour(x, colour):
    global np
    np[x] = colour
    np.show()

if __name__ == "__main__":
    main()
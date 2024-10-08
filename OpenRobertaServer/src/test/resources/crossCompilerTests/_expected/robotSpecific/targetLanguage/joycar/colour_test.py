import microbit
import random
import math
import neopixel


def led_set_colour(x, colour):
    global np
    np[x] = colour
    np.show()

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass
np = neopixel.NeoPixel(microbit.pin0, 8)

timer1 = microbit.running_time()

___item = [(255, 20, 150), (204, 0, 0), (255, 204, 0), (51, 102, 255), (51, 0, 51), (0, 0, 0), (102, 255, 255), (255, 204, 255)]

def run():
    global timer1, ___item
    for ___item2 in ___item:
        led_set_colour(1, ___item2)
        microbit.sleep(2000)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
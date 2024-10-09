import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___Animation = [microbit.Image.HEART_SMALL, microbit.Image.HEART, microbit.Image.HEART_SMALL, microbit.Image.HEART, microbit.Image.HEART_SMALL, microbit.Image.HEART, microbit.Image.HEART_SMALL, microbit.Image.HEART, microbit.Image.HEART_SMALL, microbit.Image.HAPPY, microbit.Image.HAPPY, microbit.Image.HAPPY, microbit.Image.SMILE, microbit.Image.HAPPY, microbit.Image.SMILE, microbit.Image.HAPPY, microbit.Image.HAPPY, microbit.Image.HAPPY, microbit.Image.HAPPY, microbit.Image.SMILE, microbit.Image.HAPPY]

def ____showImageMatrix():
    global timer1, ___Animation
    # tests the show image block
    microbit.display.show(microbit.Image('90000:00000:00000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('90000:00000:00000:00000:00000').shift_right(1))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00090:00000:00000:00000:00000').shift_left(1))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00090:00000:00000:00000').shift_up(1))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('99990:99999:99999:99999:99999').invert())
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00009:00000:00000:00000:00000').shift_down(1))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00009:00000:00000').shift_up(1))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00009:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00009:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00000:00009'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00000:00090'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00000:00900'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00000:09000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00000:90000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:90000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:90000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:90000:00000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:09000:00000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00900:00000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00090:00000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00090:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00090:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:00900:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00000:09000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:09000:00000:00000'))
    microbit.sleep(100)
    microbit.display.show(microbit.Image('00000:00000:00900:00000:00000'))
    microbit.sleep(100)

def ____showAnimation():
    global timer1, ___Animation
    microbit.display.show(___Animation)
    music.pitch(195, 500, microbit.pin16)

def ____turnOnAllLEDS():
    global timer1, ___Animation
    # sets all LEDs on all different brightness levels
    for ___k in range(int(0), int(10), int(1)):
        for ___i in range(int(0), int(5), int(1)):
            for ___j in range(int(0), int(5), int(1)):
                microbit.display.set_pixel(___i, ___j, ___k)
                microbit.sleep(200)
    microbit.sleep(500)
    microbit.display.clear()
    microbit.sleep(500)

def ____getBrightness():
    global timer1, ___Animation
    # prints the LED brightness value
    for ___m in range(int(0), int(10), int(1)):
        microbit.display.set_pixel(0, 0, ___m)
        microbit.display.scroll(str(microbit.display.get_pixel(0, 0)))
    microbit.display.clear()


def run():
    global timer1, ___Animation
    # This program tests all display blocks (except the serial monitor)
    ____turnOnAllLEDS()
    ____getBrightness()
    ____showImageMatrix()
    ____showAnimation()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
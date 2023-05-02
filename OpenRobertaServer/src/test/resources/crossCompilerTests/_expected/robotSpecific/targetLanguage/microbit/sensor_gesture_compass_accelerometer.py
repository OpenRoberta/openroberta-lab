import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("make gesture upright")
    while True:
        if "up" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("make gesture upside down")
    while True:
        if "down" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("make gesture at the front side")
    while True:
        if "face down" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("make gesture at the back")
    while True:
        if "face up" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("make gesture shaking")
    while True:
        if "shake" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("make gesture freely falling")
    while True:
        if "freefall" == microbit.accelerometer.current_gesture() == True:
            break
    microbit.display.scroll("ANGLE OF COMPASS:")
    microbit.display.scroll(str(microbit.compass.heading()))
    microbit.display.scroll("SPEED IN X DIRECTION")
    microbit.display.scroll(str(microbit.accelerometer.get_x()))
    microbit.display.scroll("SPEED IN Y DIRECTION")
    microbit.display.scroll(str(microbit.accelerometer.get_y()))
    microbit.display.scroll("SPEED IN Z DIRECTION")
    microbit.display.scroll(str(microbit.accelerometer.get_z()))
    microbit.display.scroll("ACCELEROMETER STRENGTH:")
    microbit.display.scroll(str(math.sqrt(microbit.accelerometer.get_x()**2 + microbit.accelerometer.get_y()**2 + microbit.accelerometer.get_z()**2)))
    microbit.display.scroll("Gesture Upright?")
    microbit.display.scroll(str("up" == microbit.accelerometer.current_gesture()))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
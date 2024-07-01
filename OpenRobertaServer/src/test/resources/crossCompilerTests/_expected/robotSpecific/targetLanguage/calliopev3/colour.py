import calliopemini
import random
import math
import neopixel

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
np = neopixel.NeoPixel(calliopemini.pin_RGB, 3)


___color = (255, 0, 0)
def ____wait():
    global timer1, ___color
    calliopemini.display.scroll("Next Color")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.display.scroll("Next Color")
    calliopemini.sleep(500)

def run():
    global timer1, ___color
    np[1] = ((153, 153, 153))
    np.show()
    ____wait()
    np[1] = ((204, 0, 0))
    np.show()
    ____wait()
    np[1] = ((255, 102, 0))
    np.show()
    ____wait()
    np[1] = ((255, 204, 51))
    np.show()
    ____wait()
    np[1] = ((51, 204, 0))
    np.show()
    ____wait()
    np[1] = ((0, 204, 204))
    np.show()
    ____wait()
    np[1] = ((51, 102, 255))
    np.show()
    ____wait()
    np[1] = ((204, 51, 204))
    np.show()
    ____wait()
    np[1] = (___color)
    np.show()
    ____wait()
    np[1] = (255, 20, 150, 255)
    np.show()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
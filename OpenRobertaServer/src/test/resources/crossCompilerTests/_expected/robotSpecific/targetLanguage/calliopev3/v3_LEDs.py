import calliopemini
import random
import math
import neopixel

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
np = neopixel.NeoPixel(calliopemini.pin_RGB, 3)


def run():
    global timer1
    print("Rotating through LEDs press A to continue")
    print("Left LED RED")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[0] = ((255, 0, 0))
    np.show()
    print("Left LED OFF")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[0] = ((0, 0, 0))
    np.show()

    print("center LED RED")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[1] = ((255, 0, 0))
    np.show()
    print("center LED OFF")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[1] = ((0, 0, 0))
    np.show()

    print("right LED RED")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[2] = ((255, 0, 0))
    np.show()
    print("right LED OFF")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[2] = ((0, 0, 0))
    np.show()

    print("all LEDs RED")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np.fill((255, 0, 0))
    np.show()
    print("all LEDs OFF")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np.clear()

    print("right LED green")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[2] = ((51, 204, 0))
    np.show()
    print("left LED white")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[2] = ((255, 255, 255))
    np.show()
    print("centre LED black")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    np[1] = ((0, 0, 0))
    np.show()
    print("DONE")

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
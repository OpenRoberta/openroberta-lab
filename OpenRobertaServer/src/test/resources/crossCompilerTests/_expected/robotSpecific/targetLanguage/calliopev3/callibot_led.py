import calliopemini
import random
import math
from callibot2 import Callibot2


def ____wait():
    global timer1
    calliopemini.sleep(700)
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
callibot = Callibot2()


def run():
    global timer1
    print("R RGB RED")
    callibot.set_rgb_led(1, (255, 0, 0))
    ____wait()
    print("R RGB OFF")
    callibot.set_rgb_led(1, (0, 0, 0))
    ____wait()
    print("R2 Green")
    callibot.set_rgb_led(4, (51, 204, 0))
    ____wait()
    print("R2 RGB OFF")
    callibot.set_rgb_led(4, (0, 0, 0))
    ____wait()
    print("R3 Blue")
    callibot.set_rgb_led(2, (0, 0, 153))
    ____wait()
    print("R3 RGB OFF")
    callibot.set_rgb_led(2, (0, 0, 0))
    ____wait()
    print("R4 White")
    callibot.set_rgb_led(3, (255, 255, 255))
    ____wait()
    print("R4 RGB OFF")
    callibot.set_rgb_led(3, (0, 0, 0))
    ____wait()
    print("L on")
    callibot.set_red_led_on(1)
    ____wait()
    print("L off")
    callibot.set_red_led_off(1)
    ____wait()
    print("L2 On")
    callibot.set_red_led_on(2)
    ____wait()
    print("L2 Off")
    callibot.set_red_led_off(2)

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        callibot.stop()


if __name__ == "__main__":
    main()
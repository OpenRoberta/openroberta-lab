import calliopemini
import random
import math
from callibot2 import Callibot2

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
callibot = Callibot2()


def ____wait():
    global timer1
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)


def run():
    global timer1
    calliopemini.display.scroll("Curve Right")
    callibot.set_speed(30, 10)
    ____wait()
    calliopemini.display.scroll("Curve Left")
    callibot.set_speed(10, 30)
    ____wait()
    calliopemini.display.scroll("Forward")
    callibot.set_speed(100, 100)
    ____wait()
    calliopemini.display.scroll("Backwards")
    callibot.set_speed(-100, -100)
    ____wait()
    calliopemini.display.scroll("Turn Left")
    callibot.set_speed(-50, 50)
    ____wait()
    calliopemini.display.scroll("Turn Right")
    callibot.set_speed(50, -50)
    ____wait()
    calliopemini.display.scroll("STOP")
    callibot.stop()
    ____wait()
    calliopemini.display.scroll("Left On")
    callibot.set_speed_motor(1, 30)
    ____wait()
    calliopemini.display.scroll("Stop Left")
    callibot.stop_motor(1)
    ____wait()
    calliopemini.display.scroll("Right On")
    callibot.set_speed_motor(1, 30)
    ____wait()
    calliopemini.display.scroll("Stop Right")
    callibot.stop_motor(2)
    ____wait()
    calliopemini.display.scroll("Servo 90")
    callibot.servo(1, 90)
    ____wait()
    calliopemini.display.scroll("Servo 45")
    callibot.servo(2, 45)

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        callibot.stop()


if __name__ == "__main__":
    main()
import calliopemini
import random
import math
from callibot2 import Callibot2

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
callibot = Callibot2()

def run():
    global timer1
    print("callibiot sensor tests press a to go through")
    calliopemini.sleep(500)
    print("line ")
    while True:
        print("".join(str(arg) for arg in ["I1: ", callibot.get_line_sensor(1)]))
        print("".join(str(arg) for arg in ["I2: ", callibot.get_line_sensor(2)]))
        if calliopemini.button_a.is_pressed():
            break
    calliopemini.sleep(500)
    print("distance ultrasonic")
    while True:
        print(callibot.get_ultrasonic_sensor())
        if calliopemini.button_a.is_pressed():
            break
    calliopemini.sleep(500)
    print("front key")
    while True:
        print("".join(str(arg) for arg in ["K1: ", callibot.get_bumper_sensor(1)]))
        print("".join(str(arg) for arg in ["K2: ", callibot.get_bumper_sensor(1)]))
        if calliopemini.button_a.is_pressed():
            break

def main():
    try:
        run()
    except Exception as e:
        raise
    finally:
        callibot.stop()

if __name__ == "__main__":
    main()
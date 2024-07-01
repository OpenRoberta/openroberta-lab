import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___item = -100
def run():
    global timer1, ___item
    set_motor("A", -100)
    calliopemini.sleep(2000)
    set_motor("A", -0.01)
    calliopemini.sleep(500)
    set_motor("A", -5)
    calliopemini.sleep(2000)
    set_motor("A", -0.01)
    calliopemini.sleep(500)
    set_motor("A", 5)
    calliopemini.sleep(2000)
    set_motor("A", -0.01)
    calliopemini.sleep(500)
    set_motor("A", 100)
    calliopemini.sleep(2000)
    set_motor("A", -0.01)
    calliopemini.sleep(500)

def main():
    try:
        run()
    except Exception as e:
        raise

def set_motor(port, speed):
    digit = 0
    if (speed < 0):
        digit = 1
        speed *= -1
    speed = int(speed * 10.23)
    calliopemini.pin_M_MODE.write_digital(1)
    if (port == "A"):
        calliopemini.pin_M0_DIR.write_digital(digit)
        calliopemini.pin_M0_SPEED.write_analog(speed)
    else:
        calliopemini.pin_M1_DIR.write_digital(digit)
        calliopemini.pin_M1_SPEED.write_analog(speed)

if __name__ == "__main__":
    main()
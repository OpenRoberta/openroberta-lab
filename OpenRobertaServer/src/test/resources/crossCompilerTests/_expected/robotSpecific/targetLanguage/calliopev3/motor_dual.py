import calliopemini
import random
import math

def set_both_motors(speed_A, speed_B):
    digit_0 = 0
    digit_1 = 0
    if (speed_A < 0):
        digit_0 = 1
        speed_A *= -1
    if (speed_B < 0):
        digit_1 = 1
        speed_B *= -1
    speed_A = int(speed_A * 10.23)
    speed_B = int(speed_B * 10.23)
    calliopemini.pin_M_MODE.write_digital(1)
    calliopemini.pin_M0_DIR.write_digital(digit_0)
    calliopemini.pin_M1_DIR.write_digital(digit_1)
    calliopemini.pin_M0_SPEED.write_analog(speed_A)
    calliopemini.pin_M1_SPEED.write_analog(speed_B)

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

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()

def run():
    global timer1
    set_both_motors(50, 100)
    calliopemini.sleep(2000)
    set_both_motors(0, 0)
    calliopemini.sleep(500)
    set_motor("A", 100)
    calliopemini.sleep(2000)
    set_both_motors(0, 0)
    calliopemini.sleep(500)
    set_motor("B", 100)
    calliopemini.sleep(2000)
    set_both_motors(0, 0)
    calliopemini.sleep(500)
    set_both_motors(100, 25)
    calliopemini.sleep(2000)
    set_both_motors(0, 0)
    calliopemini.sleep(500)
    set_both_motors(25, 100)
    calliopemini.sleep(2000)
    set_both_motors(0, 0)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import calliopemini
import random
import math


def servo_get_angle(angle):
    if (angle < 0): 
        angle = 0
    if (angle > 180): 
        angle = 180
    return round(0.55 * angle) + 25

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


def ____move():
    global timer1, ___n
    print("M1 M2 Speed 50 Variable")
    set_both_motors(___n, ___n)
    ____wait()
    print("M1 M2 speed -50")
    set_both_motors(-50, -50)
    ____wait()
    print("M1 100, M2 -100")
    set_both_motors(100, -100)
    ____wait()
    print("M1 0 M2 0")
    set_both_motors(0, 0)
    ____wait()
    print("M1 -100 M2 100")
    set_both_motors(-100, 100)
    ____wait()
    print("M1 1000 M2 1000")
    set_both_motors(1000, 1000)
    ____wait()
    print("STOP")
    set_both_motors(0, 0)
    ____wait()
    print("M1 100")
    set_motor("A", 100)
    ____wait()
    print("M1 Stop floating")
    set_motor("A", 0)
    ____wait()
    print("M2 100")
    set_motor("B", 100)
    ____wait()
    print("M2 stop floating")
    set_motor("B", 0)
    ____wait()
    print("M1 -100")
    set_motor("A", -100)
    ____wait()
    print("M1 Break")
    set_motor("A", -0.01)
    ____wait()
    print("M2 -100")
    set_motor("B", -100)
    ____wait()
    print("M2 Break")
    set_motor("B", -0.01)
    ____wait()
    print("M1 Variable 50")
    set_motor("A", ___n)
    ____wait()
    print("M1 Sleep")
    set_motor("A", 0)
    ____wait()
    print("M2 Variable 50")
    set_motor("B", ___n)
    ____wait()
    print("M2 Sleep")
    set_motor("B", 0)
    ____wait()
    print("-------------Servo Tests:---------------")
    print("Servo to 50 Variable")
    calliopemini.pin1.write_analog(servo_get_angle(___n))
    ____wait()
    print("Servo 0")
    calliopemini.pin1.write_analog(servo_get_angle(0))
    ____wait()
    print("Servo 90")
    calliopemini.pin1.write_analog(servo_get_angle(90))
    ____wait()
    print("Servo 180")
    calliopemini.pin1.write_analog(servo_get_angle(180))
    ____wait()
    print("Servo -180")
    calliopemini.pin1.write_analog(servo_get_angle(-180))
    ____wait()
    print("Servo 360")
    calliopemini.pin1.write_analog(servo_get_angle(360))
    ____wait()
    print("Servo 90")
    calliopemini.pin1.write_analog(servo_get_angle(90))
    print("DONE")

def ____wait():
    global timer1, ___n
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(700)

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
calliopemini.pin1.set_analog_period(20)


___n = 50

def run():
    global timer1, ___n
    ____move()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
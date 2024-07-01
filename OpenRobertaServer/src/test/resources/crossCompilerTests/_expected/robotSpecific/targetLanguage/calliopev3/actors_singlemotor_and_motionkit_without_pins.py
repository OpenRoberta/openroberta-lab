import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
calliopemini.pin_A1_RX.set_analog_period(20)
calliopemini.pin_A1_TX.set_analog_period(20)


___off = 0
___max = 100
___negative = 0
def ____move():
    global timer1, ___off, ___max, ___negative
    print("m1 On")
    set_motor("A", ___max)
    ____wait()
    print("M1 float")
    set_motor("A", 0)
    ____wait()
    print("M1 Negative")
    set_motor("A", ___negative)
    ____wait()
    print("Stop")
    set_motor("A", 0)
    ____wait()
    print("M1 On")
    set_motor("A", ___max)
    ____wait()
    print("Brake")
    set_motor("A", -0.01)
    ____wait()
    print("M1 On")
    set_motor("A", ___max)
    ____wait()
    print("Sleep")
    set_motor("A", 0)
    ____wait()
    print("-----------------------Motion kit:-------------------")
    print("left FW")
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(180))
    ____wait()
    print("left BW")
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(0))
    ____wait()
    print("left Off")
    calliopemini.pin_A1_TX.write_analog(0)
    ____wait()
    print("right FW")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(0))
    ____wait()
    print("right BW")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(180))
    ____wait()
    print("right OFF")
    calliopemini.pin_A1_RX.write_analog(0)
    ____wait()
    print("Turn right")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(180))
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(180))
    ____wait()
    print("Turn left")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(0))
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(0))
    ____wait()
    print("Forwards")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(0))
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(180))
    ____wait()
    print("Backwards")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(180))
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(0))
    ____wait()
    print("STOP")
    calliopemini.pin_A1_RX.write_analog(0)
    calliopemini.pin_A1_TX.write_analog(0)
    ____wait()
    print("Only left on")
    calliopemini.pin_A1_RX.write_analog(0)
    calliopemini.pin_A1_TX.write_analog(servo_get_angle(180))
    ____wait()
    print("Only right on")
    calliopemini.pin_A1_RX.write_analog(servo_get_angle(0))
    calliopemini.pin_A1_TX.write_analog(0)

def ____wait():
    global timer1, ___off, ___max, ___negative
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(500)

def run():
    global timer1, ___off, ___max, ___negative
    ____move()

def main():
    try:
        run()
    except Exception as e:
        raise

def servo_get_angle(angle):
    if (angle < 0): 
        angle = 0
    if (angle > 180): 
        angle = 180
    return round(0.55 * angle) + 25

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
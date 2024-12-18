import microbit
import random
import math

def servo_set_angle(pin, angle):
    if (angle < 0): angle = 0
    if (angle > 180): angle = 180
    duty = round(0.55 * angle) + 25
    if pin == 1:
        microbit.pin1.write_analog(duty)
    elif pin == 2:
        microbit.pin13.write_analog(duty)

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

microbit.pin1.set_analog_period(20)
microbit.pin13.set_analog_period(20)

timer1 = microbit.running_time()

def run():
    global timer1
    print("Move Servo A = 0 B = 180 Logo = 90")
    while True:
        if microbit.button_a.is_pressed():
            servo_set_angle(1, 0)
        elif microbit.button_b.is_pressed():
            servo_set_angle(1, 180)
        elif microbit.pin_logo.is_touched():
            servo_set_angle(1, 90)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import fischertechnik.factories as txt_factory
from fischertechnik.controller.Motor import Motor
import math
import time


def motor_start(motor, speed):
    motor.set_speed(speed_to_pwm(speed), Motor.CCW)
    motor.start()

def omnidrive_curve(speed_fl, speed_fr, speed_rl, speed_rr):
    motor_start(front_left_motor, speed_fl)
    motor_start(front_right_motor, speed_fr)
    motor_start(rear_left_motor, speed_rl)
    motor_start(rear_right_motor, speed_rr)

def omnidrive_curve_distance(distance, speed_l, speed_r):
    if distance < 0:
        speed_l = -speed_l
        speed_r = -speed_r
    left_direction = 1 if speed_l > 0 else -1
    right_direction = 1 if speed_r > 0 else -1
    omnidrive_curve(speed_l, speed_r, speed_l, speed_r)
    wheel_circumference = WHEEL_DIAMETER * math.pi
    time.sleep(0.010)
    while True:
        distance_left = left_direction * (wheel_circumference * front_left_motor.get_distance() / STEPS_PER_ROTATION)
        distance_right = right_direction * (wheel_circumference * front_right_motor.get_distance() / STEPS_PER_ROTATION)
        distance_traveled = (distance_left + distance_right) / 2
        if abs(distance_traveled) >= abs(distance):
            front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
            break
        time.sleep(0.010)

def omnidrive_straight_distance(distance, speed_fl, speed_fr, speed_rl, speed_rr):
    direction = 1
    if distance < 0:
        direction = -1
        distance = abs(distance)
    constant = WHEEL_DIAMETER / (2 * 4)
    distance_per_wheel = distance / constant / 4
    steps_per_wheel = int(distance_per_wheel / (2 * math.pi) * STEPS_PER_ROTATION)
    front_left_motor.set_speed(speed_to_pwm(direction * speed_fl), Motor.CCW)
    front_right_motor.set_speed(speed_to_pwm(direction * speed_fr), Motor.CCW)
    rear_left_motor.set_speed(speed_to_pwm(direction * speed_rl), Motor.CCW)
    rear_right_motor.set_speed(speed_to_pwm(direction * speed_rr), Motor.CCW)
    front_left_motor.set_distance(steps_per_wheel, front_right_motor, rear_left_motor, rear_right_motor)
    while True:
        if (not front_left_motor.is_running()
        and not front_right_motor.is_running()
        and not rear_left_motor.is_running()
        and not rear_right_motor.is_running()):
            break
        time.sleep(0.010)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)

def omnidrive_turn_degrees(speed, degrees):
    if degrees < 0:
        speed = -speed
        degrees = abs(degrees)
    speed = speed_to_pwm(speed)
    rotations = (WHEEL_DIAMETER * math.pi * degrees) / (360 * TRACK_WIDTH)
    steps_per_wheel = int(rotations * STEPS_PER_ROTATION)

    front_left_motor.set_speed(speed, Motor.CCW)
    front_right_motor.set_speed(speed, Motor.CW)
    rear_left_motor.set_speed(speed, Motor.CCW)
    rear_right_motor.set_speed(speed, Motor.CW)
    front_left_motor.set_distance(steps_per_wheel, front_right_motor, rear_left_motor, rear_right_motor)
    while True:
        if (not front_left_motor.is_running()
        and not front_right_motor.is_running()
        and not rear_left_motor.is_running()
        and not rear_right_motor.is_running()):
            break
        time.sleep(0.010)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)

def speed_to_pwm(speed):
    speed = max(min(speed, 100), -100)
    return int((speed / 100) * 512)

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_motor_factory()
txt_factory.init_counter_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

TXT_M_M3_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 3)
TXT_M_C3_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 3)
TXT_M_C3_motor_step_counter.set_motor(TXT_M_M3_motor)
TXT_M_M2_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 2)
TXT_M_C2_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 2)
TXT_M_C2_motor_step_counter.set_motor(TXT_M_M2_motor)
TXT_M_M4_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 4)
TXT_M_C4_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 4)
TXT_M_C4_motor_step_counter.set_motor(TXT_M_M4_motor)
TXT_M_M1_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 1)
TXT_M_C1_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 1)
TXT_M_C1_motor_step_counter.set_motor(TXT_M_M1_motor)
txt_factory.initialized()
time.sleep(0.1)
#init omnidrive
front_left_motor = TXT_M_M1_motor
front_right_motor = TXT_M_M2_motor
rear_left_motor = TXT_M_M3_motor
rear_right_motor = TXT_M_M4_motor
WHEEL_DIAMETER = 6
TRACK_WIDTH = 15
WHEEL_BASE = 10.2
STEPS_PER_ROTATION = 128


def run():
    print("Driving Forwards")
    omnidrive_curve(30, 30, 30, 30)
    time.sleep(1000/1000)
    omnidrive_curve(-30, -30, -30, -30)
    print("Driving Backwards")
    time.sleep(1000/1000)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
    print("Testing 20cm")
    omnidrive_straight_distance(20, 30, 30, 30, 30)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
    time.sleep(500/1000)
    omnidrive_straight_distance(20, -30, -30, -30, -30)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
    time.sleep(500/1000)
    omnidrive_straight_distance(-20, -30, -30, -30, -30)
    omnidrive_straight_distance(20, -30, -30, -30, -30)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
    time.sleep(500/1000)
    print("Testing Curve")
    omnidrive_curve(80, 30, 80, 30)
    time.sleep(1000/1000)
    omnidrive_curve(-(80), -(30), -(80), -(30))
    time.sleep(1000/1000)
    omnidrive_curve(-40, 40, -40, 40)
    time.sleep(1000/1000)
    omnidrive_curve_distance(20, 30, 40)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)
    time.sleep(500/1000)
    print("90 Degree right turn")
    omnidrive_turn_degrees(30, 90)
    print("45 Degree left turn twice")
    omnidrive_turn_degrees(-30, 45)
    omnidrive_turn_degrees(-30, 45)
    print("turn right 2 seconds")
    omnidrive_curve(30, -(30), 30, -(30))
    time.sleep(2000/1000)
    print("turn left 2 seconds")
    omnidrive_curve(-(30), 30, -(30), 30)
    time.sleep(2000/1000)
    front_left_motor.stop_sync(front_right_motor, rear_left_motor, rear_right_motor)

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
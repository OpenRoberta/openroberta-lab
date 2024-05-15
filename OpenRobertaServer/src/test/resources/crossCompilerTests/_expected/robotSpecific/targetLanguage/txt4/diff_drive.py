import fischertechnik.factories as txt_factory
from fischertechnik.controller.Motor import Motor
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_motor_factory()
txt_factory.init_counter_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

TXT_M_M2_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 2)
TXT_M_C2_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 2)
TXT_M_C2_motor_step_counter.set_motor(TXT_M_M2_motor)
TXT_M_M1_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 1)
TXT_M_C1_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 1)
TXT_M_C1_motor_step_counter.set_motor(TXT_M_M1_motor)
txt_factory.initialized()
#init differentialDrive
left_motor = TXT_M_M1_motor
right_motor = TXT_M_M2_motor
WHEEL_DIAMETER = 6
TRACK_WIDTH = 15
STEPS_PER_ROTATION = 128


def diffdrive_turn_degrees(speed, degrees):
    if degrees < 0:
        speed = -speed
        degrees = abs(degrees)
    speed = speed_to_pwm(speed)
    arc_length = math.radians(degrees) * (TRACK_WIDTH / 2)
    rotations = arc_length / (WHEEL_DIAMETER * math.pi)
    steps_per_wheel = int(rotations * STEPS_PER_ROTATION)

    left_motor.set_speed(speed, Motor.CCW)
    right_motor.set_speed(speed, Motor.CW)
    left_motor.set_distance(steps_per_wheel, right_motor)
    while True:
        if not left_motor.is_running() and not right_motor.is_running():
            break
        time.sleep(0.010)
    left_motor.stop_sync(right_motor)

def diffdrive(speed):
    speed = speed_to_pwm(speed)
    left_motor.set_speed(speed, Motor.CCW)
    right_motor.set_speed(speed, Motor.CCW)
    left_motor.start_sync(right_motor)

def diffdrive_curve(speed_l, speed_r):
    motor_start(left_motor, speed_l)
    motor_start(right_motor, speed_r)

def diffdrive_distance(distance, speed_l, speed_r):
    if distance < 0:
        speed_l = -speed_l
        speed_r = -speed_r
    left_direction = 1 if speed_l > 0 else -1
    right_direction = 1 if speed_r > 0 else -1
    diffdrive_curve(speed_l, speed_r)
    wheel_circumference = WHEEL_DIAMETER * math.pi
    time.sleep(0.010)
    while True:
        distance_left = left_direction * (wheel_circumference * left_motor.get_distance() / STEPS_PER_ROTATION)
        distance_right = right_direction * (wheel_circumference * right_motor.get_distance() / STEPS_PER_ROTATION)
        distance_traveled = (distance_left + distance_right) / 2
        if abs(distance_traveled) >= abs(distance):
            left_motor.stop_sync(right_motor)
            break
        time.sleep(0.010)

def motor_start(motor, speed):
    motor.set_speed(speed_to_pwm(speed), Motor.CCW)
    motor.start()

def speed_to_pwm(speed):
    speed = max(min(speed, 100), -100)
    return int((speed / 100) * 512)

def run():
    print("Driving Forwards")
    diffdrive(30)
    time.sleep(1000/1000)
    diffdrive(-30)
    print("Driving Backwards")
    time.sleep(1000/1000)
    left_motor.stop_sync(right_motor)
    print("Testing 20cm")
    diffdrive_distance(20, 30, 30)
    left_motor.stop_sync(right_motor)
    time.sleep(500/1000)
    diffdrive_distance(20, -30, -30)
    left_motor.stop_sync(right_motor)
    time.sleep(500/1000)
    diffdrive_distance(-20, -30, -30)
    diffdrive_distance(20, -30, -30)
    left_motor.stop_sync(right_motor)
    time.sleep(500/1000)
    print("Testing Curve")
    diffdrive_curve(40, 30)
    diffdrive_curve(-(40), -(30))
    diffdrive_curve(-40, 40)
    diffdrive_distance(20, 30, 40)
    left_motor.stop_sync(right_motor)
    time.sleep(500/1000)
    print("90 Degree right turn")
    diffdrive_turn_degrees(30, 90)
    print("45 Degree left turn twice")
    diffdrive_turn_degrees(-30, 45)
    diffdrive_turn_degrees(-30, 45)
    print("turn right 2 seconds")
    diffdrive_curve(30, -(30))
    time.sleep(2000/1000)
    print("turn left 2 seconds")
    diffdrive_curve(-(30), 30)
    time.sleep(2000/1000)
    left_motor.stop_sync(right_motor)

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
import fischertechnik.factories as txt_factory
from fischertechnik.controller.Motor import Motor
import math
import time


def motor_start(motor, speed):
    motor.set_speed(speed_to_pwm(speed), Motor.CCW)
    motor.start()

def motor_start_for(motor, speed, degrees):
    steps = int((degrees / 360) * STEPS_PER_ROTATION)
    motor.set_speed(speed_to_pwm(speed), Motor.CCW)
    motor.set_distance(steps)
    while True:
        if not motor.is_running():
            break
        time.sleep(0.010)
    motor.stop()

def speed_to_pwm(speed):
    speed = max(min(speed, 100), -100)
    return int((speed / 100) * 512)

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_motor_factory()
txt_factory.init_counter_factory()
txt_factory.init_servomotor_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

TXT_M_M3_motor = txt_factory.motor_factory.create_encodermotor(TXT_M, 3)
TXT_M_C3_motor_step_counter = txt_factory.counter_factory.create_encodermotor_counter(TXT_M, 3)
TXT_M_C3_motor_step_counter.set_motor(TXT_M_M3_motor)
TXT_M_S1_servomotor = txt_factory.servomotor_factory.create_servomotor(TXT_M, 1)
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
STEPS_PER_ROTATION = 128


def run():
    print("Moving Motors")
    print("M1 Motor for 2 seconds")
    motor_start(TXT_M_M1_motor, 30)
    time.sleep(2000/1000)
    TXT_M_M1_motor.stop()
    print("M2 Motor for 2 seconds")
    motor_start(TXT_M_M2_motor, 30)
    TXT_M_M2_motor.stop()
    time.sleep(2000/1000)
    print("M1 Motor for 2 seconds negative speed")
    motor_start(TXT_M_M2_motor, -50)
    time.sleep(2000/1000)
    TXT_M_M2_motor.stop()
    print("M1 then M2 Motor for 5 rotations")
    motor_start_for(TXT_M_M1_motor, 30, 360 * 5)
    motor_start_for(TXT_M_M2_motor, 30, 360 * 5)
    print("M1 then M2 Motor for 180 degrees")
    time.sleep(500/1000)
    motor_start_for(TXT_M_M1_motor, 30, 180)
    motor_start_for(TXT_M_M2_motor, 30, 180)
    print("Servo test on S1")
    print("to 180 degrees")
    TXT_M_S1_servomotor.set_position(int((180 / 180) * 512))
    time.sleep(1000/1000)
    print("to 0 degrees")
    TXT_M_S1_servomotor.set_position(int((0 / 180) * 512))
    time.sleep(1000/1000)
    print("to 90 degrees")
    TXT_M_S1_servomotor.set_position(int((90 / 180) * 512))
    time.sleep(1000/1000)

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
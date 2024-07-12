import fischertechnik.factories as txt_factory
from fischertechnik.controller.Motor import Motor
from lib.display import display
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
TXT_M_I3_ultrasonic_distance_meter = txt_factory.input_factory.create_ultrasonic_distance_meter(TXT_M, 3)
TXT_M_I1_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, 1)
TXT_M_I2_trail_follower = txt_factory.input_factory.create_trail_follower(TXT_M, 2)
TXT_M_I5_color_sensor = txt_factory.input_factory.create_color_sensor(TXT_M, 5)
TXT_M_I4_mini_switch = txt_factory.input_factory.create_mini_switch(TXT_M, 4)
txt_factory.initialized()
time.sleep(0.1)
STEPS_PER_ROTATION = 128

_timer1 = time.time()
_timer2 = time.time()
_timer3 = time.time()
_timer4 = time.time()
_timer5 = time.time()

def motor_start(motor, speed):
    motor.set_speed(speed_to_pwm(speed), Motor.CCW)
    motor.start()

def speed_to_pwm(speed):
    speed = max(min(speed, 100), -100)
    return int((speed / 100) * 512)

def run():
    global _timer1, _timer2, _timer3, _timer4, _timer5
    while not(TXT_M_I3_ultrasonic_distance_meter):
        pass

    print("SENSOR TEST")
    print("Press right button on Display to continue")
    print("Ultrasonic distance on port I3")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I3_ultrasonic_distance_meter.get_distance())
        time.sleep(500/1000)
    print("Bottom Infrared left")
    time.sleep(500/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print((not TXT_M_I1_trail_follower.get_state()))
        time.sleep(500/1000)
    print("Bottom Infrared right")
    time.sleep(500/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print((not TXT_M_I1_trail_follower.get_state()))
        time.sleep(500/1000)
    print("Encoder C1 in degree")
    print("Starting M1 Motor")
    time.sleep(1000/1000)
    motor_start(TXT_M_M1_motor, 30)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int(TXT_M_C1_motor_step_counter.get_count() / STEPS_PER_ROTATION * 360))
        time.sleep(500/1000)
    TXT_M_M1_motor.stop()
    print("Encoder C2 in rotation")
    print("Starting M2 Motor")
    time.sleep(1000/1000)
    motor_start(TXT_M_M2_motor, 30)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int(TXT_M_C2_motor_step_counter.get_count() // STEPS_PER_ROTATION))
        time.sleep(500/1000)
    TXT_M_M2_motor.stop()
    print("Reset Encoder and return value")
    TXT_M_C1_motor_step_counter.reset()
    time.sleep(1000/1000)
    print(int(TXT_M_C1_motor_step_counter.get_count() / STEPS_PER_ROTATION * 360))
    print("Timer 1-5")
    time.sleep(1000/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(((time.time() - _timer1) * 1000))
        print(((time.time() - _timer2) * 1000))
        print(((time.time() - _timer3) * 1000))
        print(((time.time() - _timer4) * 1000))
        print(((time.time() - _timer5) * 1000))
        time.sleep(500/1000)
    print("Reset and return Timer 1")
    _timer1 = time.time()
    print(((time.time() - _timer1) * 1000))
    print("Mini-Switch on Port I4")
    time.sleep(1000/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I4_mini_switch.get_state())
        time.sleep(500/1000)
    print("Light Sensor on I5")
    time.sleep(1000/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(((TXT_M_I5_color_sensor.get_voltage() / 2000) * 100))
        time.sleep(500/1000)
    print("DONE")

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
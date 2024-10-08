import fischertechnik.factories as txt_factory
from lib.display import display
import math
import time

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_i2c_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

TXT_M_I2C_2_gesture_sensor = txt_factory.i2c_factory.create_gesture_sensor(TXT_M, 2)
TXT_M_I2C_1_environment_sensor = txt_factory.i2c_factory.create_environment_sensor(TXT_M, 1)
txt_factory.initialized()
TXT_M_I2C_2_gesture_sensor.enable_light()
TXT_M_I2C_2_gesture_sensor.enable_proximity()
time.sleep(0.1)



def ____environmentalSensor():
    print("Environmental Sensor")
    print("starting calibration if needed this takes a few minutes")
    TXT_M_I2C_1_environment_sensor.calibrate()
    while not not TXT_M_I2C_1_environment_sensor.needs_calibration():
        pass
    print("Callibration needed?")
    print(TXT_M_I2C_1_environment_sensor.needs_calibration())
    time.sleep(500/1000)
    print("Temperature")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_environment_sensor.get_temperature())
        time.sleep(500/1000)
    print("humidity")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_environment_sensor.get_humidity())
        time.sleep(500/1000)
    print("pressure")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_environment_sensor.get_pressure())
        time.sleep(500/1000)
    print("IAQ")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_environment_sensor.get_indoor_air_quality_as_number())
        time.sleep(500/1000)
    print("accuracy")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_environment_sensor.get_accuracy())
        time.sleep(500/1000)

def ____rgbGesture():
    print("rgb_gesture sensor")
    print("Color")
    time.sleep(500/1000)
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(hex(int(TXT_M_I2C_2_gesture_sensor.get_hex()[1:], 16)))
        time.sleep(500/1000)
    print("Ambient Light")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int((TXT_M_I2C_2_gesture_sensor.get_ambient() / 255) * 100))
        time.sleep(500/1000)
    print("Do Gestures")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int((TXT_M_I2C_2_gesture_sensor.get_ambient() / 255) * 100))
        time.sleep(500/1000)
    print("Proximity")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int((TXT_M_I2C_2_gesture_sensor.get_proximity() / 255) * 100))
        time.sleep(500/1000)
    print("RGB")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(int((TXT_M_I2C_2_gesture_sensor.get_proximity() / 255) * 100))
        time.sleep(500/1000)

def run():
    print("I2C Sensors Test Press the right display button to go to next sensor")
    ____environmentalSensor()
    ____rgbGesture()

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
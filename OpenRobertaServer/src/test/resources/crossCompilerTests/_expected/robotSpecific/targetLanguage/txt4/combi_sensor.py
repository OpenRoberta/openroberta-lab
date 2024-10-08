import fischertechnik.factories as txt_factory
from lib.display import display
import math
import time


def ____combiSensor():
    print("CombiSensor aka IMU")
    print("Acceleration X")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_acceleration_x())
        time.sleep(500/1000)
    print("Acceleration Y")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_acceleration_y())
        time.sleep(500/1000)
    print("Acceleration Z")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_acceleration_z())
        time.sleep(500/1000)
    print("Compass X")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_magnetic_field_x())
        time.sleep(500/1000)
    print("Compass Y")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_magnetic_field_y())
        time.sleep(500/1000)
    print("Compass Z")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_magnetic_field_z())
        time.sleep(500/1000)
    print("Gyro X")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_rotation_x())
        time.sleep(500/1000)
    print("Gyro Y")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_rotation_y())
        time.sleep(500/1000)
    print("Gyro Z")
    while True:
        if display.get_attr("buttonRight.pressed"):
            break
        print(TXT_M_I2C_1_combined_sensor_6pin.get_rotation_z())
        time.sleep(500/1000)

txt_factory.init()
txt_factory.init_input_factory()
txt_factory.init_i2c_factory()
TXT_M = txt_factory.controller_factory.create_graphical_controller()

TXT_M_I2C_1_combined_sensor_6pin = txt_factory.i2c_factory.create_combined_sensor_6pin(TXT_M, 1)
txt_factory.initialized()
TXT_M_I2C_1_combined_sensor_6pin.init_accelerometer(2, 1.5625)
TXT_M_I2C_1_combined_sensor_6pin.init_magnetometer(25)
TXT_M_I2C_1_combined_sensor_6pin.init_gyrometer(250, 12.5)
time.sleep(0.1)


def run():
    print("I2C Sensors Test Press the right display button to go to next sensor")
    ____combiSensor()

def main():
    try:
        run()
    except Exception as e:
        print(e)

main()
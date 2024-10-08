import calliopemini
import random
import math
from machine import time_pulse_us
from tcs3472 import tcs3472


def measure_distance_cm(echo=calliopemini.pin_A1_RX, trigger=calliopemini.pin_A1_RX):
    trigger.write_digital(1)
    trigger.write_digital(0) 
    msec = time_pulse_us(echo, 1)
    echo_time = msec / 1000000
    dist_cm = (echo_time / 2) * 34300
    return dist_cm 


def ____sensors():
    global timer1, ___colourVar
    calliopemini.display.scroll(str(measure_distance_cm()))
    ___colourVar = color_sensor.rgb()
    calliopemini.display.scroll(str(int( color_sensor.light() / LIGHT_CONST)))

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
color_sensor = tcs3472()
LIGHT_CONST = 40


___colourVar = (255, 0, 0)

def run():
    global timer1, ___colourVar
    ____sensors()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import calliopemini
import random
import math
import machine
from sht31 import SHT31

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
sht31 = SHT31()

___colourVar = (255, 0, 0)
___numberList = [0, 0]

def ____sensorsWaitUntil():
    global timer1, ___colourVar, ___numberList
    print("Press A")
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    print("Press B")
    while True:
        if calliopemini.button_b.is_pressed() == True:
            break
    print("Press Pin 0")
    while True:
        if calliopemini.pin0.is_touched() == True:
            break
    print("Press Pin 2")
    while True:
        if calliopemini.pin2.is_touched() == True:
            break
    print("upright gesture")
    while True:
        if ("up" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("upside down gesture")
    while True:
        if ("down" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("front side gesture")
    while True:
        if ("face down" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("at the back gesture")
    while True:
        if ("face up" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("shaking gesture")
    while True:
        if ("shake" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("freely falling gesture")
    while True:
        if ("freefall" == calliopemini.accelerometer.current_gesture()) == True:
            break
    print("compass smaller 30")
    while True:
        if calliopemini.compass.heading() < 30:
            break
    print("sound louder 50")
    while True:
        if int((calliopemini.microphone.sound_level() / 255) * 100) > 50:
            break
    print("timer bigger than 500")
    while True:
        if ( calliopemini.running_time() - timer1 ) > 500:
            break
    print("temperature over 20 ")
    while True:
        if calliopemini.temperature() > 20:
            break
    print("light over smaller 30")
    while True:
        if round(calliopemini.display.read_light_level() / 2.55) < 30:
            break
    print("accelerometer x over 50%")
    while True:
        if calliopemini.accelerometer.get_x() > 250:
            break
    print("accelerometer y over 50%")
    while True:
        if calliopemini.accelerometer.get_y() > 250:
            break
    print("accelerometer z over 50%")
    while True:
        if calliopemini.accelerometer.get_z() > 250:
            break
    print("accelerometer combined over 50%")
    while True:
        if math.sqrt(calliopemini.accelerometer.get_x()**2 + calliopemini.accelerometer.get_y()**2 + calliopemini.accelerometer.get_z()**2) > 250:
            break
    print("humidity sensor smaller 30")
    while True:
        if sht31.get_temp_humi("h") < 30:
            break
    print("humidity sensor temperature smaller 30")
    while True:
        if sht31.get_temp_humi("t") < 30:
            break

def run():
    global timer1, ___colourVar, ___numberList
    ____sensorsWaitUntil()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___numberVar = 0
___booleanVar = True
___stringVar = ""
___imageVar = microbit.Image.HEART
___numberList = [0, 0]
___booleanList = [True, True]
___stringList = ["", ""]
___imageList = [microbit.Image.HEART, microbit.Image.HEART]
def sensorsWaitUntil():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    while True:
        if microbit.button_a.is_pressed() == True:
            break
    while True:
        if microbit.button_b.is_pressed() == True:
            break
    while True:
        if microbit.pin0.is_touched() == True:
            break
    while True:
        if microbit.pin1.is_touched() == True:
            break
    while True:
        if microbit.pin2.is_touched() == True:
            break
    while True:
        if "up" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if "down" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if "face down" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if "face up" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if "shake" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if "freefall" == microbit.accelerometer.current_gesture() == True:
            break
    while True:
        if microbit.compass.heading() < 30:
            break
    while True:
        if ( microbit.running_time() - timer1 ) > 500:
            break
    while True:
        if microbit.temperature() < 20:
            break
    while True:
        if microbit.pin3.read_analog() < 30:
            break
    while True:
        if microbit.pin5.read_digital() < 30:
            break
    while True:
        if microbit.pin6.read_pulsehigh() < 30:
            break
    while True:
        if microbit.pin7.read_pulselow() < 30:
            break
    while True:
        if microbit.accelerometer.get_x() > 30:
            break
    while True:
        if microbit.accelerometer.get_x() > 30:
            break
    while True:
        if microbit.accelerometer.get_y() > 30:
            break
    while True:
        if microbit.accelerometer.get_z() > 30:
            break

def sensors():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    microbit.display.scroll(str(microbit.button_a.is_pressed()))
    microbit.display.scroll(str(microbit.button_b.is_pressed()))
    microbit.display.scroll(str("up" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str("down" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str("face down" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str("face up" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str("shake" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str("freefall" == microbit.accelerometer.current_gesture()))
    microbit.display.scroll(str(microbit.compass.heading()))
    microbit.display.scroll(str(( microbit.running_time() - timer1 )))
    timer1 = microbit.running_time()
    microbit.display.scroll(str(microbit.temperature()))
    microbit.display.scroll(str(microbit.pin0.is_touched()))
    microbit.display.scroll(str(microbit.pin1.is_touched()))
    microbit.display.scroll(str(microbit.pin2.is_touched()))
    microbit.display.scroll(str(microbit.pin1.read_analog()))
    microbit.display.scroll(str(microbit.pin2.read_analog()))
    microbit.display.scroll(str(microbit.pin3.read_analog()))
    microbit.display.scroll(str(microbit.pin4.read_analog()))
    microbit.display.scroll(str(microbit.pin10.read_analog()))
    microbit.display.scroll(str(microbit.pin5.read_digital()))
    microbit.display.scroll(str(microbit.pin6.read_digital()))
    microbit.display.scroll(str(microbit.pin7.read_digital()))
    microbit.display.scroll(str(microbit.pin8.read_digital()))
    microbit.display.scroll(str(microbit.pin9.read_digital()))
    microbit.display.scroll(str(microbit.pin11.read_digital()))
    microbit.display.scroll(str(microbit.pin12.read_digital()))
    microbit.display.scroll(str(microbit.pin13.read_digital()))
    microbit.display.scroll(str(microbit.pin14.read_digital()))
    microbit.display.scroll(str(microbit.pin15.read_digital()))
    microbit.display.scroll(str(microbit.pin16.read_digital()))
    microbit.display.scroll(str(microbit.pin19.read_digital()))
    microbit.display.scroll(str(microbit.pin20.read_digital()))
    microbit.display.scroll(str(microbit.accelerometer.get_x()))
    microbit.display.scroll(str(round(microbit.display.read_light_level() / 2.55)))

def run():
    global timer1, ___numberVar, ___booleanVar, ___stringVar, ___imageVar, ___numberList, ___booleanList, ___stringList, ___imageList
    sensors()
    sensorsWaitUntil()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
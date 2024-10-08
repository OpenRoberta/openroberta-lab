import calliopemini
import random
import math
import machine


def ____sensors():
    global timer1
    calliopemini.display.scroll(str(calliopemini.pin1.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin2.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin_A1_RX.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin4.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin5.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin6.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin_A1_RX.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin_A1_TX.read_analog()))
    calliopemini.display.scroll(str(calliopemini.pin0.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin3.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin_A0_SCL.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin7.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin8.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin9.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin10.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin11.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin12.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin18.read_digital()))
    calliopemini.display.scroll(str(calliopemini.pin19.read_digital()))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin0, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin3, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin_A0_SCL, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin7, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin8, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin9, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin10, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin11, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin12, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin18, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin19, 1)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin0, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin3, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin_A0_SCL, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin7, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin8, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin9, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin10, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin11, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin12, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin18, 0)))
    calliopemini.display.scroll(str(machine.time_pulse_us(calliopemini.pin19, 0)))

def ____sensorsWaitUntil():
    global timer1
    while True:
        if calliopemini.pin1.read_analog() < 30:
            break
    while True:
        if calliopemini.pin2.read_analog() < 30:
            break
    while True:
        if calliopemini.pin_A1_RX.read_analog() < 30:
            break
    while True:
        if calliopemini.pin4.read_analog() < 30:
            break
    while True:
        if calliopemini.pin5.read_analog() < 30:
            break
    while True:
        if calliopemini.pin6.read_analog() < 30:
            break
    while True:
        if calliopemini.pin_A1_RX.read_analog() < 30:
            break
    while True:
        if calliopemini.pin_A1_TX.read_analog() < 30:
            break
    while True:
        if calliopemini.pin0.read_digital() < 30:
            break
    while True:
        if calliopemini.pin3.read_digital() < 30:
            break
    while True:
        if calliopemini.pin_A0_SCL.read_digital() < 30:
            break
    while True:
        if calliopemini.pin7.read_digital() < 30:
            break
    while True:
        if calliopemini.pin8.read_digital() < 30:
            break
    while True:
        if calliopemini.pin9.read_digital() < 30:
            break
    while True:
        if calliopemini.pin10.read_digital() < 30:
            break
    while True:
        if calliopemini.pin11.read_digital() < 30:
            break
    while True:
        if calliopemini.pin12.read_digital() < 30:
            break
    while True:
        if calliopemini.pin18.read_digital() < 30:
            break
    while True:
        if calliopemini.pin19.read_digital() < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin0, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin3, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin_A0_SCL, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin7, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin8, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin9, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin10, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin11, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin12, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin18, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin19, 1) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin0, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin3, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin_A0_SCL, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin7, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin8, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin9, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin10, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin11, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin12, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin18, 0) < 30:
            break
    while True:
        if machine.time_pulse_us(calliopemini.pin19, 0) < 30:
            break

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


def run():
    global timer1
    ____sensors()
    ____sensorsWaitUntil()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
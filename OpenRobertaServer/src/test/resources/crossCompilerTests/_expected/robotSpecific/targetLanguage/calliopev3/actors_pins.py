import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___off = 0
___on = 1
___analogon = 255
def ____pin():
    global timer1, ___off, ___on, ___analogon
    calliopemini.pin1.write_analog(___analogon)
    ____wait()
    calliopemini.pin1.write_analog(___analogon)
    ____wait()
    calliopemini.pin2.write_analog(___analogon)
    ____wait()
    calliopemini.pin2.write_analog(___off)
    ____wait()
    calliopemini.pin_A1_RX.write_analog(___analogon)
    ____wait()
    calliopemini.pin_A1_RX.write_analog(___off)
    ____wait()
    calliopemini.pin4.write_analog(___analogon)
    ____wait()
    calliopemini.pin4.write_analog(___off)
    ____wait()
    calliopemini.pin5.write_analog(___analogon)
    ____wait()
    calliopemini.pin5.write_analog(___off)
    ____wait()
    calliopemini.pin6.write_analog(___analogon)
    ____wait()
    calliopemini.pin6.write_analog(___off)
    ____wait()
    calliopemini.pin_A1_RX.write_analog(___analogon)
    ____wait()
    calliopemini.pin_A1_RX.write_analog(___off)
    ____wait()
    calliopemini.pin_A1_TX.write_analog(___analogon)
    ____wait()
    calliopemini.pin_A1_TX.write_analog(___off)
    ____wait()
    calliopemini.pin0.write_digital(___on)
    ____wait()
    calliopemini.pin0.write_digital(___off)
    ____wait()
    calliopemini.pin3.write_digital(___on)
    ____wait()
    calliopemini.pin3.write_digital(___off)
    ____wait()
    calliopemini.pin_A0_SCL.write_digital(___on)
    ____wait()
    calliopemini.pin_A0_SCL.write_digital(___off)
    ____wait()
    calliopemini.pin7.write_digital(___on)
    ____wait()
    calliopemini.pin7.write_digital(___off)
    ____wait()
    calliopemini.pin8.write_digital(___on)
    ____wait()
    calliopemini.pin8.write_digital(___off)
    ____wait()
    calliopemini.pin9.write_digital(___on)
    ____wait()
    calliopemini.pin9.write_digital(___off)
    ____wait()
    calliopemini.pin10.write_digital(___on)
    ____wait()
    calliopemini.pin10.write_digital(___off)
    ____wait()
    calliopemini.pin11.write_digital(___on)
    ____wait()
    calliopemini.pin11.write_digital(___off)
    ____wait()
    calliopemini.pin12.write_digital(___on)
    ____wait()
    calliopemini.pin12.write_digital(___off)
    ____wait()
    calliopemini.pin18.write_digital(___on)
    ____wait()
    calliopemini.pin18.write_digital(___off)
    ____wait()
    calliopemini.pin19.write_digital(___on)
    ____wait()
    calliopemini.pin19.write_digital(___off)
    ____wait()
    calliopemini.display.on()
    ____wait()
    calliopemini.display.off()

def ____wait():
    global timer1, ___off, ___on, ___analogon
    while True:
        if calliopemini.button_a.is_pressed() == True:
            break
    calliopemini.sleep(800)

def run():
    global timer1, ___off, ___on, ___analogon
    ____pin()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
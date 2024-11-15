import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.off()
    print("ON Pin 0")
    microbit.pin0.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 0")
    microbit.pin0.write_digital(0)
    # -------------------------------------------
    print("100 Analog Pin 1")
    microbit.pin1.write_analog(100)
    microbit.sleep(500)
    print("0 Analog Pin 1")
    microbit.pin1.write_analog(0)
    # -------------------------------------------
    print("ON Pin 2")
    microbit.pin2.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 2")
    microbit.pin2.write_digital(0)
    # -------------------------------------------
    print("ON Pin 3")
    microbit.pin3.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 3")
    microbit.pin3.write_digital(0)
    # -------------------------------------------
    print("ON Pin 4")
    microbit.pin4.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 4")
    microbit.pin4.write_digital(0)
    # -------------------------------------------
    print("ON Pin 6")
    microbit.pin6.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 6")
    microbit.pin6.write_digital(0)
    # -------------------------------------------
    print("ON Pin 7")
    microbit.pin7.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 7")
    microbit.pin7.write_digital(0)
    # -------------------------------------------
    print("ON Pin 8")
    microbit.pin8.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 8")
    microbit.pin8.write_digital(0)
    # -------------------------------------------
    print("ON Pin 9")
    microbit.pin9.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 9")
    microbit.pin9.write_digital(0)
    # -------------------------------------------
    print("ON Pin 10")
    microbit.pin10.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 10")
    microbit.pin10.write_digital(0)
    # -------------------------------------------
    print("ON Pin 12")
    microbit.pin12.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 12")
    microbit.pin12.write_digital(0)
    # -------------------------------------------
    print("ON Pin 13")
    microbit.pin13.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 13")
    microbit.pin13.write_digital(0)
    # -------------------------------------------
    print("ON Pin 14")
    microbit.pin14.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 14")
    microbit.pin14.write_digital(0)
    # -------------------------------------------
    print("ON Pin 15")
    microbit.pin15.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 15")
    microbit.pin15.write_digital(0)
    # -------------------------------------------
    print("ON Pin 16")
    microbit.pin16.write_digital(1)
    microbit.sleep(500)
    print("OFF Pin 16")
    microbit.pin16.write_digital(0)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
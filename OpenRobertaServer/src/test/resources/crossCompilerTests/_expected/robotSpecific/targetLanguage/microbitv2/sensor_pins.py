import microbit
import random
import math
import machine

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.off()
    print("Pulse LOW Pin 1")
    print(machine.time_pulse_us(microbit.pin1, 0))
    print("Pulse High Pin 1")
    print(machine.time_pulse_us(microbit.pin1, 1))
    print("Sensor Analog 0")
    print(microbit.pin0.read_analog())
    print("Sensor Digital 1")
    print(microbit.pin1.read_digital())
    print("Sensor Digital 2")
    print(microbit.pin2.read_digital())
    print("Sensor Digital 3")
    print(microbit.pin3.read_digital())
    print("Sensor Digital 4")
    print(microbit.pin4.read_digital())
    print("Sensor Digital 5")
    print(microbit.pin5.read_digital())
    print("Sensor Digital 6")
    print(microbit.pin6.read_digital())
    print("Sensor Digital 7")
    print(microbit.pin7.read_digital())
    print("Sensor Digital 9")
    print(microbit.pin8.read_digital())
    print("Sensor Digital 10")
    print(microbit.pin9.read_digital())
    print("Sensor Digital 11")
    print(microbit.pin10.read_digital())
    print("Sensor Digital 12")
    print(microbit.pin11.read_digital())
    print("Sensor Digital 13")
    print(microbit.pin12.read_digital())
    print("Sensor Digital 14")
    print(microbit.pin13.read_digital())
    print("Sensor Digital 15")
    print(microbit.pin14.read_digital())
    print("Sensor Digital 16")
    print(microbit.pin15.read_digital())
    print("Sensor Digital 17")
    print(microbit.pin16.read_digital())

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
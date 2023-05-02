import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("Pulse LOW Pin 1")
    microbit.display.scroll(str(microbit.pin1.read_pulselow()))
    microbit.display.scroll("Pulse High Pin 1")
    microbit.display.scroll(str(microbit.pin1.read_pulsehigh()))
    microbit.display.scroll("Sensor Analog 0")
    microbit.display.scroll(str(microbit.pin0.read_analog()))
    microbit.display.scroll("Sensor Digital 1")
    microbit.display.scroll(str(microbit.pin1.read_digital()))
    microbit.display.scroll("Sensor Digital 2")
    microbit.display.scroll(str(microbit.pin2.read_digital()))
    microbit.display.scroll("Sensor Digital 3")
    microbit.display.scroll(str(microbit.pin3.read_digital()))
    microbit.display.scroll("Sensor Digital 4")
    microbit.display.scroll(str(microbit.pin4.read_digital()))
    microbit.display.scroll("Sensor Digital 5")
    microbit.display.scroll(str(microbit.pin5.read_digital()))
    microbit.display.scroll("Sensor Digital 6")
    microbit.display.scroll(str(microbit.pin6.read_digital()))
    microbit.display.scroll("Sensor Digital 7")
    microbit.display.scroll(str(microbit.pin7.read_digital()))
    microbit.display.scroll("Sensor Digital 9")
    microbit.display.scroll(str(microbit.pin8.read_digital()))
    microbit.display.scroll("Sensor Digital 10")
    microbit.display.scroll(str(microbit.pin9.read_digital()))
    microbit.display.scroll("Sensor Digital 11")
    microbit.display.scroll(str(microbit.pin10.read_digital()))
    microbit.display.scroll("Sensor Digital 12")
    microbit.display.scroll(str(microbit.pin11.read_digital()))
    microbit.display.scroll("Sensor Digital 13")
    microbit.display.scroll(str(microbit.pin12.read_digital()))
    microbit.display.scroll("Sensor Digital 14")
    microbit.display.scroll(str(microbit.pin13.read_digital()))
    microbit.display.scroll("Sensor Digital 15")
    microbit.display.scroll(str(microbit.pin14.read_digital()))
    microbit.display.scroll("Sensor Digital 16")
    microbit.display.scroll(str(microbit.pin15.read_digital()))
    microbit.display.scroll("Sensor Digital 17")
    microbit.display.scroll(str(microbit.pin16.read_digital()))
    microbit.display.scroll("Sensor Digital 18")
    microbit.display.scroll(str(microbit.pin19.read_digital()))
    microbit.display.scroll("Sensor Digital 19")
    microbit.display.scroll(str(microbit.pin20.read_digital()))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
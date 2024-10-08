import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___accX = microbit.accelerometer.get_x()
___accY = microbit.accelerometer.get_y()
___accZ = microbit.accelerometer.get_z()

def run():
    global timer1, ___accX, ___accY, ___accZ
    microbit.display.show(microbit.Image('90009:09090:00900:09090:90009'))
    while True:
        if ( microbit.accelerometer.get_x() - ___accX ) < -500:
            music.pitch(987, 500, microbit.pin16)
        elif ( microbit.accelerometer.get_x() - ___accX ) > 500:
            music.pitch(130, 500, microbit.pin16)
        if microbit.button_a.is_pressed():
            microbit.sleep(500)
            break
    microbit.display.show(microbit.Image('90009:09090:00900:00900:00900'))
    while True:
        if ( microbit.accelerometer.get_y() - ___accY ) < -500:
            music.pitch(987, 500, microbit.pin16)
        elif ( microbit.accelerometer.get_y() - ___accY ) > 500:
            music.pitch(130, 500, microbit.pin16)
        if microbit.button_a.is_pressed():
            microbit.sleep(500)
            break
    microbit.display.show(microbit.Image('99999:00090:00900:09000:99999'))
    while True:
        if ( microbit.accelerometer.get_z() - ___accZ ) < -500:
            music.pitch(987, 500, microbit.pin16)
        elif ( microbit.accelerometer.get_z() - ___accZ ) > 500:
            music.pitch(130, 500, microbit.pin16)
        if microbit.button_a.is_pressed():
            break

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
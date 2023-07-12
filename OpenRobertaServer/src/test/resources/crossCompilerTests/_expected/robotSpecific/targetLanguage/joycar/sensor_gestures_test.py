import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    while True:
        if ("up" == microbit.accelerometer.current_gesture()):
            microbit.display.show(microbit.Image('00900:09990:90909:00900:00900'))
        elif ("down" == microbit.accelerometer.current_gesture()):
            microbit.display.show(microbit.Image('00900:00900:90909:09990:00900'))
        elif ("face down" == microbit.accelerometer.current_gesture()):
            microbit.display.show(microbit.Image('99999:90909:90909:00900:00900'))
        elif ("face up" == microbit.accelerometer.current_gesture()):
            microbit.display.show(microbit.Image('00900:00900:90909:90909:99999'))
        elif ("shake" == microbit.accelerometer.current_gesture()):
            microbit.display.show(microbit.Image('09090:90909:09090:90909:09090'))
        elif ("freefall" == microbit.accelerometer.current_gesture()):
            music.pitch(261, 2000, microbit.pin16)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
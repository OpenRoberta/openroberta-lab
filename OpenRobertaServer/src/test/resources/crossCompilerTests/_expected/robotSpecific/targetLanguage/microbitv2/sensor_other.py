import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("Temperature")
    microbit.display.scroll(str(microbit.temperature()))
    microbit.display.scroll("microphone")
    microbit.display.scroll(str(int((microbit.microphone.sound_level() / 255) * 100)))
    microbit.display.scroll("make a sound")
    while True:
        if int((microbit.microphone.sound_level() / 255) * 100) > 50:
            break
    microbit.display.scroll("light sensor")
    microbit.display.scroll(str(round(microbit.display.read_light_level() / 2.55)))
    microbit.display.scroll("Timer:")
    microbit.display.scroll(str(( microbit.running_time() - timer1 )))
    timer1 = microbit.running_time()
    microbit.display.scroll("Timer Reset")
    microbit.display.scroll(str(( microbit.running_time() - timer1 )))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
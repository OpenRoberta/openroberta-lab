import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("giggle")
    microbit.audio.play(microbit.Sound.GIGGLE)
    microbit.sleep(1000)
    microbit.display.scroll("happy")
    microbit.audio.play(microbit.Sound.HAPPY)
    microbit.sleep(1000)
    microbit.display.scroll("hello")
    microbit.audio.play(microbit.Sound.HELLO)
    microbit.sleep(1000)
    microbit.display.scroll("mysterious")
    microbit.audio.play(microbit.Sound.MYSTERIOUS)
    microbit.sleep(1000)
    microbit.display.scroll("sad")
    microbit.audio.play(microbit.Sound.SAD)
    microbit.sleep(1000)
    microbit.display.scroll("slide")
    microbit.audio.play(microbit.Sound.SLIDE)
    microbit.sleep(1000)
    microbit.display.scroll("soaring")
    microbit.audio.play(microbit.Sound.SOARING)
    microbit.sleep(1000)
    microbit.display.scroll("spring")
    microbit.audio.play(microbit.Sound.SPRING)
    microbit.sleep(1000)
    microbit.display.scroll("twinkle")
    microbit.audio.play(microbit.Sound.TWINKLE)
    microbit.sleep(1000)
    microbit.display.scroll("yawn")
    microbit.audio.play(microbit.Sound.YAWN)
    microbit.sleep(1000)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
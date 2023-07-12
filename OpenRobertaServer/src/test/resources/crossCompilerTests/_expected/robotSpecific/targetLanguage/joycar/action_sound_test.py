import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    music.pitch(300, 100, microbit.pin16)
    music.pitch(261, 2000, microbit.pin16)
    music.pitch(493, 1000, microbit.pin16)
    music.play(music.DADADADUM, microbit.pin16)
    music.play(music.POWER_DOWN, microbit.pin16)
    microbit.set_volume(int(2.55 * 10))
    music.play(music.POWER_UP, microbit.pin16)
    music.pitch(300, 100, microbit.pin16)
    microbit.set_volume(int(2.55 * 100))
    music.pitch(100, 100, microbit.pin16)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
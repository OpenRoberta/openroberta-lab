import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("dadadadum")
    music.play(music.DADADADUM)
    microbit.display.scroll("entertainer")
    music.play(music.ENTERTAINER)
    microbit.display.scroll("prelude")
    music.play(music.PRELUDE)
    microbit.display.scroll("ode")
    music.play(music.ODE)
    microbit.display.scroll("nyan")
    music.play(music.NYAN)
    microbit.display.scroll("ringtone")
    music.play(music.RINGTONE)
    microbit.display.scroll("funk")
    music.play(music.FUNK)
    microbit.display.scroll("blues")
    music.play(music.BLUES)
    microbit.display.scroll("birthday")
    music.play(music.BIRTHDAY)
    microbit.display.scroll("wedding")
    music.play(music.WEDDING)
    microbit.display.scroll("funeral")
    music.play(music.FUNERAL)
    microbit.display.scroll("punchline")
    music.play(music.PUNCHLINE)
    microbit.display.scroll("python")
    music.play(music.PYTHON)
    microbit.display.scroll("baddy")
    music.play(music.BADDY)
    microbit.display.scroll("chase")
    music.play(music.CHASE)
    microbit.display.scroll("ba_ding")
    music.play(music.BA_DING)
    microbit.display.scroll("wawawawaa")
    music.play(music.WAWAWAWAA)
    microbit.display.scroll("jump up")
    music.play(music.JUMP_UP)
    microbit.display.scroll("jump down")
    music.play(music.JUMP_DOWN)
    microbit.display.scroll("power up")
    music.play(music.POWER_UP)
    microbit.display.scroll("power down")
    music.play(music.POWER_DOWN)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
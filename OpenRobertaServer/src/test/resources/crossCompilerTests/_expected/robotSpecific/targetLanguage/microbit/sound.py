import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("PLAY NOTE")
    music.pitch(261, 2000)
    microbit.display.scroll("PLAY TONE")
    music.pitch(300, 100)
    microbit.display.scroll("PLAY FILE")
    music.play(music.DADADADUM)
    music.play(music.ENTERTAINER)
    music.play(music.PRELUDE)
    music.play(music.ODE)
    music.play(music.NYAN)
    music.play(music.RINGTONE)
    music.play(music.FUNK)
    music.play(music.BLUES)
    music.play(music.BIRTHDAY)
    music.play(music.WEDDING)
    music.play(music.FUNERAL)
    music.play(music.PUNCHLINE)
    music.play(music.PYTHON)
    music.play(music.BADDY)
    music.play(music.CHASE)
    music.play(music.BA_DING)
    music.play(music.WAWAWAWAA)
    music.play(music.JUMP_UP)
    music.play(music.JUMP_DOWN)
    music.play(music.POWER_UP)
    music.play(music.POWER_DOWN)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
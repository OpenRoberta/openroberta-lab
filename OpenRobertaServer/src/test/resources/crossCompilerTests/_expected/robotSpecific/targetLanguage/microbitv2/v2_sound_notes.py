import microbit
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("Play Frequency")
    music.pitch(300, 100)
    music.pitch(100, 50)
    microbit.display.scroll("Play Full Note")
    music.pitch(261, 2000)
    microbit.display.scroll("Play Half Note")
    music.pitch(466, 1000)
    microbit.display.scroll("Play Quarter Note")
    music.pitch(466, 500)
    microbit.display.scroll("Play Eighth Note")
    music.pitch(466, 250)
    microbit.display.scroll("Play Sixteenth Note")
    music.pitch(466, 125)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
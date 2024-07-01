import calliopemini
import random
import math
import music

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___n = 0
def ____sounds():
    global timer1, ___n
    music.pitch(___n, ___n)
    music.pitch(261, 2000)
    music.pitch(293, 1000)
    music.pitch(329, 500)
    music.pitch(349, 250)
    music.pitch(391, 125)

def run():
    global timer1, ___n
    ____sounds()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
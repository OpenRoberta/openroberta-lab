import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
brightness = 9


def run():
    global timer1
    calliopemini.display.show(calliopemini.Image('00000:00000:00000:00000:00000'))
    calliopemini.display.show(calliopemini.Image('00000:09000:00900:00090:00000').invert())
    calliopemini.display.show(calliopemini.Image('00000:09000:00900:00090:00000').shift_up(1))
    calliopemini.display.show(calliopemini.Image.HEART)
    calliopemini.display.show(calliopemini.Image.SMILE)
    calliopemini.display.show(calliopemini.Image.STICKFIGURE)
    calliopemini.display.show(calliopemini.Image.GIRAFFE)
    calliopemini.display.show(calliopemini.Image.UMBRELLA)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
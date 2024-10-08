import calliopemini
import random
import math
from tm1637 import TM1637


def set_brightness(val):
    global brightness
    if val < 0 or val >= 10:
        raise ValueError("Brightness must be in the range [0, 9]")
    for y in range(5):
        for x in range(5):
            calliopemini.display.set_pixel(x,y,val)
    brightness = val


def ____display():
    global timer1, ___n, ___b, ___s, ___c, ___i, ___nl, ___bl, ___sl, ___cl, ___il
    calliopemini.display.scroll(str(___n))
    calliopemini.display.scroll(str(___b))
    calliopemini.display.scroll(str(___s))
    calliopemini.display.show(str(___n))
    calliopemini.display.show(str(___b))
    calliopemini.display.show(str(___s))
    calliopemini.display.show(___i)
    calliopemini.display.show(___il)
    calliopemini.display.clear()
    set_brightness(___n)
    calliopemini.display.scroll(str(brightness))
    calliopemini.display.set_pixel(___n, ___n, ___n)
    calliopemini.display.scroll(str(calliopemini.display.get_pixel(___n, ___n)))
    print(___n)
    print(___b)
    print(___s)
    fdd.show(str(int(___n)), ___n, ___b)
    fdd.clear()

def ____action():
    global timer1, ___n, ___b, ___s, ___c, ___i, ___nl, ___bl, ___sl, ___cl, ___il
    ____display()

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()
fdd = TM1637()
brightness = 9


___n = 0
___b = True
___s = ""
___c = (255, 0, 0)
___i = calliopemini.Image.HEART
___nl = [0, 0]
___bl = [True, True]
___sl = ["", ""]
___cl = [(255, 0, 0), (255, 0, 0)]
___il = [calliopemini.Image.HEART, calliopemini.Image.HEART]

def run():
    global timer1, ___n, ___b, ___s, ___c, ___i, ___nl, ___bl, ___sl, ___cl, ___il
    ____action()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
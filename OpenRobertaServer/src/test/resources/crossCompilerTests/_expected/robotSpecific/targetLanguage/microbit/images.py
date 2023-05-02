import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___Element = microbit.Image('90009:09090:00900:09090:90009')
___Element2 = [microbit.Image.HEART, microbit.Image.ASLEEP, microbit.Image.SAD]
def run():
    global timer1, ___Element, ___Element2
    microbit.display.show(microbit.Image('90900:90900:90900:90900:90900'))
    microbit.sleep(1500)
    microbit.display.show(___Element)
    microbit.sleep(1500)
    microbit.display.show(microbit.Image('90900:90900:90900:90900:90900').shift_right(1))
    microbit.sleep(1500)
    microbit.display.show(microbit.Image('99900:90000:90000:90000:90000').invert())
    microbit.sleep(1500)
    microbit.display.show(microbit.Image.HEART)
    microbit.sleep(1500)
    microbit.display.show(microbit.Image.HEART_SMALL)
    microbit.sleep(1500)
    microbit.display.show(microbit.Image.SAD)
    microbit.sleep(1500)
    microbit.display.show(microbit.Image.COW)
    microbit.sleep(1500)
    microbit.display.show(___Element2)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
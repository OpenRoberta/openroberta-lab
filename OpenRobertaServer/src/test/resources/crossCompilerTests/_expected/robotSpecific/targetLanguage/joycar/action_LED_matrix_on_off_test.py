import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.show(microbit.Image('99999:90009:90009:90009:99999'))
    microbit.sleep(500)
    microbit.display.off()
    microbit.sleep(500)
    microbit.display.show(microbit.Image('99999:99999:99999:99999:99999'))
    microbit.sleep(2000)
    microbit.display.on()
    microbit.sleep(500)
    microbit.display.show(microbit.Image('00000:09990:09990:09990:00000'))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
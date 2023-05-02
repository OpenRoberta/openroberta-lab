import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___Element2 = [6, 7, 8]
def run():
    global timer1, ___Element2
    for ___i in range(int(0), int(5), int(1)):
        microbit.display.scroll(str(___i))
        microbit.sleep(500)
    for ___Element in ___Element2:
        microbit.display.scroll(str(___Element))
        microbit.sleep(500)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
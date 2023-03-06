import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

def run():
    global timer1
    microbit.display.scroll("TEXT")
    microbit.display.show(str(0))
    microbit.display.show(microbit.Image('90009:09090:00900:09090:90009'))
    microbit.sleep(1000)
    microbit.display.clear()
    microbit.display.show(microbit.Image.HEART)
    microbit.sleep(1000)
    microbit.display.clear()
    microbit.display.set_pixel(0, 0, 5)
    microbit.sleep(500)
    microbit.display.set_pixel(0, 0, 100)
    microbit.sleep(500)
    microbit.display.set_pixel(0, 0, 50)
    print("Serial Monitor")
    microbit.display.scroll("LED Brightness expected 50")
    microbit.display.scroll(str(microbit.display.get_pixel(0, 0)))
    microbit.display.set_pixel(0, 0, 0)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
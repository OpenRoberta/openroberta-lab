import microbit
import random
import math
import radio

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()
radio.on()

def run():
    global timer1
    radio.config(group=0)
    radio.config(power=7)
    radio.send(str("Hi"))
    microbit.display.scroll(str(radio.receive()))
    radio.config(power=0)
    radio.send(str("Bye"))
    microbit.display.scroll(str(radio.receive()))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2

import radio

radio.on()
class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = 0
item2 = True
item3 = ""
def run():
    global timer1, item, item2, item3
    radio.config(group=0)
    radio.config(power=7)
    radio.send(str(1))
    radio.config(power=7)
    radio.send(str(True))
    radio.config(power=7)
    radio.send(str("123"))
    item = ((lambda x: 0 if x is None else float(x))(radio.receive()))
    item2 = ('True' == radio.receive())
    item3 = radio.receive()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math
import radio

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()
radio.on()

___item = 0
___item2 = True
___item3 = ""
def run():
    global timer1, ___item, ___item2, ___item3
    radio.config(group=0)
    radio.config(power=7)
    radio.send(str(1))
    radio.config(power=7)
    radio.send(str(True))
    radio.config(power=7)
    radio.send(str("123"))
    ___item = ((lambda x: 0 if x is None else float(x))(radio.receive()))
    ___item2 = ('True' == radio.receive())
    ___item3 = radio.receive()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

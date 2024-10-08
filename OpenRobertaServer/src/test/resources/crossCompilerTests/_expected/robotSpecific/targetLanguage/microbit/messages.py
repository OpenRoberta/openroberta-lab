import microbit
import random
import math
import radio


def receive_message(type):
    msg = radio.receive()
    if type == "Number":
        try:
            digit = float(msg)
        except (ValueError, TypeError) as e:
            digit = 0
        return digit
    elif type == "Boolean":
        return ((lambda x: False if x is None else x == 'True')(msg))
    elif type == "String":
        return ((lambda x: '' if x is None else x)(msg))

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()
radio.on()

def run():
    global timer1
    radio.config(group=0)
    radio.config(power=7)
    radio.send(str("Hi"))
    microbit.display.scroll(str(receive_message("String")))
    radio.config(power=0)
    radio.send(str("Bye"))
    microbit.display.scroll(str(receive_message("String")))

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
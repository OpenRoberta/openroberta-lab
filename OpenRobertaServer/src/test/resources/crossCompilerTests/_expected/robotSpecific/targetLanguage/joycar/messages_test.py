import microbit
import random
import math
import radio

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()
radio.on()

___Element = 0
def run():
    global timer1, ___Element
    radio.config(group=0)
    print("Press A to send on channel 0")
    while True:
        if microbit.button_a.is_pressed():
            radio.config(power=7)
            radio.send(str(1))
            print("Sending 1 with strength 7")
        microbit.sleep(500)
        print(receive_message("Number"))

def main():
    try:
        run()
    except Exception as e:
        raise

def receive_message(type):
    msg = radio.receive()
    if type == "Number":
        try:
            digit = float(msg)
        except (ValueError, TypeError) as e:
            digit = 0
        return digit
    elif type == "Boolean":
        return ((lambda x: False if x is None else x == True)(msg))
    elif type == "String":
        return ((lambda x: '' if x is None else x)(msg))

if __name__ == "__main__":
    main()
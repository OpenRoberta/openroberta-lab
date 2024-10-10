import microbit
import random
import math
import radio


def _isPrime(number):
    if(number == 0 or number == 1):
        return False
    for i in range(2, int(math.floor(math.sqrt(number))) + 1):
        remainder = number % i
        if remainder == 0:
            return False
    return True

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

___num = 0
___boolT = True
___boolF = False
___listN = [0, 0, 0]
___str = ""
___listN2 = [0, 0, 0, 0, 0, 0]
___ima = microbit.Image.HEART
___listIma = [microbit.Image.HEART, microbit.Image.HEART, microbit.Image.HEART]

def run():
    global timer1, ___num, ___boolT, ___boolF, ___listN, ___str, ___listN2, ___ima, ___listIma
    ___num = ( math.exp(2) + math.sin(90) ) - ( random.randint(1, 10) * math.ceil(float(2.3)) )
    ___num = ( ( ( sum(___listN) + ___listN[0] ) + ___listN.index(0) ) + ___listN[0] ) - ___listN2.pop(1)
    ___boolF = ( ( ( (10 % 2) == 0 and (7 % 2) == 1 ) or ( _isPrime(11) and (8 % 1) == 0 ) ) or ( not ___listN and 5 > 0 ) ) or ( - (3) < 0 and (10 % 5) == 0 )
    ___str = "".join(str(arg) for arg in [___listN[0], "Hello", chr((int)(65))])
    ___listN2 = ___listN[0:3]
    ___ima = microbit.Image.HEART
    ___num = microbit.display.get_pixel(300, 500)
    ___boolT = microbit.pin_logo.is_touched()
    ___boolT = ("up" == microbit.accelerometer.current_gesture())
    ___boolT = receive_message("Boolean")
    ___ima = microbit.Image('00009:00009:00009:00009:00009').shift_down(1)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
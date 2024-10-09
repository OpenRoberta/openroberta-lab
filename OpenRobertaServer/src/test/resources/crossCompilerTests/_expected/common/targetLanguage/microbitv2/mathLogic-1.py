import microbit
import random
import math


def _isPrime(number):
    if(number == 0 or number == 1):
        return False
    for i in range(2, int(math.floor(math.sqrt(number))) + 1):
        remainder = number % i
        if remainder == 0:
            return False
    return True

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___r1 = 0
___r2 = 0
___b1 = True
___r3 = 0
___sim = True

def run():
    global timer1, ___r1, ___r2, ___b1, ___r3, ___sim
    ___r1 = math.sqrt(( 20 - ( 2 * ( 4 / float(2) ) ) ) + math.pow(3, 2))
    ___b1 = ___b1 and not (___r1 % 2) == 0
    ___b1 = ___b1 and (___r1 % 2) == 1
    ___b1 = ___b1 and _isPrime(___r1)
    ___b1 = ___b1 and (___r1 % 1) == 0
    ___b1 = ___b1 and ___r1 > 0
    ___b1 = ___b1 and not ___r1 < 0
    ___b1 = ___b1 and (___r1 % 5) == 0
    ___b1 = ___b1 and not (___r1 % 3) == 0
    ___r1 += 1
    ___b1 = ___b1 and (___r1 % 2) == 0
    ___r2 = math.sqrt(20)
    ___b1 = ___b1 and not (___r2 % 1) == 0
    ___b1 = ___b1 and ( round(___r2) == 4 )
    ___b1 = ___b1 and ( math.ceil(___r2) == 5 )
    ___b1 = ___b1 and ( math.floor(___r2) == 4 )
    ___b1 = ___b1 and ( ___r1 > ___r2 )
    ___b1 = ___b1 and ( ___r1 >= ___r2 )
    ___b1 = ___b1
    ___b1 = ( ___b1 and ( ___r2 < ___r1 ) ) and ( ___r1 <= ___r1 )
    ___b1 = ___b1 and ( ( ( ___r1 ) % ( 4 ) ) == 2 )
    ___b1 = ___b1 and ( 29 == ( min(max(math.pow(3, 2), 1), 20) + ( min(max(9, 3 * 4), 18) + min(max(3 * 3, 5), 8) ) ) )
    ___b1 = ___b1 and ( 11 > ( random.random() * random.randint(1, 10) ) )
    # if b1 is true, the test succeeded, otherwise it failed :-)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
import microbit
import random
import math


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = 0

def ____plusOperations(___item2):
    global timer1, ___item
    ___item2 = ( 1 * 2 ) + ( 3 + 4 )
    ___item2 = min(max(( ( 6 + 5 ) % ( 5 ) ), 1), 100)
    ___item2 = ( ( math.sqrt(6) + math.sin(5) ) % ( 5 ) )
    ___item2 = ( ( 6 + math.pi ) % ( round(float(7.8)) ) )
    ___item2 = ( ( 6 + random.randint(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item2 = ( ( random.random() + 5 ) % ( 5 ) )

def ____multiplicationOperations(___item4):
    global timer1, ___item
    ___item4 = ( 1 * 2 ) * ( 3 + 4 )
    ___item4 = min(max(( ( 6 * 5 ) % ( 5 ) ), 1), 100)
    ___item4 = ( ( math.sqrt(6) * math.sin(5) ) % ( 5 ) )
    ___item4 = ( ( 6 * math.pi ) % ( round(float(7.8)) ) )
    ___item4 = ( ( 6 * random.randint(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item4 = ( ( random.random() * 5 ) % ( 5 ) )

def ____exponentOperations(___item6):
    global timer1, ___item
    ___item6 = math.pow(1 * 2, 3 + 4)
    ___item6 = min(max(( ( math.pow(6, 5) ) % ( 5 ) ), 1), 100)
    ___item6 = ( ( math.pow(math.sqrt(6), math.sin(5)) ) % ( 5 ) )
    ___item6 = ( ( math.pow(6, math.pi) ) % ( round(float(7.8)) ) )
    ___item6 = ( ( math.pow(6, random.randint(10 - 1, 100 - 1)) ) % ( 5 ) )
    ___item6 = ( ( math.pow(random.random(), 5) ) % ( 5 ) )

def ____minusOperations(___item3):
    global timer1, ___item
    ___item3 = ( 1 * 2 ) - ( 3 + 4 )
    ___item3 = min(max(( ( 6 - 5 ) % ( 5 ) ), 1), 100)
    ___item3 = ( ( math.sqrt(6) - math.sin(5) ) % ( 5 ) )
    ___item3 = ( ( 6 - math.pi ) % ( round(float(7.8)) ) )
    ___item3 = ( ( 6 - random.randint(10 - 1, 100 - 1) ) % ( 5 ) )
    ___item3 = ( ( random.random() - 5 ) % ( 5 ) )

def ____divisionOperations(___item5):
    global timer1, ___item
    ___item5 = ( 1 * 2 ) / float(( 3 + 4 ))
    ___item5 = min(max(( ( 6 / float(5) ) % ( 5 ) ), 1), 100)
    ___item5 = ( ( math.sqrt(6) / float(math.sin(5)) ) % ( 5 ) )
    ___item5 = ( ( 6 / float(math.pi) ) % ( round(float(7.8)) ) )
    ___item5 = ( ( 6 / float(random.randint(10 - 1, 100 - 1)) ) % ( 5 ) )
    ___item5 = ( ( random.random() / float(5) ) % ( 5 ) )


def run():
    global timer1, ___item
    ____plusOperations(___item)
    ____minusOperations(___item)
    ____multiplicationOperations(___item)
    ____divisionOperations(___item)
    ____exponentOperations(___item)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
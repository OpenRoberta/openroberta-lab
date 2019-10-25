import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = 0
___item2 = True
def run():
    global timer1, ___item, ___item2
    ___item = min(max(___item, 1), 100)
    ___item2 = (___item % 2) == 0
    ___item2 = (___item % 2) == 1
    ___item2 = _isPrime(___item)
    ___item2 = (___item % 1) == 0
    ___item2 = ___item > 0
    ___item2 = ___item < 0
    ___item2 = (___item % ___item) == 0
    ___item = random.randint(1, 100)
    ___item = random.random()

def main():
    try:
        run()
    except Exception as e:
        raise

def _isPrime(number):
  if(number==0 or number==1):
    return False
  for i in range(2,int(math.floor(math.sqrt(number)))+1):
    remainder = number % i
    if remainder==0:
      return False
  return True

if __name__ == "__main__":
    main()

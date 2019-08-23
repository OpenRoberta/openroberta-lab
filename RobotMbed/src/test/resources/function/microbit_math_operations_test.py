import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

item = 0
item2 = True
def run():
    global timer1, item, item2
    item = min(max(item, 1), 100)
    item2 = (item % 2) == 0
    item2 = (item % 2) == 1
    item2 = _isPrime(item)
    item2 = (item % 1) == 0
    item2 = item > 0
    item2 = item < 0
    item2 = (item % item) == 0
    item = random.randint(1, 100)
    item = random.random()

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
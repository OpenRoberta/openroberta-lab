import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = [0, 0, 0]
___item2 = [True, True, True]
___item3 = ["", "", ""]
___item4 = [microbit.Image.HEART, microbit.Image.HEART, microbit.Image.HEART]
___item5 = []
___item6 = []
___item7 = []
___item8 = []
___item9 = 0
def run():
    global timer1, ___item, ___item2, ___item3, ___item4, ___item5, ___item6, ___item7, ___item8, ___item9
    ___item9 = sum(___item)
    ___item9 = min(___item)
    ___item9 = max(___item)
    ___item9 = float(sum(___item))/len(___item)
    ___item9 = _median(___item)
    ___item9 = _standard_deviation(___item)
    ___item9 = ___item[0]

def main():
    try:
        run()
    except Exception as e:
        raise

def _median(l):
    l = sorted(l)
    l_len = len(l)
    if l_len < 1:
        return None
    if l_len % 2 == 0:
        return ( l[int( (l_len-1) / 2)] + l[int( (l_len+1) / 2)] ) / 2.0
    else:
        return l[int( (l_len-1) / 2)]

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))

if __name__ == "__main__":
    main()

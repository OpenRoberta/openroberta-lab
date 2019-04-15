import microbit
import random
import math

_GOLDEN_RATIO = (1 + 5 ** 0.5) / 2


class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

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

timer1 = microbit.running_time()

item = [0, 0, 0]
item2 = [True, True, True]
item3 = ["", "", ""]
item4 = [microbit.Image.HEART, microbit.Image.HEART, microbit.Image.HEART]
item5 = []
item6 = []
item7 = []
item8 = []
item9 = 0
def run():
    global timer1, item, item2, item8, item7, item9, item4, item3, item6, item5
    item9 = sum(item)
    item9 = min(item)
    item9 = max(item)
    item9 = float(sum(item))/len(item)
    item9 = _median(item)
    item9 = _standard_deviation(item)
    item9 = item[0]

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

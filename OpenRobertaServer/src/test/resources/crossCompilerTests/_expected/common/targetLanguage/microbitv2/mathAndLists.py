import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___l1 = [0, 0, 0, 0]
___x = 0
___item3 = 0
___l2 = []
___b = True
def run():
    global timer1, ___l1, ___x, ___item3, ___l2, ___b
    ___x = sum(___l1)
    ___x = min(___l1)
    ___x = max(___l1)
    ___x = float(sum(___l1))/len(___l1)
    ___x = _median(___l1)
    ___x = _standard_deviation(___l1)
    ___l2 = ___l1
    ___b = not ___l1
    ___x = len( ___l1)
    ___x = ___l1.index(0)
    ___x = (len(___l1) - 1) - ___l1[::-1].index(0)
    ___x = ___l1[0]
    ___x = ___l1[-1 -0]
    ___x = ___l1[0]
    ___x = ___l1[-1]
    ___x = ___l1.pop(0)
    ___x = ___l1.pop(-1 -0)
    ___x = ___l1.pop(0)
    ___x = ___l1.pop(-1)
    ___l1.pop(0)
    ___l1.pop(-1 -0)
    ___l1.pop(0)
    ___l1.pop(-1)
    ___l1[0] = 0
    ___l1[-1 -0] = 0
    ___l1[0] = 0
    ___l1[-1] = 0
    ___l1.insert(0, 0)
    ___l1.insert(-1 -0, 0)
    ___l1.insert(0, 0)
    ___l1.insert(-1, 0)

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
        return (l[int((l_len - 1) / 2)] + l[int((l_len + 1) / 2)] ) / 2.0
    else:
        return l[int((l_len - 1) / 2)]

def _standard_deviation(l):
    mean = float(sum(l)) / len(l)
    sd = 0
    for i in l:
        sd += (i - mean)*(i - mean)
    return math.sqrt(sd / len(l))
if __name__ == "__main__":
    main()
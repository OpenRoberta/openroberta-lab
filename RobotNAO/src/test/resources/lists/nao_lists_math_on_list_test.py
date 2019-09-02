#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


item = [0, 0, 0]
item2 = 0

def run():
    h.setAutonomousLife('OFF')
    global item, item2
    item2 = sum(item)
    item2 = min(item)
    item2 = max(item)
    item2 = float(sum(item)) / len(item)
    item2 = _median(item)
    item2 = _standard_deviation(item)
    item2 = item[0]

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

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

#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


___item = [0, 0, 0]
___item2 = 0

def run():
    h.setAutonomousLife('OFF')
    global ___item, ___item2
    ___item2 = ___item[0]
    ___item2 = ___item[-1 -0]
    ___item2 = ___item[0]
    ___item2 = ___item[-1]
    ___item2 = ___item[0]
    ___item2 = ___item.pop(0)
    ___item2 = ___item.pop(-1 -0)
    ___item2 = ___item.pop(0)
    ___item2 = ___item.pop(-1)
    ___item2 = ___item.pop(0)
    ___item.pop(0)
    ___item.pop(-1 -0)
    ___item.pop(0)
    ___item.pop(-1)
    ___item.pop(0)
    ___item[0] = 0
    ___item[-1 -0] = 0
    ___item[0] = 0
    ___item[-1] = 0
    ___item[0] = 0
    ___item.insert(0, 0)
    ___item.insert(-1 -0, 0)
    ___item.insert(0, 0)
    ___item.insert(-1, 0)
    ___item.insert(0, 0)

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()

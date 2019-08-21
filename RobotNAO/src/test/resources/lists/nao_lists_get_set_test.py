#!/usr/bin/python

import math
import time
from roberta import Hal
h = Hal()


item = [0, 0, 0]
item2 = 0

def run():
    h.setAutonomousLife('OFF')
    global item, item2
    item2 = item[0]
    item2 = item[-1 -0]
    item2 = item[0]
    item2 = item[-1]
    item2 = item[0]
    item2 = item.pop(0)
    item2 = item.pop(-1 -0)
    item2 = item.pop(0)
    item2 = item.pop(-1)
    item2 = item.pop(0)
    item.pop(0)
    item.pop(-1 -0)
    item.pop(0)
    item.pop(-1)
    item.pop(0)
    item[0] = 0
    item[-1 -0] = 0
    item[0] = 0
    item[-1] = 0
    item[0] = 0
    item.insert(0, 0)
    item.insert(-1 -0, 0)
    item.insert(0, 0)
    item.insert(-1, 0)
    item.insert(0, 0)

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
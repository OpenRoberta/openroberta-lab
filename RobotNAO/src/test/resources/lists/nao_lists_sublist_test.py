#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


item = [0, 0, 0]
item2 = []
item3 = []
item4 = []
item5 = []

def run():
    h.setAutonomousLife('OFF')
    global item, item2, item3, item4, item5
    item = item[0:0]
    item = item[0:-1 -0]
    item = item[0:]
    item = item[-1 -0:0]
    item = item[-1 -0:-1 -0]
    item = item[-1 -0:]
    item = item[0:0]
    item = item[0:-1 -0]
    item = item[0:]

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()

#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


___item = [0, 0, 0]
___item2 = []
___item3 = []
___item4 = []
___item5 = []

def run():
    h.setAutonomousLife('OFF')
    global ___item, ___item2, ___item3, ___item4, ___item5
    ___item = ___item[0:0]
    ___item = ___item[0:-1 -0]
    ___item = ___item[0:]
    ___item = ___item[-1 -0:0]
    ___item = ___item[-1 -0:-1 -0]
    ___item = ___item[-1 -0:]
    ___item = ___item[0:0]
    ___item = ___item[0:-1 -0]
    ___item = ___item[0:]

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()

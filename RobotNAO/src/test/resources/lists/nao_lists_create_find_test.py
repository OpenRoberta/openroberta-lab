#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


___item = [0, 0, 0]
___item2 = [True, True, True]
___item3 = ["a", "b", "c"]
___item4 = [0xff0000, 0xff0000, 0xff0000]
___item5 = []
___item6 = []
___item7 = []
___item8 = []
___item9 = [5] *  5
___item10 = [True] *  5
___item11 = ["c"] *  5
___item12 = [0xff0000] *  5
___item13 = 0
___item14 = True
___item15 = "a"
___item16 = 0xff0000

def run():
    h.setAutonomousLife('OFF')
    global ___item, ___item2, ___item3, ___item4, ___item5, ___item6, ___item7, ___item8, ___item9, ___item10, ___item11, ___item12, ___item13, ___item14, ___item15, ___item16
    ___item13 = ___item.index(5)
    ___item13 = (len(___item)-1)-___item[::-1].index(5)
    ___item13 = ___item2.index(True)
    ___item13 = (len(___item2)-1)-___item2[::-1].index(True)
    ___item13 = ___item3.index("a")
    ___item13 = (len(___item3)-1)-___item3[::-1].index("a")
    ___item13 = ___item4.index(0xff0000)
    ___item13 = (len(___item4)-1)-___item4[::-1].index(0xff0000)
    ___item13 = len(___item)
    ___item14 = not ___item
    ___item14 = not ___item2
    ___item14 = not ___item3
    ___item14 = not ___item4

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()

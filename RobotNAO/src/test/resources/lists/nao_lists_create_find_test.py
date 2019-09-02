#!/usr/bin/python

import math
import time
import random
from roberta import Hal
h = Hal()


item = [0, 0, 0]
item2 = [True, True, True]
item3 = ["a", "b", "c"]
item4 = [0xff0000, 0xff0000, 0xff0000]
item5 = []
item6 = []
item7 = []
item8 = []
item9 = [5] *  5
item10 = [True] *  5
item11 = ["c"] *  5
item12 = [0xff0000] *  5
item13 = 0
item14 = True
item15 = "a"
item16 = 0xff0000

def run():
    h.setAutonomousLife('OFF')
    global item, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14, item15, item16
    item13 = item.index(5)
    item13 = (len(item)-1)-item[::-1].index(5)
    item13 = item2.index(True)
    item13 = (len(item2)-1)-item2[::-1].index(True)
    item13 = item3.index("a")
    item13 = (len(item3)-1)-item3[::-1].index("a")
    item13 = item4.index(0xff0000)
    item13 = (len(item4)-1)-item4[::-1].index(0xff0000)
    item13 = len(item)
    item14 = not item
    item14 = not item2
    item14 = not item3
    item14 = not item4

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()

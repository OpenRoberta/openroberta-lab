#!/usr/bin/python

import math
import time
from roberta import Hal
from roberta import BlocklyMethods
h = Hal()


item = BlocklyMethods.createListWith(0, 0, 0)
item2 = BlocklyMethods.createListWith(True, True, True)
item3 = BlocklyMethods.createListWith("a", "b", "c")
item4 = BlocklyMethods.createListWith(0xff0000, 0xff0000, 0xff0000)
item5 = BlocklyMethods.createListWith()
item6 = BlocklyMethods.createListWith()
item7 = BlocklyMethods.createListWith()
item8 = BlocklyMethods.createListWith()
item9 = BlocklyMethods.createListWithItem(5, 5)
item10 = BlocklyMethods.createListWithItem(True, 5)
item11 = BlocklyMethods.createListWithItem("c", 5)
item12 = BlocklyMethods.createListWithItem(0xff0000, 5)
item13 = 0
item14 = True
item15 = "a"
item16 = 0xff0000

def run():
    h.setAutonomousLife('OFF')
    global item, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13, item14, item15, item16
    item13 = BlocklyMethods.findFirst( item, 5)
    item13 = BlocklyMethods.findLast( item, 5)
    item13 = BlocklyMethods.findFirst( item2, True)
    item13 = BlocklyMethods.findLast( item2, True)
    item13 = BlocklyMethods.findFirst( item3, "a")
    item13 = BlocklyMethods.findLast( item3, "a")
    item13 = BlocklyMethods.findFirst( item4, 0xff0000)
    item13 = BlocklyMethods.findLast( item4, 0xff0000)
    item13 = BlocklyMethods.length( item)
    item14 = BlocklyMethods.isEmpty( item)
    item14 = BlocklyMethods.isEmpty( item2)
    item14 = BlocklyMethods.isEmpty( item3)
    item14 = BlocklyMethods.isEmpty( item4)

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
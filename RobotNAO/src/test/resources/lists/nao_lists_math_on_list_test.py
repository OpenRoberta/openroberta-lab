#!/usr/bin/python

import math
import time
from roberta import Hal
from roberta import BlocklyMethods
h = Hal()


item = BlocklyMethods.createListWith(0, 0, 0)
item2 = 0

def run():
    h.setAutonomousLife('OFF')
    global item, item2
    item2 = BlocklyMethods.sumOnList(item)
    item2 = BlocklyMethods.minOnList(item)
    item2 = BlocklyMethods.maxOnList(item)
    item2 = BlocklyMethods.averageOnList(item)
    item2 = BlocklyMethods.medianOnList(item)
    item2 = BlocklyMethods.standardDeviatioin(item)
    item2 = BlocklyMethods.randOnList(item)

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
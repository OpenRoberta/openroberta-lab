#!/usr/bin/python

import math
import time
from roberta import Hal
from roberta import BlocklyMethods
h = Hal()


item = BlocklyMethods.createListWith(0, 0, 0)
item2 = BlocklyMethods.createListWith()
item3 = BlocklyMethods.createListWith()
item4 = BlocklyMethods.createListWith()
item5 = BlocklyMethods.createListWith()

def run():
    h.setAutonomousLife('OFF')
    global item, item2, item3, item4, item5
    item = BlocklyMethods.listsGetSubList( item, 'from_start', 0, 'from_start', 0)
    item = BlocklyMethods.listsGetSubList( item, 'from_start', 0, 'from_end', 0)
    item = BlocklyMethods.listsGetSubList( item, 'from_start', 0, 'last')
    item = BlocklyMethods.listsGetSubList( item, 'from_end', 0, 'from_start', 0)
    item = BlocklyMethods.listsGetSubList( item, 'from_end', 0, 'from_end', 0)
    item = BlocklyMethods.listsGetSubList( item, 'from_end', 0, 'last')
    item = BlocklyMethods.listsGetSubList( item, 'first', 'from_start', 0)
    item = BlocklyMethods.listsGetSubList( item, 'first', 'from_end', 0)
    item = BlocklyMethods.listsGetSubList( item, 'first', 'last')

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
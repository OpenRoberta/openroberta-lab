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
    item2 = BlocklyMethods.listsGetIndex(item, 'get', 'from_start', 0)
    item2 = BlocklyMethods.listsGetIndex(item, 'get', 'from_end', 0)
    item2 = BlocklyMethods.listsGetIndex(item, 'get', 'first')
    item2 = BlocklyMethods.listsGetIndex(item, 'get', 'last')
    item2 = BlocklyMethods.listsGetIndex(item, 'get', 'random')
    item2 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'from_start', 0)
    item2 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'from_end', 0)
    item2 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'first')
    item2 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'last')
    item2 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item, 'remove', 'random')
    BlocklyMethods.listsSetIndex(item, 'set', 0, 'from_start', 0)
    BlocklyMethods.listsSetIndex(item, 'set', 0, 'from_end', 0)
    BlocklyMethods.listsSetIndex(item, 'set', 0, 'first')
    BlocklyMethods.listsSetIndex(item, 'set', 0, 'last')
    BlocklyMethods.listsSetIndex(item, 'set', 0, 'random')
    BlocklyMethods.listsSetIndex(item, 'insert', 0, 'from_start', 0)
    BlocklyMethods.listsSetIndex(item, 'insert', 0, 'from_end', 0)
    BlocklyMethods.listsSetIndex(item, 'insert', 0, 'first')
    BlocklyMethods.listsSetIndex(item, 'insert', 0, 'last')
    BlocklyMethods.listsSetIndex(item, 'insert', 0, 'random')

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
#!/usr/bin/python

from __future__ import absolute_import
from roberta.ev3 import Hal
from roberta.BlocklyMethods import BlocklyMethods
from ev3dev import ev3 as ev3dev
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

_brickConfiguration = {
    'wheel-diameter': 5.6,
    'track-width': 18.0,
    'actors': {
    },
    'sensors': {
    },
}
hal = Hal(_brickConfiguration)

item = BlocklyMethods.createListWith(0, 0, 0)
item2 = BlocklyMethods.createListWith(True, True, True)
item3 = BlocklyMethods.createListWith("1", "2", "3")
item4 = BlocklyMethods.createListWith('white', 'white', 'white')
item5 = BlocklyMethods.createListWith(None, None, None)
item6 = 0
item7 = True
item8 = "123"
item9 = 'white'
item10 = None
def run():
    global item, item2, item8, item7, item9, item4, item3, item10, item6, item5
    item6 = BlocklyMethods.listsGetIndex(item, 'get', 'from_start', 0)
    item6 = BlocklyMethods.listsGetIndex(item, 'get', 'from_end', 0)
    item6 = BlocklyMethods.listsGetIndex(item, 'get', 'first')
    item6 = BlocklyMethods.listsGetIndex(item, 'get', 'last')
    item6 = BlocklyMethods.listsGetIndex(item, 'get', 'random')
    item6 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'from_start', 0)
    item6 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'from_end', 0)
    item6 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'first')
    item6 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'last')
    item6 = BlocklyMethods.listsGetIndex(item, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item, 'remove', 'random')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get', 'from_start', 0)
    item7 = BlocklyMethods.listsGetIndex(item2, 'get', 'from_end', 0)
    item7 = BlocklyMethods.listsGetIndex(item2, 'get', 'first')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get', 'last')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get', 'random')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get_remove', 'from_start', 0)
    item7 = BlocklyMethods.listsGetIndex(item2, 'get_remove', 'from_end', 0)
    item7 = BlocklyMethods.listsGetIndex(item2, 'get_remove', 'first')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get_remove', 'last')
    item7 = BlocklyMethods.listsGetIndex(item2, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item2, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item2, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item2, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item2, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item2, 'remove', 'random')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get', 'from_start', 0)
    item8 = BlocklyMethods.listsGetIndex(item3, 'get', 'from_end', 0)
    item8 = BlocklyMethods.listsGetIndex(item3, 'get', 'first')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get', 'last')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get', 'random')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get_remove', 'from_start', 0)
    item8 = BlocklyMethods.listsGetIndex(item3, 'get_remove', 'from_end', 0)
    item8 = BlocklyMethods.listsGetIndex(item3, 'get_remove', 'first')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get_remove', 'last')
    item8 = BlocklyMethods.listsGetIndex(item3, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item3, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item3, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item3, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item3, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item3, 'remove', 'random')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get', 'from_start', 0)
    item9 = BlocklyMethods.listsGetIndex(item4, 'get', 'from_end', 0)
    item9 = BlocklyMethods.listsGetIndex(item4, 'get', 'first')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get', 'last')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get', 'random')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get_remove', 'from_start', 0)
    item9 = BlocklyMethods.listsGetIndex(item4, 'get_remove', 'from_end', 0)
    item9 = BlocklyMethods.listsGetIndex(item4, 'get_remove', 'first')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get_remove', 'last')
    item9 = BlocklyMethods.listsGetIndex(item4, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item4, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item4, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item4, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item4, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item4, 'remove', 'random')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get', 'from_start', 0)
    item10 = BlocklyMethods.listsGetIndex(item5, 'get', 'from_end', 0)
    item10 = BlocklyMethods.listsGetIndex(item5, 'get', 'first')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get', 'last')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get', 'random')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get_remove', 'from_start', 0)
    item10 = BlocklyMethods.listsGetIndex(item5, 'get_remove', 'from_end', 0)
    item10 = BlocklyMethods.listsGetIndex(item5, 'get_remove', 'first')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get_remove', 'last')
    item10 = BlocklyMethods.listsGetIndex(item5, 'get_remove', 'random')
    BlocklyMethods.listsGetIndex(item5, 'remove', 'from_start', 0)
    BlocklyMethods.listsGetIndex(item5, 'remove', 'from_end', 0)
    BlocklyMethods.listsGetIndex(item5, 'remove', 'first')
    BlocklyMethods.listsGetIndex(item5, 'remove', 'last')
    BlocklyMethods.listsGetIndex(item5, 'remove', 'random')

def main():
    try:
        run()
    except Exception as e:
        hal.drawText('Fehler im EV3', 0, 0)
        hal.drawText(e.__class__.__name__, 0, 1)
        hal.drawText(str(e), 0, 2)
        hal.drawText('Press any key', 0, 4)
        while not hal.isKeyPressed('any'): hal.waitFor(500)
        raise

if __name__ == "__main__":
    main()
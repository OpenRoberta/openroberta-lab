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
    BlocklyMethods.listsSetIndex(item2, 'set', True, 'from_start', 0)
    BlocklyMethods.listsSetIndex(item2, 'set', True, 'from_end', 0)
    BlocklyMethods.listsSetIndex(item2, 'set', True, 'first')
    BlocklyMethods.listsSetIndex(item2, 'set', True, 'last')
    BlocklyMethods.listsSetIndex(item2, 'set', True, 'random')
    BlocklyMethods.listsSetIndex(item2, 'insert', True, 'from_start', 0)
    BlocklyMethods.listsSetIndex(item2, 'insert', True, 'from_end', 0)
    BlocklyMethods.listsSetIndex(item2, 'insert', True, 'first')
    BlocklyMethods.listsSetIndex(item2, 'insert', True, 'last')
    BlocklyMethods.listsSetIndex(item2, 'insert', True, 'random')
    BlocklyMethods.listsSetIndex(item3, 'set', "123", 'from_start', 0)
    BlocklyMethods.listsSetIndex(item3, 'set', "123", 'from_end', 0)
    BlocklyMethods.listsSetIndex(item3, 'set', "123", 'first')
    BlocklyMethods.listsSetIndex(item3, 'set', "123", 'last')
    BlocklyMethods.listsSetIndex(item3, 'set', "123", 'random')
    BlocklyMethods.listsSetIndex(item3, 'insert', "123", 'from_start', 0)
    BlocklyMethods.listsSetIndex(item3, 'insert', "123", 'from_end', 0)
    BlocklyMethods.listsSetIndex(item3, 'insert', "123", 'first')
    BlocklyMethods.listsSetIndex(item3, 'insert', "123", 'last')
    BlocklyMethods.listsSetIndex(item3, 'insert', "123", 'random')
    BlocklyMethods.listsSetIndex(item4, 'set', 'black', 'from_start', 0)
    BlocklyMethods.listsSetIndex(item4, 'set', 'black', 'from_end', 0)
    BlocklyMethods.listsSetIndex(item4, 'set', 'black', 'first')
    BlocklyMethods.listsSetIndex(item4, 'set', 'black', 'last')
    BlocklyMethods.listsSetIndex(item4, 'set', 'black', 'random')
    BlocklyMethods.listsSetIndex(item4, 'insert', 'black', 'from_start', 0)
    BlocklyMethods.listsSetIndex(item4, 'insert', 'black', 'from_end', 0)
    BlocklyMethods.listsSetIndex(item4, 'insert', 'black', 'first')
    BlocklyMethods.listsSetIndex(item4, 'insert', 'black', 'last')
    BlocklyMethods.listsSetIndex(item4, 'insert', 'black', 'random')
    BlocklyMethods.listsSetIndex(item5, 'set', hal.waitForConnection(), 'from_start', 0)
    BlocklyMethods.listsSetIndex(item5, 'set', hal.waitForConnection(), 'from_end', 0)
    BlocklyMethods.listsSetIndex(item5, 'set', hal.waitForConnection(), 'first')
    BlocklyMethods.listsSetIndex(item5, 'set', hal.waitForConnection(), 'last')
    BlocklyMethods.listsSetIndex(item5, 'set', hal.waitForConnection(), 'random')
    BlocklyMethods.listsSetIndex(item5, 'insert', hal.waitForConnection(), 'from_start', 0)
    BlocklyMethods.listsSetIndex(item5, 'insert', hal.waitForConnection(), 'from_end', 0)
    BlocklyMethods.listsSetIndex(item5, 'insert', hal.waitForConnection(), 'first')
    BlocklyMethods.listsSetIndex(item5, 'insert', hal.waitForConnection(), 'last')
    BlocklyMethods.listsSetIndex(item5, 'insert', hal.waitForConnection(), 'random')

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
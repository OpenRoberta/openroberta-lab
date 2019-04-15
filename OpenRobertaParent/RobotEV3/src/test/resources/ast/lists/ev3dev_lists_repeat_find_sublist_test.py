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
item3 = BlocklyMethods.createListWith("", "", "")
item4 = BlocklyMethods.createListWith('white', 'white', 'white')
item5 = BlocklyMethods.createListWith(None, None, None)
item6 = 0
item7 = True
def run():
    global item, item2, item7, item4, item3, item6, item5
    item = BlocklyMethods.createListWithItem(0, 5)
    item2 = BlocklyMethods.createListWithItem(True, 5)
    item3 = BlocklyMethods.createListWithItem("", 5)
    item4 = BlocklyMethods.createListWithItem('white', 5)
    item5 = BlocklyMethods.createListWithItem(hal.waitForConnection(), 5)
    item = BlocklyMethods.listsGetSubList( item, 'from_start', 0, 'from_start', 0)
    item2 = BlocklyMethods.listsGetSubList( item2, 'from_start', 0, 'from_start', 0)
    item3 = BlocklyMethods.listsGetSubList( item3, 'from_start', 0, 'from_start', 0)
    item4 = BlocklyMethods.listsGetSubList( item4, 'from_start', 0, 'from_start', 0)
    item5 = BlocklyMethods.listsGetSubList( item5, 'from_start', 0, 'from_start', 0)
    item6 = BlocklyMethods.findFirst( item, 0)
    item6 = BlocklyMethods.findFirst( item2, True)
    item6 = BlocklyMethods.findFirst( item3, "")
    item6 = BlocklyMethods.findFirst( item4, 'white')
    item6 = BlocklyMethods.findFirst( item5, hal.waitForConnection())
    item6 = BlocklyMethods.findLast( item, 0)
    item6 = BlocklyMethods.findLast( item2, True)
    item6 = BlocklyMethods.findLast( item3, "")
    item6 = BlocklyMethods.findLast( item4, 'white')
    item6 = BlocklyMethods.findLast( item5, hal.waitForConnection())

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
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
    item6 = BlocklyMethods.length( item)
    item6 = BlocklyMethods.length( item2)
    item6 = BlocklyMethods.length( item3)
    item6 = BlocklyMethods.length( item4)
    item6 = BlocklyMethods.length( item5)
    item7 = BlocklyMethods.isEmpty( item)
    item7 = BlocklyMethods.isEmpty( item2)
    item7 = BlocklyMethods.isEmpty( item3)
    item7 = BlocklyMethods.isEmpty( item4)
    item7 = BlocklyMethods.isEmpty( item5)
    item6 = BlocklyMethods.sumOnList(item)
    item6 = BlocklyMethods.minOnList(item)
    item6 = BlocklyMethods.maxOnList(item)
    item6 = BlocklyMethods.averageOnList(item)
    item6 = BlocklyMethods.medianOnList(item)
    item6 = BlocklyMethods.standardDeviatioin(item)
    item6 = BlocklyMethods.randOnList(item)

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
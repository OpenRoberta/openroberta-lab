#!/usr/bin/python

import math
import time
from roberta import Hal
from roberta import BlocklyMethods
h = Hal()



def run():
    h.applyPosture("Stand")
    h.applyPosture("LyingBack")

def main():
    try:
        run()
    except Exception as e:
        h.say("Error!" + str(e))
    finally:
        h.myBroker.shutdown()

if __name__ == "__main__":
    main()
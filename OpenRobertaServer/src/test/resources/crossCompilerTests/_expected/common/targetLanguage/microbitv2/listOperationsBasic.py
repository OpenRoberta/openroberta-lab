import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___initialEmptyNumbers = []
___initialEmptyBoolean = []
___initialEmptyStrings = []
___number = 3
___bool = True
___string = "c"
___item = 0

def run():
    global timer1, ___initialEmptyNumbers, ___initialEmptyBoolean, ___initialEmptyStrings, ___number, ___bool, ___string, ___item
    # Basis List Operations START
    if not ___initialEmptyNumbers:
        ___initialEmptyNumbers = [1, 2]
        ___item = len( ___initialEmptyNumbers)
        ___item = ___initialEmptyNumbers.index(1)
        ___item = ___initialEmptyNumbers.index(5)
        ___initialEmptyNumbers[0] = 2
    if not ___initialEmptyBoolean:
        ___initialEmptyBoolean = [True, False]
        ___item = len( ___initialEmptyBoolean)
        ___item = ___initialEmptyBoolean.index(___bool)
        ___initialEmptyBoolean.insert(-1, True)
    if not ___initialEmptyStrings:
        ___initialEmptyStrings = ["a", "b"]
        ___item = len( ___initialEmptyStrings)
        ___item = ___initialEmptyStrings.index("a")
        ___initialEmptyStrings[-1 -2] = "c"
        ___initialEmptyStrings.insert(-1 -1, "d")
    # Basis List Operations END

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
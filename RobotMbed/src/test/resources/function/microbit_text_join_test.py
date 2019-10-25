import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___item = "a"
def run():
    global timer1, ___item
    ___item = "".join(str(arg) for arg in ["sadf", "sdf"])

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()

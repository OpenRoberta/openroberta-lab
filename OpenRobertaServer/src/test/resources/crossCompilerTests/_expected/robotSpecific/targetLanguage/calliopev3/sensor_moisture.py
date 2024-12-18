import calliopemini
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()

def run():
    global timer1
    print("Moisture sensor test")
    while True:
        print(((calliopemini.pin_A1_RX.read_analog() / 950) * 100))
        calliopemini.sleep(150)

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
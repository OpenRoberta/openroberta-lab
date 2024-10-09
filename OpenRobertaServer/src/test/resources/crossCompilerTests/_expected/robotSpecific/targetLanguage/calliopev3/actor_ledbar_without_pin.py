import calliopemini
import random
import math


def write16(di, dcki, data):
    state = dcki.read_digital()
    for i in range(15,-1,-1):
      di.write_digital((data >> i) & 1)
      state = not state
      dcki.write_digital(state)

def set_led(led_no, brightness):
    di = calliopemini.pin_A1_RX
    dcki = calliopemini.pin_A1_TX
    di.write_digital(0)
    dcki.write_digital(0)
    if led_no < 0 or led_no >= 10:
        raise ValueError("Led_no must be in the range [0, 9]")

    val = 1 << led_no
    write16(di, dcki, 0)
    for i in range(10):
        write16(di, dcki, brightness if (val >> i) & 1 else 0)

    write16(di, dcki,0)
    write16(di, dcki,0)
    di.write_digital(0)
    calliopemini.sleep(1)
    for i in range(4):
        di.write_digital(1)
        di.write_digital(0)
    calliopemini.sleep(1)

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = calliopemini.running_time()


___n = 0

def ____lights():
    global timer1, ___n
    set_led(___n,___n)


def run():
    global timer1, ___n
    ____lights()

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
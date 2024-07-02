import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___ergebnis = 0
def run():
    global timer1, ___ergebnis
    # Math basics START
    ___ergebnis = ___ergebnis + 1
    ___ergebnis = ___ergebnis - 3
    ___ergebnis = ___ergebnis * -1
    ___ergebnis = ___ergebnis / float(2)
    if not 1 == ___ergebnis:
        print("Assertion failed: ", "pos-1", 1, "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( float(0.1) - float(0.1) )
    ___ergebnis = ___ergebnis + ( 5 * 2 )
    ___ergebnis = ___ergebnis + ( 3 / float(2) )
    if not float(12.5) == ___ergebnis:
        print("Assertion failed: ", "pos-2", float(12.5), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis * ( 1 + 2 )
    ___ergebnis = ___ergebnis * ( 1 - 2 )
    ___ergebnis = ___ergebnis * ( 1 / float(2) )
    if not float(-18.75) == ___ergebnis:
        print("Assertion failed: ", "pos-3", float(-18.75), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis / float(( float(0.1) + float(0.1) ))
    ___ergebnis = ___ergebnis / float(( float(0.1) - float(0.2) ))
    ___ergebnis = ___ergebnis / float(( float(0.1) * float(0.1) ))
    if not float(1e-7) > math.fabs(93750 - ___ergebnis):
        print("Assertion failed: ", "pos-4", float(1e-7), "GT", math.fabs(93750 - ___ergebnis))
    ___ergebnis = ___ergebnis - ( float(1.535345) + float(0.999999999999999) )
    ___ergebnis = ___ergebnis - ( float(0.1111111111111111) + float(0.9999999999999999) )
    ___ergebnis = ___ergebnis - ( 435 + float(0.14543) )
    if not float(1e-7) > math.fabs(float(93311.208113889) - ___ergebnis):
        print("Assertion failed: ", "pos-5", float(1e-7), "GT", math.fabs(float(93311.208113889) - ___ergebnis))
    # Math basics END

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
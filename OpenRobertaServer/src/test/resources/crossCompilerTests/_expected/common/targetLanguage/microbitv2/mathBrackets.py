import microbit
import random
import math

class BreakOutOfALoop(Exception): pass
class ContinueLoop(Exception): pass

timer1 = microbit.running_time()

___ergebnis = 0

def run():
    global timer1, ___ergebnis
    # Grundrechenarten Basics  --START--
    ___ergebnis = 2 + ( ( 3 * 4 ) / float(5) )
    if not float(4.4) == ___ergebnis:
        print("Assertion failed: ", "POS-1", float(4.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( 3 + 4 ) * 5 ) )
    if not float(74.4) == ___ergebnis:
        print("Assertion failed: ", "POS-2", float(74.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( 3 * ( 4 + 5 ) ) )
    if not float(128.4) == ___ergebnis:
        print("Assertion failed: ", "POS-3", float(128.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 * 4 ) - 5 ) * 6 ) )
    if not float(172.4) == ___ergebnis:
        print("Assertion failed: ", "POS-4", float(172.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) * 5 ) * 6 ) )
    if not float(592.4) == ___ergebnis:
        print("Assertion failed: ", "POS-5", float(592.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( 6 * ( ( 3 + 4 ) * 5 ) ) )
    if not float(1012.4) == ___ergebnis:
        print("Assertion failed: ", "POS-7", float(1012.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 + ( ( ( 3 + 4 ) / float(( 5 - 6 )) ) - ( ( 7 * 8 ) + ( 9 + 10 ) ) ) )
    if not float(932.4) == ___ergebnis:
        print("Assertion failed: ", "POS-13", float(932.4), "EQ", ___ergebnis)
    ___ergebnis = ___ergebnis + ( 2 * ( ( ( 3 + 4 ) + ( 5 * 6 ) ) * ( ( 7 * 8 ) + ( 9 - 10 ) ) ) )
    print("SUCCESS" if ( ___ergebnis == float(5002.4) ) else "FAIL")
    # Grundrechenarten Basics  --END--

def main():
    try:
        run()
    except Exception as e:
        raise

if __name__ == "__main__":
    main()
package de.fhg.iais.roberta.codegen.lejos;

public class BlocklyMethods {
    public static boolean isEven(double number) {
        return (number % 2 == 0);
    }

    public static boolean isOdd(double number) {
        return (number % 2 == 1);
    }

    public static boolean isPrime(double number) {
        for ( int i = 2; i <= Math.sqrt(number); i++ ) {
            double remainder = number % i;
            if ( remainder == 0 ) {
                return false;
            }
        }
        return true;
    }

}

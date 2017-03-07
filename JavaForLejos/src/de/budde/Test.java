package de.budde;

public class Test {

    public static void main(String[] args) {
        int i = 0;
        if ( true ) {
            while ( true ) {
                if ( i > 5 ) {
                    break;
                }
                System.out.println(i);
                i++;
            }
        }
        System.out.println("outside");
    }

}

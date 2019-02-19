package de.fhg.iais.roberta.util.nonsequential;

import org.junit.Ignore;
import org.junit.Test;

public class NonsequentialTest {

    private static void waitMillisec(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch ( InterruptedException e ) {
            throw new RuntimeException("sleep interrupted - rethrowing", e);
        }
    }

    private boolean run = true;

    /**
     * this is an example about problems with non-sequential execution. Generated code could be like this. The story: write a program to<br>
     * <br>
     * - assemble some information (maybe colors the robot detects with its color sensor during moving)<br>
     * - terminate, if a button is pressed<br>
     * <br>
     * <b>First have a look at the program: what do you expect? what happens?</b>
     */
    @Ignore // please ALWAYS ignore this test after you run it, REALLY.
    @Test
    public void testStopWhenButtonPressed() throws InterruptedException {
        Thread waitForButton = new Thread() {
            @Override
            public void run() {
                System.out.println("the button has not been pressed yet");
                waitMillisec(1000);
                System.out.println("now the button is pressed. This will set run to false");
                run = false;
                System.out.println("button thread has done its job and terminates");
            }
        };
        Thread working = new Thread() {
            @Override
            public void run() {
                System.out.println("working thread started");
                while ( run ) {
                    // do something: look at the color sensor, if a new color is detected, put it into a list of already visited colors
                }
                System.out.println("working thread terminated");
            }
        };
        // start the two threads. Wait until both terminate
        waitForButton.start();
        working.start();
        waitForButton.join();
        working.join();
    }

}

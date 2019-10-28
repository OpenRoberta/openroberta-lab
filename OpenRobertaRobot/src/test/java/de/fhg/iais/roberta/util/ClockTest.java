package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

public class ClockTest {

    @Ignore
    @Test
    public void test() throws InterruptedException {
        Clock c1 = Clock.start();
        Thread.sleep(1200);
        assertEquals(1, c1.elapsedSec());
        assertEquals("1 sec", c1.elapsedSecFormatted());
        Thread.sleep(1000);
        assertEquals(2, c1.elapsedSec());
        assertEquals("2 secs", c1.elapsedSecFormatted());

        c1.elapsedMsec();
        c1.elapsedMsecFormatted();
    }

}

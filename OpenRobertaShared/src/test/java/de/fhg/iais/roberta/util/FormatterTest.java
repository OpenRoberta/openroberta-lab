package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;

import org.junit.Test;

public class FormatterTest {

    private static final double EPS_FOR_BEING_EQUAL = 0.00001;

    @Test
    public void testd2s() {
        assertEquals("0.0", Formatter.d2s(0.0));
        assertEquals("3.1", Formatter.d2s(3.1));
        assertEquals("3.0", Formatter.d2s(3.0));
        assertEquals("3.1", Formatter.d2s(3.14));
        assertEquals("-3.1", Formatter.d2s(-3.14));
        assertEquals("100.1", Formatter.d2s(100.123));
        assertEquals("100.2", Formatter.d2s(100.189));
    }

    @Test
    public void tests2d() throws ParseException {
        assertEquals(0.0, Formatter.s2d("0.0"), EPS_FOR_BEING_EQUAL);
        assertEquals(3.1, Formatter.s2d("3.1"), EPS_FOR_BEING_EQUAL);
        assertEquals(3.0, Formatter.s2d("3.0"), EPS_FOR_BEING_EQUAL);
        assertEquals(3.1, Formatter.s2d("3.14"), EPS_FOR_BEING_EQUAL);
        assertEquals(-3.1, Formatter.s2d("-3.14"), EPS_FOR_BEING_EQUAL);
        assertEquals(100.1, Formatter.s2d("100.123"), EPS_FOR_BEING_EQUAL);
        assertEquals(100.2, Formatter.s2d("100.189"), EPS_FOR_BEING_EQUAL);
    }

}

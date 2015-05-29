package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testJavaIdentifier() {
        assertTrue(Util.isValidJavaIdentifier("P"));
        assertTrue(Util.isValidJavaIdentifier("Pid"));
        assertTrue(Util.isValidJavaIdentifier("€Pid_diP€"));
        assertFalse(Util.isValidJavaIdentifier(null));
        assertFalse(Util.isValidJavaIdentifier(""));
        assertFalse(Util.isValidJavaIdentifier("1qay"));
        assertFalse(Util.isValidJavaIdentifier(" Pid"));
        assertFalse(Util.isValidJavaIdentifier("class"));
        assertFalse(Util.isValidJavaIdentifier("for"));
    }

    @Test
    public void testFormat() {
        assertEquals("1.0", Util.formatDouble1digit(1.0));
        assertEquals("1.3", Util.formatDouble1digit(1.3));
        assertEquals("1.3", Util.formatDouble1digit(1.28));
        assertEquals("1.3", Util.formatDouble1digit(1.32));
        assertEquals("15.6", Util.formatDouble1digit(15.600005));
        assertEquals("-5.5", Util.formatDouble1digit(-5.50006));
        assertEquals("15567.6", Util.formatDouble1digit(15567.6005));
    }
}

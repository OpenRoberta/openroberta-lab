package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testJavaIdentifier() {
        assertTrue(Util1.isValidJavaIdentifier("P"));
        assertTrue(Util1.isValidJavaIdentifier("Pid"));
        assertTrue(Util1.isValidJavaIdentifier("€Pid_diP€"));
        assertTrue(Util1.isValidJavaIdentifier("_ö_ä_ü_ß_"));
        assertTrue(Util1.isValidJavaIdentifier("ö_ä_ü_ß"));
        assertTrue(Util1.isValidJavaIdentifier("__üäö$€"));
        assertFalse(Util1.isValidJavaIdentifier(null));
        assertFalse(Util1.isValidJavaIdentifier(""));
        assertFalse(Util1.isValidJavaIdentifier("1qay"));
        assertFalse(Util1.isValidJavaIdentifier(" Pid"));
        assertFalse(Util1.isValidJavaIdentifier("class"));
        assertFalse(Util1.isValidJavaIdentifier("for"));
    }

    @Test
    public void testFormat() {
        assertEquals("1.0", Util1.formatDouble1digit(1.0));
        assertEquals("1.3", Util1.formatDouble1digit(1.3));
        assertEquals("1.3", Util1.formatDouble1digit(1.28));
        assertEquals("1.3", Util1.formatDouble1digit(1.32));
        assertEquals("15.6", Util1.formatDouble1digit(15.600005));
        assertEquals("-5.5", Util1.formatDouble1digit(-5.50006));
        assertEquals("15567.6", Util1.formatDouble1digit(15567.6005));
    }
}

package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class UtilTest {
    private static RobertaProperties robertaProperties;

    @BeforeClass
    public static void setup() {
        robertaProperties = new RobertaProperties(Util1.loadProperties(null));
    }

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

    @Test
    public void testMissingProperty() {
        boolean browserVisibility = Boolean.parseBoolean(robertaProperties.getStringProperty("does.not.exist"));
        assertEquals(false, browserVisibility);
    }

    @Test
    public void testPow() {
        assertEquals(0, Util1.pow2(-1));
        assertEquals(1, Util1.pow2(0));
        assertEquals(2, Util1.pow2(1));
        assertEquals(4, Util1.pow2(2));
        assertEquals(16, Util1.pow2(4));
        assertEquals(1024, Util1.pow2(10));
    }

    @Test
    public void testCopyFrom() {
        String[] from =
            {
                "a",
                "b",
                "c"
            };
        from = Arrays.copyOfRange(from, 1, from.length);
        assertEquals(2, from.length);
        assertEquals("b", from[0]);
        assertEquals("c", from[1]);
    }
}

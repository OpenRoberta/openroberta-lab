package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.fhg.iais.roberta.util.dbc.DbcException;

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

    @Test
    public void testGetRobotNumberFromProperty() {
        Util1.setRobertaProperties(Util1.loadProperties(null));
        assertEquals(1, Util1.getRobotNumberFromProperty("ev3"));
        assertEquals(2, Util1.getRobotNumberFromProperty("nxt"));
        assertEquals(3, Util1.getRobotNumberFromProperty("ardu"));
    }

    @Test(expected = DbcException.class)
    public void testGetRobotNumberFromPropertyWrong() {
        Util1.setRobertaProperties(Util1.loadProperties(null));
        Util1.getRobotNumberFromProperty("ev31");
    }

    @Test
    public void testMissingProperty() {
        Util1.setRobertaProperties(Util1.loadProperties(null));
        boolean browserVisibility = Boolean.parseBoolean(Util1.getStringProperty("browser.visibility"));
        assertEquals(false, browserVisibility);
    }
}

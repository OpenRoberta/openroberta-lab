package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

public class UtilTest {
    @Test
    public void testJavaIdentifier() {
        assertTrue(Util.isValidJavaIdentifier("P"));
        assertTrue(Util.isValidJavaIdentifier("Pid"));
        assertTrue(Util.isValidJavaIdentifier("€Pid_diP€"));
        assertTrue(Util.isValidJavaIdentifier("_ö_ä_ü_ß_"));
        assertTrue(Util.isValidJavaIdentifier("ö_ä_ü_ß"));
        assertTrue(Util.isValidJavaIdentifier("__üäö$€"));
        assertFalse(Util.isValidJavaIdentifier(null));
        assertFalse(Util.isValidJavaIdentifier(""));
        assertFalse(Util.isValidJavaIdentifier("1qay"));
        assertFalse(Util.isValidJavaIdentifier(" Pid"));
        assertFalse(Util.isValidJavaIdentifier("class"));
        assertFalse(Util.isValidJavaIdentifier("for"));
    }

    @Test
    public void testCreateMapOk() {
        Map<String, String> map = Util.createMap("1", "2", "3", "4");
        assertTrue(map.size() == 2);
        assertNull(map.get("2"));
        assertNull(map.get("5"));
        assertEquals("2", map.get("1"));
        assertEquals("4", map.get("3"));
    }

    @Test(expected = RuntimeException.class)
    public void testCreateMapException() {
        Util.createMap("1", "2", "3");
    }
}

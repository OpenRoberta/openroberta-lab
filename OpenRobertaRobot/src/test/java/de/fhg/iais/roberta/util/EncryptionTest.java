package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class EncryptionTest {

    @Test
    public void testTheDifference1() throws Exception {
        String h1 = Encryption.createHash("123456");
        String h2 = Encryption.createHash("123456");
        assertFalse(h1.equals(h2));
    }

    @Test
    public void testTheDifference2() throws Exception {
        String h1 = Encryption.createHash("Pid+Craesy+Minscha+Mucki+Garzi");
        String h2 = Encryption.createHash("Pid+Craesy+Minscha+Mucki+Garzi");
        assertFalse(h1.equals(h2));
    }

    @Test
    public void testTheHash() throws Exception {
        String h1 = Encryption.createHash("Pid+Craesy+Minscha+Mucki+Garzi");
        String h2 = Encryption.createHash("Pid+Craesy+Minscha+Mucki+Garzi");
        assertTrue(Encryption.isPasswordCorrect(h1, "Pid+Craesy+Minscha+Mucki+Garzi"));
        assertTrue(Encryption.isPasswordCorrect(h2, "Pid+Craesy+Minscha+Mucki+Garzi"));
    }
}

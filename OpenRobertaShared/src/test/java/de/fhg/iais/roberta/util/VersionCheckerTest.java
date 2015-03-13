package de.fhg.iais.roberta.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VersionCheckerTest {

    @Test
    public void test1() {
        VersionChecker c = new VersionChecker("1.1.0", "1.1.0");
        assertTrue(c.versionValid("1.1.0"));
        assertTrue(!c.versionValid("1.1.1"));
    }

    @Test
    public void test2() {
        VersionChecker c = new VersionChecker("1.1.0-SNAPSHOT", "1.1.0-SNAPSHOT");
        assertTrue(c.versionValid("1.1.0-SNAPSHOT"));
        assertTrue(c.versionValid("1.1.0"));
        assertTrue(!c.versionValid("1.1.1-SNAPSHOT"));
    }

    @Test
    public void test3() {
        VersionChecker c = new VersionChecker("1.1.0-RELEASE", "1.1.0-RELEASE");
        assertTrue(c.versionValid("1.1.0-RELEASE"));
        assertTrue(c.versionValid("1.1.0-SNAPSHOT")); // !!!
        assertTrue(c.versionValid("1.1.0"));
        assertTrue(!c.versionValid("1.1.1-RELEASE"));
    }

}

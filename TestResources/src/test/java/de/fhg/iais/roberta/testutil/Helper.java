package de.fhg.iais.roberta.testutil;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper {

    /**
     * Asserts if two XML string are identical by ignoring white space.
     *
     * @param arg1 first XML string
     * @param arg2 second XML string
     * @throws Exception
     */
    public static void assertXML(String arg1, String arg2) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        final Diff diff = XMLUnit.compareXML(arg1, arg2);
        Assert.assertTrue(diff.identical());
    }

}

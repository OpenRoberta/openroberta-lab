package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.mbed.HelperCalliopeForXmlTest;

public class CalliopeListsTest {
    HelperCalliopeForXmlTest calliopeHelper = new HelperCalliopeForXmlTest();

    @Test
    public void calliopeGetSetTest() throws Exception {
        this.calliopeHelper.compareExistingAndGeneratedSource("lists/calliope_lists_get_set_test.cpp", "/lists/calliope_lists_get_set_test.xml");
    }

    @Test
    public void calliopeFindFirstLastTest() throws Exception {
        this.calliopeHelper
            .compareExistingAndGeneratedSource("lists/calliope_lists_find_last_first_test.cpp", "/lists/calliope_lists_find_last_first_test.xml");
    }
}

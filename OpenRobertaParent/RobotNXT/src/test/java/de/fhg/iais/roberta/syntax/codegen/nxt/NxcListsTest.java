package de.fhg.iais.roberta.syntax.codegen.nxt;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.nxt.HelperNxtForXmlTest;

public class NxcListsTest {
    private final HelperNxtForXmlTest nxtHelper = new HelperNxtForXmlTest();
    Configuration standardConfiguration = HelperNxtForXmlTest.makeConfiguration();

    @Test
    public void nxtListsGetSetFindTest() throws Exception {
        this.nxtHelper
            .compareExistingAndGeneratedNxcSource(
                "ast/actions/nxc_arrays_find_get_set_test.nxc",
                "/ast/actions/nxc_arrays_find_get_set_test.xml",
                this.standardConfiguration);
    }

}

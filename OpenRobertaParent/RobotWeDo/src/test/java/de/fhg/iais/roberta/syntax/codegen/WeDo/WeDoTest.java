package de.fhg.iais.roberta.syntax.codegen.WeDo;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.wedo.HelperWedoForXml;

public class WeDoTest {
    HelperWedoForXml helperWeDo = new HelperWedoForXml();

    @Test
    public void weDoEverythingTest() throws Exception {
        this.helperWeDo.compareExistingAndGeneratedVmSource("wedo_everything_test.json", "/wedo_everything_test.xml");
    }
}

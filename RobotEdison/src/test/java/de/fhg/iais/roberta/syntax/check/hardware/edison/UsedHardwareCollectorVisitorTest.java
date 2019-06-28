package de.fhg.iais.roberta.syntax.check.hardware.edison;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.edison.HelperEdisonForXmlTest;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;
import org.junit.Test;

import java.util.ArrayList;

public class UsedHardwareCollectorVisitorTest {
    private final HelperEdisonForXmlTest h = new HelperEdisonForXmlTest();

    @Test
    public void TestAllHelperMethods() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/collector/all_helper_methods.xml");
        Configuration edisonConfig = this.h.makeConfig();

        EdisonUsedHardwareCollectorVisitor checker = new EdisonUsedHardwareCollectorVisitor(phrases, edisonConfig);
    }
}
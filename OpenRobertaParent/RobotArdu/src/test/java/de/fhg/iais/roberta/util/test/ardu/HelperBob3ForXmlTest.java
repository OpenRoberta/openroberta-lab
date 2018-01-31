package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.arduino.Bob3Configuration;
import de.fhg.iais.roberta.factory.arduino.bob3.Factory;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class HelperBob3ForXmlTest extends AbstractHelperForXmlTest {

    public HelperBob3ForXmlTest() {
        super(new Factory(new RobertaProperties(Util1.loadProperties(null))), new Bob3Configuration.Builder().build());
    }
}

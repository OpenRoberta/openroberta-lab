package de.fhg.iais.roberta.util.test.ev3;

import de.fhg.iais.roberta.factory.Ev3DevFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class HelperEv3DevForXmlTest extends HelperEv3ForXmlTest {
    public HelperEv3DevForXmlTest() {
        super(new Ev3DevFactory(new PluginProperties("ev3dev", "", "", Util1.loadProperties("classpath:/ev3dev.properties"))));
    }
}

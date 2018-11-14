package de.fhg.iais.roberta.util.test.ardu;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;

public class HelperMBotForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperMBotForXmlTest() {
        super(new MbotFactory(new PluginProperties("mbot", "", "", Util1.loadProperties("classpath:mbot.properties"))), new Configuration.Builder().build());
    }
}

package de.fhg.iais.roberta.testsetup;

import java.util.Properties;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class TestBase {
    @BeforeClass
    public static void loadPropertiesForTests() {
        Properties properties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(properties);
    }
}

package de.fhg.iais.roberta.ast;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.AbstractUsedHardwareCollectorVisitor;

public class ConfigurationComponentTest {

    private class TestConfiguration extends AbstractUsedHardwareCollectorVisitor {
        //TODO create fake for this class
        public TestConfiguration(UsedHardwareBean.Builder builder, ArrayList<ArrayList<Phrase<Void>>> phrases) {
            super(null, null);
        }

        @Override
        public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
            return null;
        }

    }

    @Test(expected = DbcException.class)
    public void connectionTest() throws Exception {
        TestConfiguration testConfiguration = new TestConfiguration(null, null);
        ConfigurationComponent cc = new ConfigurationComponent("type", false, "port", "userPort", Collections.emptyMap());
        Assert.isTrue(cc.getProperty().getBlockType().equals("type"));
        cc.accept(testConfiguration);
    }
}

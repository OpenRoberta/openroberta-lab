package de.fhg.iais.roberta.syntax.check.hardware.nao;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.test.nao.HelperNaoForXmlTest;

public class UsedHardwareCollectorVisitorTest {
    HelperNaoForXmlTest h = new HelperNaoForXmlTest();

    private Configuration makeConfiguration() {
        return new NAOConfiguration.Builder().build();
    }

    @Test
    public void testLearnFace_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/learnface.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());

        Assert.assertEquals("[UsedSensor [null, NAOFACE, null]]", checkVisitor.getUsedSensors().toString());

    }

    @Test
    public void testForgetFace_returnsListWithOneUsedSensor() throws Exception {
        ArrayList<ArrayList<Phrase<Void>>> phrases = this.h.generateASTs("/hardwarecheck/forgetface.xml");

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(phrases, makeConfiguration());

        Assert.assertEquals("[UsedSensor [null, NAOFACE, null]]", checkVisitor.getUsedSensors().toString());

    }

}

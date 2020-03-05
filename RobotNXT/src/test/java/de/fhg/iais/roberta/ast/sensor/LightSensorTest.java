package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorTest extends NxtAstTest {

    @Test
    public void sensorSetLight() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=162, y=238], LightSensor [3, LIGHT, NO_SLOT]], "
                + "[Location [x=163, y=263], LightSensor [4, AMBIENTLIGHT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setLight.xml");
    }

    @Test
    public void getMode() throws Exception {
        final List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) forest.get(0).get(1);

        Assert.assertEquals(SC.LIGHT, cs.getMode());

    }

    @Test
    public void getPort() throws Exception {
        final List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setLight.xml");

        final LightSensor<Void> cs = (LightSensor<Void>) forest.get(0).get(1);
        final LightSensor<Void> cs1 = (LightSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("3", cs.getPort());
        Assert.assertEquals("4", cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setLight.xml");
    }
}

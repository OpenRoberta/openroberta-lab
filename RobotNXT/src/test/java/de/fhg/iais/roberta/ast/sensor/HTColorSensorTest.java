package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HTColorSensorTest extends NxtAstTest {

    @Test
    public void sensorSetColor() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=-15, y=107], HTColorSensor [3, COLOUR, NO_SLOT]], "
                + "[Location [x=-13, y=147], HTColorSensor [1, LIGHT, NO_SLOT]], "
                + "[Location [x=-11, y=224], HTColorSensor [4, AMBIENTLIGHT, NO_SLOT]],"
                + "[Location [x=-9, y=270], HTColorSensor [2, RGB, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setHTColor.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setHTColor.xml");

        HTColorSensor<Void> cs0 = (HTColorSensor<Void>) forest.get(0).get(1);
        HTColorSensor<Void> cs1 = (HTColorSensor<Void>) forest.get(1).get(1);
        HTColorSensor<Void> cs2 = (HTColorSensor<Void>) forest.get(2).get(1);
        HTColorSensor<Void> cs3 = (HTColorSensor<Void>) forest.get(3).get(1);

        Assert.assertEquals(SC.COLOUR, cs0.getMode());
        Assert.assertEquals(SC.LIGHT, cs1.getMode());
        Assert.assertEquals(SC.AMBIENTLIGHT, cs2.getMode());
        Assert.assertEquals(SC.RGB, cs3.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setHTColor.xml");

        HTColorSensor<Void> cs0 = (HTColorSensor<Void>) forest.get(0).get(1);
        HTColorSensor<Void> cs1 = (HTColorSensor<Void>) forest.get(1).get(1);
        HTColorSensor<Void> cs2 = (HTColorSensor<Void>) forest.get(2).get(1);
        HTColorSensor<Void> cs3 = (HTColorSensor<Void>) forest.get(3).get(1);

        Assert.assertEquals("3", cs0.getPort());
        Assert.assertEquals("1", cs1.getPort());
        Assert.assertEquals("4", cs2.getPort());
        Assert.assertEquals("2", cs3.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setHTColor.xml");
    }
}

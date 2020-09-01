package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ColorSensorTest extends AstTest {

    @Test
    public void sensorSetColor() throws Exception {
        final String a =
            "BlockAST [project=[[Location [x=-15, y=107], ColorSensor [3, COLOUR, NO_SLOT]], "
                + "[Location [x=-13, y=147], ColorSensor [1, LIGHT, NO_SLOT]], "
                + "[Location [x=-11, y=187], ColorSensor [2, RGB, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setColor.xml");
    }

    @Test
    public void getMode() throws Exception {
        final List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) forest.get(0).get(1);

        Assert.assertEquals(SC.COLOUR, cs.getMode());
    }

    @Test
    public void getPort() throws Exception {
        final List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setColor.xml");

        final ColorSensor<Void> cs = (ColorSensor<Void>) forest.get(0).get(1);
        final ColorSensor<Void> cs1 = (ColorSensor<Void>) forest.get(1).get(1);
        final ColorSensor<Void> cs2 = (ColorSensor<Void>) forest.get(2).get(1);

        Assert.assertEquals("3", cs.getPort());
        Assert.assertEquals("1", cs1.getPort());
        Assert.assertEquals("2", cs2.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setColor.xml");
    }
}

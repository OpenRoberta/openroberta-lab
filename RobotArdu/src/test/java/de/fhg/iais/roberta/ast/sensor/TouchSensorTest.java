package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TouchSensorTest extends AstTest {

    @Test
    public void sensorTouch() throws Exception {
        String a = "BlockAST [project=[[Location [x=-86, y=1], TouchSensor [1, DEFAULT, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_Touch.xml");
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_Touch.xml");

        TouchSensor<Void> cs = (TouchSensor<Void>) forest.get(0).get(1);

        Assert.assertEquals("1", cs.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_Touch.xml");
    }
}

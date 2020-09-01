package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BrickSensorTest extends AstTest {

    @Test
    public void main() throws Exception {
        String a = "BlockAST [project=[[Location [x=-19, y=1], KeysSensor [ENTER, PRESSED, NO_SLOT]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_brick1.xml");
    }

    @Test
    public void getKey() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_brick1.xml");
        KeysSensor<Void> bs = (KeysSensor<Void>) forest.get(0).get(1);
        Assert.assertEquals("ENTER", bs.getPort());
    }

    @Test
    public void sensorBrick() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [KeysSensor [ENTER, PRESSED, NO_SLOT]]\n\n"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_brick.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_brick1.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_brick.xml");
    }
}

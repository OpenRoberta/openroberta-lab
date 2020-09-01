package de.fhg.iais.roberta.ast.sensor;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class InfraredSensorTest extends AstTest {

    @Test
    public void sensorSetInfrared() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-23, y=157], InfraredSensor [4, OBSTACLE, NO_SLOT]], "
                + "[Location [x=-19, y=199], InfraredSensor [3, PRESENCE, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_setInfrared.xml");
    }

    @Test
    public void getMode() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) forest.get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals(SC.OBSTACLE, cs.getMode());
        Assert.assertEquals(SC.PRESENCE, cs1.getMode());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/sensors/sensor_setInfrared.xml");

        InfraredSensor<Void> cs = (InfraredSensor<Void>) forest.get(0).get(1);
        InfraredSensor<Void> cs1 = (InfraredSensor<Void>) forest.get(1).get(1);

        Assert.assertEquals("4", cs.getPort());
        Assert.assertEquals("3", cs1.getPort());
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_setInfrared.xml");
    }
}

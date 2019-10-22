package de.fhg.iais.roberta.ast.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VoltageSensorTest extends AstTest {

    @Test
    public void voltageSensorJaxbToAstTransformation() throws Exception {
        String a = "BlockAST [project=[[Location [x=38, y=238], VoltageSensor [NO_PORT, VALUE, NO_SLOT]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_Voltage.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_Voltage.xml");
    }

}

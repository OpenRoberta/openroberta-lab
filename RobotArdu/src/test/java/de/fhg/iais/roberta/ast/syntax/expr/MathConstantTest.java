package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class MathConstantTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {

        String a = "M_PIM_EM_GOLDEN_RATIOM_SQRT2M_SQRT1_2M_INFINITY";
        //"Float.POSITIVE_INFINITY";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_constant.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Ignore
    public void Test1() throws Exception {

        final String a = "RotateMotor(B,M_PI,360.0*((1.0+sqrt(5.0))/2.0)))";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_constant1.xml", new ArduinoCxxGeneratorWorker());
    }

}

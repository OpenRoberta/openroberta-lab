package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class MathNumberPropertyTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "(fmod(0,2)==0)(fmod(0,2)!=0)_isPrime(0)(0==floor(0))(0>0)(0<0)(fmod(0,0)==0)";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_number_property.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void Test1() throws Exception {
        final String a = "___item=(fmod(0,2)==0);";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/math/math_number_property1.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

}

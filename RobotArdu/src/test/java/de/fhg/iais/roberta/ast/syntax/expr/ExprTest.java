package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class ExprTest extends ArduinoAstTest {

    @Test
    public void test1() throws Exception {
        final String a = "\n8 + (-3 + 5)88 - ( 8 + (-3 + 5))(88 - ( 8 + (-3 + 5)))  - ( 88 - ( 8 + (-3 + 5) ))2 * ( 2 - 2 )\n" + "2 - (2 * 2)";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/expr/expr1.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void test2() throws Exception {
        final String a =
            "\n2 * ( 2 - 2 )\n" + "2 - (2 * 2)(88 - ( 8 + (-3 + 5))) - (2 * 2)((88 - ( 8 + (-3 + 5))) - (2 * 2) )/((float) ((88 -( 8 + (-3 + 5)))-(2 * 2) ))";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/expr/expr2.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }
}
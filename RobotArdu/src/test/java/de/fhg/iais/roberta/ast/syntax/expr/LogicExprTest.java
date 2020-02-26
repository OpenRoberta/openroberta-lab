package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class LogicExprTest extends ArduinoAstTest {

    @Test
    public void test1() throws Exception {
        final String a =
            "\nfalse == true\n"
                + "true != false\n"
                + "false == false\n"
                + "(5 <= 7)==(8 > 9)\n"
                + " (5 != 7)>=(8 == 9) \n"
                + "(5 + 7)>= ((8 + 4)/((float) (9 + 3))) \n"
                + "((5 + 7)==(5 + 7 ))>= ((8 + 4 ) /((float) ( 9 + 3))) \n"
                + "((5 + 7)==(5 + 7) )>= (((5 + 7)== (5 + 7)) && ((5 + 7) <= (5 + 7)))\n"
                + "!((5 + 7)==(5 + 7) )== true";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/expr/logic_expr.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void logicNegate() throws Exception {
        final String a = "\n!((0!= 0)&&false)";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/expr/logic_negate.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void logicNull() throws Exception {
        final String a = "\nNULL";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/expr/logic_null.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }
}
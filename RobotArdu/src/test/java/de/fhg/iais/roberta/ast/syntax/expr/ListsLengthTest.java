package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class ListsLengthTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "((int){0.1,0.0,0}.size())";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_length.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }
}

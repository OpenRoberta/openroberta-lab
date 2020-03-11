package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class ListsIsEmptyTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {
        final String a = "{0,0,0}.empty()";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_is_empty.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }
}

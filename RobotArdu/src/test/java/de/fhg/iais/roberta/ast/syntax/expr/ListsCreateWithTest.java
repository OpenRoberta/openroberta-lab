package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.arduino.ArduinoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.codegen.ArduinoCxxGeneratorWorker;
import de.fhg.iais.roberta.worker.collect.ArduinoUsedHardwareCollectorWorker;

public class ListsCreateWithTest extends ArduinoAstTest {

    @Test
    public void Test() throws Exception {
        String a = "{1.0,3.1,2}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_create_with.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void Test1() throws Exception {
        String a = "{\"a\",\"b\",\"c\"}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_create_with1.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void Test2() throws Exception {
        String a = "{true,true,false}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_create_with2.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void Test3() throws Exception {
        String a = "{true,true,true}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_create_with3.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }

    @Test
    public void Test4() throws Exception {
        String a = "{RGB(0x58,0x58,0x58),RGB(0xB3,0x00,0x06),RGB(0x53,0x21,0x15)}";

        UnitTestHelper.checkWorkers(testFactory, a, "/syntax/lists/lists_create_with4.xml", new ArduinoUsedHardwareCollectorWorker(), new ArduinoCxxGeneratorWorker());
    }
}

package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoListsTest extends ArduinoAstTest {

    static ConfigurationAst configuration =
        ArduTestHelper.mkConfigurationAst(new ConfigurationComponent("LED", true, "LED", "L", Util.createMap("INPUT", "13")));

    @Test
    public void listsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/lists/arduino_lists_test.ino", "/ast/lists/arduino_lists_test.xml", configuration);
    }

    @Test
    public void listsOccuranceTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_occurance_lists_test.ino",
                "/ast/lists/arduino_occurance_lists_test.xml",
                configuration);
    }

    @Test
    public void listsRepeatTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_list_repeat_test.ino",
                "/ast/lists/arduino_list_repeat_test.xml",
                configuration);
    }

    @Test
    public void listsSublistTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_sublist_test.ino",
                "/ast/lists/arduino_sublist_test.xml",
                configuration);
    }

    @Test
    public void listsGetSetTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_lists_get_set_test.ino",
                "/ast/lists/arduino_lists_get_set_test.xml",
                configuration);
    }

    @Test
    public void showSource_failsWithError_whenListOpsAreUsedWithListCreate() {
        Assert
            .assertThrows(
                "ValidatorWorker failed with 6 errors",
                AssertionError.class,
                () -> UnitTestHelper.checkWorkflow(testFactory, "showsource", "/syntax/lists/lists_ops_used_with_create.xml"));
    }

}

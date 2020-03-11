package de.fhg.iais.roberta.syntax.codegen.arduino.arduino;

import java.util.Arrays;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoListsTest extends ArduinoAstTest {

    @Test
    public void listsTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/ast/lists/arduino_lists_test.ino", "/ast/lists/arduino_lists_test.xml", builder.build());
    }

    @Test
    public void listsOccuranceTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_occurance_lists_test.ino",
                "/ast/lists/arduino_occurance_lists_test.xml",
                builder.build());
    }

    @Test
    public void listsRepeatTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_list_repeat_test.ino",
                "/ast/lists/arduino_list_repeat_test.xml",
                builder.build());
    }

    @Test
    public void listsSublistTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_sublist_test.ino",
                "/ast/lists/arduino_sublist_test.xml",
                builder.build());
    }

    @Test
    public void listsGetSetTest() throws Exception {
        Map<String, String> ledPins = Util.createMap("INPUT", "13");
        ConfigurationComponent led = new ConfigurationComponent("LED", true, "LED", "L", ledPins);
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setTrackWidth(17f).setWheelDiameter(5.6f).addComponents(Arrays.asList(led));
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/arduino_lists_get_set_test.ino",
                "/ast/lists/arduino_lists_get_set_test.xml",
                builder.build());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void showSource_failsWithError_whenListOpsAreUsedWithListCreate() {
        this.exceptionRule.expect(AssertionError.class);
        this.exceptionRule.expectMessage("ValidatorWorker failed with 6 errors");
        UnitTestHelper.checkWorkflow(testFactory, "showsource", "/syntax/lists/lists_ops_used_with_create.xml");
    }

}

package de.fhg.iais.roberta.visitor;

import java.util.Properties;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.IWorker;

public class SpikeConfigurationAndCodegenTest {
    private static RobotFactory spikeFactory;
    private static RobotFactory spikePybricksFactory;

    private static final String START_BLOCK =
        block(
            "robControls_start",
            "start1",
            "<mutation declare=\"false\"></mutation>",
            field("DEBUG", ""));

    private static final String EMPTY_PROGRAM = programBlockSet();

    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        Properties spikeProps = Util.loadPropertiesRecursively("classpath:/spike.properties");
        spikeFactory = new RobotFactory(new PluginProperties("spike", "", "", spikeProps));

        Properties pybricksProps = Util.loadPropertiesRecursively("classpath:/spikePybricks.properties");
        spikePybricksFactory = new RobotFactory(new PluginProperties("spikePybricks", "", "", pybricksProps));
    }

    private static String blockSet(String... blocks) {
        StringBuilder sb = new StringBuilder(
            "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"spike\" xmlversion=\"3.1\" description=\"\" tags=\"\">");
        for ( String block : blocks ) {
            sb.append("<instance x=\"0\" y=\"0\">").append(block).append("</instance>");
        }
        sb.append("</block_set>");
        return sb.toString();
    }

    private static String programBlockSet(String... blocks) {
        return blockSet(START_BLOCK + String.join("", blocks));
    }

    private static String field(String name, String value) {
        return "<field name=\"" + name + "\">" + value + "</field>";
    }

    private static String value(String name, String innerBlock) {
        return "<value name=\"" + name + "\">" + innerBlock + "</value>";
    }

    private static String numberValue(String name, String num) {
        return value(name, block("math_number", "num_" + name, field("NUM", num)));
    }

    private static String block(String type, String id, String... content) {
        return block(type, id, false, content);
    }

    private static String block(String type, String id, boolean disabled, String... content) {
        return "<block type=\"" + type + "\" id=\"" + id + "\" intask=\"true\""
            + (disabled ? " disabled=\"true\"" : "")
            + ">"
            + String.join("", content)
            + "</block>";
    }

    private static String configBlock(String type, String name, String port) {
        return configBlock(type, name, port, false);
    }

    private static String configBlock(String type, String name, String port, boolean disabled) {
        return block(type, name, disabled, field("NAME", name), field("PORT", port));
    }

    private static String diffDriveBlock(String motorL, String motorR) {
        return diffDriveBlock(motorL, motorR, false);
    }

    private static String diffDriveBlock(String motorL, String motorR, boolean disabled) {
        return block(
            "robConf_differentialdrive",
            "dd1",
            disabled,
            field("NAME", "_D"),
            field("BRICK_WHEEL_DIAMETER", "5.6"),
            field("BRICK_TRACK_WIDTH", "11.5"),
            field("MOTOR_L", motorL),
            field("MOTOR_R", motorR));
    }

    private static String debugSensorSampleBlock(String sensorType, String mode, String sensorPort, String slot) {
        String sampleBlock = block(
            "robSensors_" + sensorType + "_getSample",
            "sample_" + sensorPort,
            "<mutation mode=\"" + mode + "\"></mutation>",
            field("MODE", mode),
            field("SENSORPORT", sensorPort),
            field("SLOT", slot));
        return block("robActions_debug", "dbg_" + sensorPort, value("OUT", sampleBlock));
    }

    private static void executeWorkflow(String workflow, RobotFactory factory, Project project) {
        project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        for ( IWorker worker : factory.getWorkerPipe(workflow) ) {
            if ( project.hasSucceeded() || worker.mustRunEvenIfPreviousWorkerFailed() ) {
                worker.execute(project);
            }
        }
    }

    @Test
    public void testDefaultConfigurationValid() {
        Project project = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikeFactory, EMPTY_PROGRAM).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertEquals(0, project.getErrorCounter());
        Assert.assertEquals(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS, project.getResult());
    }

    @Test
    public void testOverlappingSensorPortsDetected() {
        String configWithOverlappingSensors = blockSet(
            configBlock("robConf_colour", "col1", "C"),
            configBlock("robConf_ultrasonic", "ult1", "C"));

        Project project = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, EMPTY_PROGRAM, configWithOverlappingSensors).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertTrue(project.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project.getResult());
        JSONObject errorInfo = project.getConfAnnotationList().get("ult1") != null
            ? project.getConfAnnotationList().get("ult1")
            : project.getConfAnnotationList().get("col1");
        Assert.assertNotNull(errorInfo);
        Assert.assertTrue(errorInfo.toString().contains("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
    }

    @Test
    public void testOverlappingMotorPortsDetected() {
        String configWithOverlappingMotors = blockSet(
            configBlock("robConf_motor", "mot1", "A"),
            configBlock("robConf_motor", "mot2", "A"));

        Project project = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, EMPTY_PROGRAM, configWithOverlappingMotors).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertTrue(project.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project.getResult());
        JSONObject errorInfo = project.getConfAnnotationList().get("mot2") != null
            ? project.getConfAnnotationList().get("mot2")
            : project.getConfAnnotationList().get("mot1");
        Assert.assertNotNull(errorInfo);
        Assert.assertTrue(errorInfo.toString().contains("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
    }

    @Test
    public void testOverlappingDiffDrivePortsDetected() {
        String configWithSameDiffDrivePorts = blockSet(
            diffDriveBlock("A", "A"),
            configBlock("robConf_motor", "MA", "A"));

        Project project = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, EMPTY_PROGRAM, configWithSameDiffDrivePorts).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertTrue(project.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project.getResult());
        JSONObject errorInfo = project.getConfAnnotationList().get("dd1");
        Assert.assertNotNull(errorInfo);
        Assert.assertTrue(errorInfo.toString().contains("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
    }

    @Test
    public void testSensorOverlappingWithDiffDrivePortDetected() {
        String configWithSensorOnDiffDrivePort = blockSet(
            diffDriveBlock("A", "B"),
            configBlock("robConf_motor", "motA", "A"),
            configBlock("robConf_motor", "motB", "B"),
            configBlock("robConf_ultrasonic", "ultConflict", "A"));

        Project project = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, EMPTY_PROGRAM, configWithSensorOnDiffDrivePort).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertTrue(project.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project.getResult());
        JSONObject errorInfo = project.getConfAnnotationList().get("ultConflict") != null
            ? project.getConfAnnotationList().get("ultConflict")
            : project.getConfAnnotationList().get("motA");
        Assert.assertNotNull(errorInfo);
        Assert.assertTrue(errorInfo.toString().contains("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
    }

    @Test
    public void testDisconnectedComponentsIgnoredInValidationButPreservedInXml() {
        String configWithDisconnectedBlocks = blockSet(
            configBlock("robConf_motor", "MA", "A"),
            configBlock("robConf_motor", "motDisconnected", "", true),
            configBlock("robConf_ultrasonic", "ultDisconnected", "", true));

        Project project = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, EMPTY_PROGRAM, configWithDisconnectedBlocks).build();
        executeWorkflow("showsource", spikeFactory, project);
        Assert.assertEquals(0, project.getErrorCounter());
        Assert.assertEquals(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS, project.getResult());
        // Active configuration components only include connected motor MA
        Assert.assertEquals(1, project.getConfigurationAst().getConfigurationComponents().size());
        Assert.assertNotNull(project.getConfigurationAst().optConfigurationComponent("MA"));
        Assert.assertNull(project.getConfigurationAst().optConfigurationComponent("motDisconnected"));
        // Regenerated XML preserves all 3 blocks including disconnected ones
        Assert.assertEquals(3, project.getConfigurationAst().getAllConfigurationComponents().size());
        Assert.assertTrue(project.getConfigurationAsBlocklyXML().contains("id=\"motDisconnected\""));
        Assert.assertTrue(project.getConfigurationAsBlocklyXML().contains("id=\"ultDisconnected\""));
    }

    @Test
    public void testMultipleSensorsAndAuxMotorsInCodegenAndSim() {
        String customConfig = blockSet(
            diffDriveBlock("A", "B"),
            configBlock("robConf_motor", "MA", "A"),
            configBlock("robConf_motor", "MB", "B"),
            configBlock("robConf_motor", "MC", "C"),
            configBlock("robConf_ultrasonic", "U", "D"),
            configBlock("robConf_ultrasonic", "U2", "E"),
            configBlock("robConf_touch", "B", "F"));

        String programUsingAuxMotorAndMultiUltrasonic = programBlockSet(
            block(
                "actions_motor_on_for",
                "motC1",
                field("ACTORPORT", "MC"),
                field("UNIT", "DEGREES"),
                numberValue("POWER", "50"),
                numberValue("VALUE", "180")),
            debugSensorSampleBlock("ultrasonic", "DISTANCE", "U", ""),
            debugSensorSampleBlock("ultrasonic", "DISTANCE", "U2", ""),
            debugSensorSampleBlock("encoder", "DEGREE", "MC", ""));

        // Test Spike Lego Python codegen
        Project spikeProject = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programUsingAuxMotorAndMultiUltrasonic, customConfig).build();
        executeWorkflow("showsource", spikeFactory, spikeProject);
        Assert.assertEquals(0, spikeProject.getErrorCounter());
        String spikeCode = spikeProject.getSourceCodeBuilder().toString();
        Assert.assertTrue(spikeCode.contains("motorC = spike.Motor('C')"));
        Assert.assertTrue(spikeCode.contains("ultrasonic_sensor_U = spike.DistanceSensor('D')"));
        Assert.assertTrue(spikeCode.contains("ultrasonic_sensor_U2 = spike.DistanceSensor('E')"));
        Assert.assertTrue(spikeCode.contains("motorC.get_degrees_counted()"));

        // Test Spike Pybricks Python codegen
        Project pybricksProject = UnitTestHelper.setupWithConfigAndProgramXML(spikePybricksFactory, programUsingAuxMotorAndMultiUltrasonic, customConfig).build();
        executeWorkflow("showsource", spikePybricksFactory, pybricksProject);
        Assert.assertEquals(0, pybricksProject.getErrorCounter());
        String pybricksCode = pybricksProject.getSourceCodeBuilder().toString();
        Assert.assertTrue(pybricksCode.contains("motorC = Motor(Port.C)"));
        Assert.assertTrue(pybricksCode.contains("ultrasonic_sensor_U = UltrasonicSensor(Port.D)"));
        Assert.assertTrue(pybricksCode.contains("ultrasonic_sensor_U2 = UltrasonicSensor(Port.E)"));
        Assert.assertTrue(pybricksCode.contains("motorC.angle()"));

        // Test Simulation codegen
        Project simProject = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programUsingAuxMotorAndMultiUltrasonic, customConfig).build();
        executeWorkflow("getsimulationcode", spikeFactory, simProject);
        Assert.assertEquals(0, simProject.getErrorCounter());
        String simJson = simProject.getSourceCodeBuilder().toString();
        Assert.assertNotNull(simJson);
        Assert.assertTrue(simJson.contains("\"U\""));
        Assert.assertTrue(simJson.contains("\"U2\""));
        Assert.assertTrue(simJson.contains("\"c\""));
    }

    @Test
    public void testBuiltinHubSensorsAndActorsWithoutConfigBlocks() {
        String programUsingBuiltins = programBlockSet(
            block(
                "actions_display_text",
                "disp1",
                field("ACTORPORT", "_B"),
                field("TYPE", "TEXT"),
                value("TEXT", block("text", "txt1", field("TEXT", "Hi")))),
            block("actions_display_clear", "disp2", field("ACTORPORT", "_B")),
            block(
                "actions_play_note",
                "play1",
                field("ACTORPORT", "_S"),
                field("DURATION", "500"),
                field("FREQUENCE", "261.626")),
            block(
                "actions_rgbLed_hidden_on",
                "led1",
                "<hide name=\"ACTORPORT\" value=\"_R\"></hide>",
                value("COLOUR", block("robColour_picker", "col1", field("COLOUR", "#FA010C")))),
            debugSensorSampleBlock("gyro", "ANGLE", "_K", "Z"),
            debugSensorSampleBlock("key", "PRESSED", "left", ""),
            debugSensorSampleBlock("key", "PRESSED", "right", ""));

        Project spikeProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikeFactory, programUsingBuiltins).build();
        executeWorkflow("showsource", spikeFactory, spikeProject);
        Assert.assertEquals(0, spikeProject.getErrorCounter());
        String spikeCode = spikeProject.getSourceCodeBuilder().toString();
        Assert.assertTrue(spikeCode.contains("hub.light_matrix.write(\"Hi\")"));
        Assert.assertTrue(spikeCode.contains("hub.speaker.beep("));
        Assert.assertTrue(spikeCode.contains("hub.left_button.is_pressed()"));
        Assert.assertTrue(spikeCode.contains("hub.right_button.is_pressed()"));

        Project pybricksProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikePybricksFactory, programUsingBuiltins).build();
        executeWorkflow("showsource", spikePybricksFactory, pybricksProject);
        Assert.assertEquals(0, pybricksProject.getErrorCounter());
        String pybricksCode = pybricksProject.getSourceCodeBuilder().toString();
        Assert.assertTrue(pybricksCode.contains("display_text(str(\"Hi\"))"));
        Assert.assertTrue(pybricksCode.contains("hub.speaker.beep("));
        Assert.assertTrue(pybricksCode.contains("(Button.LEFT in hub.buttons.pressed())"));
        Assert.assertTrue(pybricksCode.contains("(Button.RIGHT in hub.buttons.pressed())"));

        Project simProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikeFactory, programUsingBuiltins).build();
        executeWorkflow("getsimulationcode", spikeFactory, simProject);
        Assert.assertEquals(0, simProject.getErrorCounter());
    }

    @Test
    public void testDrivingWithMissingMotorOnDiffDrivePortFails() {
        String programDriving = programBlockSet(
            block(
                "actions_motorDiff_on_for",
                "drive1",
                field("DIRECTION", "FOREWARD"),
                numberValue("POWER", "50"),
                numberValue("DISTANCE", "20")));

        // Case 1: Differential drive has MOTOR_L = C (no motor on port C) and is marked disabled="true" by the frontend
        String configWithInactiveDiffDrive = blockSet(
            diffDriveBlock("C", "B", true),
            configBlock("robConf_motor", "MA", "A"),
            configBlock("robConf_motor", "MB", "B"));

        Project project1 = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programDriving, configWithInactiveDiffDrive).build();
        executeWorkflow("showsource", spikeFactory, project1);
        Assert.assertTrue(project1.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project1.getResult());
        Assert.assertTrue(project1.getProgramAsBlocklyXML().contains("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));

        // Case 2: Differential drive has MOTOR_R = D (where an ultrasonic sensor is connected, not a motor)
        String configWithSensorOnDiffDrivePort = blockSet(
            diffDriveBlock("A", "D", true),
            configBlock("robConf_motor", "MA", "A"),
            configBlock("robConf_ultrasonic", "U", "D"));

        Project project2 = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programDriving, configWithSensorOnDiffDrivePort).build();
        executeWorkflow("getsimulationcode", spikeFactory, project2);
        Assert.assertTrue(project2.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project2.getResult());
        Assert.assertTrue(project2.getProgramAsBlocklyXML().contains("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
    }
}

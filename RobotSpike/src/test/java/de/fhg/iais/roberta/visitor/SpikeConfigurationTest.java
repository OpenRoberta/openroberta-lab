package de.fhg.iais.roberta.visitor;

import java.util.Properties;

import org.json.JSONArray;
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

public class SpikeConfigurationTest {
    private static RobotFactory spikeFactory;
    private static RobotFactory spikePybricksFactory;

    private static final String EMPTY_PROGRAM = Util.readResourceContent("/visitors/spike_empty_program.xml");

    @BeforeClass
    public static void setup() {
        AstFactory.loadBlocks();
        Properties spikeProps = Util.loadPropertiesRecursively("classpath:/spike.properties");
        spikeFactory = new RobotFactory(new PluginProperties("spike", "", "", spikeProps));

        Properties pybricksProps = Util.loadPropertiesRecursively("classpath:/spikePybricks.properties");
        spikePybricksFactory = new RobotFactory(new PluginProperties("spikePybricks", "", "", pybricksProps));
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
        String configWithOverlappingSensors = Util.readResourceContent("/visitors/spike_overlapping_sensor_ports.xml");

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
        String configWithOverlappingMotors = Util.readResourceContent("/visitors/spike_overlapping_motor_ports.xml");

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
        String configWithSameDiffDrivePorts = Util.readResourceContent("/visitors/spike_overlapping_diff_drive_ports.xml");

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
        String configWithSensorOnDiffDrivePort = Util.readResourceContent("/visitors/spike_sensor_on_diff_drive_port.xml");

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
        String configWithDisconnectedBlocks = Util.readResourceContent("/visitors/spike_disconnected_components.xml");

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
    public void testMultipleSensorsAndAuxMotorsInSimulation() {
        String customConfig = Util.readResourceContent("/visitors/spike_multi_sensor_configuration.xml");
        String programUsingAuxMotorAndMultiUltrasonic = Util.readResourceContent("/visitors/spike_multi_sensor_program.xml");

        // Python code generation is covered by crossCompilerTests/robotSpecific/
        // spike/multiSensorAuxMotor.xml and spikePybricks/multiSensorAuxMotor.xml.
        // Test Simulation codegen
        Project simProject = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programUsingAuxMotorAndMultiUltrasonic, customConfig).build();
        executeWorkflow("getsimulationcode", spikeFactory, simProject);
        Assert.assertEquals(0, simProject.getErrorCounter());
        Assert.assertNotNull(simProject.getCompiledHex());

        JSONArray ops = new JSONObject(simProject.getCompiledHex()).getJSONArray("ops");
        boolean motorC = false;
        boolean ultrasonicU = false;
        boolean ultrasonicU2 = false;
        boolean encoderC = false;

        for ( int i = 0; i < ops.length(); i++ ) {
            JSONObject op = ops.getJSONObject(i);
            String opcode = op.optString("opc");
            String port = op.optString("port");
            if ( "motorOnAction".equals(opcode) && "c".equals(port) && "degree".equals(op.optString("motorDuration")) ) {
                motorC = true;
            } else if ( "GetSample".equals(opcode) ) {
                String sensor = op.optString("GetSample");
                String mode = op.optString("mode");
                if ( "ultrasonic".equals(sensor) && "distance".equals(mode) ) {
                    ultrasonicU |= "U".equals(port);
                    ultrasonicU2 |= "U2".equals(port);
                } else if ( "encoder".equals(sensor) && "degree".equals(mode) && "c".equals(port) ) {
                    encoderC = true;
                }
            }
        }

        Assert.assertTrue("Simulation must drive auxiliary motor C", motorC);
        Assert.assertTrue("Simulation must read ultrasonic sensor U", ultrasonicU);
        Assert.assertTrue("Simulation must read ultrasonic sensor U2", ultrasonicU2);
        Assert.assertTrue("Simulation must read encoder of motor C", encoderC);
    }

    @Test
    public void testBuiltinHubSensorsAndActorsWithDefaultConfig() {
        String programUsingBuiltins = Util.readResourceContent("/visitors/spike_builtin_hub.xml");

        Project spikeProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikeFactory, programUsingBuiltins).build();
        executeWorkflow("showsource", spikeFactory, spikeProject);
        Assert.assertEquals(0, spikeProject.getErrorCounter());
        Assert.assertEquals(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS, spikeProject.getResult());

        Project pybricksProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikePybricksFactory, programUsingBuiltins).build();
        executeWorkflow("showsource", spikePybricksFactory, pybricksProject);
        Assert.assertEquals(0, pybricksProject.getErrorCounter());
        Assert.assertEquals(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS, pybricksProject.getResult());

        Project simProject = UnitTestHelper.setupWithProgramXMLWithDefaultConfig(spikeFactory, programUsingBuiltins).build();
        executeWorkflow("getsimulationcode", spikeFactory, simProject);
        Assert.assertEquals(0, simProject.getErrorCounter());
    }

    @Test
    public void testDrivingWithMissingMotorOnDiffDrivePortFails() {
        String programDriving = Util.readResourceContent("/visitors/spike_diff_drive_program.xml");

        // Case 1: Differential drive has MOTOR_L = C (no motor on port C) and is marked disabled="true" by the frontend
        String configWithInactiveDiffDrive = Util.readResourceContent("/visitors/spike_diff_drive_missing_left_motor.xml");

        Project project1 = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programDriving, configWithInactiveDiffDrive).build();
        executeWorkflow("showsource", spikeFactory, project1);
        Assert.assertTrue(project1.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project1.getResult());
        Assert.assertTrue(project1.getProgramAsBlocklyXML().contains("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));

        // Case 2: Differential drive has MOTOR_R = D (where an ultrasonic sensor is connected, not a motor)
        String configWithSensorOnDiffDrivePort = Util.readResourceContent("/visitors/spike_diff_drive_missing_right_motor.xml");

        Project project2 = UnitTestHelper.setupWithConfigAndProgramXML(spikeFactory, programDriving, configWithSensorOnDiffDrivePort).build();
        executeWorkflow("getsimulationcode", spikeFactory, project2);
        Assert.assertTrue(project2.getErrorCounter() > 0);
        Assert.assertEquals(Key.PROGRAM_INVALID_STATEMETNS, project2.getResult());
        Assert.assertTrue(project2.getProgramAsBlocklyXML().contains("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
    }
}

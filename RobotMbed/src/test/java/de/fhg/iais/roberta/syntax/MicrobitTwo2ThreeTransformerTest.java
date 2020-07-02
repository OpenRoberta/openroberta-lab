package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerWorker;

public class MicrobitTwo2ThreeTransformerTest {

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"microbit\" xmlversion=\"2.0\">"
            + "    <instance x=\"138\" y=\"88\">"
            + "        <block id=\"1\" type=\"mbedBrick_microbit-Brick\" />"
            + "    </instance>"
            + "</block_set>";
    private static IRobotFactory testFactory;
    private static ConfigurationAst configuration;

    @BeforeClass
    public static void setupBefore() throws Exception {
        List<String> pluginDefines = new ArrayList<>();
        pluginDefines.add("microbit:robot.configuration.type = old-S");
        pluginDefines.add("microbit:robot.configuration.old.toplevelblock = mbedBrick_microbit-Brick");
        testFactory = Util.configureRobotPlugin("microbit", "", "", pluginDefines);
        BlockSet blockSet = JaxbHelper.xml2BlockSet(OLD_CONFIGURATION_XML);
        configuration =
            Jaxb2ConfigurationAst
                .blocks2OldConfig(
                    blockSet,
                    testFactory.getBlocklyDropdownFactory(),
                    testFactory.optTopBlockOfOldConfiguration(),
                    testFactory.optSensorPrefix());
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedCompass_WhenGivenOldCompass() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[CompassSensor[C,ANGLE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[CompassSensor[C,ANGLE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=COMPASS,isActor=true,userDefinedName=C,portName=C,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_compass.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedKey_WhenGivenOldKey() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[KeysSensor[A,PRESSED,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[KeysSensor[B,PRESSED,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[KeysSensor[A,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[KeysSensor[B,PRESSED,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=A,portName=A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=B,portName=B,componentProperties={PIN1=B}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_key.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLight_WhenGivenOldLight() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[LightSensor[L,VALUE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[LightSensor[L,LIGHT_VALUE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=L,portName=L,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_light.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinPull_WhenGivenPinPull() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "PinSetPullAction[UP,A_0_D],"
                + "PinSetPullAction[UP,A_1_D],"
                + "PinSetPullAction[UP,A_2_D],"
                + "PinSetPullAction[UP,A_3_D],"
                + "PinSetPullAction[UP,A_4_D],"
                + "PinSetPullAction[UP,A_5_D],"
                + "PinSetPullAction[UP,A_6_D],"
                + "PinSetPullAction[UP,A_7_D],"
                + "PinSetPullAction[UP,A_8_D],"
                + "PinSetPullAction[UP,A_9_D],"
                + "PinSetPullAction[UP,A_10_D],"
                + "PinSetPullAction[UP,A_11_D],"
                + "PinSetPullAction[UP,A_12_D],"
                + "PinSetPullAction[UP,A_13_D],"
                + "PinSetPullAction[UP,A_14_D],"
                + "PinSetPullAction[UP,A_15_D],"
                + "PinSetPullAction[UP,A_16_D],"
                + "PinSetPullAction[UP,A_19_D],"
                + "PinSetPullAction[UP,A_20_D],"
                + "PinSetPullAction[DOWN,A_0_D],"
                + "PinSetPullAction[DOWN,A_1_D],"
                + "PinSetPullAction[DOWN,A_2_D],"
                + "PinSetPullAction[DOWN,A_3_D],"
                + "PinSetPullAction[DOWN,A_4_D],"
                + "PinSetPullAction[DOWN,A_5_D],"
                + "PinSetPullAction[DOWN,A_6_D],"
                + "PinSetPullAction[DOWN,A_7_D],"
                + "PinSetPullAction[DOWN,A_8_D],"
                + "PinSetPullAction[DOWN,A_9_D],"
                + "PinSetPullAction[DOWN,A_10_D],"
                + "PinSetPullAction[DOWN,A_11_D],"
                + "PinSetPullAction[DOWN,A_12_D],"
                + "PinSetPullAction[DOWN,A_13_D],"
                + "PinSetPullAction[DOWN,A_14_D],"
                + "PinSetPullAction[DOWN,A_15_D],"
                + "PinSetPullAction[DOWN,A_16_D],"
                + "PinSetPullAction[DOWN,A_19_D],"
                + "PinSetPullAction[DOWN,A_20_D],"
                + "PinSetPullAction[NONE,A_0_D],"
                + "PinSetPullAction[NONE,A_1_D],"
                + "PinSetPullAction[NONE,A_2_D],"
                + "PinSetPullAction[NONE,A_3_D],"
                + "PinSetPullAction[NONE,A_4_D],"
                + "PinSetPullAction[NONE,A_5_D],"
                + "PinSetPullAction[NONE,A_6_D],"
                + "PinSetPullAction[NONE,A_7_D],"
                + "PinSetPullAction[NONE,A_8_D],"
                + "PinSetPullAction[NONE,A_9_D],"
                + "PinSetPullAction[NONE,A_10_D],"
                + "PinSetPullAction[NONE,A_11_D],"
                + "PinSetPullAction[NONE,A_12_D],"
                + "PinSetPullAction[NONE,A_13_D],"
                + "PinSetPullAction[NONE,A_14_D],"
                + "PinSetPullAction[NONE,A_15_D],"
                + "PinSetPullAction[NONE,A_16_D],"
                + "PinSetPullAction[NONE,A_19_D],"
                + "PinSetPullAction[NONE,A_20_D]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_9_D,portName=A_9_D,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_6_D,portName=A_6_D,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_5_D,portName=A_5_D,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_4_D,portName=A_4_D,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_3_D,portName=A_3_D,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_2_D,portName=A_2_D,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_1_D,portName=A_1_D,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_0_D,portName=A_0_D,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_16_D,portName=A_16_D,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_19_D,portName=A_19_D,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_12_D,portName=A_12_D,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_13_D,portName=A_13_D,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_14_D,portName=A_14_D,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_15_D,portName=A_15_D,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_20_D,portName=A_20_D,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_7_D,portName=A_7_D,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_10_D,portName=A_10_D,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_8_D,portName=A_8_D,componentProperties={PIN1=8}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_11_D,portName=A_11_D,componentProperties={PIN1=11}"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/microbit/old_pin_pull.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedTemperature_WhenGivenOldTemperature() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[TemperatureSensor[TM,VALUE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[TemperatureSensor[TM,TEMPERATURE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=TEMPERATURE,isActor=true,userDefinedName=TM,portName=TM,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_temperature.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedWriteToPin_WhenGivenOldWriteToPin() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "PinWriteValueAction[ANALOG,A_0_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A_1_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A_2_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A_3_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A_4_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A_10_A,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_0_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_1_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_2_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_3_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_4_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_5_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_6_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_7_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_8_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_9_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_10_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_11_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_12_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_13_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_14_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_15_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_16_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_19_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A_20_D,NumConst[1]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_9_D,portName=A_9_D,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_6_D,portName=A_6_D,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_5_D,portName=A_5_D,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_4_D,portName=A_4_D,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_3_D,portName=A_3_D,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_2_D,portName=A_2_D,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_4_A,portName=A_4_A,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_1_D,portName=A_1_D,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_3_A,portName=A_3_A,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_0_D,portName=A_0_D,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_2_A,portName=A_2_A,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_1_A,portName=A_1_A,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_0_A,portName=A_0_A,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_16_D,portName=A_16_D,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_19_D,portName=A_19_D,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_12_D,portName=A_12_D,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_13_D,portName=A_13_D,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_14_D,portName=A_14_D,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_15_D,portName=A_15_D,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_10_D,portName=A_10_D,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_11_D,portName=A_11_D,componentProperties={PIN1=11}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A_10_A,portName=A_10_A,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_20_D,portName=A_20_D,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_7_D,portName=A_7_D,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A_8_D,portName=A_8_D,componentProperties={PIN1=8}"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/microbit/old_write_to_pin.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAccelerometer_WhenGivenOldAccelerometer() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[AccelerometerSensor[Acc,VALUE,X]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[Acc,VALUE,Y]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[Acc,VALUE,Z]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[Acc,VALUE,STRENGTH]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,Y]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,Z]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,STRENGTH]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ACCELEROMETER,isActor=true,userDefinedName=Acc,portName=Acc,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_accelerometer.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedSounds_WhenGivenOldSounds() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "ToneAction[NumConst[300],NumConst[100]],"
                + "PlayNoteAction[duration=2000,frequency=261.626],"
                + "PlayNoteAction[duration=1000,frequency=261.626],"
                + "PlayNoteAction[duration=500,frequency=261.626],"
                + "PlayNoteAction[duration=250,frequency=261.626],"
                + "PlayNoteAction[duration=125,frequency=261.626]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=BZ,portName=BZ,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_sounds.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinsSensor_WhenGivenOldPinsSensor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_0_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_1_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_2_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_3_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_4_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_10_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_0_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_1_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_2_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_3_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_4_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_5_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_6_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_7_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_8_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_9_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_10_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_11_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_12_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_13_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_14_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_15_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_16_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_19_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_20_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_0_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_1_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_2_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_3_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_4_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_5_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_6_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_7_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_8_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_9_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_10_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_11_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_12_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_13_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_14_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_15_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_16_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_19_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_20_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_0_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_1_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_2_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_3_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_4_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_5_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_6_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_7_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_8_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_9_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_10_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_11_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_12_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_13_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_14_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_15_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_16_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_19_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_20_D,PULSELOW,EMPTY_SLOT]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_20_D,portName=S_20_D,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_14_D,portName=S_14_D,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_15_D,portName=S_15_D,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_11_D,portName=S_11_D,componentProperties={PIN1=11}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_16_D,portName=S_16_D,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_10_D,portName=S_10_D,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_12_D,portName=S_12_D,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_13_D,portName=S_13_D,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_8_D,portName=S_8_D,componentProperties={PIN1=8}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_7_D,portName=S_7_D,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_19_D,portName=S_19_D,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_9_D,portName=S_9_D,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_4_D,portName=S_4_D,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_3_D,portName=S_3_D,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_6_D,portName=S_6_D,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_5_D,portName=S_5_D,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_3_A,portName=S_3_A,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_0_D,portName=S_0_D,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_2_A,portName=S_2_A,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_2_D,portName=S_2_D,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_4_A,portName=S_4_A,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_1_D,portName=S_1_D,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_1_A,portName=S_1_A,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_0_A,portName=S_0_A,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_10_A,portName=S_10_A,componentProperties={PIN1=10}"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/microbit/old_pins_sensor.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedWaitFor_WhenGivenOldWaitFor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "WaitStmt[(repeat[WAIT,SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,UP,EMPTY_SLOT]]]])]]]]";

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_wait_for.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("robControls_wait_for", project.getProgramAst().getTree().get(0).get(2).getProperty().getBlockType());
    }
}

package de.fhg.iais.roberta.syntax;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Util;
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

    @BeforeClass
    public static void setupBefore() throws Exception {
        List<String> pluginDefines = new ArrayList<>();
        testFactory = Util.configureRobotPlugin("microbit", "", "", pluginDefines);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedCompass_WhenGivenOldCompass() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[CompassSensor[_C,ANGLE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[CompassSensor[_C,ANGLE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=COMPASS,isActor=true,userDefinedName=_C,portName=_C,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_compass.xml"), OLD_CONFIGURATION_XML).build();

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
            UnitTestHelper.setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_key.xml"), OLD_CONFIGURATION_XML).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLight_WhenGivenOldLight() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[LightSensor[_L,VALUE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[LightSensor[_L,LIGHT_VALUE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=_L,portName=_L,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_light.xml"), OLD_CONFIGURATION_XML).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinPull_WhenGivenPinPull() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "PinSetPullAction[UP,P0],"
                + "PinSetPullAction[UP,P1],"
                + "PinSetPullAction[UP,P2],"
                + "PinSetPullAction[UP,P3],"
                + "PinSetPullAction[UP,P4],"
                + "PinSetPullAction[UP,P5],"
                + "PinSetPullAction[UP,P6],"
                + "PinSetPullAction[UP,P7],"
                + "PinSetPullAction[UP,P8],"
                + "PinSetPullAction[UP,P9],"
                + "PinSetPullAction[UP,P10],"
                + "PinSetPullAction[UP,P11],"
                + "PinSetPullAction[UP,P12],"
                + "PinSetPullAction[UP,P13],"
                + "PinSetPullAction[UP,P14],"
                + "PinSetPullAction[UP,P15],"
                + "PinSetPullAction[UP,P16],"
                + "PinSetPullAction[UP,P19],"
                + "PinSetPullAction[UP,P20],"
                + "PinSetPullAction[DOWN,P0],"
                + "PinSetPullAction[DOWN,P1],"
                + "PinSetPullAction[DOWN,P2],"
                + "PinSetPullAction[DOWN,P3],"
                + "PinSetPullAction[DOWN,P4],"
                + "PinSetPullAction[DOWN,P5],"
                + "PinSetPullAction[DOWN,P6],"
                + "PinSetPullAction[DOWN,P7],"
                + "PinSetPullAction[DOWN,P8],"
                + "PinSetPullAction[DOWN,P9],"
                + "PinSetPullAction[DOWN,P10],"
                + "PinSetPullAction[DOWN,P11],"
                + "PinSetPullAction[DOWN,P12],"
                + "PinSetPullAction[DOWN,P13],"
                + "PinSetPullAction[DOWN,P14],"
                + "PinSetPullAction[DOWN,P15],"
                + "PinSetPullAction[DOWN,P16],"
                + "PinSetPullAction[DOWN,P19],"
                + "PinSetPullAction[DOWN,P20],"
                + "PinSetPullAction[NONE,P0],"
                + "PinSetPullAction[NONE,P1],"
                + "PinSetPullAction[NONE,P2],"
                + "PinSetPullAction[NONE,P3],"
                + "PinSetPullAction[NONE,P4],"
                + "PinSetPullAction[NONE,P5],"
                + "PinSetPullAction[NONE,P6],"
                + "PinSetPullAction[NONE,P7],"
                + "PinSetPullAction[NONE,P8],"
                + "PinSetPullAction[NONE,P9],"
                + "PinSetPullAction[NONE,P10],"
                + "PinSetPullAction[NONE,P11],"
                + "PinSetPullAction[NONE,P12],"
                + "PinSetPullAction[NONE,P13],"
                + "PinSetPullAction[NONE,P14],"
                + "PinSetPullAction[NONE,P15],"
                + "PinSetPullAction[NONE,P16],"
                + "PinSetPullAction[NONE,P19],"
                + "PinSetPullAction[NONE,P20]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P9,portName=P9,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P6,portName=P6,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P5,portName=P5,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P4,portName=P4,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P16,portName=P16,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P19,portName=P19,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P12,portName=P12,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P13,portName=P13,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P14,portName=P14,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P15,portName=P15,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P20,portName=P20,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P7,portName=P7,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P10,portName=P10,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P8,portName=P8,componentProperties={PIN1=8}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P11,portName=P11,componentProperties={PIN1=11}"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/microbit/old_pin_pull.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedTemperature_WhenGivenOldTemperature() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[TemperatureSensor[_T,VALUE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[TemperatureSensor[_T,TEMPERATURE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=TEMPERATURE,isActor=true,userDefinedName=_T,portName=_T,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_temperature.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedWriteToPin_WhenGivenOldWriteToPin() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "PinWriteValueAction[ANALOG,P0,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P1,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P2,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P3,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P4,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P10,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P0_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P1_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P2_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P3_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P4_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P5,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P6,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P7,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P8,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P9,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P10_2,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P11,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P12,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P13,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P14,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P15,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P16,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P19,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P20,NumConst[1]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P9,portName=P9,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P6,portName=P6,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P5,portName=P5,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P4_2,portName=P4_2,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P3_2,portName=P3_2,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P2_2,portName=P2_2,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P4,portName=P4,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P1_2,portName=P1_2,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P0_2,portName=P0_2,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P16,portName=P16,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P19,portName=P19,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P12,portName=P12,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P13,portName=P13,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P14,portName=P14,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P15,portName=P15,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P10_2,portName=P10_2,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P11,portName=P11,componentProperties={PIN1=11}",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P10,portName=P10,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P20,portName=P20,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P7,portName=P7,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P8,portName=P8,componentProperties={PIN1=8}"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/microbit/old_write_to_pin.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAccelerometer_WhenGivenOldAccelerometer() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[AccelerometerSensor[_A,VALUE,X]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[_A,VALUE,Y]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[_A,VALUE,Z]]],"
                + "DebugAction[SensorExpr[AccelerometerSensor[_A,VALUE,STRENGTH]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Y]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Z]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,STRENGTH]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ACCELEROMETER,isActor=true,userDefinedName=_A,portName=_A,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_accelerometer.xml"), OLD_CONFIGURATION_XML)

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
                "ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=BZ,portName=BZ,componentProperties={PIN1=NO_PORT}]"
            };

        Project project =
            UnitTestHelper.setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_sounds.xml"), OLD_CONFIGURATION_XML).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinsSensor_WhenGivenOldPinsSensor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P0,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P1,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P2,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P3,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P4,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P10,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P0_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P1_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P2_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P3_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P4_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P5,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P6,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P7,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P8,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P9,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P10_2,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P11,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P12,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P13,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P14,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P15,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P16,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P19,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P20,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P0_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P1_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P2_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P3_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P4_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P5,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P6,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P7,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P8,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P9,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P10_2,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P11,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P12,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P13,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P14,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P15,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P16,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P19,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P20,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P0_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P1_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P2_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P3_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P4_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P5,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P6,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P7,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P8,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P9,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P10_2,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P11,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P12,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P13,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P14,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P15,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P16,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P19,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[P20,PULSELOW,EMPTY_SLOT]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P20,portName=P20,componentProperties={PIN1=20}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P14,portName=P14,componentProperties={PIN1=14}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P15,portName=P15,componentProperties={PIN1=15}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P11,portName=P11,componentProperties={PIN1=11}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P16,portName=P16,componentProperties={PIN1=16}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P10_2,portName=P10_2,componentProperties={PIN1=10}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P12,portName=P12,componentProperties={PIN1=12}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P13,portName=P13,componentProperties={PIN1=13}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P8,portName=P8,componentProperties={PIN1=8}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P7,portName=P7,componentProperties={PIN1=7}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P19,portName=P19,componentProperties={PIN1=19}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P9,portName=P9,componentProperties={PIN1=9}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P4_2,portName=P4_2,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P3_2,portName=P3_2,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P6,portName=P6,componentProperties={PIN1=6}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P5,portName=P5,componentProperties={PIN1=5}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P0_2,portName=P0_2,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P2_2,portName=P2_2,componentProperties={PIN1=2}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P4,portName=P4,componentProperties={PIN1=4}",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P1_2,portName=P1_2,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P10,portName=P10,componentProperties={PIN1=10}"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/microbit/old_pins_sensor.xml"), OLD_CONFIGURATION_XML)

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
            UnitTestHelper.setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_wait_for.xml"), OLD_CONFIGURATION_XML).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("robControls_wait_for", project.getProgramAst().getTree().get(0).get(2).getProperty().getBlockType());
    }
}

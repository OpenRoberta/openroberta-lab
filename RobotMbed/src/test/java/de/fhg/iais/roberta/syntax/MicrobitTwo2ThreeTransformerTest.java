package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerWorker;

public class MicrobitTwo2ThreeTransformerTest {

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"microbit\" xmlversion=\"2.0\">"
            + "    <instance x=\"138\" y=\"88\">"
            + "        <block id=\"1\" type=\"mbedBrick_microbit-Brick\" />"
            + "    </instance>"
            + "</block_set>";
    private static RobotFactory testFactory;

    @BeforeClass
    public static void setupBefore() throws Exception {
        AstFactory.loadBlocks();
        List<String> pluginDefines = new ArrayList<>();
        testFactory = Util.configureRobotPlugin("microbit", "", "", pluginDefines);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedCompass_WhenGivenOldCompass() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[CompassSensor[_C,ANGLE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[CompassSensor[_C,ANGLE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=COMPASS,isActor=true,userDefinedName=_C,portName=_C,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_compass.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedKey_WhenGivenOldKey() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[KeysSensor[A,PRESSED,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[KeysSensor[B,PRESSED,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[KeysSensor[A,PRESSED,- EMPTY_SLOT -]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[KeysSensor[B,PRESSED,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=A,portName=A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=B,portName=B,componentProperties={PIN1=B}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_key.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLight_WhenGivenOldLight() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[LightSensor[_L,VALUE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[LightSensor[_L,LIGHT_VALUE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=_L,portName=_L,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_light.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedTemperature_WhenGivenOldTemperature() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[TemperatureSensor[_T,VALUE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[TemperatureSensor[_T,VALUE,- EMPTY_SLOT -]]]]]]]";
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
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P0, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P1, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P3, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P4, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P10, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P0_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P1_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P2_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P3_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P4_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P5, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P6, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P7, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P8, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P9, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P10_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P11, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P12, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P13, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P14, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P15, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P16, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P19, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P20, value: NumConst[value:1]]]]]";
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
                .setupWithConfigAndProgramXML(
                    testFactory,
                    Util.readResourceContent("/transform/two2three/microbit/old_write_to_pin.xml"),
                    OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAccelerometer_WhenGivenOldAccelerometer() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[_A,VALUE,X]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[_A,VALUE,Y]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[_A,VALUE,Z]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[_A,VALUE,STRENGTH]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,X]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Y]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Z]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,STRENGTH]]]]]]]";
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
                + "ToneAction[frequency:NumConst[value:300],duration:NumConst[value:100],port:BZ],"
                + "PlayNoteAction[duration:2000,frequency:261.626,port:BZ],"
                + "PlayNoteAction[duration:1000,frequency:261.626,port:BZ],"
                + "PlayNoteAction[duration:500,frequency:261.626,port:BZ],"
                + "PlayNoteAction[duration:250,frequency:261.626,port:BZ],"
                + "PlayNoteAction[duration:125,frequency:261.626,port:BZ]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=BZ,portName=BZ,componentProperties={PIN1=- EMPTY_PORT -}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_sounds.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinsSensor_WhenGivenOldPinsSensor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P4,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P10,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P4_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P5,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P6,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P7,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P8,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P9,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P10_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P11,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P12,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P13,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P14,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P15,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P16,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P19,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P20,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P4_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P5,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P6,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P7,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P8,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P9,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P10_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P11,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P12,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P13,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P14,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P15,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P16,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P19,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P20,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P4_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P5,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P6,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P7,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P8,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P9,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P10_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P11,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P12,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P13,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P14,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P15,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P16,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P19,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P20,PULSELOW,- EMPTY_SLOT -]]]]]]";
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
                + "WaitStmt[(repeat[WAIT,SensorExpr[GetSampleSensor[GestureSensor[- EMPTY_PORT -,UP,- EMPTY_SLOT -]]]])]]]]";

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_wait_for.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("robControls_wait_for", project.getProgramAst().getTree().get(0).get(2).getProperty().getBlockType());
    }
}

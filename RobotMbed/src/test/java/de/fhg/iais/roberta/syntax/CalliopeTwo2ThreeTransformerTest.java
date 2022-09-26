package de.fhg.iais.roberta.syntax;

import java.util.Collections;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerWorker;

public class CalliopeTwo2ThreeTransformerTest {

    private static RobotFactory testFactory;

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"calliope\" xmlversion=\"2.0\">"
            + "    <instance x=\"138\" y=\"88\">"
            + "        <block id=\"1\" type=\"mbedBrick_Calliope-Brick\" />"
            + "    </instance>"
            + "</block_set>";

    @BeforeClass
    public static void setupBefore() throws Exception {
        AstFactory.loadBlocks();
        testFactory = Util.configureRobotPlugin("calliope2017NoBlue", "", "", Collections.emptyList());
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
    public void executeTransformer_ShouldReturnTransformedLedRgbled_WhenGivenOldLedRgbled() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:_R],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:CalliBot_links_vorne],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:CalliBot_rechts_vorne],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:CalliBot_links_hinten],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:CalliBot_rechts_hinten],"
                + "LedOnAction[ledColor:ColorConst[hexValue:#ff0000],port:CalliBot_alle],"
                + "LightStatusAction[_R, OFF],"
                + "LightStatusAction[CalliBot_links_vorne, OFF],"
                + "LightStatusAction[CalliBot_rechts_vorne, OFF],"
                + "LightStatusAction[CalliBot_links_hinten, OFF],"
                + "LightStatusAction[CalliBot_rechts_hinten, OFF],"
                + "LightStatusAction[CalliBot_alle, OFF],"
                + "LightAction[L_CalliBot_links,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_links,OFF,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_rechts,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_rechts,OFF,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_beide,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_beide,OFF,DEFAULT,EmptyExpr[defVal=COLOR]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=_R,portName=_R,componentProperties={}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_led_rgbled.xml"), OLD_CONFIGURATION_XML)

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
    public void executeTransformer_ShouldReturnTransformedSound_WhenGivenOldSound() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[SoundSensor[_S,SOUND,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[SoundSensor[_S,SOUND,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SOUND,isActor=true,userDefinedName=_S,portName=_S,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_sound.xml"), OLD_CONFIGURATION_XML)
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
    public void executeTransformer_ShouldReturnTransformedUltrasonic_WhenGivenOldUltrasonic() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[UltrasonicSensor[A1,DISTANCE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[UltrasonicSensor[CalliBot_vorne,DISTANCE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[UltrasonicSensor[A1,DISTANCE,- EMPTY_SLOT -]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[UltrasonicSensor[CalliBot_vorne,DISTANCE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_ultrasonic.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedWriteToPin_WhenGivenOldWriteToPin() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P1, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: P2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: A1, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: C04, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: C05, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: C06, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: C16, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: ANALOG, port: C17, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P0, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P1_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P2_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: P3, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: A0, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: A1_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C04_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C05_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C06_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C07, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C08, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C09, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C10, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C11, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C12, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C16_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C17_2, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C18, value: NumConst[value:1]],"
                + "MbedPinWriteValueAction[pinValue: DIGITAL, port: C19, value: NumConst[value:1]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P2_2,portName=P2_2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P1_2,portName=P1_2,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C06_2,portName=C06_2,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C07,portName=C07,componentProperties={PIN1=C07}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A0,portName=A0,componentProperties={PIN1=4}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C04_2,portName=C04_2,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A1_2,portName=A1_2,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C05_2,portName=C05_2,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C05,portName=C05,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C06,portName=C06,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C04,portName=C04,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C08,portName=C08,componentProperties={PIN1=C08}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C09,portName=C09,componentProperties={PIN1=C09}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C10,portName=C10,componentProperties={PIN1=C10}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C18,portName=C18,componentProperties={PIN1=C18}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C17_2,portName=C17_2,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C16_2,portName=C16_2,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C17,portName=C17,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C16,portName=C16,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C12,portName=C12,componentProperties={PIN1=C12}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C11,portName=C11,componentProperties={PIN1=C11}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C19,portName=C19,componentProperties={PIN1=C19}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(
                    testFactory,
                    Util.readResourceContent("/transform/two2three/calliope/old_write_to_pin.xml"),
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
    public void executeTransformer_ShouldReturnTransformedGyro_WhenGivenOldGyro() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[GyroSensor[_G,ANGLE,X]]],"
                + "DebugAction[value:SensorExpr[GyroSensor[_G,ANGLE,Y]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,X]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,Y]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=GYRO,isActor=true,userDefinedName=_G,portName=_G,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_gyro.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedHumidity_WhenGivenOldHumidity() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[HumiditySensor[A1,HUMIDITY,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[HumiditySensor[A1,TEMPERATURE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[HumiditySensor[A1,HUMIDITY,- EMPTY_SLOT -]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[HumiditySensor[A1,TEMPERATURE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=HUMIDITY,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_humidity.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedInfrared_WhenGivenOldInfrared() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[InfraredSensor[I_CalliBot_links,LINE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[InfraredSensor[I_CalliBot_rechts,LINE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[InfraredSensor[I_CalliBot_links,LINE,- EMPTY_SLOT -]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[InfraredSensor[I_CalliBot_rechts,LINE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_infrared.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedServo_WhenGivenOldServo() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[]," +
                "ServoSetAction[port:P1,value:NumConst[value:90]]," +
                "ServoSetAction[port:P2,value:NumConst[value:90]]," +
                "ServoSetAction[port:A1,value:NumConst[value:90]]," +
                "ServoSetAction[port:C04,value:NumConst[value:90]]," +
                "ServoSetAction[port:C05,value:NumConst[value:90]]," +
                "ServoSetAction[port:C06,value:NumConst[value:90]]," +
                "ServoSetAction[port:C16,value:NumConst[value:90]]," +
                "ServoSetAction[port:C17,value:NumConst[value:90]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=C06,portName=C06,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=C17,portName=C17,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=C05,portName=C05,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=C16,portName=C16,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=C04,portName=C04,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_servo.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedSounds_WhenGivenOldSounds() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "ToneAction[frequency:NumConst[value:300],duration:NumConst[value:100],port:_B],"
                + "PlayNoteAction[duration:2000,frequency:261.626,port:_B],"
                + "PlayNoteAction[duration:1000,frequency:261.626,port:_B],"
                + "PlayNoteAction[duration:500,frequency:261.626,port:_B],"
                + "PlayNoteAction[duration:250,frequency:261.626,port:_B],"
                + "PlayNoteAction[duration:125,frequency:261.626,port:_B]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=_B,portName=_B,componentProperties={}]"
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
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17,ANALOG,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,DIGITAL,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,PULSEHIGH,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P0,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,PULSELOW,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,PULSELOW,- EMPTY_SLOT -]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C16_2,portName=C16_2,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C17_2,portName=C17_2,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C18,portName=C18,componentProperties={PIN1=C18}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C19,portName=C19,componentProperties={PIN1=C19}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P1_2,portName=P1_2,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P2_2,portName=P2_2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=C04,portName=C04,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=C05,portName=C05,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=C06,portName=C06,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C04_2,portName=C04_2,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C05_2,portName=C05_2,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C06_2,portName=C06_2,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C07,portName=C07,componentProperties={PIN1=C07}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C08,portName=C08,componentProperties={PIN1=C08}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C09,portName=C09,componentProperties={PIN1=C09}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C10,portName=C10,componentProperties={PIN1=C10}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C11,portName=C11,componentProperties={PIN1=C11}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=C12,portName=C12,componentProperties={PIN1=C12}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=C16,portName=C16,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=C17,portName=C17,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=A1_2,portName=A1_2,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=A0,portName=A0,componentProperties={PIN1=4}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/calliope/old_pins_sensor.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLedbar_WhenGivenOldLedbar() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=512,y=50],MainTask[]," + "LedBarSetAction[x:NumConst[value:0],brightness:NumConst[value:5]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LEDBAR,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_ledbar.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedFourdigitdisplay_WhenGivenOldFourdigitdisplay() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "FourDigitDisplayShowAction[value:NumConst[value:1234],position:NumConst[value:0],colon:BoolConst[value:true]],"
                + "FourDigitDisplayClearAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=FOURDIGITDISPLAY,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_fourdigitdisplay.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotor_WhenGivenOldSingleMotor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "MotorOnAction[Port_A,MotionParam[speed=NumConst[value:30],duration=null]],"
                + "MotorStop[port=Port_A,mode=FLOAT],"
                + "MotorStop[port=Port_A,mode=NONFLOAT],"
                + "MotorStop[port=Port_A,mode=SLEEP]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_single_motor_on_stop.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotors_WhenGivenOldMotors() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "BothMotorsOnAction[speedA:NumConst[value:30],speedB:NumConst[value:10],portA:Port_A,portB:Port_B],"
                + "BothMotorsOnAction[speedA:NumConst[value:30],speedB:NumConst[value:10],portA:CalliBot_links,portB:CalliBot_rechts],"
                + "BothMotorsStopAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_motors_on_stop.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotorSingle_WhenGivenOldMotorSingle() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "MotorOnAction[Port_A,MotionParam[speed=NumConst[value:30],duration=null]],"
                + "MotorOnAction[Port_B,MotionParam[speed=NumConst[value:30],duration=null]],"
                + "MotorOnAction[CalliBot_links,MotionParam[speed=NumConst[value:30],duration=null]],"
                + "MotorOnAction[CalliBot_rechts,MotionParam[speed=NumConst[value:30],duration=null]],"
                + "MotorStop[port=Port_A],"
                + "MotorStop[port=Port_B],"
                + "MotorStop[port=CalliBot_links],"
                + "MotorStop[port=CalliBot_rechts]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_motor_on_stop_single.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotors_WhenGivenOldMotorDouble() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "BothMotorsOnAction[speedA:NumConst[value:30],speedB:NumConst[value:30],portA:Port_A,portB:Port_B],"
                + "BothMotorsOnAction[speedA:NumConst[value:30],speedB:NumConst[value:30],portA:CalliBot_links,portB:CalliBot_rechts],"
                + "BothMotorsStopAction[],"
                + "BothMotorsStopAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={LED_B=L_CalliBot_beide, RGBLED_RF=CalliBot_rechts_vorne, INFRARED_L=I_CalliBot_links,"
                    + "RGBLED_A=CalliBot_alle, RGBLED_LF=CalliBot_links_vorne, RGBLED_LR=CalliBot_links_hinten, MOTOR_L=CalliBot_links,"
                    + "LED_R=L_CalliBot_rechts, RGBLED_RR=CalliBot_rechts_hinten, ULTRASONIC=CalliBot_vorne, LED_L=L_CalliBot_links,"
                    + "MOTOR_R=CalliBot_rechts, INFRARED_R=I_CalliBot_rechts}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/two2three/old_motor_on_stop_double.xml"), OLD_CONFIGURATION_XML)

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

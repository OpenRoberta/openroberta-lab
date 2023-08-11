package de.fhg.iais.roberta.syntax;

import java.util.Collections;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.XsltAndJavaTransformer;
import de.fhg.iais.roberta.util.ast.AstFactory;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerWorker;

public class CalliopeTwo2ThreeTransformerTest {

    private static RobotFactory testFactory;
    private static XsltAndJavaTransformer xsltAndJavaTransformer;

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"calliope\" xmlversion=\"2.0\">"
            + "    <instance x=\"1201\" y=\"148\">"
            + "        <block type=\"robConf_ultrasonic\" id=\"A1\" inline=\"false\" intask=\"true\">"
            + "            <field name=\"NAME\">A1</field>"
            + "            <field name=\"PIN1\">1</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"269\" y=\"213\">"
            + "        <block type=\"robConf_key\" id=\"Kbp:=x@gw@n.b~NH|1|e\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">A</field>"
            + "            <field name=\"PIN1\">A</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"213\">"
            + "        <block type=\"robConf_light\" id=\"9Ktt(7_4+XmKC0]5Zha?\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">L</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"624\" y=\"213\">"
            + "        <block type=\"robConf_buzzer\" id=\"}%d2_68IJd*7Oy{qrMR,\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">BZ</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"799\" y=\"213\">"
            + "        <block type=\"robConf_callibot\" id=\"CalliBot\" inline=\"false\" intask=\"true\">"
            + "            <field name=\"NAME\">CalliBot</field>"
            + "            <field name=\"MOTOR_L\">CalliBot_links</field>"
            + "            <field name=\"MOTOR_R\">CalliBot_rechts</field>"
            + "            <field name=\"RGBLED_LF\">CalliBot_links_vorne</field>"
            + "            <field name=\"RGBLED_RF\">CalliBot_rechts_vorne</field>"
            + "            <field name=\"RGBLED_LR\">CalliBot_links_hinten</field>"
            + "            <field name=\"RGBLED_RR\">CalliBot_rechts_hinten</field>"
            + "            <field name=\"RGBLED_A\">CalliBot_alle</field>"
            + "            <field name=\"LED_L\">L_CalliBot_links</field>"
            + "            <field name=\"LED_R\">L_CalliBot_rechts</field>"
            + "            <field name=\"LED_B\">L_CalliBot_beide</field>"
            + "            <field name=\"INFRARED_L\">I_CalliBot_links</field>"
            + "            <field name=\"INFRARED_R\">I_CalliBot_rechts</field>"
            + "            <field name=\"ULTRASONIC\">CalliBot_vorne</field>"
            + "            <field name=\"SERVO_S1\">S</field>"
            + "            <field name=\"SERVO_S2\">S5</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"255\">"
            + "        <block type=\"robConf_temperature\" id=\"0y].dnS)P)~Jjzeu3/0C\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">TM</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"624\" y=\"251\">"
            + "        <block type=\"robConf_rgbled\" id=\"lgi%C3,J!smR:eP0aal_\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">R</field>"
            + "            <field name=\"PIN1\">0</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"269\" y=\"277\">"
            + "        <block type=\"robConf_key\" id=\"~wk*JZQvB.fj=I`N#NWp\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">B</field>"
            + "            <field name=\"PIN1\">B</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"297\">"
            + "        <block type=\"robConf_compass\" id=\"O%Oi0Tq`hGEvrgHGPYY=\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">C</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"335\">"
            + "        <block type=\"robConf_gyro\" id=\"lIT?C5y|(7FWCQIUn~pc\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">G</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"80\" y=\"377\">"
            + "        <block type=\"robConf_digitalout\" id=\"5[8k:Bl.8*_E-ryV!m/P\" intask=\"true\">"
            + "            <field name=\"NAME\">S6</field>"
            + "            <field name=\"PIN1\">0</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"373\">"
            + "        <block type=\"robConf_accelerometer\" id=\"#(ufvG,s^FcqI,aFn/A{\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">Acc</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"400\" y=\"410\">"
            + "        <block type=\"robConf_sound\" id=\"e:4V0FcDx%9,.7xO,Ns@\" intask=\"true\" deletable=\"false\">"
            + "            <field name=\"NAME\">M</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"92\" y=\"499\">"
            + "        <block type=\"robConf_analogout\" id=\"1j|wBv@dkUUX*y71EcLT\" intask=\"true\">"
            + "            <field name=\"NAME\">S7</field>"
            + "            <field name=\"PIN1\">1</field>"
            + "        </block>"
            + "    </instance>"
            + "    <instance x=\"326\" y=\"542\">"
            + "        <block type=\"robConf_humidity\" id=\"k,P]gmdyA(=4li[lHum_\" intask=\"true\">"
            + "            <field name=\"NAME\">H</field>"
            + "            <field name=\"PIN1\">5</field>"
            + "        </block>"
            + "    </instance>"
            + "</block_set>";

    @BeforeClass
    public static void setupBefore() throws Exception {
        AstFactory.loadBlocks();
        xsltAndJavaTransformer = new XsltAndJavaTransformer();
        testFactory = Util.configureRobotPlugin("calliope2017NoBlue", "", "", Collections.emptyList());
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedCompass_WhenGivenOldCompass() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[CompassSensor[C,ANGLE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[CompassSensor[C,ANGLE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=COMPASS,category=CONFIGURATION_SENSOR,userDefinedName=C,portName=C,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_compass.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=KEY,category=CONFIGURATION_SENSOR,userDefinedName=A,portName=A,componentProperties={PIN1=A}]",
                "ConfigurationComponent[componentType=KEY,category=CONFIGURATION_SENSOR,userDefinedName=B,portName=B,componentProperties={PIN1=B}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_key.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLedRgbled_WhenGivenOldLedRgbled() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "RgbLedOnAction[port:CalliBot_links_vorne,colour:ColorConst[hexValue:#ff0000]],"
                + "RgbLedOnAction[port:CalliBot_links_hinten,colour:ColorConst[hexValue:#ff0000]],"
                + "RgbLedOnAction[port:CalliBot_rechts_hinten,colour:ColorConst[hexValue:#ff0000]],"
                + "RgbLedOnAction[port:CalliBot_rechts_vorne,colour:ColorConst[hexValue:#ff0000]],"
                + "RgbLedOffAction[port:CalliBot_links_vorne],"
                + "RgbLedOffAction[port:CalliBot_links_hinten],"
                + "RgbLedOffAction[port:CalliBot_rechts_hinten],"
                + "RgbLedOffAction[port:CalliBot_rechts_vorne],"
                + "LedAction[port:L_CalliBot_links,mode:ON],"
                + "LedAction[port:L_CalliBot_links,mode:OFF],"
                + "LedAction[port:L_CalliBot_rechts,mode:ON],"
                + "LedAction[port:L_CalliBot_rechts,mode:OFF]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent [componentType=RGBLED, category=CONFIGURATION_ACTOR, userDefinedName=R, portName=R, componentProperties={PIN1=0}]",
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_led_rgbled.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLight_WhenGivenOldLight() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[LightSensor[L,VALUE,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[LightSensor[L,LIGHT_VALUE,-EMPTY_SLOT-]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LIGHT,category=CONFIGURATION_SENSOR,userDefinedName=L,portName=L,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_light.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedSound_WhenGivenOldSound() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[SoundSensor[M,SOUND,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[SoundSensor[M,SOUND,-EMPTY_SLOT-]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SOUND,category=CONFIGURATION_SENSOR,userDefinedName=M,portName=M,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_sound.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedTemperature_WhenGivenOldTemperature() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[value:SensorExpr[TemperatureSensor[TM,VALUE,- EMPTY_SLOT -]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[TemperatureSensor[TM,VALUE,- EMPTY_SLOT -]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=TEMPERATURE,category=CONFIGURATION_SENSOR,userDefinedName=TM,portName=TM,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_temperature.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=ULTRASONIC,category=CONFIGURATION_SENSOR,userDefinedName=A1,portName=A1,componentProperties={PIN1=1}]",
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_ultrasonic.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P0,portName=P0,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P2_2,portName=P2_2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P1_2,portName=P1_2,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C06_2,portName=C06_2,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C07,portName=C07,componentProperties={PIN1=C07}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=A0,portName=A0,componentProperties={PIN1=4}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C04_2,portName=C04_2,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=A1_2,portName=A1_2,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C05_2,portName=C05_2,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C05,portName=C05,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C06,portName=C06,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=P3,portName=P3,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C04,portName=C04,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C08,portName=C08,componentProperties={PIN1=C08}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C09,portName=C09,componentProperties={PIN1=C09}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C10,portName=C10,componentProperties={PIN1=C10}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C18,portName=C18,componentProperties={PIN1=C18}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C17_2,portName=C17_2,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C16_2,portName=C16_2,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C17,portName=C17,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C16,portName=C16,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C12,portName=C12,componentProperties={PIN1=C12}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C11,portName=C11,componentProperties={PIN1=C11}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,category=CONFIGURATION_ACTOR,userDefinedName=C19,portName=C19,componentProperties={PIN1=C19}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/calliope/old_write_to_pin.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedAccelerometer_WhenGivenOldAccelerometer() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[Acc,VALUE,X]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[Acc,VALUE,Y]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[Acc,VALUE,Z]]],"
                + "DebugAction[value:SensorExpr[AccelerometerSensor[Acc,VALUE,STRENGTH]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,X]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,Y]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,Z]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[AccelerometerSensor[Acc,DEFAULT,STRENGTH]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ACCELEROMETER,category=CONFIGURATION_SENSOR,userDefinedName=Acc,portName=Acc,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_accelerometer.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedGyro_WhenGivenOldGyro() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[GyroSensor[G,ANGLE,X]]],"
                + "DebugAction[value:SensorExpr[GyroSensor[G,ANGLE,Y]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[GyroSensor[G,ANGLE,X]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[GyroSensor[G,ANGLE,Y]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=GYRO,category=CONFIGURATION_SENSOR,userDefinedName=G,portName=G,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_gyro.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedHumidity_WhenGivenOldHumidity() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[HumiditySensor[H,HUMIDITY,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[HumiditySensor[H,TEMPERATURE,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[HumiditySensor[H,HUMIDITY,-EMPTY_SLOT-]]]],"
                + "DebugAction[value:SensorExpr[GetSampleSensor[HumiditySensor[H,TEMPERATURE,-EMPTY_SLOT-]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=HUMIDITY,category=CONFIGURATION_SENSOR,userDefinedName=H,portName=H,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_humidity.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_infrared.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=C06,portName=C06,componentProperties={PIN1=C06}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=C17,portName=C17,componentProperties={PIN1=C17}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=C05,portName=C05,componentProperties={PIN1=C05}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=C16,portName=C16,componentProperties={PIN1=C16}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=C04,portName=C04,componentProperties={PIN1=C04}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=P2,portName=P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=P1,portName=P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,category=CONFIGURATION_ACTOR,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_servo.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=BUZZER,category=CONFIGURATION_ACTOR,userDefinedName=BZ,portName=BZ,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_sounds.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedPinsSensor_WhenGivenOldPinsSensor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[S7,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17,ANALOG,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[S6,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,DIGITAL,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[S6,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,PULSEHIGH,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[S6,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P1,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P2_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[P3,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A0,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[A1_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C04_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C05_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C06_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C07,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C08,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C09,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C10,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C11,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C12,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C16_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C17_2,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C18,PULSELOW,-EMPTY_SLOT-]]],"
                + "DebugAction[value:SensorExpr[PinGetValueSensor[C19,PULSELOW,-EMPTY_SLOT-]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=S6, portName=S6, componentProperties={PIN1=0}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=S7, portName=S7, componentProperties={PIN1=1}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C04, portName=C04, componentProperties={PIN1=C04}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C06, portName=C06, componentProperties={PIN1=C06}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C05, portName=C05, componentProperties={PIN1=C05}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C08, portName=C08, componentProperties={PIN1=C08}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C07, portName=C07, componentProperties={PIN1=C07}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C09, portName=C09, componentProperties={PIN1=C09}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=A0, portName=A0, componentProperties={PIN1=4}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=A1, portName=A1, componentProperties={PIN1=5}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C17_2, portName=C17_2, componentProperties={PIN1=C17}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C16_2, portName=C16_2, componentProperties={PIN1=C16}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=A1_2, portName=A1_2, componentProperties={PIN1=5}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=P1, portName=P1, componentProperties={PIN1=1}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=P2, portName=P2, componentProperties={PIN1=2}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C11, portName=C11, componentProperties={PIN1=C11}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=P3, portName=P3, componentProperties={PIN1=3}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C10, portName=C10, componentProperties={PIN1=C10}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C12, portName=C12, componentProperties={PIN1=C12}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C17, portName=C17, componentProperties={PIN1=C17}]",
                "ConfigurationComponent [componentType=ANALOG_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C16, portName=C16, componentProperties={PIN1=C16}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C19, portName=C19, componentProperties={PIN1=C19}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C18, portName=C18, componentProperties={PIN1=C18}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=P2_2, portName=P2_2, componentProperties={PIN1=2}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C06_2, portName=C06_2, componentProperties={PIN1=C06}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C04_2, portName=C04_2, componentProperties={PIN1=C04}]",
                "ConfigurationComponent [componentType=DIGITAL_PIN, category=CONFIGURATION_SENSOR, userDefinedName=C05_2, portName=C05_2, componentProperties={PIN1=C05}]",
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/calliope/old_pins_sensor.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=LEDBAR,category=CONFIGURATION_ACTOR,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_ledbar.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=FOURDIGITDISPLAY,category=CONFIGURATION_ACTOR,userDefinedName=A1,portName=A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_fourdigitdisplay.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_single_motor_on_stop.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_motors_on_stop.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_motor_on_stop_single.xml"), OLD_CONFIGURATION_XML)
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
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_B,portName=Port_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,category=CONFIGURATION_ACTOR,userDefinedName=Port_A,portName=Port_A,componentProperties={PIN1=A}]",
                "ConfigurationComponent [componentType=CALLIBOT, category=CONFIGURATION_ACTOR, userDefinedName=CalliBot, portName=CalliBot, componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_motor_on_stop_double.xml"), OLD_CONFIGURATION_XML)
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
                .setupWithConfigAndProgramXML(testFactory, xsltAndJavaTransformer, Util.readResourceContent("/transform/two2three/old_wait_for.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        Assert.assertEquals("robControls_wait_for", project.getProgramAst().getTree().get(0).get(2).getProperty().getBlockType());
    }
}

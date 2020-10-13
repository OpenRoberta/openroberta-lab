package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;
import de.fhg.iais.roberta.worker.MbedThree2ThreeOneTransformerWorker;

public class CalliopeThree2ThreeOneTransformerTest {

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"calliope\" xmlversion=\"3.0\" description=\"\" tags=\"\">"
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
    private static IRobotFactory testFactory;

    @BeforeClass
    public static void setupBefore() throws Exception {
        testFactory = Util.configureRobotPlugin("calliope2017NoBlue", "", "", Collections.emptyList());
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
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_compass.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLedRgbled_WhenGivenOldLedRgbled() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "LedOnAction[_R,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_links_vorne,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_rechts_vorne,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_links_hinten,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_rechts_hinten,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_alle,ColorConst[#ff0000]],"
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
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=_R,portName=_R,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={MOTOR_L=CalliBot_links, MOTOR_R=CalliBot_rechts, RGBLED_LF=CalliBot_links_vorne,"
                    + "RGBLED_RF=CalliBot_rechts_vorne, RGBLED_LR=CalliBot_links_hinten, RGBLED_RR=CalliBot_rechts_hinten,"
                    + "RGBLED_A=CalliBot_alle, LED_L=L_CalliBot_links, LED_R=L_CalliBot_rechts, LED_B=L_CalliBot_beide,"
                    + "INFRARED_L=I_CalliBot_links, INFRARED_R=I_CalliBot_rechts, ULTRASONIC=CalliBot_vorne, SERVO_S1=S, SERVO_S2=S5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_led_rgbled.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
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
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_light.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedSound_WhenGivenOldSound() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[SoundSensor[_S,SOUND,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[SoundSensor[_S,SOUND,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SOUND,isActor=true,userDefinedName=_S,portName=_S,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_sound.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
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
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_temperature.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedUltrasonic_WhenGivenOldUltrasonic() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[UltrasonicSensor[A1,DISTANCE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[UltrasonicSensor[CalliBot_vorne,DISTANCE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[UltrasonicSensor[A1,DISTANCE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[UltrasonicSensor[CalliBot_vorne,DISTANCE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=A1,portName=A1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=CALLIBOT, isActor=true, userDefinedName=CalliBot, portName=CalliBot,"
                    + "componentProperties={MOTOR_L=CalliBot_links, MOTOR_R=CalliBot_rechts, RGBLED_LF=CalliBot_links_vorne,"
                    + "RGBLED_RF=CalliBot_rechts_vorne, RGBLED_LR=CalliBot_links_hinten, RGBLED_RR=CalliBot_rechts_hinten,"
                    + "RGBLED_A=CalliBot_alle, LED_L=L_CalliBot_links, LED_R=L_CalliBot_rechts, LED_B=L_CalliBot_beide,"
                    + "INFRARED_L=I_CalliBot_links, INFRARED_R=I_CalliBot_rechts, ULTRASONIC=CalliBot_vorne, SERVO_S1=S, SERVO_S2=S5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_ultrasonic.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
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
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_accelerometer.xml"), OLD_CONFIGURATION_XML)

                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedGyro_WhenGivenOldGyro() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[GyroSensor[_G,ANGLE,X]]],"
                + "DebugAction[SensorExpr[GyroSensor[_G,ANGLE,Y]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,Y]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=GYRO,isActor=true,userDefinedName=_G,portName=_G,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_gyro.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
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
                "ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=_B,portName=_B,componentProperties={}]"
            };

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_sounds.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedWaitFor_WhenGivenOldWaitFor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[GetSampleSensor[KeysSensor[A,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[KeysSensor[B,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinTouchSensor[0,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinTouchSensor[1,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinTouchSensor[2,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinTouchSensor[3,PRESSED,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,UP,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,DOWN,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,FACE_DOWN,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,FACE_UP,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,SHAKE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GestureSensor[NO_PORT,FREEFALL,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[CompassSensor[_C,ANGLE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[SoundSensor[_S,SOUND,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[TimerSensor[1,VALUE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[TemperatureSensor[_T,TEMPERATURE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[LightSensor[_L,LIGHT_VALUE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinGetValueSensor[S7,ANALOG,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinGetValueSensor[S6,DIGITAL,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinGetValueSensor[S6,PULSEHIGH,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[PinGetValueSensor[S6,PULSELOW,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[_G,ANGLE,Y]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Y]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,Z]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[AccelerometerSensor[_A,DEFAULT,STRENGTH]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[HumiditySensor[H,HUMIDITY,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[HumiditySensor[H,TEMPERATURE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[UltrasonicSensor[CalliBot_vorne,DISTANCE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[InfraredSensor[I_CalliBot_links,LINE,EMPTY_SLOT]]]]]]]";

        Project project =
            UnitTestHelper
                .setupWithConfigAndProgramXML(testFactory, Util.readResourceContent("/transform/three2threeone/old_wait_for.xml"), OLD_CONFIGURATION_XML)
                .build();

        new MbedThree2ThreeOneTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
    }
}

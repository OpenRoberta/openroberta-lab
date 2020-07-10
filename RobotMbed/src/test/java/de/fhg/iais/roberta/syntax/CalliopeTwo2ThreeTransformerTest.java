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

public class CalliopeTwo2ThreeTransformerTest {

    private static IRobotFactory testFactory;
    private static ConfigurationAst configuration;

    private static final String OLD_CONFIGURATION_XML =
        "<block_set xmlns=\"http://de.fhg.iais.roberta.blockly\" robottype=\"calliope\" xmlversion=\"2.0\">"
            + "    <instance x=\"138\" y=\"88\">"
            + "        <block id=\"1\" type=\"mbedBrick_Calliope-Brick\" />"
            + "    </instance>"
            + "</block_set>";

    @BeforeClass
    public static void setupBefore() throws Exception {
        List<String> pluginDefines = new ArrayList<>();
        pluginDefines.add("calliope2017NoBlue:robot.configuration.type = old-S");
        pluginDefines.add("calliope2017NoBlue:robot.configuration.old.toplevelblock = mbedBrick_Calliope-Brick");
        testFactory = Util.configureRobotPlugin("calliope2017NoBlue", "", "", pluginDefines);
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
    public void executeTransformer_ShouldReturnTransformedLedRgbled_WhenGivenOldLedRgbled() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "LedOnAction[R,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_links_vorne,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_rechts_vorne,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_links_hinten,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_rechts_hinten,ColorConst[#ff0000]],"
                + "LedOnAction[CalliBot_alle,ColorConst[#ff0000]],"
                + "LightStatusAction[R, OFF],"
                + "LightStatusAction[CalliBot_links_vorne, OFF],"
                + "LightStatusAction[CalliBot_rechts_vorne, OFF],"
                + "LightStatusAction[CalliBot_links_hinten, OFF],"
                + "LightStatusAction[CalliBot_rechts_hinten, OFF],"
                + "LightStatusAction[CalliBot_alle, OFF],"
                + "LightAction[L_CalliBot_links,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_links,OFF,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_rechts,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[L_CalliBot_rechts,OFF,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[CalliBot_beide,ON,DEFAULT,EmptyExpr[defVal=COLOR]],"
                + "LightAction[CalliBot_beide,OFF,DEFAULT,EmptyExpr[defVal=COLOR]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=R,portName=R,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=CalliBot_links_hinten,portName=CalliBot_links_hinten,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=L_CalliBot_links,portName=L_CalliBot_links,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=CalliBot_links_vorne,portName=CalliBot_links_vorne,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=CalliBot_rechts_hinten,portName=CalliBot_rechts_hinten,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=CalliBot_alle,portName=CalliBot_alle,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=CalliBot_rechts_vorne,portName=CalliBot_rechts_vorne,componentProperties={PIN1=4}]",
                "ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=L_CalliBot_rechts,portName=L_CalliBot_rechts,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=CalliBot_beide,portName=CalliBot_beide,componentProperties={PIN1=3}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_led_rgbled.xml"))
                .setConfigurationAst(configuration)
                .build();

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
                + "PinSetPullAction[UP,P0_D],"
                + "PinSetPullAction[UP,P1_D],"
                + "PinSetPullAction[UP,P2_D],"
                + "PinSetPullAction[UP,P3_D],"
                + "PinSetPullAction[UP,A0_D],"
                + "PinSetPullAction[UP,A1_D],"
                + "PinSetPullAction[UP,C04_D],"
                + "PinSetPullAction[UP,C05_D],"
                + "PinSetPullAction[UP,C06_D],"
                + "PinSetPullAction[UP,C07_D],"
                + "PinSetPullAction[UP,C08_D],"
                + "PinSetPullAction[UP,C09_D],"
                + "PinSetPullAction[UP,C10_D],"
                + "PinSetPullAction[UP,C11_D],"
                + "PinSetPullAction[UP,C12_D],"
                + "PinSetPullAction[UP,C16_D],"
                + "PinSetPullAction[UP,C17_D],"
                + "PinSetPullAction[UP,C18_D],"
                + "PinSetPullAction[UP,C19_D],"
                + "PinSetPullAction[DOWN,P0_D],"
                + "PinSetPullAction[DOWN,P1_D],"
                + "PinSetPullAction[DOWN,P2_D],"
                + "PinSetPullAction[DOWN,P3_D],"
                + "PinSetPullAction[DOWN,A0_D],"
                + "PinSetPullAction[DOWN,A1_D],"
                + "PinSetPullAction[DOWN,C04_D],"
                + "PinSetPullAction[DOWN,C05_D],"
                + "PinSetPullAction[DOWN,C06_D],"
                + "PinSetPullAction[DOWN,C07_D],"
                + "PinSetPullAction[DOWN,C08_D],"
                + "PinSetPullAction[DOWN,C09_D],"
                + "PinSetPullAction[DOWN,C10_D],"
                + "PinSetPullAction[DOWN,C11_D],"
                + "PinSetPullAction[DOWN,C12_D],"
                + "PinSetPullAction[DOWN,C16_D],"
                + "PinSetPullAction[DOWN,C17_D],"
                + "PinSetPullAction[DOWN,C18_D],"
                + "PinSetPullAction[DOWN,C19_D],"
                + "PinSetPullAction[NONE,P0_D],"
                + "PinSetPullAction[NONE,P1_D],"
                + "PinSetPullAction[NONE,P2_D],"
                + "PinSetPullAction[NONE,P3_D],"
                + "PinSetPullAction[NONE,A0_D],"
                + "PinSetPullAction[NONE,A1_D],"
                + "PinSetPullAction[NONE,C04_D],"
                + "PinSetPullAction[NONE,C05_D],"
                + "PinSetPullAction[NONE,C06_D],"
                + "PinSetPullAction[NONE,C07_D],"
                + "PinSetPullAction[NONE,C08_D],"
                + "PinSetPullAction[NONE,C09_D],"
                + "PinSetPullAction[NONE,C10_D],"
                + "PinSetPullAction[NONE,C11_D],"
                + "PinSetPullAction[NONE,C12_D],"
                + "PinSetPullAction[NONE,C16_D],"
                + "PinSetPullAction[NONE,C17_D],"
                + "PinSetPullAction[NONE,C18_D],"
                + "PinSetPullAction[NONE,C19_D]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P0_D,portName=P0_D,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C10_D,portName=C10_D,componentProperties={PIN1=C10}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P2_D,portName=P2_D,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P1_D,portName=P1_D,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C06_D,portName=C06_D,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C18_D,portName=C18_D,componentProperties={PIN1=C18}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C07_D,portName=C07_D,componentProperties={PIN1=C07}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C17_D,portName=C17_D,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A0_D,portName=A0_D,componentProperties={PIN1=4}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C04_D,portName=C04_D,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C16_D,portName=C16_D,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A1_D,portName=A1_D,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C05_D,portName=C05_D,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P3_D,portName=P3_D,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C12_D,portName=C12_D,componentProperties={PIN1=C12}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C11_D,portName=C11_D,componentProperties={PIN1=C11}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C08_D,portName=C08_D,componentProperties={PIN1=C08}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C09_D,portName=C09_D,componentProperties={PIN1=C09}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C19_D,portName=C19_D,componentProperties={PIN1=C19}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/calliope/old_pin_pull.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedSound_WhenGivenOldSound() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[SoundSensor[M,SOUND,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[SoundSensor[M,SOUND,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SOUND,isActor=true,userDefinedName=M,portName=M,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_sound.xml")).setConfigurationAst(configuration).build();

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
    public void executeTransformer_ShouldReturnTransformedUltrasonic_WhenGivenOldUltrasonic() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=549,y=76],MainTask[],"
                + "DebugAction[SensorExpr[UltrasonicSensor[U,DISTANCE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[UltrasonicSensor[CalliBot,DISTANCE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[UltrasonicSensor[U,DISTANCE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[UltrasonicSensor[CalliBot,DISTANCE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=U,portName=U,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=CalliBot,portName=CalliBot,componentProperties={PIN1=2}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_ultrasonic.xml"))
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
                + "PinWriteValueAction[ANALOG,P1_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,P2_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,A1_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,C04_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,C05_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,C06_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,C16_A,NumConst[1]],"
                + "PinWriteValueAction[ANALOG,C17_A,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P0_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P1_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P2_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,P3_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A0_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,A1_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C04_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C05_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C06_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C07_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C08_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C09_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C10_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C11_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C12_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C16_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C17_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C18_D,NumConst[1]],"
                + "PinWriteValueAction[DIGITAL,C19_D,NumConst[1]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P0_D,portName=P0_D,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P2_A,portName=P2_A,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P2_D,portName=P2_D,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P1_D,portName=P1_D,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=P1_A,portName=P1_A,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C06_D,portName=C06_D,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C07_D,portName=C07_D,componentProperties={PIN1=C07}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A0_D,portName=A0_D,componentProperties={PIN1=4}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C04_D,portName=C04_D,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A1_D,portName=A1_D,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C05_D,portName=C05_D,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A1_A,portName=A1_A,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C05_A,portName=C05_A,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C06_A,portName=C06_A,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=P3_D,portName=P3_D,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C04_A,portName=C04_A,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C08_D,portName=C08_D,componentProperties={PIN1=C08}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C09_D,portName=C09_D,componentProperties={PIN1=C09}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C10_D,portName=C10_D,componentProperties={PIN1=C10}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C18_D,portName=C18_D,componentProperties={PIN1=C18}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C17_D,portName=C17_D,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C16_D,portName=C16_D,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C17_A,portName=C17_A,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=C16_A,portName=C16_A,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C12_D,portName=C12_D,componentProperties={PIN1=C12}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C11_D,portName=C11_D,componentProperties={PIN1=C11}],",
                "ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=C19_D,portName=C19_D,componentProperties={PIN1=C19}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/calliope/old_write_to_pin.xml"))
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
    public void executeTransformer_ShouldReturnTransformedGyro_WhenGivenOldGyro() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[GyroSensor[G,ANGLE,X]]],"
                + "DebugAction[SensorExpr[GyroSensor[G,ANGLE,Y]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[G,ANGLE,X]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[GyroSensor[G,ANGLE,Y]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=GYRO,isActor=true,userDefinedName=G,portName=G,componentProperties={}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_gyro.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedHumidity_WhenGivenOldHumidity() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[HumiditySensor[H,HUMIDITY,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[HumiditySensor[H,TEMPERATURE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[HumiditySensor[H,HUMIDITY,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[HumiditySensor[H,TEMPERATURE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=HUMIDITY,isActor=true,userDefinedName=H,portName=H,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_humidity.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedInfrared_WhenGivenOldInfrared() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "DebugAction[SensorExpr[InfraredSensor[I_CalliBot_links,LINE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[InfraredSensor[I_CalliBot_rechts,LINE,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[InfraredSensor[I_CalliBot_links,LINE,EMPTY_SLOT]]]],"
                + "DebugAction[SensorExpr[GetSampleSensor[InfraredSensor[I_CalliBot_rechts,LINE,EMPTY_SLOT]]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=INFRARED,isActor=true,userDefinedName=I_CalliBot_links,portName=I_CalliBot_links,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=INFRARED,isActor=true,userDefinedName=I_CalliBot_rechts,portName=I_CalliBot_rechts,componentProperties={PIN1=1}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_infrared.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedServo_WhenGivenOldServo() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "ServoSetAction[S_P1,NumConst[90]],"
                + "ServoSetAction[S_P2,NumConst[90]],"
                + "ServoSetAction[S_A1,NumConst[90]],"
                + "ServoSetAction[S_C04,NumConst[90]],"
                + "ServoSetAction[S_C05,NumConst[90]],"
                + "ServoSetAction[S_C06,NumConst[90]],"
                + "ServoSetAction[S_C16,NumConst[90]],"
                + "ServoSetAction[S_C17,NumConst[90]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_C06,portName=S_C06,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_C17,portName=S_C17,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_C05,portName=S_C05,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_C16,portName=S_C16,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_C04,portName=S_C04,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_P2,portName=S_P2,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_P1,portName=S_P1,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S_A1,portName=S_A1,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_servo.xml")).setConfigurationAst(configuration).build();

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
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P1_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P2_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A1_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C04_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C05_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C06_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C16_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C17_A,ANALOG,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P0_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P1_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P2_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P3_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A0_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A1_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C04_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C05_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C06_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C07_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C08_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C09_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C10_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C11_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C12_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C16_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C17_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C18_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C19_D,DIGITAL,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P0_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P1_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P2_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P3_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A0_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A1_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C04_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C05_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C06_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C07_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C08_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C09_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C10_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C11_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C12_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C16_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C17_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C18_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C19_D,PULSEHIGH,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P0_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P1_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P2_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_P3_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A0_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_A1_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C04_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C05_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C06_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C07_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C08_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C09_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C10_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C11_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C12_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C16_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C17_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C18_D,PULSELOW,EMPTY_SLOT]]],"
                + "DebugAction[SensorExpr[PinGetValueSensor[S_C19_D,PULSELOW,EMPTY_SLOT]]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_A1_A,portName=S_A1_A,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C16_D,portName=S_C16_D,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C17_D,portName=S_C17_D,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C18_D,portName=S_C18_D,componentProperties={PIN1=C18}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C19_D,portName=S_C19_D,componentProperties={PIN1=C19}],",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_P1_A,portName=S_P1_A,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_P1_D,portName=S_P1_D,componentProperties={PIN1=1}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_P2_D,portName=S_P2_D,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_P2_A,portName=S_P2_A,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_P0_D,portName=S_P0_D,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_C04_A,portName=S_C04_A,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_C05_A,portName=S_C05_A,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_P3_D,portName=S_P3_D,componentProperties={PIN1=3}]",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_C06_A,portName=S_C06_A,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C04_D,portName=S_C04_D,componentProperties={PIN1=C04}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C05_D,portName=S_C05_D,componentProperties={PIN1=C05}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C06_D,portName=S_C06_D,componentProperties={PIN1=C06}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C07_D,portName=S_C07_D,componentProperties={PIN1=C07}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C08_D,portName=S_C08_D,componentProperties={PIN1=C08}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C09_D,portName=S_C09_D,componentProperties={PIN1=C09}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C10_D,portName=S_C10_D,componentProperties={PIN1=C10}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C11_D,portName=S_C11_D,componentProperties={PIN1=C11}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_C12_D,portName=S_C12_D,componentProperties={PIN1=C12}],",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_C16_A,portName=S_C16_A,componentProperties={PIN1=C16}],",
                "ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S_C17_A,portName=S_C17_A,componentProperties={PIN1=C17}],",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_A1_D,portName=S_A1_D,componentProperties={PIN1=5}]",
                "ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S_A0_D,portName=S_A0_D,componentProperties={PIN1=4}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/calliope/old_pins_sensor.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedLedbar_WhenGivenOldLedbar() {
        String expectedProgramAst = "BlockAST[project=[[Location[x=512,y=50],MainTask[]," + "LedBarSetAction[NumConst[0],NumConst[5]]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=LEDBAR,isActor=true,userDefinedName=LL,portName=LL,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper.setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_ledbar.xml")).setConfigurationAst(configuration).build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedFourdigitdisplay_WhenGivenOldFourdigitdisplay() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "FourDigitDisplayShowAction[NumConst[1234],NumConst[0],BoolConst[true]],"
                + "FourDigitDisplayClearAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=FOURDIGITDISPLAY,isActor=true,userDefinedName=FDD,portName=FDD,componentProperties={PIN1=5}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_fourdigitdisplay.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotor_WhenGivenOldSingleMotor() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "MotorOnAction[M_A,MotionParam[speed=NumConst[30],duration=null]],"
                + "MotorStop[port=M_A,mode=FLOAT],"
                + "MotorStop[port=M_A,mode=NONFLOAT],"
                + "MotorStop[port=M_A,mode=SLEEP]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_A,portName=M_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_single_motor_on_stop.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotors_WhenGivenOldMotors() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "BothMotorsOnAction[M_A,NumConst[30],M_B,NumConst[10]],"
                + "BothMotorsOnAction[CalliBot_links,NumConst[30],CalliBot_rechts,NumConst[10]],"
                + "SingleMotorStopAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_B,portName=M_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_links,portName=CalliBot_links,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_rechts,portName=CalliBot_rechts,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_A,portName=M_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_motors_on_stop.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotorSingle_WhenGivenOldMotorSingle() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "MotorOnAction[M_A,MotionParam[speed=NumConst[30],duration=null]],"
                + "MotorOnAction[M_B,MotionParam[speed=NumConst[30],duration=null]],"
                + "MotorOnAction[CalliBot_links,MotionParam[speed=NumConst[30],duration=null]],"
                + "MotorOnAction[CalliBot_rechts,MotionParam[speed=NumConst[30],duration=null]],"
                + "MotorStop[port=M_A],"
                + "MotorStop[port=M_B],"
                + "MotorStop[port=CalliBot_links],"
                + "MotorStop[port=CalliBot_rechts]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_B,portName=M_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_links,portName=CalliBot_links,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_rechts,portName=CalliBot_rechts,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_A,portName=M_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_motor_on_stop_single.xml"))
                .setConfigurationAst(configuration)
                .build();

        new MbedTwo2ThreeTransformerWorker().execute(project);
        UnitTestHelper.checkAstEquality(project.getProgramAst().getTree().toString(), expectedProgramAst);
        UnitTestHelper.checkAstContains(project.getConfigurationAst().getConfigurationComponentsValues().toString(), expectedToBeInConfigAst);
    }

    @Test
    public void executeTransformer_ShouldReturnTransformedMotors_WhenGivenOldMotorDouble() {
        String expectedProgramAst =
            "BlockAST[project=[[Location[x=512,y=50],MainTask[],"
                + "BothMotorsOnAction[M_A,NumConst[30],M_B,NumConst[30]],"
                + "BothMotorsOnAction[CalliBot_links,NumConst[30],CalliBot_rechts,NumConst[30]],"
                + "SingleMotorStopAction[],"
                + "SingleMotorStopAction[]]]]";
        String[] expectedToBeInConfigAst =
            {
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_B,portName=M_B,componentProperties={PIN1=B}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_links,portName=CalliBot_links,componentProperties={PIN1=0}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=CalliBot_rechts,portName=CalliBot_rechts,componentProperties={PIN1=2}]",
                "ConfigurationComponent[componentType=MOTOR,isActor=true,userDefinedName=M_A,portName=M_A,componentProperties={PIN1=A}]"
            };

        Project project =
            UnitTestHelper
                .setupWithProgramXML(testFactory, Util.readResourceContent("/transform/old_motor_on_stop_double.xml"))
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

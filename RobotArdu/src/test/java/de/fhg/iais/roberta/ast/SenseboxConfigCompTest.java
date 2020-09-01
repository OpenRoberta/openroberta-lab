package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SenseboxConfigCompTest extends AstTest {

    @Test
    public void senseBoxBrick_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SENSEBOX,isActor=true,userDefinedName=,portName=null,componentProperties={NAME1=ID1,ID1=}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robBrick_senseBox-Brick.xml");
    }

    @Test
    public void senseBoxBrick_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robBrick_senseBox-Brick.xml");
    }

    @Test
    public void accelerometer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ACCELEROMETER,isActor=true,userDefinedName=B,portName=B,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_accelerometer.xml");
    }

    @Test
    public void accelerometer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_accelerometer.xml");
    }

    @Test
    public void analogin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A2,portName=A2,componentProperties={INPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_analogin.xml");
    }

    @Test
    public void analogin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_analogin.xml");
    }

    @Test
    public void analogout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S4,portName=S4,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_analogout.xml");
    }

    @Test
    public void analogout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_analogout.xml");
    }

    @Test
    public void buzzer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=S,portName=S,componentProperties={+=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_buzzer.xml");
    }

    @Test
    public void buzzer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_buzzer.xml");
    }

    @Test
    public void digitalin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A,portName=A,componentProperties={INPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_digitalin.xml");
    }

    @Test
    public void digitalin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_digitalin.xml");
    }

    @Test
    public void digitalout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S3,portName=S3,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_digitalout.xml");
    }

    @Test
    public void digitalout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_digitalout.xml");
    }

    @Test
    public void gps_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=GPS,isActor=true,userDefinedName=G3,portName=G3,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_gps.xml");
    }

    @Test
    public void gps_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_gps.xml");
    }

    @Test
    public void humidity_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=HUMIDITY,isActor=true,userDefinedName=L3,portName=L3,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_humidity.xml");
    }

    @Test
    public void humidity_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_humidity.xml");
    }

    @Test
    public void key_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=T,portName=T,componentProperties={PIN1=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_key.xml");
    }

    @Test
    public void key_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_key.xml");
    }

    @Test
    public void lcdi2c_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LCDI2C,isActor=true,userDefinedName=myDisplay,portName=myDisplay,componentProperties={TITLE=P,XLABEL=X,YLABEL=Y,XSTART=0,XEND=100,YSTART=0,YEND=50,XTICK=10,YTICK=10,I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_lcdi2c.xml");
    }

    @Test
    public void lcdi2c_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_lcdi2c.xml");
    }

    @Test
    public void led_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=G,portName=G,componentProperties={INPUT=8}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_led.xml");
    }

    @Test
    public void led_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_led.xml");
    }

    @Test
    public void light_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=L2,portName=L2,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_light.xml");
    }

    @Test
    public void light_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_light.xml");
    }

    @Test
    public void lightveml_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LIGHTVEML,isActor=true,userDefinedName=S5,portName=S5,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_lightveml.xml");
    }

    @Test
    public void lightveml_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_lightveml.xml");
    }

    @Test
    public void particle_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=PARTICLE,isActor=true,userDefinedName=S6,portName=S6,componentProperties={SERIAL=Serial1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_particle.xml");
    }

    @Test
    public void particle_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_particle.xml");
    }

    @Test
    public void potentiometer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=POTENTIOMETER,isActor=true,userDefinedName=P,portName=P,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_potentiometer.xml");
    }

    @Test
    public void potentiometer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_potentiometer.xml");
    }

    @Test
    public void rgbled_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=R2,portName=R2,componentProperties={RED=1,GREEN=2,BLUE=3}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_rgbled.xml");
    }

    @Test
    public void rgbled_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_rgbled.xml");
    }

    @Test
    public void sdcard_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SENSEBOX_SDCARD,isActor=true,userDefinedName=S2,portName=S2,componentProperties={NAO_FILENAME=FILE.TXT,Socket=XBEE2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_sdcard.xml");
    }

    @Test
    public void sdcard_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_sdcard.xml");
    }

    @Test
    public void sound_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SOUND,isActor=true,userDefinedName=G2,portName=G2,componentProperties={OUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_sound.xml");
    }

    @Test
    public void sound_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_sound.xml");
    }

    @Test
    public void temperature_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=TEMPERATURE,isActor=true,userDefinedName=T2,portName=T2,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_temperature.xml");
    }

    @Test
    public void temperature_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_temperature.xml");
    }

    @Test
    public void ultrasonic_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=U,portName=U,componentProperties={TRIG=1,ECHO=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_ultrasonic.xml");
    }

    @Test
    public void ultrasonic_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_ultrasonic.xml");
    }

    @Test
    public void wireless_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=WIRELESS,isActor=true,userDefinedName=W,portName=W,componentProperties={Socket=XBEE1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_wireless.xml");
    }

    @Test
    public void wireless_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_wireless.xml");
    }

    @Test
    public void environmental_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST[project=[ConfigurationComponent[componentType=ENVIRONMENTAL,isActor=true,userDefinedName=E,portName=E,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_environmental.xml");
    }

    @Test
    public void environmental_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_environmental.xml");
    }
}

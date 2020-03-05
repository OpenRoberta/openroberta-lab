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
            "BlockAST [project=[ConfigurationComponent[componentType=ACCELEROMETER,isActor=true,userDefinedName=B,portName=null,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_accelerometer.xml");
    }

    @Test
    public void accelerometer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_accelerometer.xml");
    }

    @Test
    public void analogin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A2,portName=null,componentProperties={INPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_analogin.xml");
    }

    @Test
    public void analogin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_analogin.xml");
    }

    @Test
    public void analogout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S4,portName=null,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_analogout.xml");
    }

    @Test
    public void analogout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_analogout.xml");
    }

    @Test
    public void buzzer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=S,portName=null,componentProperties={+=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_buzzer.xml");
    }

    @Test
    public void buzzer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_buzzer.xml");
    }

    @Test
    public void digitalin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A,portName=null,componentProperties={INPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_digitalin.xml");
    }

    @Test
    public void digitalin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_digitalin.xml");
    }

    @Test
    public void digitalout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S3,portName=null,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_digitalout.xml");
    }

    @Test
    public void digitalout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_digitalout.xml");
    }

    @Test
    public void gps_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=GPS,isActor=true,userDefinedName=G3,portName=null,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_gps.xml");
    }

    @Test
    public void gps_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_gps.xml");
    }

    @Test
    public void humidity_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=HUMIDITY,isActor=true,userDefinedName=L3,portName=null,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_humidity.xml");
    }

    @Test
    public void humidity_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_humidity.xml");
    }

    @Test
    public void key_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=T,portName=null,componentProperties={PIN1=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_key.xml");
    }

    @Test
    public void key_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_key.xml");
    }

    @Test
    public void lcdi2c_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LCDI2C,isActor=true,userDefinedName=myDisplay,portName=null,componentProperties={TITLE=P,XLABEL=X,YLABEL=Y,XSTART=0,XEND=100,YSTART=0,YEND=50,XTICK=10,YTICK=10,I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_lcdi2c.xml");
    }

    @Test
    public void lcdi2c_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_lcdi2c.xml");
    }

    @Test
    public void led_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=G,portName=null,componentProperties={INPUT=8}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_led.xml");
    }

    @Test
    public void led_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_led.xml");
    }

    @Test
    public void light_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=L2,portName=null,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_light.xml");
    }

    @Test
    public void light_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_light.xml");
    }

    @Test
    public void lightveml_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LIGHTVEML,isActor=true,userDefinedName=S5,portName=null,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_lightveml.xml");
    }

    @Test
    public void lightveml_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_lightveml.xml");
    }

    @Test
    public void particle_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=PARTICLE,isActor=true,userDefinedName=S6,portName=null,componentProperties={SERIAL=Serial1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_particle.xml");
    }

    @Test
    public void particle_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_particle.xml");
    }

    @Test
    public void potentiometer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=POTENTIOMETER,isActor=true,userDefinedName=P,portName=null,componentProperties={OUTPUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_potentiometer.xml");
    }

    @Test
    public void potentiometer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_potentiometer.xml");
    }

    @Test
    public void rgbled_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=R2,portName=null,componentProperties={RED=1,GREEN=2,BLUE=3}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_rgbled.xml");
    }

    @Test
    public void rgbled_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_rgbled.xml");
    }

    @Test
    public void sdcard_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SENSEBOX_SDCARD,isActor=true,userDefinedName=S2,portName=null,componentProperties={NAO_FILENAME=FILE.TXT,Socket=XBEE2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_sdcard.xml");
    }

    @Test
    public void sdcard_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_sdcard.xml");
    }

    @Test
    public void sound_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SOUND,isActor=true,userDefinedName=G2,portName=null,componentProperties={OUT=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_sound.xml");
    }

    @Test
    public void sound_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_sound.xml");
    }

    @Test
    public void temperature_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=TEMPERATURE,isActor=true,userDefinedName=T2,portName=null,componentProperties={I2C=I2C}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_temperature.xml");
    }

    @Test
    public void temperature_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_temperature.xml");
    }

    @Test
    public void ultrasonic_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ULTRASONIC,isActor=true,userDefinedName=U,portName=null,componentProperties={TRIG=1,ECHO=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_ultrasonic.xml");
    }

    @Test
    public void ultrasonic_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_ultrasonic.xml");
    }

    @Test
    public void wireless_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=WIRELESS,isActor=true,userDefinedName=W,portName=null,componentProperties={Socket=XBEE1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/sensebox/robConf_wireless.xml");
    }

    @Test
    public void wireless_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/sensebox/robConf_wireless.xml");
    }
}

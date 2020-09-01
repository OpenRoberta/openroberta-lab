package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ArduinoConfigCompTest extends AstTest {

    @Test
    public void accelerometer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ACCELEROMETER,isActor=true,userDefinedName=B,portName=B,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_accelerometer.xml");
    }

    @Test
    public void accelerometer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_accelerometer.xml");
    }

    @Test
    public void analogin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_INPUT,isActor=true,userDefinedName=A2,portName=A2,componentProperties={INPUT=3}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_analogin.xml");
    }

    @Test
    public void analogin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_analogin.xml");
    }

    @Test
    public void analogout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=ANALOG_PIN,isActor=true,userDefinedName=S5,portName=S5,componentProperties={OUTPUT=A0}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_analogout.xml");
    }

    @Test
    public void analogout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_analogout.xml");
    }

    @Test
    public void buzzer_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=BUZZER,isActor=true,userDefinedName=S3,portName=S3,componentProperties={+=5}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_buzzer.xml");
    }

    @Test
    public void buzzer_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_buzzer.xml");
    }

    @Test
    public void digitalin_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_INPUT,isActor=true,userDefinedName=A,portName=A,componentProperties={INPUT=0}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_digitalin.xml");
    }

    @Test
    public void digitalin_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_digitalin.xml");
    }

    @Test
    public void digitalout_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=DIGITAL_PIN,isActor=true,userDefinedName=S4,portName=S4,componentProperties={OUTPUT=0}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_digitalout.xml");
    }

    @Test
    public void digitalout_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_digitalout.xml");
    }

    @Test
    public void gyro_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=GYRO,isActor=true,userDefinedName=K,portName=K,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_gyro.xml");
    }

    @Test
    public void gyro_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_gyro.xml");
    }

    @Test
    public void humidity_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=HUMIDITY,isActor=true,userDefinedName=L5,portName=L5,componentProperties={OUTPUT=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_humidity.xml");
    }

    @Test
    public void humidity_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_humidity.xml");
    }

    @Test
    public void infrared_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=INFRARED,isActor=true,userDefinedName=I,portName=I,componentProperties={OUTPUT=11}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_infrared.xml");
    }

    @Test
    public void infrared_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_infrared.xml");
    }

    @Test
    public void key_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=KEY,isActor=true,userDefinedName=T,portName=T,componentProperties={PIN1=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_key.xml");
    }

    @Test
    public void key_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_key.xml");
    }

    @Test
    public void lcd_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LCD,isActor=true,userDefinedName=L2,portName=L2,componentProperties={RS=12,E=11,D4=5,D5=4,D6=3,D7=2}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_lcd.xml");
    }

    @Test
    public void lcd_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_lcd.xml");
    }

    @Test
    public void lcdi2c_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LCDI2C,isActor=true,userDefinedName=L3,portName=L3,componentProperties={}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_lcdi2c.xml");
    }

    @Test
    public void lcdi2c_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_lcdi2c.xml");
    }

    @Test
    public void led_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LED,isActor=true,userDefinedName=L,portName=L,componentProperties={INPUT=LED_BUILTIN}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_led.xml");
    }

    @Test
    public void led_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_led.xml");
    }

    @Test
    public void light_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=LIGHT,isActor=true,userDefinedName=L4,portName=L4,componentProperties={OUTPUT=A0}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_light.xml");
    }

    @Test
    public void light_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_light.xml");
    }

    @Test
    public void motion_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=MOTION,isActor=true,userDefinedName=B,portName=B,componentProperties={OUTPUT=7}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_motion.xml");
    }

    @Test
    public void motion_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_motion.xml");
    }

    @Test
    public void relay_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=RELAY,isActor=true,userDefinedName=R,portName=R,componentProperties={IN=6}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_relay.xml");
    }

    @Test
    public void relay_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_relay.xml");
    }

    @Test
    public void rgbled_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=RGBLED,isActor=true,userDefinedName=R2,portName=R2,componentProperties={RED=5,GREEN=6,BLUE=3}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_rgbled.xml");
    }

    @Test
    public void rgbled_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_rgbled.xml");
    }

    @Test
    public void servo_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=SERVOMOTOR,isActor=true,userDefinedName=S,portName=S,componentProperties={PULSE=1}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_servo.xml");
    }

    @Test
    public void servo_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_servo.xml");
    }

    @Test
    public void stepmotor_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=STEPMOTOR,isActor=true,userDefinedName=S2,portName=S2,componentProperties={IN1=6,IN2=5,IN3=4,IN4=3}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_stepmotor.xml");
    }

    @Test
    public void stepmotor_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_stepmotor.xml");
    }

    @Test
    public void temperature_make_ByDefault_ReturnInstanceOfConfigurationComponentClass() {
        String expectedResult =
            "BlockAST [project=[ConfigurationComponent[componentType=TEMPERATURE,isActor=true,userDefinedName=T2,portName=T2,componentProperties={OUTPUT=A0}]]]";
        UnitTestHelper.checkConfigAstEquality(testFactory, expectedResult, "/ast/config/arduino/robConf_temperature.xml");
    }

    @Test
    public void temperature_astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkConfigReverseTransformation(testFactory, "/ast/config/arduino/robConf_temperature.xml");
    }
}

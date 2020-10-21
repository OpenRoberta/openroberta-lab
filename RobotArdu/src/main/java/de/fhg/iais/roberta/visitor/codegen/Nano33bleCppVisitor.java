package de.fhg.iais.roberta.visitor.codegen;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a StringBuilder.
 * <b>This class generates C++ code for the Arduino Nano 33 BLE.</b> <br>
 */
public class Nano33bleCppVisitor extends AbstractCommonArduinoCppVisitor implements IArduinoVisitor<Void> {
    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public Nano33bleCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("_lcd_" + showTextAction.getPort() + ".setCursor(");
        showTextAction.getX().accept(this);
        this.sb.append(",");
        showTextAction.getY().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("_lcd_" + showTextAction.getPort() + ".print(");
        showTextAction.getMsg().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("_lcd_" + clearDisplayAction.getPort() + ".clear();");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( !lightAction.getMode().toString().equals(BlocklyConstants.DEFAULT) ) {
            this.sb.append("digitalWrite(_led_" + lightAction.getPort() + ", " + lightAction.getMode().getValues()[0] + ");");
        } else {
            Map<String, Object> channels = new LinkedHashMap<>();
            String redName;
            String greenName;
            String blueName;
            if ( this.configuration.getRobotName().equals("unowifirev2")
                && isInternalRgbLed(this.configuration.getConfigurationComponent(lightAction.getPort())) ) {
                redName = "WiFiDrv::analogWrite(25";
                greenName = "WiFiDrv::analogWrite(26";
                blueName = "WiFiDrv::analogWrite(27";
            } else {
                redName = "analogWrite(_led_red_" + lightAction.getPort();
                greenName = "analogWrite(_led_green_" + lightAction.getPort();
                blueName = "analogWrite(_led_blue_" + lightAction.getPort();
            }
            if ( lightAction.getRgbLedColor().getClass().equals(ColorConst.class) ) {
                String hexValue = ((ColorConst<Void>) lightAction.getRgbLedColor()).getHexValueAsString();
                hexValue = hexValue.split("#")[1];
                channels.put(redName, String.valueOf(Integer.decode("0x" + hexValue.substring(0, 2))));
                channels.put(greenName, String.valueOf(Integer.decode("0x" + hexValue.substring(2, 4))));
                channels.put(blueName, String.valueOf(Integer.decode("0x" + hexValue.substring(4, 6))));
            } else if ( lightAction.getRgbLedColor().getClass().equals(Var.class) ) {
                String tempVarName = "___" + ((Var<Void>) lightAction.getRgbLedColor()).getValue();
                channels.put(redName, "RCHANNEL(" + tempVarName + ")");
                channels.put(greenName, "GCHANNEL(" + tempVarName + ")");
                channels.put(blueName, "BCHANNEL(" + tempVarName + ")");
            } else {
                channels.put(redName, ((RgbColor<Void>) lightAction.getRgbLedColor()).getR());
                channels.put(greenName, ((RgbColor<Void>) lightAction.getRgbLedColor()).getG());
                channels.put(blueName, ((RgbColor<Void>) lightAction.getRgbLedColor()).getB());
            }
            channels.forEach((name, v) -> {
                this.sb.append(name).append(", ");
                if ( v instanceof Phrase ) {
                    ((Phrase<Void>) v).accept(this);
                } else {
                    this.sb.append(v);
                }
                this.sb.append(");");
                nlIndent();
            });
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String[] colors =
            {
                "red",
                "green",
                "blue"
            };
        for ( int i = 0; i < 3; i++ ) {
            this.sb.append("analogWrite(_led_" + colors[i] + "_" + lightStatusAction.getPort() + ", 0);");
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //9 - sound port
        this.sb.append("tone(_buzzer_").append(toneAction.getPort()).append(", ");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("delay(");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        boolean step = motorOnAction.getParam().getDuration() != null;
        if ( step ) {//step motor
            this.sb.append("_stepper_" + motorOnAction.getUserDefinedPort() + ".setSpeed(");
            motorOnAction.getParam().getSpeed().accept(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_stepper_" + motorOnAction.getUserDefinedPort() + ".step(_SPU_" + motorOnAction.getUserDefinedPort() + "*(");
            motorOnAction.getDurationValue().accept(this);
            this.sb.append(")");
            if ( motorOnAction.getDurationMode().equals(MotorMoveMode.DEGREE) ) {
                this.sb.append("/360");
            }
            this.sb.append(");");
        } else {//servo motor
            this.sb.append("_servo_" + motorOnAction.getUserDefinedPort() + ".write(");
            motorOnAction.getParam().getSpeed().accept(this);
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        this.sb.append("digitalWrite(_relay_").append(relayAction.getPort()).append(", ").append(relayAction.getMode().getValues()[0]).append(");");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("analogRead(_output_" + lightSensor.getPort() + ")/10.24");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> button) {
        this.sb.append("digitalRead(_taster_" + button.getPort() + ")");
        return null;
    }

    public void createDistanceUltrasonicSensor() {
        this.sb.append("double _getUltrasonicDistance(int trigger, int echo)");
        nlIndent();
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        this.sb.append("digitalWrite(trigger, LOW);");
        nlIndent();
        this.sb.append("delay(5);");
        nlIndent();
        this.sb.append("digitalWrite(trigger, HIGH);");
        nlIndent();
        this.sb.append("delay(10);");
        nlIndent();
        this.sb.append("digitalWrite(trigger, LOW);");
        nlIndent();
        this.sb.append("return pulseIn(echo, HIGH) * 0.03432/2;");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb
            .append("_getUltrasonicDistance(_trigger_")
            .append(ultrasonicSensor.getPort())
            .append(", ")
            .append("_echo_")
            .append(ultrasonicSensor.getPort())
            .append(")");
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        this.sb.append("analogRead(_moisturePin_" + moistureSensor.getPort() + ")/10.24");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("map(analogRead(_TMP36_" + temperatureSensor.getPort() + "), 0, 410, -50, 150)");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> potentiometer) {
        this.sb.append("((double)analogRead(_output_" + potentiometer.getPort() + "))*5/1024");
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        switch ( humiditySensor.getMode() ) {
            case SC.HUMIDITY:
                this.sb.append("_dht_" + humiditySensor.getPort() + ".readHumidity()");
                break;
            case SC.TEMPERATURE:
                this.sb.append("_dht_" + humiditySensor.getPort() + ".readTemperature()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!");
        }
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        this.sb.append("analogRead(_S_" + dropSensor.getPort() + ")/10.24");
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        this.sb.append("analogRead(_SensorPin_" + pulseSensor.getPort() + ")");
        return null;
    }

    public Void readRFIDData(String sensorName) {
        this.sb.append("String _readRFIDData(MFRC522 &mfrc522) {");
        incrIndentation();
        nlIndent();
        this.sb.append("if (!mfrc522.PICC_IsNewCardPresent()) {");
        incrIndentation();
        nlIndent();
        this.sb.append("return \"N/A\";");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        this.sb.append("if (!mfrc522.PICC_ReadCardSerial()) {");
        incrIndentation();
        nlIndent();
        this.sb.append("return \"N/D\";");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        this.sb.append("return String(((long)(mfrc522.uid.uidByte[0])<<24)");
        incrIndentation();
        nlIndent();
        this.sb.append("| ((long)(mfrc522.uid.uidByte[1])<<16)");
        nlIndent();
        this.sb.append("| ((long)(mfrc522.uid.uidByte[2])<<8)");
        nlIndent();
        this.sb.append("| ((long)(mfrc522.uid.uidByte[3]), HEX));");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        return null;

    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
            switch ( rfidSensor.getMode() ) {
                case SC.PRESENCE:
                    this.sb.append("_mfrc522_" + rfidSensor.getPort() + ".PICC_IsNewCardPresent()");
                    break;
                case SC.IDONE:
                    this.sb.append("_readRFIDData(_mfrc522_").append(rfidSensor.getPort()).append(")");
                    break;
                default:
                    throw new DbcException("Invalide mode for RFID Sensor!");
            }
        }
        return null;
    }

    public void createMeasureIRSensor() {
        this.sb.append("bool _getIRPresence(IRrecv &irrecv) {");
        incrIndentation();
        nlIndent();
        this.sb.append("decode_results results;");
        nlIndent();
        this.sb.append("if (irrecv.decode(&results)) {");
        incrIndentation();
        nlIndent();
        this.sb.append("irrecv.resume();");
        nlIndent();
        this.sb.append("return true;");
        decrIndentation();
        nlIndent();
        this.sb.append("} else {");
        incrIndentation();
        nlIndent();
        this.sb.append("return false;");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        nlIndent();
        this.sb.append("long int  _getIRValue(IRrecv &irrecv) {");
        incrIndentation();
        nlIndent();
        this.sb.append("decode_results results;");
        nlIndent();
        this.sb.append("if (irrecv.decode(&results)) {");
        incrIndentation();
        nlIndent();
        this.sb.append("long int tmpValue = results.value;");
        nlIndent();
        this.sb.append("irrecv.resume();");
        nlIndent();
        this.sb.append("return tmpValue;");
        decrIndentation();
        nlIndent();
        this.sb.append("} else {");
        incrIndentation();
        nlIndent();
        this.sb.append("return 0;");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        switch ( infraredSensor.getMode() ) {
            case SC.PRESENCE:
                this.sb.append("_getIRPresence(_irrecv_").append(infraredSensor.getPort()).append(")");
                break;
            case SC.VALUE:
                this.sb.append("_getIRValue(_irrecv_").append(infraredSensor.getPort()).append(")");
                break;
            default:
                throw new DbcException(infraredSensor.getKind().getName() + " mode is not supported: " + infraredSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("digitalRead(_output_" + motionSensor.getPort() + ")");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.sb.append("_imu_").append(accelerometerSensor.getPort()).append(".readFloatAccel").append(accelerometerSensor.getMode()).append("()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.sb.append("_imu_").append(gyroSensor.getPort()).append(".readFloatGyro").append(gyroSensor.getMode()).append("()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {

        mainTask.getVariables().accept(this);
        nlIndent();
        generateConfigurationVariables();
        generateTimerVariables();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( (this.configuration.getConfigurationComponents().isEmpty() || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER)) && numberConf == 0 ) {
            nlIndent();
        }
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            if ( usedSensor.getType().equals(SC.INFRARED) ) {
                nlIndent();
                createMeasureIRSensor();
                nlIndent();
                break;
            }
        }
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            if ( usedConfigurationBlock.getComponentType().equals(SC.ULTRASONIC) ) {
                createDistanceUltrasonicSensor();
                nlIndent();
                break;
            }
        }
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            if ( usedConfigurationBlock.getComponentType().equals(SC.RFID) && !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                readRFIDData(usedConfigurationBlock.getUserDefinedPortName());
                nlIndent();
                break;
            }
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.sb.append("void setup()");
        nlIndent();
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.sb.append("Serial.begin(9600);");
            nlIndent();
        }
        generateConfigurationSetup();
        generateUsedVars();
        this.sb.delete(this.sb.lastIndexOf("\n"), this.sb.length());
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        } else {
            decrIndentation();
        }
        this.sb.append("// This file is automatically generated by the Open Roberta Lab.");
        nlIndent();
        nlIndent();
        this.sb.append("#define _ARDUINO_STL_NOT_NEEDED");
        nlIndent();
        this.sb.append("#include <Arduino.h>");
        nlIndent();
        nlIndent();
        LinkedHashSet<String> headerFiles = new LinkedHashSet<>();
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.HUMIDITY:
                    headerFiles.add("#include <DHT_sensor_library/DHT.h>");
                    break;
                case SC.INFRARED:
                    headerFiles.add("#include <IRremote/src/IRremote.h>");
                    break;
                case SC.RFID:
                    if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                        headerFiles.add("#include <MFRC522/src/MFRC522.h>");
                    }
                    break;
                case SC.LCD:
                    headerFiles.add("#include <LiquidCrystal/src/LiquidCrystal.h>");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    headerFiles.add("#include <LiquidCrystal_I2C/LiquidCrystal_I2C.h>");
                    break;
                case SC.STEPMOTOR:
                    headerFiles.add("#include <Stepper/src/Stepper.h>");
                    break;
                case SC.SERVOMOTOR:
                    headerFiles.add("#include <Servo/src/Servo.h>");
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    headerFiles.add("#include <SparkFun_LSM6DS3_Breakout/src/SparkFunLSM6DS3.h>");
                    break;
                case SC.RGBLED:
                    if ( this.configuration.getRobotName().equals("unowifirev2") && isInternalRgbLed(usedConfigurationBlock) ) {
                        headerFiles.add("#include <WiFiNINA.h>");
                        headerFiles.add("#include <utility/wifi_drv.h>");
                    }
                    break;
                case SC.ULTRASONIC:
                case SC.MOTION:
                case SC.MOISTURE:
                case SC.KEY:
                case SC.LIGHT:
                case SC.POTENTIOMETER:
                case SC.TEMPERATURE:
                case SC.DROP:
                case SC.PULSE:
                case SC.LED:
                case SC.BUZZER:
                case SC.RELAY:
                case SC.DIGITAL_INPUT:
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_PIN:
                case SC.ANALOG_PIN:
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
        for ( String header : headerFiles ) {
            this.sb.append(header);
            nlIndent();
        }
        this.sb.append("#include <NEPODefs.h>");
        nlIndent();

        super.generateProgramPrefix(withWrapping);
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.HUMIDITY:
                    this.sb.append("_dht_" + usedConfigurationBlock.getUserDefinedPortName() + ".begin();");
                    nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.sb.append("pinMode(_trigger_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                    nlIndent();
                    this.sb.append("pinMode(_echo_" + usedConfigurationBlock.getUserDefinedPortName() + ", INPUT);");
                    nlIndent();
                    break;
                case SC.MOTION:
                    this.sb.append("pinMode(_output_" + usedConfigurationBlock.getUserDefinedPortName() + ", INPUT);");
                    nlIndent();
                    break;
                case SC.MOISTURE:
                    break;
                case SC.INFRARED:
                    this.sb.append("pinMode(13, OUTPUT);");
                    nlIndent();
                    this.sb.append("_irrecv_" + usedConfigurationBlock.getUserDefinedPortName() + ".enableIRIn();");
                    nlIndent();
                    break;
                case SC.KEY:
                    this.sb.append("pinMode(_taster_" + usedConfigurationBlock.getUserDefinedPortName() + ", INPUT);");
                    nlIndent();
                case SC.LIGHT:
                    break;
                case SC.POTENTIOMETER:
                    break;
                case SC.TEMPERATURE:
                    break;
                case SC.DROP:
                    break;
                case SC.PULSE:
                    break;
                case SC.RFID:
                    if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                        this.sb.append("SPI.begin();");
                        nlIndent();
                        this.sb.append("_mfrc522_" + usedConfigurationBlock.getUserDefinedPortName() + ".PCD_Init();");
                        nlIndent();
                    }
                    break;
                case SC.LCD:
                    this.sb.append("_lcd_" + usedConfigurationBlock.getUserDefinedPortName() + ".begin(16, 2);");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    this.sb.append("_lcd_" + usedConfigurationBlock.getUserDefinedPortName() + ".begin();");
                    nlIndent();
                    break;
                case SC.LED:
                    this.sb.append("pinMode(_led_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    if ( this.configuration.getRobotName().equals("unowifirev2") && isInternalRgbLed(usedConfigurationBlock) ) {
                        this.sb.append("WiFiDrv::pinMode(25, OUTPUT);");
                        nlIndent();
                        this.sb.append("WiFiDrv::pinMode(26, OUTPUT);");
                        nlIndent();
                        this.sb.append("WiFiDrv::pinMode(27, OUTPUT);");
                        nlIndent();
                    } else {
                        this.sb.append("pinMode(_led_red_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                        nlIndent();
                        this.sb.append("pinMode(_led_green_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                        nlIndent();
                        this.sb.append("pinMode(_led_blue_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                        nlIndent();
                    }
                    break;
                case SC.BUZZER:
                    break;
                case SC.RELAY:
                    this.sb.append("pinMode(_relay_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    break;
                case SC.SERVOMOTOR:
                    this.sb
                        .append("_servo_" + usedConfigurationBlock.getUserDefinedPortName() + ".attach(" + usedConfigurationBlock.getProperty(SC.PULSE) + ");");
                    nlIndent();
                    break;
                case SC.DIGITAL_PIN:
                case SC.ANALOG_PIN:
                    this.sb.append("pinMode(_input_" + usedConfigurationBlock.getUserDefinedPortName() + ", INPUT);");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.sb.append("pinMode(_output_" + usedConfigurationBlock.getUserDefinedPortName() + ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    this.sb.append("_imu_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin();");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.HUMIDITY:
                    this.sb.append("#define DHTPIN" + blockName + " ").append(cc.getProperty("OUTPUT"));
                    nlIndent();
                    this.sb.append("#define DHTTYPE DHT11");
                    nlIndent();
                    this.sb.append("DHT _dht_" + blockName + "(DHTPIN" + blockName + ", DHTTYPE);");
                    nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.sb.append("int _trigger_" + blockName + " = ").append(cc.getProperty("TRIG")).append(";");
                    nlIndent();
                    this.sb.append("int _echo_" + blockName + " = ").append(cc.getProperty("ECHO")).append(";");
                    nlIndent();
                    break;
                case SC.MOISTURE:
                    this.sb.append("int _moisturePin_" + blockName + " = ").append(cc.getProperty("S")).append(";");
                    nlIndent();
                    break;
                case SC.INFRARED:
                    this.sb.append("IRrecv _irrecv_").append(blockName).append("(").append(cc.getProperty("OUTPUT")).append(");");
                    nlIndent();
                    break;
                case SC.LIGHT:
                    this.sb.append("int _output_" + blockName + " = ").append(cc.getProperty("OUTPUT")).append(";");
                    nlIndent();
                    break;
                case SC.MOTION:
                    this.sb.append("int _output_" + blockName + " = ").append(cc.getProperty("OUTPUT")).append(";");
                    nlIndent();
                    break;
                case SC.POTENTIOMETER:
                    this.sb.append("int _output_" + blockName + " = ").append(cc.getProperty("OUTPUT")).append(";");
                    nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.sb.append("int _TMP36_" + blockName + " = ").append(cc.getProperty("OUTPUT")).append(";");
                    nlIndent();
                    break;
                case SC.DROP:
                    this.sb.append("int _S_" + blockName + " = ").append(cc.getProperty("S")).append(";");
                    nlIndent();
                    break;
                case SC.PULSE:
                    this.sb.append("int _SensorPin_" + blockName + " = ").append(cc.getProperty("S")).append(";");
                    nlIndent();
                    break;
                case SC.RFID:
                    if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                        this.sb.append("#define SS_PIN_" + blockName + " " + cc.getProperty("SDA"));
                        nlIndent();
                        this.sb.append("#define RST_PIN_" + blockName + " " + cc.getProperty("RST"));
                        nlIndent();
                        this.sb.append("MFRC522 _mfrc522_" + blockName + "(SS_PIN_" + blockName + ", RST_PIN_" + blockName + ");");
                        nlIndent();
                    }
                    break;
                case SC.KEY:
                    this.sb.append("int _taster_" + blockName + " = ").append(cc.getProperty("PIN1")).append(";");
                    nlIndent();
                    break;
                case SC.LCD:
                    this.sb
                        .append("LiquidCrystal _lcd_" + blockName + "(")
                        .append(cc.getProperty("RS"))
                        .append(", ")
                        .append(cc.getProperty("E"))
                        .append(", ")
                        .append(cc.getProperty("D4"))
                        .append(", ")
                        .append(cc.getProperty("D5"))
                        .append(", ")
                        .append(cc.getProperty("D6"))
                        .append(", ")
                        .append(cc.getProperty("D7"))
                        .append(");");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    String address = cc.getOptProperty("ADDRESS");
                    // TODO check if this is still needed after we rework the configuration back-transformation
                    if ( address == null ) {
                        address = "0x27";
                    }
                    this.sb.append("LiquidCrystal_I2C _lcd_").append(blockName).append("(").append(address).append(", 16, 2);");
                    nlIndent();
                    break;
                case SC.LED:
                    this.sb.append("int _led_" + blockName + " = ").append(cc.getProperty("INPUT")).append(";");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    this.sb.append("int _led_red_" + blockName + " = ").append(cc.getProperty(SC.RED)).append(";");
                    nlIndent();
                    this.sb.append("int _led_green_" + blockName + " = ").append(cc.getProperty(SC.GREEN)).append(";");
                    nlIndent();
                    this.sb.append("int _led_blue_" + blockName + " = ").append(cc.getProperty(SC.BLUE)).append(";");
                    nlIndent();
                    break;
                case SC.BUZZER:
                    this.sb.append("int _buzzer_" + blockName + " = ").append(cc.getProperty("+")).append(";");
                    nlIndent();
                    break;
                case SC.RELAY:
                    this.sb.append("int _relay_" + blockName + " = ").append(cc.getProperty("IN")).append(";");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.sb.append("int _SPU_" + blockName + " = ").append("2048;"); //TODO: change 2048 to customized
                    nlIndent();
                    this.sb
                        .append("Stepper _stepper_" + blockName + "(_SPU_" + blockName + ", ")
                        .append(cc.getProperty("IN1"))
                        .append(", ")
                        .append(cc.getProperty("IN3"))
                        .append(", ")
                        .append(cc.getProperty("IN2"))
                        .append(", ")
                        .append(cc.getProperty("IN4"))
                        .append(");");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    this.sb.append("Servo _servo_" + blockName + ";");
                    nlIndent();
                    break;
                case SC.ANALOG_PIN:
                case SC.DIGITAL_PIN:
                    this.sb.append("int _input_").append(blockName).append(" = ").append(cc.getProperty("OUTPUT")).append(";");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.sb.append("int _output_").append(blockName).append(" = ").append(cc.getProperty("INPUT")).append(";");
                    nlIndent();
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    this.sb.append("LSM6DS3 _imu_").append(blockName).append("(SPI_MODE, SPIIMU_SS);");
                    nlIndent();
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        switch ( pinWriteValueAction.getMode() ) {
            case SC.ANALOG:
                this.sb.append("analogWrite(_output_").append(pinWriteValueAction.getPort()).append(", (int)");
                pinWriteValueAction.getValue().accept(this);
                this.sb.append(");");
                break;
            case SC.DIGITAL:
                this.sb.append("digitalWrite(_output_").append(pinWriteValueAction.getPort()).append(", (int)");
                pinWriteValueAction.getValue().accept(this);
                this.sb.append(");");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        switch ( pinGetValueSensor.getMode() ) {
            case SC.ANALOG:
                this.sb.append("analogRead(_input_").append(pinGetValueSensor.getPort()).append(")");
                break;
            case SC.DIGITAL:
                this.sb.append("digitalRead(_input_").append(pinGetValueSensor.getPort()).append(")");
                break;
            default:
                break;
        }
        return null;
    }

    private static boolean isInternalRgbLed(ConfigurationComponent cc) {
        return cc.getProperty(SC.RED).equals(SC.LED_BUILTIN)
            || cc.getProperty(SC.GREEN).equals(SC.LED_BUILTIN)
            || cc.getProperty(SC.BLUE).equals(SC.LED_BUILTIN);
    }
}

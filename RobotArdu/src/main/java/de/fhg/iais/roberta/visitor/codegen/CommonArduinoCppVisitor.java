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
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This class is implementing {@link IVisitor} and appends a human-readable, correct C++ representation of a phrase to a StringBuilder.<br>
 * <b>The phrases covered are the sensors/actors common to ALL arduino variants.</b>
 */
public abstract class CommonArduinoCppVisitor extends NepoArduinoCppVisitor implements IArduinoVisitor<Void> {

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param phrases to generate the code from
     */
    public CommonArduinoCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public final Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.add("_lcd_", showTextAction.port, ".setCursor(");
        showTextAction.x.accept(this);
        this.src.add(",");
        showTextAction.y.accept(this);
        this.src.add(");");
        nlIndent();
        this.src.add("_printToDisplay(", "_lcd_", showTextAction.port, ", ");
        showTextAction.msg.accept(this);
        BlocklyProperties blocklyProperties = showTextAction.getProperty();
        if ( blocklyProperties.blockType.contains("oledssd1306i2c") ) {
            this.src.add(", true");
        }
        this.src.add(");");
        nlIndent();
        return null;
    }

    @Override
    public final Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("_lcd_", clearDisplayAction.port);
        BlocklyProperties blocklyProperties = clearDisplayAction.getProperty();
        if ( blocklyProperties.blockType.contains("oledssd1306i2c") ) {
            this.src.add(".clearDisplay();");
            nlIndent();
            this.src.add("_lcd_", clearDisplayAction.port, (".display();"));
        } else {
            this.src.add(".clear();");
        }
        return null;
    }

    @Override
    public final Void visitLedAction(LedAction ledAction) {
        String mode = transformOnOff2HighLow(ledAction.mode);
        this.src.add("digitalWrite(_led_", ledAction.port, ", ", mode, ");");
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        Map<String, Object> channels = new LinkedHashMap<>();
        String redName;
        String greenName;
        String blueName;
        if ( this.configuration.getRobotName().equals("unowifirev2")
            && CommonArduinoCppVisitor.isInternalRgbLed(this.configuration.getConfigurationComponent(rgbLedOnAction.port)) ) {
            redName = "WiFiDrv::analogWrite(25";
            greenName = "WiFiDrv::analogWrite(26";
            blueName = "WiFiDrv::analogWrite(27";
        } else {
            redName = "analogWrite(_led_red_" + rgbLedOnAction.port;
            greenName = "analogWrite(_led_green_" + rgbLedOnAction.port;
            blueName = "analogWrite(_led_blue_" + rgbLedOnAction.port;
        }
        if ( rgbLedOnAction.colour.getClass().equals(ColorConst.class) ) {
            String hexValue = ((ColorConst) rgbLedOnAction.colour).getHexValueAsString();
            hexValue = hexValue.split("#")[1];
            channels.put(redName, String.valueOf(Integer.decode("0x" + hexValue.substring(0, 2))));
            channels.put(greenName, String.valueOf(Integer.decode("0x" + hexValue.substring(2, 4))));
            channels.put(blueName, String.valueOf(Integer.decode("0x" + hexValue.substring(4, 6))));
        } else if ( rgbLedOnAction.colour.getClass().equals(Var.class) ) {
            String tempVarName = "___" + ((Var) rgbLedOnAction.colour).name;
            channels.put(redName, "RCHANNEL(" + tempVarName + ")");
            channels.put(greenName, "GCHANNEL(" + tempVarName + ")");
            channels.put(blueName, "BCHANNEL(" + tempVarName + ")");
        } else if ( rgbLedOnAction.colour.getClass().equals(MethodExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.src.add(tempVarName, " = ");
            visitMethodCall((MethodCall) ((MethodExpr) rgbLedOnAction.colour).method);
            this.src.add(";");
            nlIndent();
            channels.put(redName, "RCHANNEL(" + tempVarName + ")");
            channels.put(greenName, "GCHANNEL(" + tempVarName + ")");
            channels.put(blueName, "BCHANNEL(" + tempVarName + ")");
        } else if ( rgbLedOnAction.colour.getClass().equals(FunctionExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.src.add(tempVarName, " = ");
            ((FunctionExpr) rgbLedOnAction.colour).function.accept(this);
            this.src.add(";");
            nlIndent();
            channels.put(redName, "RCHANNEL(" + tempVarName + ")");
            channels.put(greenName, "GCHANNEL(" + tempVarName + ")");
            channels.put(blueName, "BCHANNEL(" + tempVarName + ")");
        } else {
            channels.put(redName, ((RgbColor) rgbLedOnAction.colour).R);
            channels.put(greenName, ((RgbColor) rgbLedOnAction.colour).G);
            channels.put(blueName, ((RgbColor) rgbLedOnAction.colour).B);
        }
        channels.forEach((name, v) -> {
            this.src.add(name, ", ");
            if ( v instanceof Phrase ) {
                ((Phrase) v).accept(this);
            } else {
                this.src.add(v);
            }
            this.src.add(");");
            nlIndent();
        });
        return null;
    }

    @Override
    public final Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        String[] colors =
            {
                "red",
                "green",
                "blue"
            };
        for ( int i = 0; i < 3; i++ ) {
            this.src.add("analogWrite(_led_", colors[i], "_", rgbLedOffAction.port, ", 0);");
            nlIndent();
        }
        return null;
    }

    @Override
    public final Void visitMotorOnAction(MotorOnAction motorOnAction) {
        boolean step = motorOnAction.param.getDuration() != null;
        if ( step ) {//step motor
            this.src.add("_stepper_", motorOnAction.getUserDefinedPort(), ".setSpeed(");
            motorOnAction.param.getSpeed().accept(this);
            this.src.add(");");
            nlIndent();
            this.src.add("_stepper_", motorOnAction.getUserDefinedPort(), ".step(_SPU_", motorOnAction.getUserDefinedPort(), "*(");
            motorOnAction.getDurationValue().accept(this);
            this.src.add(")");
            if ( motorOnAction.getDurationMode().equals(MotorMoveMode.DEGREE) ) {
                this.src.add("/360");
            }
            this.src.add(");");
        } else {//servo motor
            this.src.add("_servo_", motorOnAction.getUserDefinedPort(), ".write(");
            motorOnAction.param.getSpeed().accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public final Void visitRelayAction(RelayAction relayAction) {
        this.src.add("digitalWrite(_relay_", relayAction.port, ", ", relayAction.mode.getValues()[0], ");");
        return null;
    }

    @Override
    public final Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("analogRead(_output_", lightSensor.getUserDefinedPort(), ")/10.24");
        return null;
    }

    @Override
    public final Void visitKeysSensor(KeysSensor button) {
        this.src.add("digitalRead(_taster_", button.getUserDefinedPort(), ")");
        return null;
    }

    public final void createDistanceUltrasonicSensor() {
        this.src.add("double _getUltrasonicDistance(int trigger, int echo)");
        nlIndent();
        this.src.add("{");
        incrIndentation();
        nlIndent();
        this.src.add("digitalWrite(trigger, LOW);");
        nlIndent();
        this.src.add("delay(5);");
        nlIndent();
        this.src.add("digitalWrite(trigger, HIGH);");
        nlIndent();
        this.src.add("delay(10);");
        nlIndent();
        this.src.add("digitalWrite(trigger, LOW);");
        nlIndent();
        this.src.add("return pulseIn(echo, HIGH) * 0.03432/2;");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
    }

    @Override
    public final Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.src.add("_getUltrasonicDistance(_trigger_", ultrasonicSensor.getUserDefinedPort(), ", ", "_echo_", ultrasonicSensor.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public final Void visitMoistureSensor(MoistureSensor moistureSensor) {
        this.src.add("analogRead(_moisturePin_", moistureSensor.getUserDefinedPort(), ")/10.24");
        return null;
    }

    @Override
    public final Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("map(analogRead(_TMP36_", temperatureSensor.getUserDefinedPort(), "), 0, 410, -50, 150)");
        return null;
    }

    @Override
    public final Void visitVoltageSensor(VoltageSensor potentiometer) {
        this.src.add("((double)analogRead(_output_", potentiometer.getUserDefinedPort(), "))*5/1024");
        return null;
    }

    @Override
    public final Void visitHumiditySensor(HumiditySensor humiditySensor) {
        switch ( humiditySensor.getMode() ) {
            case SC.HUMIDITY:
                this.src.add("_dht_", humiditySensor.getUserDefinedPort(), ".readHumidity()");
                break;
            case SC.TEMPERATURE:
                this.src.add("_dht_", humiditySensor.getUserDefinedPort(), ".readTemperature()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!");
        }
        return null;
    }

    @Override
    public final Void visitDropSensor(DropSensor dropSensor) {
        this.src.add("analogRead(_S_", dropSensor.getUserDefinedPort(), ")/10.24");
        return null;
    }

    @Override
    public final Void visitPulseSensor(PulseSensor pulseSensor) {
        this.src.add("analogRead(_SensorPin_", pulseSensor.getUserDefinedPort(), ")");
        return null;
    }

    public final Void readRFIDData(String sensorName) {
        this.src.add("String _readRFIDData(MFRC522 &mfrc522) {");
        incrIndentation();
        nlIndent();
        this.src.add("if (!mfrc522.PICC_IsNewCardPresent()) {");
        incrIndentation();
        nlIndent();
        this.src.add("return \"N/A\";");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        this.src.add("if (!mfrc522.PICC_ReadCardSerial()) {");
        incrIndentation();
        nlIndent();
        this.src.add("return \"N/D\";");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        this.src.add("return String(((long)(mfrc522.uid.uidByte[0])<<24)");
        incrIndentation();
        nlIndent();
        this.src.add("| ((long)(mfrc522.uid.uidByte[1])<<16)");
        nlIndent();
        this.src.add("| ((long)(mfrc522.uid.uidByte[2])<<8)");
        nlIndent();
        this.src.add("| ((long)(mfrc522.uid.uidByte[3]), HEX));");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        return null;

    }

    @Override
    public final Void visitRfidSensor(RfidSensor rfidSensor) {
        if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
            switch ( rfidSensor.getMode() ) {
                case SC.PRESENCE:
                    this.src.add("_mfrc522_", rfidSensor.getUserDefinedPort(), ".PICC_IsNewCardPresent()");
                    break;
                case SC.IDONE:
                    this.src.add("_readRFIDData(_mfrc522_", rfidSensor.getUserDefinedPort(), ")");
                    break;
                default:
                    throw new DbcException("Invalide mode for RFID Sensor!");
            }
        }
        return null;
    }

    public final void createMeasureIRSensor() {
        this.src.add("bool _getIRPresence() {");
        incrIndentation();
        nlIndent();
        this.src.add("if (IrReceiver.decode()) {");
        incrIndentation();
        nlIndent();
        this.src.add("IrReceiver.resume();");
        nlIndent();
        this.src.add("return true;");
        decrIndentation();
        nlIndent();
        this.src.add("} else {");
        incrIndentation();
        nlIndent();
        this.src.add("return false;");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        nlIndent();
        this.src.add("long int _getIRValue() {");
        incrIndentation();
        nlIndent();
        this.src.add("if (IrReceiver.decode()) {");
        incrIndentation();
        nlIndent();
        this.src.add("long int tmpValue = IrReceiver.decodedIRData.decodedRawData;");
        nlIndent();
        this.src.add("IrReceiver.resume();");
        nlIndent();
        this.src.add("return tmpValue;");
        decrIndentation();
        nlIndent();
        this.src.add("} else {");
        incrIndentation();
        nlIndent();
        this.src.add("return 0;");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
    }

    @Override
    public final Void visitInfraredSensor(InfraredSensor infraredSensor) {
        switch ( infraredSensor.getMode() ) {
            case SC.PRESENCE:
                this.src.add("_getIRPresence()");
                break;
            case SC.VALUE:
                this.src.add("_getIRValue()");
                break;
            default:
                throw new DbcException(infraredSensor.getKind().getName() + " mode is not supported: " + infraredSensor.getMode());
        }
        return null;
    }

    @Override
    public final Void visitMotionSensor(MotionSensor motionSensor) {
        this.src.add("digitalRead(_output_", motionSensor.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public final Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.src.add("_imu_", accelerometerSensor.getUserDefinedPort(), ".readFloatAccel", accelerometerSensor.getMode(), "()");
        return null;
    }

    @Override
    public final Void visitGyroSensor(GyroSensor gyroSensor) {
        this.src.add("_imu_", gyroSensor.getUserDefinedPort(), ".readFloatGyro", gyroSensor.getMode(), "()");
        return null;
    }

    @Override
    public final Void visitGyroReset(GyroReset gyroReset) {
        throw new DbcException("GyroReset not implemented");
    }

    @Override
    public final Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("_uBit.soundmotor.soundOn(");
        this.src.add(playNoteAction.frequency);
        this.src.add("); ", "_uBit.sleep(");
        this.src.add(playNoteAction.duration);
        this.src.add("); ", "_uBit.soundmotor.soundOff();");
        return null;
    }

    @Override
    public final Void visitToneAction(ToneAction toneAction) {
        //9 - sound port
        this.src.add("tone(_buzzer_", toneAction.port, ", ");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(");");
        nlIndent();
        this.src.add("delay(");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public final Void visitMainTask(MainTask mainTask) {

        mainTask.variables.accept(this);
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
            if ( usedConfigurationBlock.componentType.equals(SC.ULTRASONIC) ) {
                createDistanceUltrasonicSensor();
                nlIndent();
                break;
            }
        }
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            if ( usedConfigurationBlock.componentType.equals(SC.RFID) && !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                readRFIDData(usedConfigurationBlock.userDefinedPortName);
                nlIndent();
                break;
            }
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.src.add("void setup()");
        nlIndent();
        this.src.add("{");
        incrIndentation();
        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.src.add("Serial.begin(9600);");
            nlIndent();
        }
        generateConfigurationSetup();
        generateUsedVars();
        StringBuilder sb = src.getStringBuilder();
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        decrIndentation();
        nlIndent();
        this.src.add("}");
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
        this.src.add("// This file is automatically generated by the Open Roberta Lab.");
        nlIndent();
        nlIndent();
        if ( "nano33ble".equals(this.configuration.getRobotName()) ) {
            this.src.add("#define _ARDUINO_STL_NOT_NEEDED");
            nlIndent();
        }
        this.src.add("#include <Arduino.h>");
        nlIndent();
        nlIndent();
        LinkedHashSet<String> headerFiles = new LinkedHashSet<>();
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.HUMIDITY:
                    headerFiles.add("#include <DHT_sensor_library/DHT.h>");
                    break;
                case SC.INFRARED:
                    if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.INFRARED) ) {
                        if ( this.configuration.getRobotName().equalsIgnoreCase("uno") ||
                            this.configuration.getRobotName().equalsIgnoreCase("nano") ) {
                            if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.BUZZER) ) {
                                headerFiles.add("#define IR_USE_AVR_TIMER1");
                            } else if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERVOMOTOR) ) {
                                headerFiles.add("#define IR_USE_AVR_TIMER2");
                            } else {
                                // use the default timer, either 1 or 2
                            }
                        } else {
                            headerFiles.add("#define IR_USE_AVR_TIMER3");
                        }
                        headerFiles.add("#include <IRremote/src/IRremote.hpp>");
                    }
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
                case SC.OLEDSSD1306I2C:
                    headerFiles.add("#include <Adafruit_SSD1306.h>");
                    break;
                case SC.STEPMOTOR:
                    headerFiles.add("#include <Stepper/src/Stepper.h>");
                    break;
                case SC.SERVOMOTOR:
                    if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERVOMOTOR) ) {
                        headerFiles.add("#include <Servo/src/Servo.h>");
                    }
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    headerFiles.add("#include <SparkFun_LSM6DS3_Breakout/src/SparkFunLSM6DS3.h>");
                    break;
                case SC.RGBLED:
                    if ( "unowifirev2".equals(this.configuration.getRobotName()) && CommonArduinoCppVisitor.isInternalRgbLed(usedConfigurationBlock) ) {
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
                case SC.ROBOT:
                    break;
                case SC.LSM9DS1:
                    headerFiles.add("#include <Arduino_LSM9DS1.h>");
                    break;
                case SC.APDS9960:
                    headerFiles.add("#include <Arduino_APDS9960.h>");
                    break;
                case SC.LPS22HB:
                    headerFiles.add("#include <Arduino_LPS22HB.h>");
                    break;
                case SC.HTS221:
                    headerFiles.add("#include <Arduino_HTS221.h>");
                    break;
                case "AIFES":
                    headerFiles.add("#include <aifes.h>");
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }
        for ( String header : headerFiles ) {
            this.src.add(header);
            nlIndent();
        }
        this.src.add("#include <NEPODefs.h>");
        nlIndent();

        super.generateProgramPrefix(withWrapping);
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.ROBOT:
                    break;
                case SC.HUMIDITY:
                    this.src.add("_dht_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.src.add("pinMode(_trigger_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    this.src.add("pinMode(_echo_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
                    nlIndent();
                    break;
                case SC.MOTION:
                    this.src.add("pinMode(_output_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
                    nlIndent();
                    break;
                case SC.MOISTURE:
                    break;
                case SC.INFRARED:
                    if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.INFRARED) ) {
                        this.src.add("pinMode(13, OUTPUT);");
                        nlIndent();
                        this.src.add("IrReceiver.begin(", usedConfigurationBlock.getProperty("OUTPUT"), ", ENABLE_LED_FEEDBACK);");
                        nlIndent();
                    }
                    break;
                case SC.KEY:
                    this.src.add("pinMode(_taster_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
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
                        this.src.add("SPI.begin();");
                        nlIndent();
                        this.src.add("_mfrc522_", usedConfigurationBlock.userDefinedPortName, ".PCD_Init();");
                        nlIndent();
                    }
                    break;
                case SC.LCD:
                    this.src.add("_lcd_", usedConfigurationBlock.userDefinedPortName, ".begin(16, 2);");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    this.src.add("_lcd_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    nlIndent();
                    break;
                case SC.OLEDSSD1306I2C:
                    this.src.add("_lcd_", usedConfigurationBlock.userDefinedPortName, ".begin(SSD1306_SWITCHCAPVCC, SCREEN_ADDRESS);");
                    nlIndent();
                    this.src.add("_lcd_", usedConfigurationBlock.userDefinedPortName, ".clearDisplay();");
                    nlIndent();
                    this.src.add("_lcd_", usedConfigurationBlock.userDefinedPortName, ".setTextColor(SSD1306_WHITE);");
                    nlIndent();
                    break;

                case SC.LED:
                    this.src.add("pinMode(_led_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    if ( this.configuration.getRobotName().equals("unowifirev2") && CommonArduinoCppVisitor.isInternalRgbLed(usedConfigurationBlock) ) {
                        this.src.add("WiFiDrv::pinMode(25, OUTPUT);");
                        nlIndent();
                        this.src.add("WiFiDrv::pinMode(26, OUTPUT);");
                        nlIndent();
                        this.src.add("WiFiDrv::pinMode(27, OUTPUT);");
                        nlIndent();
                    } else {
                        this.src.add("pinMode(_led_red_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                        nlIndent();
                        this.src.add("pinMode(_led_green_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                        nlIndent();
                        this.src.add("pinMode(_led_blue_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                        nlIndent();
                    }
                    break;
                case SC.BUZZER:
                    break;
                case SC.RELAY:
                    this.src.add("pinMode(_relay_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    break;
                case SC.SERVOMOTOR:
                    if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERVOMOTOR) ) {
                        this.src.add("_servo_", usedConfigurationBlock.userDefinedPortName, ".attach(", usedConfigurationBlock.getProperty(SC.PULSE), ");");
                        nlIndent();
                    }
                    break;
                case SC.DIGITAL_PIN:
                    this.src.add("pinMode(_input_", usedConfigurationBlock.userDefinedPortName, ", INPUT");
                    if ( usedConfigurationBlock.getProperty("PIN_PULL").equals("PIN_PULL_UP") ) {
                        this.src.add("_PULLUP");
                    }
                    this.src.add(");");
                    nlIndent();
                    break;
                case SC.ANALOG_PIN:
                    this.src.add("pinMode(_input_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.src.add("pinMode(_output_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    this.src.add("_imu_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    nlIndent();
                    break;
                case SC.LSM9DS1:
                    this.src.add("IMU", ".begin();");
                    nlIndent();
                    break;
                case SC.APDS9960:
                    this.src.add("APDS", ".begin();");
                    nlIndent();
                    break;
                case SC.LPS22HB:
                    this.src.add("BARO", ".begin();");
                    nlIndent();
                    break;
                case SC.HTS221:
                    this.src.add("HTS", ".begin();");
                    nlIndent();
                    break;
                case "AIFES":
                    int FNN_3_LAYERS = Integer.parseInt(usedConfigurationBlock.getProperty("AIFES_FNN_LAYERS"));
                    for ( int i = 0; i < FNN_3_LAYERS - 1; i++ ) {
                        if ( i != 0 ) {
                            this.src.add("    ");
                        }
                        this.src.add("FNN_activations[", i, "] = ", "AIfES_E_", usedConfigurationBlock.getProperty("AIFES_LEARNINGFUNCTION"), ";\n");
                    }
                    this.src.add("    FNN.layer_count = ", usedConfigurationBlock.getProperty("AIFES_FNN_LAYERS"), ";\n",
                        "    FNN.fnn_structure = FNN_structure;\n",
                        "    FNN.fnn_activations = FNN_activations;\n",
                        "    FlatWeights = (float *)malloc(sizeof(float)*weight_number);\n",
                        "    FNN.flat_weights = FlatWeights;\n",
                        "    FNN_INIT_WEIGHTS.init_weights_method = AIfES_E_", usedConfigurationBlock.getProperty("AIFES_WEIGHT"), ";\n",
                        "    FNN_INIT_WEIGHTS.min_init_uniform = ", usedConfigurationBlock.getProperty("AIFES_MIN_WEIGHT"), ";\n",
                        "    FNN_INIT_WEIGHTS.max_init_uniform = ", usedConfigurationBlock.getProperty("AIFES_MAX_WEIGHT"), ";\n",
                        "    FNN_TRAIN.optimizer = AIfES_E_", usedConfigurationBlock.getProperty("AIFES_OPTIMIER"), ";\n",
                        "    FNN_TRAIN.learn_rate = ", usedConfigurationBlock.getProperty("AIFES_LEARNINGRATE"), "f;\n",
                        "    FNN_TRAIN.sgd_momentum = ", usedConfigurationBlock.getProperty("AIFES_MOMENTUM"), ";\n",
                        "    FNN_TRAIN.batch_size = ", usedConfigurationBlock.getProperty("AIFES_DATASET"), ";\n",
                        "    FNN_TRAIN.epochs = ", usedConfigurationBlock.getProperty("AIFES_EPOCHS"), ";\n",
                        "    FNN_TRAIN.epochs_loss_print_interval = 10;\n",
                        "    FNN_TRAIN.early_stopping = AIfES_E_early_stopping_on;\n",
                        "    FNN_TRAIN.early_stopping_target_loss = 0.004;\n",
                        "    FNN_TRAIN.loss_print_function = printLoss;\n    ");
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.componentType);
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.userDefinedPortName;
            switch ( cc.componentType ) {
                case SC.ROBOT:
                case SC.INFRARED:
                    break;
                case SC.HUMIDITY:
                    this.src.add("#define DHTPIN", blockName, " ", cc.getProperty("OUTPUT"));
                    nlIndent();
                    this.src.add("#define DHTTYPE DHT11");
                    nlIndent();
                    this.src.add("DHT _dht_", blockName, "(DHTPIN", blockName, ", DHTTYPE);");
                    nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.src.add("int _trigger_", blockName, " = ", cc.getProperty("TRIG"), ";");
                    nlIndent();
                    this.src.add("int _echo_", blockName, " = ", cc.getProperty("ECHO"), ";");
                    nlIndent();
                    break;
                case SC.MOISTURE:
                    this.src.add("int _moisturePin_", blockName, " = ", cc.getProperty("S"), ";");
                    nlIndent();
                    break;
                case SC.LIGHT:
                case SC.MOTION:
                case SC.POTENTIOMETER:
                    this.src.add("int _output_", blockName, " = ", cc.getProperty("OUTPUT"), ";");
                    nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.src.add("int _TMP36_", blockName, " = ", cc.getProperty("OUTPUT"), ";");
                    nlIndent();
                    break;
                case SC.DROP:
                    this.src.add("int _S_", blockName, " = ", cc.getProperty("S"), ";");
                    nlIndent();
                    break;
                case SC.PULSE:
                    this.src.add("int _SensorPin_", blockName, " = ", cc.getProperty("S"), ";");
                    nlIndent();
                    break;
                case SC.RFID:
                    if ( !this.configuration.getRobotName().equals("unowifirev2") ) { // TODO remove once rfid library is supported for unowifirev2
                        this.src.add("#define SS_PIN_", blockName, " ", cc.getProperty("SDA"));
                        nlIndent();
                        this.src.add("#define RST_PIN_", blockName, " ", cc.getProperty("RST"));
                        nlIndent();
                        this.src.add("MFRC522 _mfrc522_", blockName, "(SS_PIN_", blockName, ", RST_PIN_", blockName, ");");
                        nlIndent();
                    }
                    break;
                case SC.KEY:
                    this.src.add("int _taster_", blockName, " = ", cc.getProperty("PIN1"), ";");
                    nlIndent();
                    break;
                case SC.LCD:
                    this.src.add("LiquidCrystal _lcd_", blockName, "(", cc.getProperty("RS"), ", ", cc.getProperty("E"), ", ", cc.getProperty("D4"), ", ", cc.getProperty("D5"), ", ", cc.getProperty("D6"), ", ", cc.getProperty("D7"), ");");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    String addresslcdi2c = cc.getOptProperty("ADDRESS");
                    // TODO check if this is still needed after we rework the configuration back-transformation
                    if ( addresslcdi2c == null ) {
                        addresslcdi2c = "0x27";
                    }
                    this.src.add("LiquidCrystal_I2C _lcd_", blockName, "(", addresslcdi2c, ", 16, 2);");
                    nlIndent();
                    break;
                case SC.OLEDSSD1306I2C:
                    String addressssd1306 = cc.getOptProperty("ADDRESS");
                    // TODO check if this is still needed after we rework the configuration back-transformation
                    if ( addressssd1306 == null ) {
                        addressssd1306 = "0x3D";
                    }
                    this.src.add("#define SCREEN_ADDRESS ", addressssd1306);
                    nlIndent();
                    this.src.add("#define OLED_RESET 4");
                    nlIndent();
                    this.src.add("#define SCREEN_WIDTH 128");
                    nlIndent();
                    this.src.add("#define SCREEN_HEIGHT ", addressssd1306.equals("0x3D") ? "64" : "32");
                    nlIndent();
                    this.src.add("Adafruit_SSD1306 _lcd_", blockName, "(SCREEN_WIDTH, SCREEN_HEIGHT, &Wire, OLED_RESET);");
                    nlIndent();
                    break;
                case SC.LED:
                    this.src.add("int _led_", blockName, " = ", cc.getProperty("INPUT"), ";");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    this.src.add("int _led_red_", blockName, " = ", cc.getProperty(SC.RED), ";");
                    nlIndent();
                    this.src.add("int _led_green_", blockName, " = ", cc.getProperty(SC.GREEN), ";");
                    nlIndent();
                    this.src.add("int _led_blue_", blockName, " = ", cc.getProperty(SC.BLUE), ";");
                    nlIndent();
                    this.src.add("int _v_colour_temp;");
                    nlIndent();
                    break;
                case SC.BUZZER:
                    this.src.add("int _buzzer_", blockName, " = ", cc.getProperty("+"), ";");
                    nlIndent();
                    break;
                case SC.RELAY:
                    this.src.add("int _relay_", blockName, " = ", cc.getProperty("IN"), ";");
                    nlIndent();
                    break;
                case SC.STEPMOTOR:
                    this.src.add("int _SPU_", blockName, " = ", "2048;"); //TODO: change 2048 to customized
                    nlIndent();
                    this.src.add("Stepper _stepper_", blockName, "(_SPU_", blockName, ", ", cc.getProperty("IN1"), ", ", cc.getProperty("IN3"), ", ", cc.getProperty("IN2"), ", ", cc.getProperty("IN4"), ");");
                    nlIndent();
                    break;
                case SC.SERVOMOTOR:
                    if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERVOMOTOR) ) {
                        this.src.add("Servo _servo_", blockName, ";");
                        nlIndent();
                    }
                    break;
                case SC.ANALOG_PIN:
                case SC.DIGITAL_PIN:
                    this.src.add("int _input_", blockName, " = ", cc.getProperty("OUTPUT"), ";");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.src.add("int _output_", blockName, " = ", cc.getProperty("INPUT"), ";");
                    nlIndent();
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    this.src.add("LSM6DS3 _imu_", blockName, "(SPI_MODE, SPIIMU_SS);");
                    nlIndent();
                    break;
                case SC.LSM9DS1:
                    this.src.add("float xAsFloat, yAsFloat, zAsFloat;");
                    nlIndent();
                    break;
                case SC.APDS9960:
                    this.src.add("int rAsInt, gAsInt, bAsInt;");
                    nlIndent();
                    break;
                case SC.LPS22HB:
                case SC.HTS221:
                    break;
                case "AIFES":
                    StringBuilder sb = src.getStringBuilder();
                    sb.append("uint32_t global_epoch_counter = 0; \n")
                        .append("uint32_t FNN_structure[" + cc.getProperty("AIFES_FNN_LAYERS") + "] = {" + cc.getProperty("AIFES_NUMBER_INPUT_NEURONS") + "," + cc.getProperty("AIFES_NUMBER_HIDDENLAYERS_NEURONS") + "," + cc.getProperty("AIFES_NUMBER_OUTPUT_NEURONS") + "}; \n")
                        .append("AIFES_E_activations FNN_activations[" + (Integer.parseInt(cc.getProperty("AIFES_FNN_LAYERS")) + -1) + "];\n")
                        .append("uint32_t weight_number = AIFES_E_flat_weights_number_fnn_f32(FNN_structure," + cc.getProperty("AIFES_FNN_LAYERS") + ");\n")
                        .append("float *FlatWeights;\n")
                        .append("AIFES_E_model_parameter_fnn_f32 FNN;\n")
                        .append("uint32_t i;\n")
                        .append("AIFES_E_init_weights_parameter_fnn_f32  FNN_INIT_WEIGHTS;\n")
                        .append("AIFES_E_training_parameter_fnn_f32  FNN_TRAIN;\n")
                        .append("\nvoid printLoss(float loss) {\n    global_epoch_counter++;\n}\n \n")
                        .append("float input_data[" + cc.getProperty("AIFES_DATASET") + "][" + cc.getProperty("AIFES_NUMBER_INPUT_NEURONS") + "];\n")
                        .append("float target_data[" + cc.getProperty("AIFES_DATASET") + "][" + cc.getProperty("AIFES_NUMBER_OUTPUT_NEURONS") + "];\n")
                        .append("float classify_data[" + cc.getProperty("AIFES_NUMBER_INPUT_NEURONS") + "];\n")
                        .append("int8_t errorInference = 0;\n")
                        .append("int8_t targetSet = 0;\n")
                        .append("int8_t targetData = 0;\n")
                        .append("int8_t trainingSet = 0;\n")
                        .append("int8_t trainingData = 0;\n")
                        .append("int8_t currentClassifySet = 0;\n")
                        .append("uint16_t input_shape[] = {" + (Integer.parseInt(cc.getProperty("AIFES_DATASET"))) + ", (uint16_t)FNN_structure[0]};\n")
                        .append("aitensor_t input_tensor = AITENSOR_2D_F32(input_shape, input_data);\n")
                        .append("uint16_t target_shape[] = {" + (Integer.parseInt(cc.getProperty("AIFES_DATASET"))) + ", (uint16_t)FNN_structure[" + (Integer.parseInt(cc.getProperty("AIFES_FNN_LAYERS")) - 1) + "]};\n")
                        .append("aitensor_t target_tensor = AITENSOR_2D_F32(target_shape, target_data);\n")
                        .append("float output_train_data[" + (Integer.parseInt(cc.getProperty("AIFES_DATASET"))) + "];\n")
                        .append("uint16_t output_train_shape[] = {" + (Integer.parseInt(cc.getProperty("AIFES_DATASET"))) + ", (uint16_t)FNN_structure[" + (Integer.parseInt(cc.getProperty("AIFES_FNN_LAYERS")) - 1) + "]};\n")
                        .append("aitensor_t output_train_tensor = AITENSOR_2D_F32(output_train_shape, output_train_data);\n")
                        .append("uint16_t classify_shape[] = {1, (uint16_t)FNN_structure[0]};\n")
                        .append("aitensor_t classify_tensor = AITENSOR_2D_F32(classify_shape, classify_data);\n")
                        .append("float output_classify_data[1];\n")
                        .append("uint16_t output_classify_shape[] = {1, (uint16_t)FNN_structure[" + (Integer.parseInt(cc.getProperty("AIFES_FNN_LAYERS")) - 1) + "]};\n")
                        .append("aitensor_t output_classify_tensor = AITENSOR_2D_F32(output_classify_shape, output_classify_data);\n")

                        .append("\nvoid addInputData(float data) {\n     input_data[trainingSet][trainingData] = data;\n     if ((trainingSet >= " + (Integer.parseInt(cc.getProperty("AIFES_DATASET")) - 1) + ") && (trainingData >= " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_INPUT_NEURONS")) + -1) + " )) {\n          trainingSet = 0;\n          trainingData = 0;\n     }")
                        .append(" else if ((trainingSet < " + (Integer.parseInt(cc.getProperty("AIFES_DATASET")) - 1) + ") && (trainingData >= " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_INPUT_NEURONS")) + -1) + ")){\n          trainingSet = trainingSet + 1;\n          trainingData = 0;\n     }")
                        .append(" else if (trainingData < " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_INPUT_NEURONS")) + -1) + ") {\n          trainingData = trainingData + 1;\n     }\n}\n")

                        .append("\nvoid addTargetData(float data) {\n     target_data[targetSet][targetData] = data;\n     if ((targetSet >= " + (Integer.parseInt(cc.getProperty("AIFES_DATASET")) - 1) + ") && (targetData >= " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_OUTPUT_NEURONS")) + -1) + ")) {\n          targetSet = 0;\n          targetData = 0;\n     }")
                        .append(" else if ((targetSet < " + (Integer.parseInt(cc.getProperty("AIFES_DATASET")) - 1) + ") && (targetData >= " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_OUTPUT_NEURONS")) + -1) + ")){\n          targetSet = targetSet + 1;\n          targetData = 0;\n     }")
                        .append(" else if (targetData < " + (Integer.parseInt(cc.getProperty("AIFES_NUMBER_OUTPUT_NEURONS")) + -1) + ") {\n          targetData = targetData + 1;\n     }\n}\n")

                        .append("\nvoid addClassifyData(float data) {\n     classify_data[currentClassifySet] = data;\n     if (currentClassifySet >= 1) {\n          currentClassifySet = 0;\n     } else if (currentClassifySet < 1) {\n          currentClassifySet = currentClassifySet + 1;\n     }\n}\n");
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.componentType);
            }
        }
    }

    @Override
    public final Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        switch ( pinWriteValueAction.pinValue ) {
            case SC.ANALOG:
                this.src.add("analogWrite(_output_", pinWriteValueAction.port, ", (int)");
                pinWriteValueAction.value.accept(this);
                this.src.add(");");
                break;
            case SC.DIGITAL:
                this.src.add("digitalWrite(_output_", pinWriteValueAction.port, ", (int)");
                pinWriteValueAction.value.accept(this);
                this.src.add(");");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public final Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        switch ( pinGetValueSensor.getMode() ) {
            case SC.ANALOG:
                this.src.add("analogRead(_input_", pinGetValueSensor.getUserDefinedPort(), ")");
                break;
            case SC.DIGITAL:
                this.src.add("digitalRead(_input_", pinGetValueSensor.getUserDefinedPort(), ")");
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

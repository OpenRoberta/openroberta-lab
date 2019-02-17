package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinReadValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.SenseboxUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class SenseboxCppVisitor extends AbstractCommonArduinoCppVisitor implements IArduinoVisitor<Void> {

    public SenseboxCppVisitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(brickConfiguration, programPhrases, indentation);
        SenseboxUsedHardwareCollectorVisitor codePreprocessVisitor = new SenseboxUsedHardwareCollectorVisitor(programPhrases);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.usedActors = codePreprocessVisitor.getUsedActors();
        this.usedSensors = codePreprocessVisitor.getUsedSensors();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.decrIndentation();
        this.nlIndent();
        this.sb.append("unsigned long _time = millis();");
        this.nlIndent();
        mainTask.getVariables().visit(this);
        this.nlIndent();
        this.generateConfigurationVariables();
        this.nlIndent();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> (phrase.getKind().getCategory() == Category.METHOD) && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( this.configuration.getConfigurationComponents().isEmpty() && (numberConf == 0) ) {
            nlIndent();
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.sb.append("void setup()");
        this.nlIndent();
        this.sb.append("{");
        this.incrIndentation();
        this.nlIndent();
        this.sb.append("Serial.begin(9600); ");
        this.nlIndent();
        this.generateConfigurationSetup();
        this.generateUsedVars();
        this.sb.delete(this.sb.lastIndexOf("\n"), this.sb.length());
        this.decrIndentation();
        this.nlIndent();
        this.sb.append("}");
        this.nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        this.sb.append("#include \"SenseBoxMCU.h\"");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        // nothing to do because the arduino loop closes the program
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        SenseboxCppVisitor astVisitor = new SenseboxCppVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("_display_").append(clearDisplayAction.getPort()).append(".clearDisplay();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("tone(_buzzer_").append(toneAction.getPort()).append(",");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("analogRead(_mic_").append(soundSensor.getPort()).append(")");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( !lightAction.getMode().toString().equals(BlocklyConstants.DEFAULT) ) {
            this.sb.append("digitalWrite(_led_").append(lightAction.getPort()).append(", ").append(lightAction.getMode().getValues()[0] + ");");
        } else {
            if ( lightAction.getRgbLedColor().getClass().equals(ColorConst.class) ) {
                String hexValue = ((ColorConst<Void>) lightAction.getRgbLedColor()).getRgbValue();
                hexValue = hexValue.split("#")[1];
                int R = Integer.decode("0x" + hexValue.substring(0, 2));
                int G = Integer.decode("0x" + hexValue.substring(2, 4));
                int B = Integer.decode("0x" + hexValue.substring(4, 6));
                Map<String, Integer> colorConstChannels = new HashMap<>();
                colorConstChannels.put("red", R);
                colorConstChannels.put("green", G);
                colorConstChannels.put("blue", B);
                colorConstChannels.forEach((k, v) -> {
                    this.sb.append("analogWrite(_led_");
                    this.sb.append(k);
                    this.sb.append("_");
                    this.sb.append(lightAction.getPort());
                    this.sb.append(", ");
                    this.sb.append(v);
                    this.sb.append(");");
                    this.nlIndent();
                });
                return null;
            }
            if ( lightAction.getRgbLedColor().getClass().equals(Var.class) ) {
                String tempVarName = ((Var<Void>) lightAction.getRgbLedColor()).getValue();
                this.sb.append("analogWrite(_led_red_").append(lightAction.getPort()).append(", RCHANNEL(");
                this.sb.append(tempVarName);
                this.sb.append("));");
                this.nlIndent();
                this.sb.append("analogWrite(_led_green_").append(lightAction.getPort()).append(", GCHANNEL(");
                this.sb.append(tempVarName);
                this.sb.append("));");
                this.nlIndent();
                this.sb.append("analogWrite(_led_blue_").append(lightAction.getPort()).append(", BCHANNEL(");
                this.sb.append(tempVarName);
                this.sb.append("));");
                this.nlIndent();
                return null;
            }
            Map<String, Expr<Void>> Channels = new HashMap<>();
            Channels.put("red", ((RgbColor<Void>) lightAction.getRgbLedColor()).getR());
            Channels.put("green", ((RgbColor<Void>) lightAction.getRgbLedColor()).getG());
            Channels.put("blue", ((RgbColor<Void>) lightAction.getRgbLedColor()).getB());
            Channels.forEach((k, v) -> {
                this.sb.append("analogWrite(_led_").append(k).append("_").append(lightAction.getPort()).append(", ");
                v.visit(this);
                this.sb.append(");");
                this.nlIndent();
            });
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        this.sb.append("Serial.println(");
        serialWriteAction.getValue().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        return null;
    }

    @Override
    public Void visitPinReadValueAction(PinReadValueAction<Void> pinReadValueActor) {
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> button) {
        this.sb.append("digitalRead(_button_").append(button.getPort()).append(")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("analogRead(_output_").append(lightSensor.getPort()).append(")/10.24");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> potentiometer) {
        this.sb.append("((double)analogRead(_potentiometer_").append(potentiometer.getPort()).append("))*5/1024");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("_hcsr04_").append(ultrasonicSensor.getPort()).append(".getDistance()");
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        this.sb.append("_hdc1080_").append(humiditySensor.getPort());
        switch ( humiditySensor.getMode() ) {
            case SC.HUMIDITY:
                this.sb.append(".getHumidity()");
                break;
            case SC.TEMPERATURE:
                this.sb.append(".getTemperature()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!" + humiditySensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("_bmp280_").append(temperatureSensor.getPort());
        switch ( temperatureSensor.getMode() ) {
            case SC.TEMPERATURE:
                this.sb.append(".getTemperature()");
                break;
            case SC.PRESSURE:
                this.sb.append(".getPressure()");
                break;
            default:
                throw new DbcException("Invalide mode for BMP280 Sensor!" + temperatureSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        switch ( vemlLightSensor.getMode() ) {
            case SC.LIGHT:
                this.sb.append("_tsl_").append(vemlLightSensor.getPort()).append(".getIlluminance()");
                break;
            case SC.UVLIGHT:
                this.sb.append("_veml_").append(vemlLightSensor.getPort()).append(".getUvIntensity()");
                break;
            default:
                throw new DbcException("Invalide mode for VEML/TSL Sensor!" + vemlLightSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("(int) (millis() - _time)");
                break;
            case SC.RESET:
                this.sb.append("_time = millis();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        for ( Pair<String, Expr<Void>> entry : sendDataAction.getId2Phenomena() ) {
            this.sb.append("_osm.uploadMeasurement(");
            entry.getSecond().visit(this);
            this.sb.append(", _").append(entry.getFirst()).append(");");
            nlIndent();
        }
        /*for ( Expr<Void> phenomenon : listOfPhenomena ) {
            this.sb.append("_osm.uploadMeasurement(");
            phenomenon.visit(this);
            this.sb.append(",");
            String sensorName = ((SensorExpr<Void>) phenomenon).getSens().getKind().getName();
            String userDefinedPortName = ((ExternalSensor<Void>) (((SensorExpr<Void>) phenomenon).getSens())).getPort();
            switch ( sensorName ) {
                case "HUMIDITY_SENSING":
                    this.sb.append("_hdc1080_id_");
                    break;
                case "TEMPERATURE_SENSING":
                    this.sb.append("_bmp280_id_");
        
                    break;
                case "VEMLLIGHT_SENSING":
                    this.sb.append("_veml_tsl_id_");
                    break;
                default:
                    throw new DbcException("An invalid sensor has been detected: " + sensorName);
            }
            this.sb.append(userDefinedPortName);
            this.sb.append(");");
            this.nlIndent();
        }*/
        return null;
    }

    private void generateConfigurationSetup() {
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.LED:
                    this.sb.append("pinMode(_led_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    this.nlIndent();
                    break;
                case SC.RGBLED:
                    this.sb.append("pinMode(_led_red_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    this.nlIndent();
                    this.sb.append("pinMode(_led_green_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    this.nlIndent();
                    this.sb.append("pinMode(_led_blue_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                    this.nlIndent();
                    break;
                case SC.KEY:
                    this.sb.append("pinMode(_button_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", INPUT);");
                    this.nlIndent();
                    break;
                case SC.HUMIDITY:
                    this.sb.append("_hdc1080_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.sb.append("_bmp280_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.LIGHTVEML:
                    this.sb.append("_veml_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin();");
                    this.nlIndent();
                    this.sb.append("_tsl_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.WIRELESS:
                    this.sb
                        .append("_bee_")
                        //.append(usedConfigurationBlock.getUserDefinedPortName())
                        .append("->connectToWifi(\"")
                        .append(usedConfigurationBlock.getProperty("SSID"))
                        .append("\",\"")
                        .append(usedConfigurationBlock.getProperty("PASSWORD"))
                        .append("\");");
                    this.nlIndent();
                    this.sb.append("delay(1000);");
                    this.nlIndent();
                    break;
                // no additional configuration needed:
                case SC.ULTRASONIC:
                case SC.POTENTIOMETER:
                case SC.LIGHT:
                case SC.BUZZER:
                case SC.SOUND:
                case SC.SENSEBOX:
                    break;
                default:
                    throw new DbcException("Sensor is not supported: " + usedConfigurationBlock.getComponentType());
            }
        }
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            if ( usedConfigurationBlock.getComponentType().equals(SC.SENSEBOX)
                && usedConfigurationBlock.getUserDefinedPortName() != null
                && !usedConfigurationBlock.getUserDefinedPortName().isEmpty() ) {
                this.sb.append("_osm = new OpenSenseMap(\"").append(usedConfigurationBlock.getUserDefinedPortName()).append("\", _bee_);");
                nlIndent();
            }
        }
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.LED:
                    this.sb.append("int _led_").append(blockName).append(" = ").append(cc.getProperty("INPUT")).append(";");
                    this.nlIndent();
                    break;
                case SC.RGBLED:
                    this.sb.append("int _led_red_").append(blockName).append(" = ").append(cc.getProperty("RED")).append(";");
                    this.nlIndent();
                    this.sb.append("int _led_green_").append(blockName).append(" = ").append(cc.getProperty("GREEN")).append(";");
                    this.nlIndent();
                    this.sb.append("int _led_blue_").append(blockName).append(" = ").append(cc.getProperty("BLUE")).append(";");
                    this.nlIndent();
                    break;
                case SC.KEY:
                    this.sb.append("int _taster_").append(blockName).append(" = ").append(cc.getProperty("PIN1")).append(";");
                    this.nlIndent();
                    break;
                case SC.LIGHT:
                    this.sb.append("int _output_").append(blockName).append(" = ").append(cc.getProperty("OUTPUT")).append(";");
                    this.nlIndent();
                    break;
                case SC.POTENTIOMETER:
                    this.sb.append("int _potentiometer_").append(blockName).append(" = ").append(cc.getProperty("OUTPUT")).append(";");
                    this.nlIndent();
                    break;
                case SC.BUZZER:
                    this.sb.append("int _buzzer_").append(blockName).append(" = ").append(cc.getProperty("+")).append(";");
                    this.nlIndent();
                    break;
                case SC.SOUND:
                    this.sb.append("int _mic_").append(blockName).append(" = ").append(cc.getProperty("OUT")).append(";");
                    this.nlIndent();
                    break;
                case SC.HUMIDITY:
                    this.sb.append("HDC1080 _hdc1080_").append(blockName).append(";");
                    this.nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.sb.append("BMP280 _bmp280_").append(blockName).append(";");
                    this.nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.sb
                        .append("Ultrasonic _hcsr04_")
                        .append(blockName)
                        .append("(")
                        .append(cc.getProperty("TRIG"))
                        .append(", ")
                        .append(cc.getProperty("ECHO"))
                        .append(");");
                    this.nlIndent();
                    break;
                case SC.LIGHTVEML:
                    this.sb.append("VEML6070 _veml_").append(blockName).append(";");
                    this.nlIndent();
                    this.sb.append("TSL45315 _tsl_").append(blockName).append(";");
                    this.nlIndent();
                    break;
                case SC.WIRELESS:
                    this.sb
                        .append("Bee* _bee_")
                        //.append(blockName)
                        .append(" = new Bee();");
                    this.nlIndent();
                    break;
                case SC.SENSEBOX:
                    if ( cc.getUserDefinedPortName() != null && !cc.getUserDefinedPortName().isEmpty() ) {
                        Set<Entry<String, String>> componentEntrySet = cc.getComponentProperties().entrySet();
                        int maxNumberOfPairs = componentEntrySet.size() / 2;
                        String[] names = new String[maxNumberOfPairs];
                        String[] ids = new String[maxNumberOfPairs];
                        for ( Entry<String, String> entry : componentEntrySet ) {
                            String key = entry.getKey();
                            if ( key.startsWith("ID") ) {
                                int index = Integer.parseInt(key.substring(2));
                                ids[index - 1] = entry.getValue();
                            } else if ( key.startsWith("NAME") ) {
                                int index = Integer.parseInt(key.substring(4));
                                names[index - 1] = entry.getValue();
                            }
                        }
                        for ( int i = 0; i < maxNumberOfPairs; i++ ) {
                            System.out.println(ids[i]);
                            if ( !names[i].isEmpty() && !ids[i].isEmpty() ) {
                                this.sb.append("char* _").append(names[i]).append(" = \"").append(ids[i]).append("\";");
                                nlIndent();
                            }
                        }
                        this.sb.append("OpenSenseMap _osm;");
                        nlIndent();
                    }
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }
}

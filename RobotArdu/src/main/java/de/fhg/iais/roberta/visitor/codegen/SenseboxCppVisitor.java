package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.EnvironmentalSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.hardware.ISenseboxVisitor;

public class SenseboxCppVisitor extends NepoArduinoCppVisitor implements ISenseboxVisitor<Void> {

    private final String SSID;
    private final String password;

    public SenseboxCppVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst brickConfiguration,
        String SSID,
        String password,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, brickConfiguration, beans);
        this.SSID = SSID;
        this.password = password;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        this.decrIndentation();
        this.nlIndent();
        mainTask.variables.accept(this);
        this.nlIndent();
        this.generateConfigurationVariables();
        generateTimerVariables();
        this.nlIndent();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( this.configuration.getConfigurationComponents().isEmpty() && numberConf == 0 ) {
            nlIndent();
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.src.add("void setup()");
        this.nlIndent();
        this.src.add("{");
        this.incrIndentation();
        this.nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.src.add("Serial.begin(9600);");
            nlIndent();
        }
        this.nlIndent();
        this.generateConfigurationSetup();
        this.generateUsedVars();
        StringBuilder sb = src.getStringBuilder();
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        this.decrIndentation();
        this.nlIndent();
        this.src.add("}");
        this.nlIndent();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        this.src.add("// This file is automatically generated by the Open Roberta Lab.");
        this.nlIndent();
        this.src.add("#define _ARDUINO_STL_NOT_NEEDED");
        this.nlIndent();
        this.src.add("#include \"SenseBoxMCU.h\"");
        this.nlIndent();
        this.src.add("#undef max"); // TODO needed?
        this.nlIndent();
        this.src.add("#undef min"); // TODO needed?
        this.nlIndent();
        this.src.add("#include <NEPODefs.h>");
        this.nlIndent();
        if ( this.configuration.optConfigurationComponentByType(SC.SENSEBOX_SDCARD) != null ) {
            this.src.add("#include <SD.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.LCDI2C) != null ) {
            this.src.add("#include <Adafruit_GFX.h>");
            this.nlIndent();
            this.src.add("#include <Adafruit_SSD1306.h>");
            this.nlIndent();
            this.src.add("#include <Plot.h>");
            this.nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isListsUsed() ) {
            this.src.add("#include <stdlib.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.ENVIRONMENTAL) != null ) {
            this.src.add("#include <bsec.h>");
            this.nlIndent();
            this.src.add("#define _readIaq(X, Y) ((X.run()) ? Y : Y)");
            this.nlIndent();
            this.src.add("#include <Wire.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.RGBLED) != null ) {
            this.src.add("#include <Adafruit_NeoPixel.h>");
            this.nlIndent();
        }
        nlIndent();
        nlIndent();

        super.generateProgramPrefix(withWrapping);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("_display_", clearDisplayAction.port, ".clearDisplay();");
        nlIndent();
        this.src.add("_display_", clearDisplayAction.port, ".display();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.add("_display_", showTextAction.port, ".setCursor(");
        showTextAction.x.accept(this);
        this.src.add(", ");
        showTextAction.y.accept(this);
        this.src.add(");");
        nlIndent();
        this.src.add("_display_", showTextAction.port, ".setTextSize(1);");
        nlIndent();
        this.src.add("_display_", showTextAction.port, ".setTextColor(WHITE, BLACK);");
        nlIndent();
        this.src.add("_printToDisplay(", "_display_", showTextAction.port, ", ");
        showTextAction.msg.accept(this);
        this.src.add(", true);");
        nlIndent();
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction plotPointAction) {
        this.src.add("_plot_", plotPointAction.port, ".addDataPoint(");
        plotPointAction.tickmark.accept(this);
        this.src.add(", ");
        plotPointAction.value.accept(this);
        this.src.add(");");
        nlIndent();
        return null;
    }

    @Override
    public Void visitPlotClearAction(PlotClearAction plotClearAction) {
        ConfigurationComponent cc = this.configuration.optConfigurationComponentByType(SC.LCDI2C);
        String portName = null;
        if ( cc != null ) {
            portName = cc.userDefinedPortName;
        }
        if ( portName != null ) {
            this.src.add("_plot_", portName, ".clear();");
            nlIndent();
            this.src.add("_plot_", portName, ".drawPlot();");
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("tone(_buzzer_", toneAction.port, ", ");
        toneAction.frequency.accept(this);
        this.src.add(");");
        nlIndent();
        this.src.add("delay(");
        toneAction.duration.accept(this);
        this.src.add(");");
        nlIndent();
        this.src.add("noTone(_buzzer_", toneAction.port, ");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        throw new DbcException("play note not supported");
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("get_microphone_volume(_mic_", soundSensor.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public Void visitLightAction(LedAction lightAction) {
        if ( !lightAction.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            this.src.add("digitalWrite(_led_", lightAction.port, ", ", lightAction.mode.getValues()[0], ");");
        } else {
            if ( lightAction.rgbLedColor.getClass().equals(ColorConst.class) ) {
                ColorConst colorConst = (ColorConst) lightAction.rgbLedColor;
                this.src.add("_rgbled_", lightAction.port, ".setPixelColor(0, _rgbled_", lightAction.port, ".Color(", colorConst.getRedChannelInt(),
                    ", ", colorConst.getGreenChannelInt(), ", ", colorConst.getBlueChannelInt(), "));");
            } else if ( lightAction.rgbLedColor.getClass().equals(Var.class) ) {
                String tempVarName = "___" + ((Var) lightAction.rgbLedColor).name;
                this.src.add("_rgbled_", lightAction.port, ".setPixelColor(0, _rgbled_", lightAction.port, ".Color(RCHANNEL(",
                    tempVarName, "), GCHANNEL(", tempVarName, "), BCHANNEL(", tempVarName, ")));");
            } else if ( lightAction.rgbLedColor.getClass().equals(MethodExpr.class) ) {
                String tempVarName = "_v_colour_temp";
                this.src.add(tempVarName, " = ");
                visitMethodCall((MethodCall) ((MethodExpr) lightAction.rgbLedColor).method);
                this.src.add(";");
                nlIndent();
                this.src.add("_rgbled_", lightAction.port, ".setPixelColor(0, _rgbled_", lightAction.port,
                    ".Color(RCHANNEL(", tempVarName, "), GCHANNEL(", tempVarName, "), BCHANNEL(", tempVarName, ")));");
            } else if ( lightAction.rgbLedColor.getClass().equals(FunctionExpr.class) ) {
                String tempVarName = "_v_colour_temp";
                this.src.add(tempVarName, " = ");
                ((FunctionExpr) lightAction.rgbLedColor).function.accept(this);
                this.src.add(";");
                nlIndent();
                this.src.add("_rgbled_", lightAction.port, ".setPixelColor(0, _rgbled_", lightAction.port,
                    ".Color(RCHANNEL(", tempVarName, "), GCHANNEL(", tempVarName, "), BCHANNEL(", tempVarName, ")));");
            } else {
                this.src.add("_rgbled_", lightAction.port, ".setPixelColor(0, _rgbled_", lightAction.port, ".Color(");
                ((RgbColor) lightAction.rgbLedColor).R.accept(this);
                this.src.add(", ");
                ((RgbColor) lightAction.rgbLedColor).G.accept(this);
                this.src.add(", ");
                ((RgbColor) lightAction.rgbLedColor).B.accept(this);
                this.src.add("));");
            }
            nlIndent();
            this.src.add("_rgbled_", lightAction.port, ".show();");
        }
        return null;
    }

    @Override
    public Void visitLightOffAction(RgbLedOffAction lightOffAction) {
        this.src.add("_rgbled_", lightOffAction.port, ".setPixelColor(0, _rgbled_", lightOffAction.port, ".Color(0,0,0));");
        this.nlIndent();
        this.src.add("_rgbled_", lightOffAction.port, ".show();");
        this.nlIndent();
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction relayAction) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor button) {
        this.src.add("digitalRead(_button_", button.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("analogRead(_output_", lightSensor.getUserDefinedPort(), ")/10.24");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor potentiometer) {
        this.src.add("((double)analogRead(_potentiometer_", potentiometer.getUserDefinedPort(), "))*5/1024");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.src.add("_hcsr04_", ultrasonicSensor.getUserDefinedPort(), ".getDistance()");
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        this.src.add("_hdc1080_", humiditySensor.getUserDefinedPort());
        switch ( humiditySensor.getMode() ) {
            case SC.HUMIDITY:
                this.src.add(".getHumidity()");
                break;
            case SC.TEMPERATURE:
                this.src.add(".getTemperature()");
                break;
            default:
                throw new DbcException("Invalide mode for Humidity Sensor!" + humiditySensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.src.add("_bmp280_", temperatureSensor.getUserDefinedPort());
        switch ( temperatureSensor.getMode() ) {
            case SC.TEMPERATURE:
                this.src.add(".getTemperature()");
                break;
            case SC.PRESSURE:
                this.src.add(".getPressure()");
                break;
            default:
                throw new DbcException("Invalide mode for BMP280 Sensor!" + temperatureSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        switch ( vemlLightSensor.getMode() ) {
            case SC.LIGHT:
                this.src.add("_tsl_", vemlLightSensor.getUserDefinedPort(), ".getIlluminance()");
                break;
            case SC.UVLIGHT:
                this.src.add("_veml_", vemlLightSensor.getUserDefinedPort(), ".getUvIntensity()");
                break;
            default:
                throw new DbcException("Invalide mode for VEML/TSL Sensor!" + vemlLightSensor.getMode());
        }
        return null;
    }

    private void updateBmxValues(String portName) {
        nlIndent();
        this.src.add("int _getValueFromBmx(int axis, int mode) {");
        incrIndentation();
        nlIndent();
        this.src.add("int _x_axis;");
        nlIndent();
        this.src.add("int _y_axis;");
        nlIndent();
        this.src.add("int _z_axis;");
        nlIndent();
        this.src.add("switch (mode) {");
        incrIndentation();
        nlIndent();
        this.src.add("case 1:");
        incrIndentation();
        nlIndent();
        this.src.add("_bmx055_", portName, ".getRotation(&_x_axis, &_y_axis, &_z_axis);");
        nlIndent();
        this.src.add("break;");
        decrIndentation();
        nlIndent();
        this.src.add("case 2:");
        incrIndentation();
        nlIndent();
        this.src.add("_bmx055_", portName, ".getMagnet(&_x_axis, &_y_axis, &_z_axis);");
        nlIndent();
        this.src.add("break;");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        this.src.add("switch (axis) {");
        incrIndentation();
        nlIndent();
        this.src.add("case 1:");
        incrIndentation();
        nlIndent();
        this.src.add("return _x_axis;");
        decrIndentation();
        nlIndent();
        this.src.add("case 2:");
        incrIndentation();
        nlIndent();
        this.src.add("return _y_axis;");
        decrIndentation();
        nlIndent();
        this.src.add("case 3:");
        incrIndentation();
        nlIndent();
        this.src.add("return _z_axis;");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.src.add("}");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        nlIndent();
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.src.add("_bmx055_", accelerometerSensor.getUserDefinedPort(), ".getAcceleration", accelerometerSensor.getMode(), "()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        switch ( gyroSensor.getMode() ) {
            case "X":
                this.src.add("_getValueFromBmx(1, 1)");
                break;
            case "Y":
                this.src.add("_getValueFromBmx(2, 1)");
                break;
            case "Z":
                this.src.add("_getValueFromBmx(3, 1)");
                break;
        }
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        throw new DbcException("GyroReset not implemented");
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        switch ( compassSensor.getMode() ) {
            case "X":
                this.src.add("_getValueFromBmx(1, 2)");
                break;
            case "Y":
                this.src.add("_getValueFromBmx(2, 2)");
                break;
            case "Z":
                this.src.add("_getValueFromBmx(3, 2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        switch ( pinWriteValueAction.pinValue ) {
            case SC.ANALOG:
                this.src.add("analogWrite(_output_", pinWriteValueAction.port, ", ");
                pinWriteValueAction.value.accept(this);
                this.src.add(");");
                break;
            case SC.DIGITAL:
                this.src.add("digitalWrite(_output_", pinWriteValueAction.port, ", ");
                pinWriteValueAction.value.accept(this);
                this.src.add(");");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
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

    @Override
    public Void visitSendDataAction(SendDataAction sendDataAction) {
        if ( sendDataAction.destination.equals("SENSEMAP") ) {
            for ( Pair<String, Expr> entry : sendDataAction.id2Phenomena ) {
                this.src.add("_osm.uploadMeasurement(");
                entry.getSecond().accept(this);
                this.src.add(", _", entry.getFirst(), ");");
                nlIndent();
            }
        } else if ( sendDataAction.destination.equals("SDCARD") ) {
            ConfigurationComponent cc = this.configuration.optConfigurationComponentByType(SC.SENSEBOX_SDCARD);
            if ( cc == null ) {
                return null;
            }
            String filename = cc.getOptProperty("NAO_FILENAME");
            this.src.add("_dataFile = SD.open(", "\"", filename, "\", FILE_WRITE);");
            nlIndent();
            for ( Pair<String, Expr> entry : sendDataAction.id2Phenomena ) {
                this.src.add("_dataFile.print(_", entry.getFirst(), ");");
                nlIndent();
                this.src.add("_dataFile.print(\" : \");");
                nlIndent();
                this.src.add("_dataFile.println(");
                entry.getSecond().accept(this);
                this.src.add(");");
                nlIndent();
            }
            this.src.add("_dataFile.close();");
        } else {
            throw new DbcException("SendDataAction visitor in sensebox: no valid destination found");
        }
        return null;
    }

    @Override
    public Void visitParticleSensor(ParticleSensor particleSensor) {
        switch ( particleSensor.getMode() ) {
            case "PM25":
                this.src.add("_sds011_", particleSensor.getUserDefinedPort(), ".getPm25()");
                break;
            case "PM10":
                this.src.add("_sds011_", particleSensor.getUserDefinedPort(), ".getPm10()");
                break;
            default:
                throw new DbcException("Wrong mode for particle sensor");
        }
        return null;
    }

    @Override
    public Void visitGpsSensor(GpsSensor gpsSensor) {
        String mode = gpsSensor.getMode().charAt(0) + gpsSensor.getMode().substring(1).toLowerCase();
        this.src.add("_gps_", gpsSensor.getUserDefinedPort(), ".get", mode, "()");
        return null;
    }

    @Override
    public Void visitEnvironmentalSensor(EnvironmentalSensor environmentalSensor) {
        ConfigurationComponent cc = this.configuration.optConfigurationComponent(environmentalSensor.getUserDefinedPort());

        String mode;
        switch ( environmentalSensor.getMode() ) {
            case "CALIBRATION":
                mode = "iaqAccuracy";
                break;
            case "CO2EQUIVALENT":
                mode = "co2Equivalent";
                break;
            case "VOCEQUIVALENT":
                mode = "breathVocEquivalent";
                break;
            default:
                mode = environmentalSensor.getMode().toLowerCase(Locale.ENGLISH);
        }

        this.src.add("_readIaq(_iaqSensor_", cc.userDefinedPortName, ", _iaqSensor_", cc.userDefinedPortName, ".", mode, ")");
        return null;
    }

    private void generateConfigurationSetup() {
        String bmx55PortName;
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.LED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.LED) ) {
                            this.src.add("pinMode(_led_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.RGBLED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.RGBLED) ) {
                            this.src.add("_rgbled_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                            this.nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.KEY:
                    this.src.add("pinMode(_button_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
                    this.nlIndent();
                    break;
                case SC.HUMIDITY:
                    this.src.add("_hdc1080_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    this.nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.src.add("_bmp280_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    this.nlIndent();
                    break;
                case SC.LIGHTVEML:
                    this.src.add("_veml_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    this.nlIndent();
                    this.src.add("_tsl_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    this.nlIndent();
                    break;
                case SC.WIRELESS:
                    this.src.add("_bee_", "->connectToWifi(\"", this.SSID, "\",\"", this.password, "\");");
                    this.nlIndent();
                    this.src.add("delay(1000);");
                    this.nlIndent();
                    break;
                case SC.ACCELEROMETER:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.ACCELEROMETER) ) {
                            this.src.add("_bmx055_", usedConfigurationBlock.userDefinedPortName, ".beginAcc(0x03);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.COMPASS:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.COMPASS) ) {
                            bmx55PortName = this.configuration.optConfigurationComponentByType(SC.ACCELEROMETER).userDefinedPortName;
                            this.src.add("_bmx055_", bmx55PortName, ".beginMagn();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.GYRO:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.GYRO) ) {
                            bmx55PortName = this.configuration.optConfigurationComponentByType(SC.ACCELEROMETER).userDefinedPortName;
                            this.src.add("_bmx055_", bmx55PortName, ".beginGyro();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.SENSEBOX_SDCARD:
                    this.src.add("SD.begin(28);");
                    nlIndent();
                    this.src.add("_dataFile = SD.open(", "\"", usedConfigurationBlock.getOptProperty("NAO_FILENAME"), "\", FILE_WRITE);");
                    nlIndent();
                    this.src.add("_dataFile.close();");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    this.src.add("senseBoxIO.powerI2C(true);");
                    nlIndent();
                    this.src.add("delay(2000);");
                    nlIndent();
                    this.src.add("_display_", usedConfigurationBlock.userDefinedPortName, ".begin(SSD1306_SWITCHCAPVCC, 0x3D);");
                    nlIndent();
                    this.src.add("_display_", usedConfigurationBlock.userDefinedPortName, ".display();");
                    nlIndent();
                    this.src.add("delay(100);");
                    nlIndent();
                    this.src.add("_display_", usedConfigurationBlock.userDefinedPortName, ".clearDisplay();");
                    nlIndent();
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.SENSEBOX_PLOTTING) ) {
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setTitle(\"", usedConfigurationBlock.getProperty("TITLE"), "\");");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setXLabel(\"", usedConfigurationBlock.getProperty("XLABEL"), "\");");

                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setYLabel(\"", usedConfigurationBlock.getProperty("YLABEL"), "\");");

                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName,
                                ".setXRange(", usedConfigurationBlock.getProperty("XSTART"), ", ", usedConfigurationBlock.getProperty("XEND"), ");");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName,
                                ".setYRange(", usedConfigurationBlock.getProperty("YSTART"), ", ", usedConfigurationBlock.getProperty("YEND"), ");");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setXTick(", usedConfigurationBlock.getProperty("XTICK"), ");");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setYTick(", usedConfigurationBlock.getProperty("YTICK"), ");");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setXPrecision(0);");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".setYPrecision(0);");
                            nlIndent();
                            this.src.add("_plot_", usedConfigurationBlock.userDefinedPortName, ".clear();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.DIGITAL_PIN:
                case SC.ANALOG_PIN:
                    this.src.add("pinMode(_input_", usedConfigurationBlock.userDefinedPortName, ", INPUT);");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.src.add("pinMode(_output_", usedConfigurationBlock.userDefinedPortName, ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.PARTICLE:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.PARTICLE) ) {
                            this.src.add(usedConfigurationBlock.getProperty("SERIAL"), ".begin(9600);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.GPS:
                    this.src.add("_gps_", usedConfigurationBlock.userDefinedPortName, ".begin();");
                    nlIndent();
                    break;
                case SC.ENVIRONMENTAL:
                    this.src.add("Wire.begin();");
                    nlIndent();
                    this.src.add("_iaqSensor_", usedConfigurationBlock.userDefinedPortName, ".begin(BME680_I2C_ADDR_PRIMARY, Wire);");
                    nlIndent();
                    this.src.add("bsec_virtual_sensor_t _sensorList[10] = {");
                    incrIndentation();
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_RAW_TEMPERATURE,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_RAW_PRESSURE,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_RAW_HUMIDITY,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_RAW_GAS,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_IAQ,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_STATIC_IAQ,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_CO2_EQUIVALENT,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_BREATH_VOC_EQUIVALENT,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_SENSOR_HEAT_COMPENSATED_TEMPERATURE,");
                    nlIndent();
                    this.src.add("BSEC_OUTPUT_SENSOR_HEAT_COMPENSATED_HUMIDITY,");
                    decrIndentation();
                    nlIndent();
                    this.src.add("};");
                    nlIndent();
                    this.src.add("_iaqSensor_", usedConfigurationBlock.userDefinedPortName, ".updateSubscription(_sensorList, 10, BSEC_SAMPLE_RATE_LP);");
                    nlIndent();
                    break;
                // no additional configuration needed:
                case SC.ROBOT:
                case SC.ULTRASONIC:
                case SC.POTENTIOMETER:
                case SC.LIGHT:
                case SC.BUZZER:
                case SC.SOUND:
                case SC.SENSEBOX:
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
                case SC.LED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.LED) ) {
                            this.src.add("int _led_", blockName, " = ", cc.getProperty("INPUT"), ";");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.RGBLED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.RGBLED) ) {
                            this.src.add("Adafruit_NeoPixel _rgbled_", blockName, " = Adafruit_NeoPixel(1, ",
                                cc.getOptProperty("INPUT") == null ? cc.getProperty("RED") : cc.getProperty("INPUT"), ", NEO_RGB + NEO_KHZ800);");
                            this.nlIndent();
                            this.src.add("int _v_colour_temp;");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.KEY:
                    this.src.add("int _button_", blockName, " = ", cc.getProperty("PIN1"), ";");
                    this.nlIndent();
                    break;
                case SC.LIGHT:
                    this.src.add("int _output_", blockName, " = ", cc.getProperty("OUTPUT"), ";");
                    this.nlIndent();
                    break;
                case SC.POTENTIOMETER:
                    this.src.add("int _potentiometer_", blockName, " = ", cc.getProperty("OUTPUT"), ";");
                    this.nlIndent();
                    break;
                case SC.BUZZER:
                    this.src.add("int _buzzer_", blockName, " = ", cc.getProperty("+"), ";");
                    this.nlIndent();
                    break;
                case SC.SOUND:
                    this.src.add("int _mic_", blockName, " = ", cc.getProperty("OUT"), ";");
                    this.nlIndent();
                    break;
                case SC.HUMIDITY:
                    this.src.add("HDC1080 _hdc1080_", blockName, ";");
                    this.nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.src.add("BMP280 _bmp280_", blockName, ";");
                    this.nlIndent();
                    break;
                case SC.ULTRASONIC:
                    this.src.add("Ultrasonic _hcsr04_", blockName, "(", cc.getProperty("TRIG"), ", ", cc.getProperty("ECHO"), ");");
                    this.nlIndent();
                    break;
                case SC.LIGHTVEML:
                    this.src.add("VEML6070 _veml_", blockName, ";");
                    this.nlIndent();
                    this.src.add("TSL45315 _tsl_", blockName, ";");
                    this.nlIndent();
                    break;
                case SC.WIRELESS:
                    this.src.add("Bee* _bee_ = new Bee();");
                    this.nlIndent();
                    ConfigurationComponent sensebox = this.configuration.optConfigurationComponentByType(SC.SENSEBOX);
                    if ( sensebox != null ) {
                        for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                            if ( usedActor.getType().equals(SC.SEND_DATA) ) {
                                this.src.add("OpenSenseMap _osm(\"", sensebox.userDefinedPortName, "\", _bee_);");
                                nlIndent();
                                break;
                            }
                        }
                    } else {
                        throw new DbcException("SenseBox brick configuration block is missing!");
                    }
                    break;
                case SC.SENSEBOX:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.SEND_DATA) ) {
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
                                if ( !names[i].isEmpty() ) {
                                    this.src.add("char* _", names[i], " = \"", ids[i], "\";");
                                    nlIndent();
                                }
                            }
                            break;
                        }
                    }
                    break;
                case SC.LCDI2C:
                    this.src.add("#define OLED_RESET 4");
                    nlIndent();
                    this.src.add("Adafruit_SSD1306 _display_", cc.userDefinedPortName, "(OLED_RESET);");
                    nlIndent();
                    break;
                case SC.SENSEBOX_SDCARD:
                    this.src.add("File _dataFile;");
                    nlIndent();
                    break;
                case SC.ACCELEROMETER:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.COMPASS)
                            || usedSensor.getType().equals(SC.ACCELEROMETER)
                            || usedSensor.getType().equals(SC.GYRO) ) {
                            nlIndent();
                            this.src.add("BMX055 _bmx055_", blockName, ";");
                            updateBmxValues(blockName);
                            break;
                        }
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
                case SC.SENSEBOX_PLOTTING:
                case SC.COMPASS:
                case SC.GYRO:
                    break;
                case SC.GPS:
                    this.src.add("GPS _gps_", cc.userDefinedPortName, ";");
                    nlIndent();
                    break;
                case SC.PARTICLE:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.PARTICLE) ) {
                            this.src.add("SDS011 _sds011", "_", cc.userDefinedPortName, "(", cc.getProperty("SERIAL"), ");");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.ENVIRONMENTAL:
                    this.src.add("Bsec _iaqSensor_", cc.userDefinedPortName, ';');
                    nlIndent();
                    break;
                case SC.ROBOT:
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.componentType);
            }
        }
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            if ( cc.componentType.equals(SC.LCDI2C) ) {
                String blockName = cc.userDefinedPortName;
                ConfigurationComponent displayConfigurationComponent = this.configuration.optConfigurationComponentByType(SC.LCDI2C);
                String displayName;
                if ( displayConfigurationComponent != null ) {
                    displayName = displayConfigurationComponent.userDefinedPortName;
                } else {
                    displayName = "NO PORT";
                }
                this.src.add("Plot _plot_", blockName, "(&", "_display_", displayName, ");");
                nlIndent();
            }
        }
    }
}

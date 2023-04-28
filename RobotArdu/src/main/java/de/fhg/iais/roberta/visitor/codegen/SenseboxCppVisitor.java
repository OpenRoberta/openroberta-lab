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
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
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
        this.sb.append("void setup()");
        this.nlIndent();
        this.sb.append("{");
        this.incrIndentation();
        this.nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            this.sb.append("Serial.begin(9600);");
            nlIndent();
        }
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
        this.sb.append("// This file is automatically generated by the Open Roberta Lab.");
        this.nlIndent();
        this.sb.append("#define _ARDUINO_STL_NOT_NEEDED");
        this.nlIndent();
        this.sb.append("#include \"SenseBoxMCU.h\"");
        this.nlIndent();
        this.sb.append("#undef max"); // TODO needed?
        this.nlIndent();
        this.sb.append("#undef min"); // TODO needed?
        this.nlIndent();
        this.sb.append("#include <NEPODefs.h>");
        this.nlIndent();
        if ( this.configuration.optConfigurationComponentByType(SC.SENSEBOX_SDCARD) != null ) {
            this.sb.append("#include <SD.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.LCDI2C) != null ) {
            this.sb.append("#include <Adafruit_GFX.h>");
            this.nlIndent();
            this.sb.append("#include <Adafruit_SSD1306.h>");
            this.nlIndent();
            this.sb.append("#include <Plot.h>");
            this.nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isListsUsed() ) {
            this.sb.append("#include <stdlib.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.ENVIRONMENTAL) != null ) {
            this.sb.append("#include <bsec.h>");
            this.nlIndent();
            this.sb.append("#define _readIaq(X, Y) ((X.run()) ? Y : Y)");
            this.nlIndent();
            this.sb.append("#include <Wire.h>");
            this.nlIndent();
        }
        if ( this.configuration.optConfigurationComponentByType(SC.RGBLED) != null ) {
            this.sb.append("#include <Adafruit_NeoPixel.h>");
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
        this.sb.append("_display_").append(clearDisplayAction.port).append(".clearDisplay();");
        nlIndent();
        this.sb.append("_display_").append(clearDisplayAction.port).append(".display();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.sb.append("_display_").append(showTextAction.port).append(".setCursor(");
        showTextAction.x.accept(this);
        this.sb.append(", ");
        showTextAction.y.accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.port).append(".setTextSize(1);");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.port).append(".setTextColor(WHITE, BLACK);");
        nlIndent();
        this.sb.append("_printToDisplay(").append("_display_").append(showTextAction.port).append(", ");
        showTextAction.msg.accept(this);
        this.sb.append(", true);");
        nlIndent();
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction plotPointAction) {
        this.sb.append("_plot_").append(plotPointAction.port).append(".addDataPoint(");
        plotPointAction.tickmark.accept(this);
        this.sb.append(", ");
        plotPointAction.value.accept(this);
        this.sb.append(");");
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
            this.sb.append("_plot_").append(portName).append(".clear();");
            nlIndent();
            this.sb.append("_plot_").append(portName).append(".drawPlot();");
            nlIndent();
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.sb.append("tone(_buzzer_").append(toneAction.port).append(", ");
        toneAction.frequency.accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("delay(");
        toneAction.duration.accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("noTone(_buzzer_").append(toneAction.port).append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        throw new DbcException("play note not supported");
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.sb.append("get_microphone_volume(_mic_").append(soundSensor.getUserDefinedPort()).append(")");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        if ( !lightAction.mode.toString().equals(BlocklyConstants.DEFAULT) ) {
            this.sb.append("digitalWrite(_led_").append(lightAction.port).append(", ").append(lightAction.mode.getValues()[0] + ");");
        } else {
            if ( lightAction.rgbLedColor.getClass().equals(ColorConst.class) ) {
                ColorConst colorConst = (ColorConst) lightAction.rgbLedColor;
                this.sb
                    .append("_rgbled_")
                    .append(lightAction.port)
                    .append(".setPixelColor(0, _rgbled_")
                    .append(lightAction.port)
                    .append(".Color(")
                    .append(colorConst.getRedChannelInt())
                    .append(", ")
                    .append(colorConst.getGreenChannelInt())
                    .append(", ")
                    .append(colorConst.getBlueChannelInt())
                    .append("));");
            } else if ( lightAction.rgbLedColor.getClass().equals(Var.class) ) {
                String tempVarName = "___" + ((Var) lightAction.rgbLedColor).name;
                this.sb
                    .append("_rgbled_")
                    .append(lightAction.port)
                    .append(".setPixelColor(0, _rgbled_")
                    .append(lightAction.port)
                    .append(".Color(RCHANNEL(")
                    .append(tempVarName)
                    .append("), GCHANNEL(")
                    .append(tempVarName)
                    .append("), BCHANNEL(")
                    .append(tempVarName)
                    .append(")));");
            } else if ( lightAction.rgbLedColor.getClass().equals(MethodExpr.class) ) {
                String tempVarName = "_v_colour_temp";
                this.sb.append(tempVarName).append(" = ");
                visitMethodCall((MethodCall) ((MethodExpr) lightAction.rgbLedColor).method);
                this.sb.append(";");
                nlIndent();
                this.sb
                    .append("_rgbled_")
                    .append(lightAction.port)
                    .append(".setPixelColor(0, _rgbled_")
                    .append(lightAction.port)
                    .append(".Color(RCHANNEL(")
                    .append(tempVarName)
                    .append("), GCHANNEL(")
                    .append(tempVarName)
                    .append("), BCHANNEL(")
                    .append(tempVarName)
                    .append(")));");
            } else if ( lightAction.rgbLedColor.getClass().equals(FunctionExpr.class) ) {
                String tempVarName = "_v_colour_temp";
                this.sb.append(tempVarName).append(" = ");
                ((FunctionExpr) lightAction.rgbLedColor).function.accept(this);
                this.sb.append(";");
                nlIndent();
                this.sb
                    .append("_rgbled_")
                    .append(lightAction.port)
                    .append(".setPixelColor(0, _rgbled_")
                    .append(lightAction.port)
                    .append(".Color(RCHANNEL(")
                    .append(tempVarName)
                    .append("), GCHANNEL(")
                    .append(tempVarName)
                    .append("), BCHANNEL(")
                    .append(tempVarName)
                    .append(")));");
            } else {
                this.sb.append("_rgbled_").append(lightAction.port).append(".setPixelColor(0, _rgbled_").append(lightAction.port).append(".Color(");
                ((RgbColor) lightAction.rgbLedColor).R.accept(this);
                this.sb.append(", ");
                ((RgbColor) lightAction.rgbLedColor).G.accept(this);
                this.sb.append(", ");
                ((RgbColor) lightAction.rgbLedColor).B.accept(this);
                this.sb.append("));");
            }
            nlIndent();
            this.sb.append("_rgbled_").append(lightAction.port).append(".show();");
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        this.sb
            .append("_rgbled_")
            .append(lightStatusAction.getUserDefinedPort())
            .append(".setPixelColor(0, _rgbled_")
            .append(lightStatusAction.getUserDefinedPort())
            .append(".Color(0,0,0));");
        this.nlIndent();
        this.sb.append("_rgbled_").append(lightStatusAction.getUserDefinedPort()).append(".show();");
        this.nlIndent();
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction relayAction) {
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor button) {
        this.sb.append("digitalRead(_button_").append(button.getUserDefinedPort()).append(")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.sb.append("analogRead(_output_").append(lightSensor.getUserDefinedPort()).append(")/10.24");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor potentiometer) {
        this.sb.append("((double)analogRead(_potentiometer_").append(potentiometer.getUserDefinedPort()).append("))*5/1024");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.sb.append("_hcsr04_").append(ultrasonicSensor.getUserDefinedPort()).append(".getDistance()");
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor humiditySensor) {
        this.sb.append("_hdc1080_").append(humiditySensor.getUserDefinedPort());
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
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        this.sb.append("_bmp280_").append(temperatureSensor.getUserDefinedPort());
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
    public Void visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        switch ( vemlLightSensor.getMode() ) {
            case SC.LIGHT:
                this.sb.append("_tsl_").append(vemlLightSensor.getUserDefinedPort()).append(".getIlluminance()");
                break;
            case SC.UVLIGHT:
                this.sb.append("_veml_").append(vemlLightSensor.getUserDefinedPort()).append(".getUvIntensity()");
                break;
            default:
                throw new DbcException("Invalide mode for VEML/TSL Sensor!" + vemlLightSensor.getMode());
        }
        return null;
    }

    private void updateBmxValues(String portName) {
        nlIndent();
        this.sb.append("int _getValueFromBmx(int axis, int mode) {");
        incrIndentation();
        nlIndent();
        this.sb.append("int _x_axis;");
        nlIndent();
        this.sb.append("int _y_axis;");
        nlIndent();
        this.sb.append("int _z_axis;");
        nlIndent();
        this.sb.append("switch (mode) {");
        incrIndentation();
        nlIndent();
        this.sb.append("case 1:");
        incrIndentation();
        nlIndent();
        this.sb.append("_bmx055_").append(portName).append(".getRotation(&_x_axis, &_y_axis, &_z_axis);");
        nlIndent();
        this.sb.append("break;");
        decrIndentation();
        nlIndent();
        this.sb.append("case 2:");
        incrIndentation();
        nlIndent();
        this.sb.append("_bmx055_").append(portName).append(".getMagnet(&_x_axis, &_y_axis, &_z_axis);");
        nlIndent();
        this.sb.append("break;");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        this.sb.append("switch (axis) {");
        incrIndentation();
        nlIndent();
        this.sb.append("case 1:");
        incrIndentation();
        nlIndent();
        this.sb.append("return _x_axis;");
        decrIndentation();
        nlIndent();
        this.sb.append("case 2:");
        incrIndentation();
        nlIndent();
        this.sb.append("return _y_axis;");
        decrIndentation();
        nlIndent();
        this.sb.append("case 3:");
        incrIndentation();
        nlIndent();
        this.sb.append("return _z_axis;");
        decrIndentation();
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        nlIndent();
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        this.sb.append("_bmx055_").append(accelerometerSensor.getUserDefinedPort()).append(".getAcceleration").append(accelerometerSensor.getMode()).append("()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        switch ( gyroSensor.getMode() ) {
            case "X":
                this.sb.append("_getValueFromBmx(1, 1)");
                break;
            case "Y":
                this.sb.append("_getValueFromBmx(2, 1)");
                break;
            case "Z":
                this.sb.append("_getValueFromBmx(3, 1)");
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
                this.sb.append("_getValueFromBmx(1, 2)");
                break;
            case "Y":
                this.sb.append("_getValueFromBmx(2, 2)");
                break;
            case "Z":
                this.sb.append("_getValueFromBmx(3, 2)");
                break;
        }
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        switch ( pinWriteValueAction.pinValue ) {
            case SC.ANALOG:
                this.sb.append("analogWrite(_output_").append(pinWriteValueAction.port).append(", ");
                pinWriteValueAction.value.accept(this);
                this.sb.append(");");
                break;
            case SC.DIGITAL:
                this.sb.append("digitalWrite(_output_").append(pinWriteValueAction.port).append(", ");
                pinWriteValueAction.value.accept(this);
                this.sb.append(");");
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
                this.sb.append("analogRead(_input_").append(pinGetValueSensor.getUserDefinedPort()).append(")");
                break;
            case SC.DIGITAL:
                this.sb.append("digitalRead(_input_").append(pinGetValueSensor.getUserDefinedPort()).append(")");
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
                this.sb.append("_osm.uploadMeasurement(");
                entry.getSecond().accept(this);
                this.sb.append(", _").append(entry.getFirst()).append(");");
                nlIndent();
            }
        } else if ( sendDataAction.destination.equals("SDCARD") ) {
            ConfigurationComponent cc = this.configuration.optConfigurationComponentByType(SC.SENSEBOX_SDCARD);
            if ( cc == null ) {
                return null;
            }
            String filename = cc.getOptProperty("NAO_FILENAME");
            this.sb.append("_dataFile = SD.open(").append("\"").append(filename).append("\", FILE_WRITE);");
            nlIndent();
            for ( Pair<String, Expr> entry : sendDataAction.id2Phenomena ) {
                this.sb.append("_dataFile.print(_").append(entry.getFirst()).append(");");
                nlIndent();
                this.sb.append("_dataFile.print(\" : \");");
                nlIndent();
                this.sb.append("_dataFile.println(");
                entry.getSecond().accept(this);
                this.sb.append(");");
                nlIndent();
            }
            this.sb.append("_dataFile.close();");
        } else {
            throw new DbcException("SendDataAction visitor in sensebox: no valid destination found");
        }
        return null;
    }

    @Override
    public Void visitParticleSensor(ParticleSensor particleSensor) {
        switch ( particleSensor.getMode() ) {
            case "PM25":
                this.sb.append("_sds011_").append(particleSensor.getUserDefinedPort()).append(".getPm25()");
                break;
            case "PM10":
                this.sb.append("_sds011_").append(particleSensor.getUserDefinedPort()).append(".getPm10()");
                break;
            default:
                throw new DbcException("Wrong mode for particle sensor");
        }
        return null;
    }

    @Override
    public Void visitGpsSensor(GpsSensor gpsSensor) {
        String mode = gpsSensor.getMode().substring(0, 1) + gpsSensor.getMode().substring(1).toLowerCase();
        this.sb.append("_gps_").append(gpsSensor.getUserDefinedPort()).append(".get").append(mode).append("()");
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

        this.sb
            .append("_readIaq(_iaqSensor_")
            .append(cc.userDefinedPortName)
            .append(", _iaqSensor_")
            .append(cc.userDefinedPortName)
            .append(".")
            .append(mode)
            .append(")");
        return null;
    }

    private void generateConfigurationSetup() {
        String bmx55PortName;
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.componentType ) {
                case SC.LED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.LED) ) {
                            this.sb.append("pinMode(_led_").append(usedConfigurationBlock.userDefinedPortName).append(", OUTPUT);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.RGBLED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.RGBLED) ) {
                            this.sb.append("_rgbled_").append(usedConfigurationBlock.userDefinedPortName).append(".begin();");
                            this.nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.KEY:
                    this.sb.append("pinMode(_button_").append(usedConfigurationBlock.userDefinedPortName).append(", INPUT);");
                    this.nlIndent();
                    break;
                case SC.HUMIDITY:
                    this.sb.append("_hdc1080_").append(usedConfigurationBlock.userDefinedPortName).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.TEMPERATURE:
                    this.sb.append("_bmp280_").append(usedConfigurationBlock.userDefinedPortName).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.LIGHTVEML:
                    this.sb.append("_veml_").append(usedConfigurationBlock.userDefinedPortName).append(".begin();");
                    this.nlIndent();
                    this.sb.append("_tsl_").append(usedConfigurationBlock.userDefinedPortName).append(".begin();");
                    this.nlIndent();
                    break;
                case SC.WIRELESS:
                    this.sb
                        .append("_bee_")
                        //.append(usedConfigurationBlock.getUserDefinedPortName())
                        .append("->connectToWifi(\"")
                        .append(this.SSID)
                        .append("\",\"")
                        .append(this.password)
                        .append("\");");
                    this.nlIndent();
                    this.sb.append("delay(1000);");
                    this.nlIndent();
                    break;
                case SC.ACCELEROMETER:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.ACCELEROMETER) ) {
                            this.sb.append("_bmx055_").append(usedConfigurationBlock.userDefinedPortName).append(".beginAcc(0x03);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.COMPASS:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.COMPASS) ) {
                            bmx55PortName = this.configuration.optConfigurationComponentByType(SC.ACCELEROMETER).userDefinedPortName;
                            this.sb.append("_bmx055_").append(bmx55PortName).append(".beginMagn();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.GYRO:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.GYRO) ) {
                            bmx55PortName = this.configuration.optConfigurationComponentByType(SC.ACCELEROMETER).userDefinedPortName;
                            this.sb.append("_bmx055_").append(bmx55PortName).append(".beginGyro();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.SENSEBOX_SDCARD:
                    this.sb.append("SD.begin(28);");
                    nlIndent();
                    this.sb
                        .append("_dataFile = SD.open(")
                        .append("\"")
                        .append(usedConfigurationBlock.getOptProperty("NAO_FILENAME"))
                        .append("\", FILE_WRITE);");
                    nlIndent();
                    this.sb.append("_dataFile.close();");
                    nlIndent();
                    break;
                case SC.LCDI2C:
                    this.sb.append("senseBoxIO.powerI2C(true);");
                    nlIndent();
                    this.sb.append("delay(2000);");
                    nlIndent();
                    this.sb.append("_display_").append(usedConfigurationBlock.userDefinedPortName).append(".begin(SSD1306_SWITCHCAPVCC, 0x3D);");
                    nlIndent();
                    this.sb.append("_display_").append(usedConfigurationBlock.userDefinedPortName).append(".display();");
                    nlIndent();
                    this.sb.append("delay(100);");
                    nlIndent();
                    this.sb.append("_display_").append(usedConfigurationBlock.userDefinedPortName).append(".clearDisplay();");
                    nlIndent();
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.SENSEBOX_PLOTTING) ) {
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setTitle(\"")
                                .append(usedConfigurationBlock.getProperty("TITLE"))
                                .append("\");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setXLabel(\"")
                                .append(usedConfigurationBlock.getProperty("XLABEL"))
                                .append("\");");

                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setYLabel(\"")
                                .append(usedConfigurationBlock.getProperty("YLABEL"))
                                .append("\");");

                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setXRange(")
                                .append(usedConfigurationBlock.getProperty("XSTART"))
                                .append(", ")
                                .append(usedConfigurationBlock.getProperty("XEND"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setYRange(")
                                .append(usedConfigurationBlock.getProperty("YSTART"))
                                .append(", ")
                                .append(usedConfigurationBlock.getProperty("YEND"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setXTick(")
                                .append(usedConfigurationBlock.getProperty("XTICK"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.userDefinedPortName)
                                .append(".setYTick(")
                                .append(usedConfigurationBlock.getProperty("YTICK"))
                                .append(");");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.userDefinedPortName).append(".setXPrecision(0);");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.userDefinedPortName).append(".setYPrecision(0);");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.userDefinedPortName).append(".clear();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.DIGITAL_PIN:
                case SC.ANALOG_PIN:
                    this.sb.append("pinMode(_input_" + usedConfigurationBlock.userDefinedPortName + ", INPUT);");
                    nlIndent();
                    break;
                case SC.ANALOG_INPUT:
                case SC.DIGITAL_INPUT:
                    this.sb.append("pinMode(_output_" + usedConfigurationBlock.userDefinedPortName + ", OUTPUT);");
                    nlIndent();
                    break;
                case SC.PARTICLE:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.PARTICLE) ) {
                            this.sb.append(usedConfigurationBlock.getProperty("SERIAL")).append(".begin(9600);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.GPS:
                    this.sb.append("_gps_" + usedConfigurationBlock.userDefinedPortName + ".begin();");
                    nlIndent();
                    break;
                case SC.ENVIRONMENTAL:
                    this.sb.append("Wire.begin();");
                    nlIndent();
                    this.sb.append("_iaqSensor_").append(usedConfigurationBlock.userDefinedPortName).append(".begin(BME680_I2C_ADDR_PRIMARY, Wire);");
                    nlIndent();
                    this.sb.append("bsec_virtual_sensor_t _sensorList[10] = {");
                    incrIndentation();
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_RAW_TEMPERATURE,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_RAW_PRESSURE,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_RAW_HUMIDITY,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_RAW_GAS,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_IAQ,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_STATIC_IAQ,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_CO2_EQUIVALENT,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_BREATH_VOC_EQUIVALENT,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_SENSOR_HEAT_COMPENSATED_TEMPERATURE,");
                    nlIndent();
                    this.sb.append("BSEC_OUTPUT_SENSOR_HEAT_COMPENSATED_HUMIDITY,");
                    decrIndentation();
                    nlIndent();
                    this.sb.append("};");
                    nlIndent();
                    this.sb
                        .append("_iaqSensor_")
                        .append(usedConfigurationBlock.userDefinedPortName)
                        .append(".updateSubscription(_sensorList, 10, BSEC_SAMPLE_RATE_LP);");
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
                            this.sb.append("int _led_").append(blockName).append(" = ").append(cc.getProperty("INPUT")).append(";");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.RGBLED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.RGBLED) ) {
                            this.sb
                                .append("Adafruit_NeoPixel _rgbled_")
                                .append(blockName)
                                .append(" = Adafruit_NeoPixel(1, ")
                                .append(cc.getOptProperty("INPUT") == null ? cc.getProperty("RED") : cc.getProperty("INPUT"))
                                .append(", NEO_RGB + NEO_KHZ800);");
                            this.nlIndent();
                            this.sb.append("int _v_colour_temp;");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.KEY:
                    this.sb.append("int _button_").append(blockName).append(" = ").append(cc.getProperty("PIN1")).append(";");
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
                    this.sb.append("Bee* _bee_ = new Bee();");
                    this.nlIndent();
                    ConfigurationComponent sensebox = this.configuration.optConfigurationComponentByType(SC.SENSEBOX);
                    if ( sensebox != null ) {
                        for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                            if ( usedActor.getType().equals(SC.SEND_DATA) ) {
                                this.sb.append("OpenSenseMap _osm(\"").append(sensebox.userDefinedPortName).append("\", _bee_);");
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
                                    this.sb.append("char* _").append(names[i]).append(" = \"").append(ids[i]).append("\";");
                                    nlIndent();
                                }
                            }
                            break;
                        }
                    }
                    break;
                case SC.LCDI2C:
                    this.sb.append("#define OLED_RESET 4");
                    nlIndent();
                    this.sb.append("Adafruit_SSD1306 _display_").append(cc.userDefinedPortName).append("(OLED_RESET);");
                    nlIndent();
                    break;
                case SC.SENSEBOX_SDCARD:
                    this.sb.append("File _dataFile;");
                    nlIndent();
                    break;
                case SC.ACCELEROMETER:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.COMPASS)
                            || usedSensor.getType().equals(SC.ACCELEROMETER)
                            || usedSensor.getType().equals(SC.GYRO) ) {
                            nlIndent();
                            this.sb.append("BMX055 _bmx055_").append(blockName).append(";");
                            updateBmxValues(blockName);
                            break;
                        }
                    }
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
                case SC.SENSEBOX_PLOTTING:
                case SC.COMPASS:
                case SC.GYRO:
                    break;
                case SC.GPS:
                    this.sb.append("GPS _gps_" + cc.userDefinedPortName + ";");
                    nlIndent();
                    break;
                case SC.PARTICLE:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.PARTICLE) ) {
                            this.sb
                                .append("SDS011 _sds011")
                                .append("_")
                                .append(cc.userDefinedPortName)
                                .append("(")
                                .append(cc.getProperty("SERIAL"))
                                .append(");");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.ENVIRONMENTAL:
                    this.sb.append("Bsec _iaqSensor_").append(cc.userDefinedPortName).append(';');
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
                this.sb.append("Plot _plot_").append(blockName).append("(&").append("_display_").append(displayName).append(");");
                nlIndent();
            }
        }
    }
}

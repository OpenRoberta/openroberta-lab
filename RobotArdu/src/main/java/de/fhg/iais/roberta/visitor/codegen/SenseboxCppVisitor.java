package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotClearAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.PlotPointAction;
import de.fhg.iais.roberta.syntax.actors.arduino.sensebox.SendDataAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.sensebox.GpsSensor;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class SenseboxCppVisitor extends AbstractCommonArduinoCppVisitor implements IArduinoVisitor<Void> {

    private final String SSID;
    private final String password;

    public SenseboxCppVisitor(
        List<ArrayList<Phrase<Void>>> programPhrases,
        ConfigurationAst brickConfiguration,
        String SSID,
        String password,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, brickConfiguration, beans);
        this.SSID = SSID;
        this.password = password;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.decrIndentation();
        this.nlIndent();
        this.sb.append("unsigned long _time = millis();");
        this.nlIndent();
        mainTask.getVariables().accept(this);
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
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERIAL) ) {
            sb.append("Serial.begin(9600);");
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
        this.sb.append("// This file is automatically generated by the Open Roberta Lab.\n");
        this.sb.append("#define _ARDUINO_STL_NOT_NEEDED\n");
        this.sb.append("#include <Arduino.h>\n");
        this.sb.append("#undef max\n");
        this.sb.append("#undef min\n");
        this.sb.append("#include <NEPODefs.h>\n");
        this.sb.append("#include \"SenseBoxMCU.h\"");
        if ( this.configuration.getConfigurationComponentbyType(SC.SENSEBOX_SDCARD) != null ) {
            this.sb.append("\n#include <SPI.h>");
            this.sb.append("\n#include <SD.h>");
        }

        if ( this.configuration.getConfigurationComponentbyType(SC.LCDI2C) != null ) {
            this.sb.append("\n#include <SPI.h>");
            this.sb.append("\n#include <Wire.h>");
            this.sb.append("\n#include <Adafruit_GFX.h>");
            this.sb.append("\n#include <Adafruit_SSD1306.h>");
            this.sb.append("\n#include <Plot.h>");
        }

        if ( this.getBean(UsedHardwareBean.class).isListsUsed() ) {
            this.sb.append("\n#include <stdlib.h>");
            this.sb.append("\n#include <list>");
        }

        super.generateProgramPrefix(withWrapping);
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
        this.sb.append("_display_").append(showTextAction.getPort()).append(".setCursor(");
        showTextAction.getX().accept(this);
        this.sb.append(", ");
        showTextAction.getY().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.getPort()).append(".setTextSize(1);");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.getPort()).append(".setTextColor(WHITE, BLACK);");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.getPort()).append(".println(");
        showTextAction.getMsg().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("_display_").append(showTextAction.getPort()).append(".display();");
        nlIndent();
        return null;
    }

    @Override
    public Void visitPlotPointAction(PlotPointAction<Void> plotPointAction) {
        this.sb.append("_plot_").append(plotPointAction.getPort()).append(".addDataPoint(");
        plotPointAction.getTickmark().accept(this);
        this.sb.append(", ");
        plotPointAction.getValue().accept(this);
        this.sb.append(");");
        nlIndent();
        return null;
    }

    @Override
    public Void visitPlotClearAction(PlotClearAction<Void> plotClearAction) {
        ConfigurationComponent cc = this.configuration.getConfigurationComponentbyType(SC.LCDI2C);
        String portName = null;
        if ( cc != null ) {
            portName = cc.getUserDefinedPortName();
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
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("tone(_buzzer_").append(toneAction.getPort()).append(", ");
        toneAction.getFrequency().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("delay(");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("noTone(_buzzer_").append(toneAction.getPort()).append(");");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("get_microphone_volume(_mic_").append(soundSensor.getPort()).append(")");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( !lightAction.getMode().toString().equals(BlocklyConstants.DEFAULT) ) {
            this.sb.append("digitalWrite(_led_").append(lightAction.getPort()).append(", ").append(lightAction.getMode().getValues()[0] + ");");
        } else {
            if ( lightAction.getRgbLedColor().getClass().equals(ColorConst.class) ) {
                ColorConst<Void> colorConst = (ColorConst<Void>) lightAction.getRgbLedColor();
                Map<String, String> colorConstChannels = new HashMap<>();
                colorConstChannels.put("red", colorConst.getRedChannelHex());
                colorConstChannels.put("green", colorConst.getGreenChannelHex());
                colorConstChannels.put("blue", colorConst.getBlueChannelHex());
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
                String tempVarName = "___" + ((Var<Void>) lightAction.getRgbLedColor()).getValue();
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
                v.accept(this);
                this.sb.append(");");
                this.nlIndent();
            });
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("analogWrite(_led_red_").append(lightStatusAction.getPort()).append(", 0);");
        this.nlIndent();
        this.sb.append("analogWrite(_led_green_").append(lightStatusAction.getPort()).append(", 0);");
        this.nlIndent();
        this.sb.append("analogWrite(_led_blue_").append(lightStatusAction.getPort()).append(", 0);");
        this.nlIndent();
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        this.sb.append("Serial.println(");
        serialWriteAction.getValue().accept(this);
        this.sb.append(");");
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
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.sb.append("_bmx055_").append(accelerometerSensor.getPort()).append(".getAcceleration").append(accelerometerSensor.getMode()).append("()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
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
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
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
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueAction) {
        switch ( pinWriteValueAction.getMode() ) {
            case SC.ANALOG:
                this.sb.append("analogWrite(_output_").append(pinWriteValueAction.getPort()).append(", ");
                pinWriteValueAction.getValue().accept(this);
                this.sb.append(");");
                break;
            case SC.DIGITAL:
                this.sb.append("digitalWrite(_output_").append(pinWriteValueAction.getPort()).append(", ");
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

    @Override
    public Void visitDataSendAction(SendDataAction<Void> sendDataAction) {
        if ( sendDataAction.getDestination().equals("SENSEMAP") ) {
            for ( Pair<String, Expr<Void>> entry : sendDataAction.getId2Phenomena() ) {
                this.sb.append("_osm.uploadMeasurement(");
                entry.getSecond().accept(this);
                this.sb.append(", _").append(entry.getFirst()).append(");");
                nlIndent();
            }
        } else if ( sendDataAction.getDestination().equals("SDCARD") ) {
            ConfigurationComponent cc = this.configuration.getConfigurationComponentbyType(SC.SENSEBOX_SDCARD);
            if ( cc == null ) {
                return null;
            }
            String filename = cc.getOptProperty("NAO_FILENAME");
            this.sb.append("_dataFile = SD.open(").append("\"").append(filename).append("\", FILE_WRITE);");
            nlIndent();
            for ( Pair<String, Expr<Void>> entry : sendDataAction.getId2Phenomena() ) {
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
    public Void visitParticleSensor(ParticleSensor<Void> particleSensor) {
        switch ( particleSensor.getMode() ) {
            case "PM25":
                this.sb.append("_sds011_").append(particleSensor.getPort()).append(".getPm25()");
                break;
            case "PM10":
                this.sb.append("_sds011_").append(particleSensor.getPort()).append(".getPm10()");
                break;
            default:
                throw new DbcException("Wrong mode for particle sensor");
        }
        return null;
    }

    @Override
    public Void visitGpsSensor(GpsSensor<Void> gpsSensor) {
        String mode = gpsSensor.getMode().substring(0, 1) + gpsSensor.getMode().substring(1).toLowerCase();
        this.sb.append("_gps_").append(gpsSensor.getPort()).append(".get").append(mode).append("()");
        return null;
    }

    private void generateConfigurationSetup() {
        String bmx55PortName;
        for ( ConfigurationComponent usedConfigurationBlock : this.configuration.getConfigurationComponentsValues() ) {
            switch ( usedConfigurationBlock.getComponentType() ) {
                case SC.LED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.LED) ) {
                            this.sb.append("pinMode(_led_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.RGBLED:
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.RGBLED) ) {
                            this.sb.append("pinMode(_led_red_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                            this.nlIndent();
                            this.sb.append("pinMode(_led_green_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                            this.nlIndent();
                            this.sb.append("pinMode(_led_blue_").append(usedConfigurationBlock.getUserDefinedPortName()).append(", OUTPUT);");
                            this.nlIndent();
                            break;
                        }
                    }
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
                            this.sb.append("_bmx055_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".beginAcc(0x03);");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.COMPASS:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.COMPASS) ) {
                            bmx55PortName = this.configuration.getConfigurationComponentbyType(SC.ACCELEROMETER).getUserDefinedPortName();
                            this.sb.append("_bmx055_").append(bmx55PortName).append(".beginMagn();");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                case SC.GYRO:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.GYRO) ) {
                            bmx55PortName = this.configuration.getConfigurationComponentbyType(SC.ACCELEROMETER).getUserDefinedPortName();
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
                    this.sb.append("_display_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".begin(SSD1306_SWITCHCAPVCC, 0x3D);");
                    nlIndent();
                    this.sb.append("_display_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".display();");
                    nlIndent();
                    this.sb.append("delay(100);");
                    nlIndent();
                    this.sb.append("_display_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".clearDisplay();");
                    nlIndent();
                    for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                        if ( usedActor.getType().equals(SC.SENSEBOX_PLOTTING) ) {
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setTitle(\"")
                                .append(usedConfigurationBlock.getProperty("TITLE"))
                                .append("\");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setXLabel(\"")
                                .append(usedConfigurationBlock.getProperty("XLABEL"))
                                .append("\");");

                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setYLabel(\"")
                                .append(usedConfigurationBlock.getProperty("YLABEL"))
                                .append("\");");

                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setXRange(")
                                .append(usedConfigurationBlock.getProperty("XSTART"))
                                .append(", ")
                                .append(usedConfigurationBlock.getProperty("XEND"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setYRange(")
                                .append(usedConfigurationBlock.getProperty("YSTART"))
                                .append(", ")
                                .append(usedConfigurationBlock.getProperty("YEND"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setXTick(")
                                .append(usedConfigurationBlock.getProperty("XTICK"))
                                .append(");");
                            nlIndent();
                            this.sb
                                .append("_plot_")
                                .append(usedConfigurationBlock.getUserDefinedPortName())
                                .append(".setYTick(")
                                .append(usedConfigurationBlock.getProperty("YTICK"))
                                .append(");");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".setXPrecision(0);");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".setYPrecision(0);");
                            nlIndent();
                            this.sb.append("_plot_").append(usedConfigurationBlock.getUserDefinedPortName()).append(".clear();");
                            nlIndent();
                            break;
                        }
                    }
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
                    this.sb.append("_gps_" + usedConfigurationBlock.getUserDefinedPortName() + ".begin();");
                    nlIndent();
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
    }

    private void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
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
                            this.sb.append("int _led_red_").append(blockName).append(" = ").append(cc.getProperty("RED")).append(";");
                            this.nlIndent();
                            this.sb.append("int _led_green_").append(blockName).append(" = ").append(cc.getProperty("GREEN")).append(";");
                            this.nlIndent();
                            this.sb.append("int _led_blue_").append(blockName).append(" = ").append(cc.getProperty("BLUE")).append(";");
                            this.nlIndent();
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
                    ConfigurationComponent sensebox = this.configuration.getConfigurationComponentbyType(SC.SENSEBOX);
                    if ( sensebox != null ) {
                        for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                            if ( usedActor.getType().equals(SC.SEND_DATA) ) {
                                this.sb.append("OpenSenseMap _osm(\"").append(sensebox.getUserDefinedPortName()).append("\", _bee_);");
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
                    this.sb.append("Adafruit_SSD1306 _display_").append(cc.getUserDefinedPortName()).append("(OLED_RESET);");
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
                    this.sb.append("GPS _gps_" + cc.getUserDefinedPortName() + ";");
                    nlIndent();
                    break;
                case SC.PARTICLE:
                    for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                        if ( usedSensor.getType().equals(SC.PARTICLE) ) {
                            this.sb
                                .append("SDS011 _sds011")
                                .append("_")
                                .append(cc.getUserDefinedPortName())
                                .append("(")
                                .append(cc.getProperty("SERIAL"))
                                .append(");");
                            nlIndent();
                            break;
                        }
                    }
                    break;
                default:
                    throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
        for ( ConfigurationComponent cc : this.configuration.getConfigurationComponentsValues() ) {
            if ( cc.getComponentType().equals(SC.LCDI2C) ) {
                String blockName = cc.getUserDefinedPortName();
                ConfigurationComponent displayConfigurationComponent = this.configuration.getConfigurationComponentbyType(SC.LCDI2C);
                String displayName;
                if ( displayConfigurationComponent != null ) {
                    displayName = displayConfigurationComponent.getUserDefinedPortName();
                } else {
                    displayName = "NO PORT";
                }
                this.sb.append("Plot _plot_").append(blockName).append("(&").append("_display_").append(displayName).append(");");
                nlIndent();
            }
        }
    }
}

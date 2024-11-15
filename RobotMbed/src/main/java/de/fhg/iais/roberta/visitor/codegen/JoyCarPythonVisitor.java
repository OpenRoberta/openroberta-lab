package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOffActionJoycar;
import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOnActionJoycar;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IJoyCarVisitor;
import de.fhg.iais.roberta.visitor.JoycarMethods;

public class JoyCarPythonVisitor extends MicrobitV2PythonVisitor implements IJoyCarVisitor<Void> {
    /**
     * initialize the Python code generator visitor.
     *
     * @param programPhrases to generate the code from
     * @param robotConfiguration
     * @param beans
     */
    public JoyCarPythonVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, robotConfiguration, beans);
    }

    @Override
    protected void visitorGenerateImports() {
        this.src.add("import microbit");
        nlIndent();
        this.src.add("import random");
        nlIndent();
        this.src.add("import math");
        nlIndent();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RGBLED) ) {
            this.src.add("import neopixel");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            this.src.add("import radio");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.MUSIC) ) {
            this.src.add("import music");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isSensorUsed(SC.ULTRASONIC) ) {
            this.src.add("import machine");
            nlIndent();
        }
        nlIndent();
    }

    @Override
    protected void visitorGenerateGlobalVariables() {
        this.src.add("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.src.add("class ContinueLoop(Exception): pass");
        nlIndent();
        if ( (this.getBean(UsedHardwareBean.class).isActorUsed("I2C")) ) {
            nlIndent();
            this.src.add("microbit.i2c.init(freq=400000, sda=microbit.pin20, scl=microbit.pin19)");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) || (this.getBean(UsedHardwareBean.class).isActorUsed(SC.MOTOR)) ) {
            this.src.add("microbit.i2c.write(0x70, b'\\x00\\x01')");
            nlIndent();
            this.src.add("microbit.i2c.write(0x70, b'\\xE8\\xAA')");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.SERVOMOTOR) ) {
            this.src.add("microbit.pin1.set_analog_period(20)");
            nlIndent();
            this.src.add("microbit.pin13.set_analog_period(20)");
            nlIndent();
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RGBLED) ) {
            this.src.add("np = neopixel.NeoPixel(microbit.pin0, 8)");
            nlIndent();
        }
        nlIndent();
        this.src.add("timer1 = microbit.running_time()");
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.RADIO) ) {
            nlIndent();
            this.src.add("radio.on()");
        }
    }


    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.ULTRASONIC_GET_DISTANCE), "(microbit.pin12, microbit.pin8)");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        boolean hasDuration = driveAction.param.getDuration() != null;
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(");
        String multiplier = driveAction.direction.toString().equals(SC.FOREWARD) ? "" : "-";
        this.src.add(multiplier);
        driveAction.param.getSpeed().accept(this);
        this.src.add(", ", multiplier);
        driveAction.param.getSpeed().accept(this);
        this.src.add(")");
        if ( hasDuration ) {
            nlIndent();
            this.src.add("microbit.sleep(");
            driveAction.param.getDuration().getValue().accept(this);
            this.src.add(")");
            nlIndent();
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(0, 0)");
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String blockName = motorOnAction.getUserDefinedPort();
        ConfigurationComponent block = this.robotConfiguration.getConfigurationComponent(blockName);
        String port = block.getComponentProperties().get("PORT");
        if ( port.contains("SERVO") ) {
            port = port.replace("SERVO", "");
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SERVO_SET_ANGLE), "(",
                port, ", ");
            motorOnAction.param.getSpeed().accept(this);
            this.src.add(")");
        } else {
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SETSPEED), "(\"", port, "\", ");
            motorOnAction.param.getSpeed().accept(this);
            this.src.add(")");
            boolean hasDuration = motorOnAction.param.getDuration() != null;
            if ( hasDuration ) {
                nlIndent();
                this.src.add("microbit.sleep(");
                motorOnAction.param.getDuration().getValue().accept(this);
                this.src.add(")");
                nlIndent();
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SETSPEED), "(\"", port, "\", 0)");
            }
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String blockName = motorStopAction.getUserDefinedPort();
        ConfigurationComponent block = this.robotConfiguration.getConfigurationComponent(blockName);
        String port = block.getComponentProperties().get("PORT");

        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SETSPEED), "(\"", port, "\", 0)");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction motorDriveStopAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(0, 0)");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        boolean hasDuration = turnAction.param.getDuration() != null;


        String multiplierRight = turnAction.direction.toString().equals(SC.RIGHT) ? "-" : "";
        String multiplierLeft = turnAction.direction.toString().equals(SC.LEFT) ? "-" : "";

        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(");
        this.src.add(multiplierLeft);
        turnAction.param.getSpeed().accept(this);
        this.src.add(", ", multiplierRight);
        turnAction.param.getSpeed().accept(this);
        this.src.add(")");

        if ( hasDuration ) {
            nlIndent();
            this.src.add("microbit.sleep(");
            turnAction.param.getDuration().getValue().accept(this);
            this.src.add(")");
            nlIndent();
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(0, 0)");
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        boolean hasDuration = curveAction.paramRight.getDuration() != null;
        String multiplier = curveAction.direction.toString().equals(SC.FOREWARD) ? "" : "-";

        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(");
        this.src.add(multiplier);
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", ", multiplier);
        curveAction.paramRight.getSpeed().accept(this);
        this.src.add(")");

        if ( hasDuration ) {
            nlIndent();
            this.src.add("microbit.sleep(");
            curveAction.paramRight.getDuration().getValue().accept(this);
            this.src.add(")");
            nlIndent();
            this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(0, 0)");
        }
        return null;
    }

    @Override
    public Void visitRgbLedOnActionJoycar(RgbLedOnActionJoycar rgbLedOnActionJoycar) {
        String port = this.robotConfiguration.getConfigurationComponent(rgbLedOnActionJoycar.getUserDefinedPort()).getProperty("PORT").substring(1);
        if ( rgbLedOnActionJoycar.slot.equals("0") && (port.equals("0") || port.equals("6")) ) {
            port = "" + (Integer.valueOf(port) + 1);
        }
        if ( rgbLedOnActionJoycar.slot.equals("1") && (port.equals("2") || port.equals("4")) ) {
            port = "" + (Integer.valueOf(port) + 1);
        }
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.LED_SET_COLOUR), "(", port, ", ");
        rgbLedOnActionJoycar.colour.accept(this);
        this.src.add(")");

        return null;
    }

    @Override
    public Void visitRgbLedOffActionJoycar(RgbLedOffActionJoycar rgbLedOffActionJoycar) {
        String port = this.robotConfiguration.getConfigurationComponent(rgbLedOffActionJoycar.getUserDefinedPort()).getProperty("PORT").substring(1);
        if ( rgbLedOffActionJoycar.slot.equals("0") && (port.equals("0") || port.equals("6")) ) {
            port = "" + (Integer.valueOf(port) + 1);
        }
        if ( rgbLedOffActionJoycar.slot.equals("1") && (port.equals("2") || port.equals("4")) ) {
            port = "" + (Integer.valueOf(port) + 1);
        }
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.LED_SET_COLOUR), "(", port, ", (0, 0, 0))");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        i2cCodeGeneration(encoderSensor);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        i2cCodeGeneration(infraredSensor);
        return null;
    }

    @Override
    public Void visitGetLineSensor(GetLineSensor getLineSensor) {
        i2cCodeGeneration(getLineSensor);
        return null;
    }

    private void i2cCodeGeneration(ExternalSensor sensorBlock) {
        String sensorName = sensorBlock.getUserDefinedPort();
        ConfigurationComponent bus = getI2CBusWithSensor(sensorName);
        ConfigurationComponent sensor = getBusSubComponent(bus, sensorName);

        String port = sensor.getProperty("BRICK_PORT");
        // Please do not change the order, it is the bit position of the sensor port
        String[] portMap = {"SPEED_L", "SPEED_R", "LINE_TRK_L", "LINE_TRK_M", "LINE_TRK_R", "OBSTCL_L", "OBSTCL_R"};
        port = Integer.toString(ArrayUtils.indexOf(portMap, port));
        String address = bus.getProperty("ADDRESS");
        if ( port.equals("5") || port.equals("6") ) {
            this.src.add("(not ");
        }
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.I2C_GET_SENSORDATA), "(", address, ")[");
        this.src.add(port, "]");
        if ( port.equals("5") || port.equals("6") ) {
            this.src.add(")");
        }
    }

    private ConfigurationComponent getBusSubComponent(ConfigurationComponent bus, String userDefinedName) {
        for ( ConfigurationComponent subComponent : bus.getSubComponents().get("BUS") ) {
            if ( subComponent.userDefinedPortName.equals(userDefinedName) ) {
                return subComponent;
            }
        }
        return null;
    }

    private ConfigurationComponent getI2CBusWithSensor(String userDefinedName) {
        for ( Map.Entry<String, ConfigurationComponent> entry : this.robotConfiguration.getConfigurationComponents().entrySet() ) {
            ConfigurationComponent component = entry.getValue();
            if ( component.componentType.equals("I2C_BUS") ) {
                if ( busHasSensor(component, userDefinedName) ) {
                    return component;
                }
            }
        }
        throw new DbcException("I2C Bus is missing");
    }

    private boolean busHasSensor(ConfigurationComponent bus, String sensorName) {
        for ( ConfigurationComponent subComponent : bus.getSubComponents().get("BUS") ) {
            if ( subComponent.userDefinedPortName.equals(sensorName) ) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        String pin = "16";
        this.src.add("music.pitch(");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(", microbit.pin", pin, ")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        String pin = "16";
        this.src.add("music.pitch(", Integer.parseInt(playNoteAction.frequency.split("\\.")[0]), ", ", playNoteAction.duration, ", microbit.pin", pin, ")");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        String pin = "16";
        switch ( playFileAction.fileName ) {
            case "DADADADUM":
            case "ENTERTAINER":
            case "PRELUDE":
            case "ODE":
            case "NYAN":
            case "RINGTONE":
            case "FUNK":
            case "BLUES":
            case "BIRTHDAY":
            case "WEDDING":
            case "FUNERAL":
            case "PUNCHLINE":
            case "PYTHON":
            case "BADDY":
            case "CHASE":
            case "BA_DING":
            case "WAWAWAWAA":
            case "JUMP_UP":
            case "JUMP_DOWN":
            case "POWER_UP":
            case "POWER_DOWN": {
                this.src.add("music.play(music.", playFileAction.fileName, ", microbit.pin", pin, ")");
                break;
            }
            default:
                throw new DbcException("Invalid play file name: " + playFileAction.fileName);
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("(", colorConst.getRedChannelInt(), ", ", colorConst.getGreenChannelInt(), ", ", colorConst.getBlueChannelInt(), ")");
        return null;
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.src.add("def main():");
        incrIndentation();
        nlIndent();
        this.src.add("try:");
        incrIndentation();
        nlIndent();
        this.src.add("run()");
        decrIndentation();
        nlIndent();
        this.src.add("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.src.add("raise");
        decrIndentation();
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) || this.getBean(UsedHardwareBean.class).isActorUsed(SC.MOTOR) ) {
            nlIndent();
            this.src.add("finally:");
            incrIndentation();
            nlIndent();
            if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.DIFFERENTIALDRIVE) ) {
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.DIFFDRIVE), "(0, 0)");
            } else if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.MOTOR) ) {
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SETSPEED), "(\"MOT_L\", ", "0)");
                nlIndent();
                this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.SETSPEED), "(\"MOT_R\", ", "0)");
            }
            decrIndentation();
        }
        decrIndentation();
        nlIndent();
        nlIndent();
        this.src.add("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.src.add("main()");
        decrIndentation();
    }

    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        String port = mbedPinWriteValueAction.port;
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        pin1 = pin1.replaceAll("\\D+", "");
        this.src.add("microbit.pin", pin1);
        String valueType = mbedPinWriteValueAction.pinValue.equals(SC.DIGITAL) ? "digital(" : "analog(";
        this.src.add(".write_", valueType);
        mbedPinWriteValueAction.value.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        String port = pinValueSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin1 = configurationComponent.getProperty("PIN1");
        pin1 = pin1.replaceAll("\\D+", "");
        String valueType = pinValueSensor.getMode().toLowerCase(Locale.ENGLISH);
        if ( valueType.equalsIgnoreCase(SC.PULSEHIGH) ) {
            this.src.add("machine.time_pulse_us(microbit.pin");
            this.src.add(pin1);
            this.src.add(", 1)");
        } else if ( valueType.equalsIgnoreCase(SC.PULSELOW) ) {
            this.src.add("machine.time_pulse_us(microbit.pin");
            this.src.add(pin1);
            this.src.add(", 0)");
        } else {
            this.src.add("microbit.pin");
            this.src.add(pin1);
            this.src.add(".read_");
            this.src.add(valueType);
            this.src.add("()");
        }
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        this.src.add(this.getBean(CodeGeneratorSetupBean.class).getHelperMethodGenerator().getHelperMethodName(JoycarMethods.RECEIVE_MESSAGE));
        this.src.add("(\"", radioReceiveAction.type, "\")");
        return null;
    }
}



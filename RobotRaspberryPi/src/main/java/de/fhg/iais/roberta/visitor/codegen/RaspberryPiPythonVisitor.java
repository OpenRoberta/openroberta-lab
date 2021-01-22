package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.Locale;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.mode.action.LightMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.*;
import de.fhg.iais.roberta.syntax.generic.raspberrypi.WriteGpioValueAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SmoothedSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public class RaspberryPiPythonVisitor extends AbstractPythonVisitor implements IRaspberryPiVisitor<Void> {
    protected final ConfigurationAst brickConfiguration;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public RaspberryPiPythonVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append("cz.Color(" + quote(colorConst.getHexValueAsString()) + ")");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("time.sleep(0.015)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("time.sleep(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.accept(this);
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global " + String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( lightAction.getRgbLedColor() instanceof EmptyExpr ) {
            if ( LightMode.ON.equals(lightAction.getMode()) ) {
                this.sb.append("pwm_led_" + lightAction.getPort() + ".on()");
            } else if ( LightMode.OFF.equals(lightAction.getMode()) ) {
                this.sb.append("pwm_led_" + lightAction.getPort() + ".off()");
            } else if ( LightMode.FLASH.equals(lightAction.getMode()) ) {
                this.sb.append("pwm_led_" + lightAction.getPort() + ".toggle()");
            }
        } else {
            this.sb.append("rgb_led_" + lightAction.getPort() + ".color = ");
            lightAction.getRgbLedColor().accept(this);
            nlIndent();
            this.sb.append("rgb_led_" + lightAction.getPort() + ".on()");
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("rgb_led_" + lightStatusAction.getUserDefinedPort() + ".off()");
        return null;
    }

    @Override
    public Void visitLedBlinkAction(LedBlinkAction<Void> ledBlinkAction) {
        this.sb.append("pwm_led_").append(ledBlinkAction.getPort()).append(".blink(");
        ledBlinkAction.getOnTime().accept(this);
        this.sb.append(", ");
        ledBlinkAction.getOffTime().accept(this);
        this.sb.append(", n=");
        ledBlinkAction.getNumBlinks().accept(this);
        this.sb.append(", background=False)");
        return null;
    }

    @Override
    public Void visitLedBlinkFrequencyAction(LedBlinkFrequencyAction<Void> ledBlinkFrequencyAction) {
        this.sb.append("blink(pwm_led_").append(ledBlinkFrequencyAction.getPort()).append(", ");
        ledBlinkFrequencyAction.getFrequency().accept(this);
        sb.append(", ");
        ledBlinkFrequencyAction.getNumBlinks().accept(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitLedPulseAction(LedPulseAction<Void> ledPulseAction) {
        this.sb.append("pwm_led_").append(ledPulseAction.getPort()).append(".pulse(");
        ledPulseAction.getFadeInTime().accept(this);
        this.sb.append(", ");
        ledPulseAction.getFadeOutTime().accept(this);
        this.sb.append(", n=");
        ledPulseAction.getNumBlinks().accept(this);
        this.sb.append(", background=False)");
        return null;
    }

    @Override
    public Void visitRgbLedBlinkAction(RgbLedBlinkAction<Void> rgbLedBlinkAction) {
        this.sb.append("rgb_led_").append(rgbLedBlinkAction.getPort()).append(".blink(");
        rgbLedBlinkAction.getOnTime().accept(this);
        this.sb.append(", ");
        rgbLedBlinkAction.getOffTime().accept(this);
        this.sb.append(", on_color=");
        rgbLedBlinkAction.getOnColor().accept(this);
        this.sb.append(", off_color=");
        rgbLedBlinkAction.getOffColor().accept(this);
        this.sb.append(", n=");
        rgbLedBlinkAction.getNumBlinks().accept(this);
        this.sb.append(", background=False)");
        return null;

    }

    @Override
    public Void visitRgbLedBlinkFrequencyAction(RgbLedBlinkFrequencyAction<Void> rgbLedBlinkFrequencyAction) {
        this.sb.append("blink_rgb(rgb_led_").append(rgbLedBlinkFrequencyAction.getPort()).append(", ");
        rgbLedBlinkFrequencyAction.getFrequency().accept(this);
        sb.append(", ");
        rgbLedBlinkFrequencyAction.getOnColor().accept(this);
        sb.append(", ");
        rgbLedBlinkFrequencyAction.getOffColor().accept(this);
        sb.append(", ");
        rgbLedBlinkFrequencyAction.getNumBlinks().accept(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitRgbLedPulseAction(RgbLedPulseAction<Void> rgbLedPulseAction) {
        this.sb.append("rgb_led_").append(rgbLedPulseAction.getPort()).append(".pulse(");
        rgbLedPulseAction.getFadeInTime().accept(this);
        this.sb.append(", ");
        rgbLedPulseAction.getFadeOutTime().accept(this);
        this.sb.append(", on_color=");
        rgbLedPulseAction.getOnColor().accept(this);
        this.sb.append(", off_color=");
        rgbLedPulseAction.getOffColor().accept(this);
        this.sb.append(", n=");
        rgbLedPulseAction.getNumBlinks().accept(this);
        this.sb.append(", background=False)");
        return null;
    }

    @Override
    public Void visitBuzzerBeepAction(BuzzerBeepAction<Void> buzzerBeepAction) {
        this.sb.append("buzzer_").append(buzzerBeepAction.getPort()).append(".beep(");
        buzzerBeepAction.getOnTime().accept(this);
        this.sb.append(", ");
        buzzerBeepAction.getOffTime().accept(this);
        this.sb.append(", ");
        buzzerBeepAction.getNumBeeps().accept(this);
        this.sb.append(", background=False)");
        return null;
    }

    @Override
    public Void visitBuzzerAction(BuzzerAction<Void> buzzerAction) {
        this.sb.append("buzzer_").append(buzzerAction.getPort()).append("." + buzzerAction.getMode().toString().toLowerCase(Locale.ROOT) + "()");
        return null;
    }

    @Override
    public Void visitMotorRaspOnAction(MotorRaspOnAction<Void> motorRaspOnAction) {
        String prefix = "motor_";
        String blockType = motorRaspOnAction.getProperty().getBlockType();
        if (blockType.equals("robActions_robot_on_rasp")||blockType.equals("robActions_robot_on_for_rasp")) {
            prefix = "robot_";
        }
        String direction = "";
        switch ( motorRaspOnAction.getDirection() ) {
            case FOREWARD:
                direction = "forward";
                break;
            case BACKWARD:
                direction = "backward";
                break;
            case LEFT:
                direction = "left";
                break;
            case RIGHT:
                direction = "right";
                break;
        }
        this.sb.append(prefix).append(motorRaspOnAction.getUserDefinedPort()).append("." + direction + "(");
        motorRaspOnAction.getParam().getSpeed().accept(this);
        this.sb.append(" / 100.0)");
        if ( blockType.equals("robActions_motor_on_for_rasp") || blockType.equals("robActions_robot_on_for_rasp") ) {
            nlIndent();
            this.sb.append("time.sleep(");
            motorRaspOnAction.getDurationValue().accept(this);
            this.sb.append(")");
            nlIndent();
            this.sb.append(prefix).append(motorRaspOnAction.getUserDefinedPort()).append(".stop()");
        }
        return null;
    }

    @Override
    public Void visitServoRaspOnAction(ServoRaspOnAction<Void> servoRaspOnAction) {
        if ( servoRaspOnAction.getProperty().getBlockType().equals("robActions_servo_motor_on_rasp") ) {
            this.sb.append("motor_servo_");
        } else {
            this.sb.append("motor_servo_angular_");
        }
        this.sb.append(servoRaspOnAction.getUserDefinedPort()).append(".");
        this.sb.append(servoRaspOnAction.getMode().toString().toLowerCase(Locale.ROOT) + "()");
        return null;
    }

    @Override
    public Void visitWriteGpioValueAction(WriteGpioValueAction<Void> voidWriteGpioValueAction) {
        return null;
    }

    @Override
    public Void visitSmoothedSensor(SmoothedSensor<Void> smoothedSensor) {
        this.sb.append("smoothed_input_" + smoothedSensor.getUserDefinedPort() + "." + smoothedSensor.getMode().toLowerCase());
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("hal.getTimerValue(1)");
                break;
            case SC.RESET:
                this.sb.append("hal.resetTimer(1)");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("distance_sensor_" + ultrasonicSensor.getUserDefinedPort() + ".distance");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("light_sensor_" + lightSensor.getUserDefinedPort() + "." + lightSensor.getMode().toLowerCase());
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("motion_sensor_" + motionSensor.getUserDefinedPort() + ".motion_detected");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("button_" + keysSensor.getUserDefinedPort() + ".is_pressed");
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        this.sb.append("digital_input_" + pinGetValueSensor.getUserDefinedPort() + ".value");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    protected void generateConfigurationVariables() {
        for ( ConfigurationComponent cc : this.brickConfiguration.getConfigurationComponentsValues() ) {
            String blockName = cc.getUserDefinedPortName();
            switch ( cc.getComponentType() ) {
                case SC.ULTRASONIC:
                    this.sb
                        .append("distance_sensor_" + blockName + " = gpz.DistanceSensor(")
                        .append(cc.getProperty("TRIG"))
                        .append(", ")
                        .append(cc.getProperty("ECHO"))
                        .append(", max_distance=")
                        .append(cc.getProperty("MAX_DISTANCE"))
                        .append(", threshold=")
                        .append(cc.getProperty("THRESHOLD"))
                        .append(");");
                    nlIndent();
                    break;

                case SC.LIGHT:
                    this.sb
                        .append("light_sensor_" + blockName + " = gpz.LightSensor(")
                        .append(cc.getProperty("OUTPUT"))
                        .append(", threshold=")
                        .append(cc.getProperty("THRESHOLD"))
                        .append(");");
                    nlIndent();
                    break;
                case SC.MOTION:
                    this.sb
                        .append("motion_sensor_" + blockName + " = gpz.MotionSensor(")
                        .append(cc.getProperty("OUTPUT"))
                        .append(", threshold=")
                        .append(cc.getProperty("THRESHOLD"))
                        .append(");");
                    nlIndent();
                    break;

                case SC.SMOOTHED_OUTPUT:
                    this.sb
                        .append("smoothed_input_")
                        .append(blockName)
                        .append(" = gpz.SmoothedInputDevice(")
                        .append(cc.getProperty("OUTPUT"))
                        .append(", threshold=")
                        .append(cc.getProperty("THRESHOLD"))
                        .append(");");
                    nlIndent();
                    break;

                case SC.KEY:
                    this.sb.append("button_" + blockName + " = gpz.Button(").append(cc.getProperty("PIN1")).append(")");
                    nlIndent();
                    break;

                case SC.LED:
                    this.sb.append("pwm_led_" + blockName + " = gpz.PWMLED(").append(cc.getProperty("INPUT")).append(")");
                    nlIndent();
                    break;
                case SC.RGBLED:
                    this.sb
                        .append("rgb_led_" + blockName + " = gpz.RGBLED(")
                        .append(cc.getProperty(SC.RED))
                        .append(", ")
                        .append(cc.getProperty(SC.GREEN))
                        .append(", ")
                        .append(cc.getProperty(SC.BLUE))
                        .append(")");
                    nlIndent();
                    break;
                case SC.BUZZER:
                    this.sb.append("buzzer_" + blockName + " = gpz.Buzzer(").append(cc.getProperty("INPUT")).append(")");
                    nlIndent();
                    break;

                case SC.TONALBUZZER:
                    this.sb.append("tonal_buzzer_" + blockName + " = gpz.TonalBuzzer(").append(cc.getProperty("INPUT")).append(")");
                    nlIndent();
                    break;

                case SC.DIGITAL_OUTPUT:
                    this.sb.append("digital_input_").append(blockName).append(" = DigitalInputDevice(").append(cc.getProperty("OUTPUT")).append(")");
                    nlIndent();
                    break;

                case SC.DIGITAL_INPUT:
                    this.sb.append("digital_output_").append(blockName).append(" = gpz.DigitalOutputDevice(").append(cc.getProperty("INPUT")).append(")");
                    nlIndent();
                    break;

                case SC.PWM_INPUT:
                    this.sb.append("output_").append(blockName).append(" = gpz.PWMOutputDevice(").append(cc.getProperty("INPUT")).append(")");
                    nlIndent();
                    break;

                case SC.MOTOR:
                    this.sb
                        .append("motor_")
                        .append(blockName)
                        .append(" = gpz.Motor(")
                        .append(cc.getProperty("PIN_FORWARD"))
                        .append(", ")
                        .append(cc.getProperty("PIN_BACKWARD"))
                        .append(")");
                    nlIndent();
                    break;

                case SC.SERVOMOTOR:
                    this.sb
                        .append("motor_servo_")
                        .append(blockName)
                        .append(" = gpz.Servo(")
                        .append(cc.getProperty("PIN1"))
                        .append(", min_pulse_width=")
                        .append(cc.getProperty("MIN_PULSE_WIDTH"))
                        .append(", max_pulse_width=")
                        .append(cc.getProperty("MAX_PULSE_WIDTH"))
                        .append(", frame_width=")
                        .append(cc.getProperty("FRAME_WIDTH"))
                        .append(")");
                    nlIndent();
                    break;

                case SC.ANGULARSERVOMOTOR:
                    this.sb
                        .append("motor_servo_angular_")
                        .append(blockName)
                        .append(" = gpz.AngularServo(")
                        .append(cc.getProperty("PIN1"))
                        .append(", min_angle=")
                        .append(cc.getProperty("MIN_ANGLE"))
                        .append(", max_angle=")
                        .append(cc.getProperty("MAX_ANGLE"))
                        .append(", min_pulse_width=")
                        .append(cc.getProperty("MIN_PULSE_WIDTH"))
                        .append(", max_pulse_width=")
                        .append(cc.getProperty("MAX_PULSE_WIDTH"))
                        .append(", frame_width=")
                        .append(cc.getProperty("FRAME_WIDTH"))
                        .append(")");
                    nlIndent();
                    break;

                case SC.PHASEMOTOR:
                    this.sb
                        .append("motor_phase_")
                        .append(blockName)
                        .append(" = gpz.PhaseEnableMotor(")
                        .append(cc.getProperty("PHASE"))
                        .append(", ")
                        .append(cc.getProperty("ENABLE"))
                        .append(")");
                    nlIndent();
                    break;

                case SC.ROBOT:
                    this.sb
                        .append("robot_")
                        .append(blockName)
                        .append(" = gpz.Robot(left=(")
                        .append(cc.getProperty("LEFT-FORWARD"))
                        .append(", ")
                        .append(cc.getProperty("LEFT-BACKWARD"))
                        .append("), right=(")
                        .append(cc.getProperty("RIGHT-FORWARD"))
                        .append(", ")
                        .append(cc.getProperty("RIGHT-BACKWARD"))
                        .append("))");
                    nlIndent();
                    break;
                case SC.RASPBERRYPI:
                    break;

                default:
                    throw new DbcException("Configuration block is not supported: " + cc.getComponentType());
            }
        }
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from __future__ import absolute_import");
        nlIndent();
        this.sb.append("import time");
        nlIndent();
        this.sb.append("import gpiozero as gpz");
        nlIndent();
        this.sb.append("import colorzero as cz");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        nlIndent();
        if ( !this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().isEmpty() ) {
            String helperMethodImpls =
                    this
                            .getBean(CodeGeneratorSetupBean.class)
                            .getHelperMethodGenerator()
                            .getHelperMethodDefinitions(this.getBean(CodeGeneratorSetupBean.class).getUsedMethods());
            this.sb.append(helperMethodImpls);
            nlIndent();
            nlIndent();
        }
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
        nlIndent();
        generateConfigurationVariables();
        nlIndent();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run()");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("print('Fehler im RaspberryPi')");
        nlIndent();
        this.sb.append("print(e.__class__.__name__)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("print(e)");
        decrIndentation();
        decrIndentation();
        nlIndent();
        nlIndent();
        this.sb.append("if __name__ == \"__main__\":");
        incrIndentation();
        nlIndent();
        this.sb.append("main()");

    }

    private String quote(String value) {
        return "'" + value.toLowerCase() + "'";
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("tonal_buzzer_").append(toneAction.getPort()).append(".play(gpz.tones.Tone(");
        toneAction.getFrequency().accept(this);
        this.sb.append("))");
        nlIndent();
        this.sb.append("time.sleep(");
        toneAction.getDuration().accept(this);
        this.sb.append(" / 1000.)");
        nlIndent();
        this.sb.append("tonal_buzzer_").append(toneAction.getPort()).append(".stop()");

        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        this.sb.append("motor_servo_angular_").append(motorOnAction.getUserDefinedPort()).append(".angle = ");
        motorOnAction.getParam().getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        this.sb.append("motor_servo_").append(motorSetPowerAction.getUserDefinedPort()).append(".value = ");
        motorSetPowerAction.getPower().accept(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String prefix = "motor_";
        if (motorStopAction.getProperty().getBlockType().equals("robActions_robot_stop_rasp")){
            prefix = "robot_";
        }
        this.sb.append(prefix).append(motorStopAction.getUserDefinedPort()).append(".stop()");
        return null;
    }
}

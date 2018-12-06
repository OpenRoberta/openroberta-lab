package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.collect.MbotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a hussentation of a phrase to a StringBuilder. <b>This
 * representation is correct C code for Arduino.</b> <br>
 */
public final class MbotCppVisitor extends AbstractCommonArduinoCppVisitor implements IMbotVisitor<Void> {
    private final boolean isTimerSensorUsed;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private MbotCppVisitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(brickConfiguration, phrases, indentation);
        final MbotUsedHardwareCollectorVisitor codePreprocessVisitor = new MbotUsedHardwareCollectorVisitor(phrases, brickConfiguration);
        this.usedSensors = codePreprocessVisitor.getUsedSensors();
        this.usedActors = codePreprocessVisitor.getUsedActors();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        final MbotCppVisitor astVisitor = new MbotCppVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("_meRgbLed.setColor(");
        this.sb.append(lightAction.getPort());
        if ( lightAction.getRgbLedColor().getClass().equals(Var.class) ) {
            String tempVarName = ((Var<Void>) lightAction.getRgbLedColor()).getValue();
            this.sb.append(", ");
            this.sb.append("RCHANNEL(");
            this.sb.append(tempVarName);
            this.sb.append("), ");
            this.sb.append("GCHANNEL(");
            this.sb.append(tempVarName);
            this.sb.append("), ");
            this.sb.append("BCHANNEL(");
            this.sb.append(tempVarName);
            this.sb.append("));");
            nlIndent();
            this.sb.append("_meRgbLed.show();");
            return null;
        }
        if ( lightAction.getRgbLedColor().getClass().equals(ColorConst.class) ) {
            String hexValue = ((ColorConst<Void>) lightAction.getRgbLedColor()).getRgbValue();
            hexValue = hexValue.split("#")[1];
            this.sb.append(", ");
            this.sb.append("0x");
            this.sb.append(hexValue.substring(0, 2));
            this.sb.append(", ");
            this.sb.append("0x");
            this.sb.append(hexValue.substring(2, 4));
            this.sb.append(", ");
            this.sb.append("0x");
            this.sb.append(hexValue.substring(4, 6));
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meRgbLed.show();");
            return null;
        }
        Map<String, Expr<Void>> Channels = new HashMap<>();
        Channels.put("red", ((RgbColor<Void>) lightAction.getRgbLedColor()).getR());
        Channels.put("green", ((RgbColor<Void>) lightAction.getRgbLedColor()).getG());
        Channels.put("blue", ((RgbColor<Void>) lightAction.getRgbLedColor()).getB());
        Channels.forEach((k, v) -> {
            this.sb.append(", ");
            v.visit(this);
        });
        this.sb.append(");");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("_meRgbLed.setColor(" + lightStatusAction.getPort());
        this.sb.append(", 0, 0, 0);");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        nlIndent();
        this.sb.append("delay(20); ");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append(", ");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append(");");
        nlIndent();
        this.sb.append("delay(20); ");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        this.sb.append("_meDCmotor").append(motorOnAction.getUserDefinedPort()).append(".run(");
        if ( !this.configuration.getFirstMotorPort(SC.RIGHT).equals(motorOnAction.getUserDefinedPort()) ) {
            this.sb.append("-1*");
        }
        this.sb.append("(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(")*255/100);");
        if ( duration != null ) {
            nlIndent();
            this.sb.append("delay(");
            motorOnAction.getDurationValue().visit(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meDCmotor").append(motorOnAction.getUserDefinedPort()).append(".stop();");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        this.sb.append("_meDCmotor").append(motorStopAction.getUserDefinedPort()).append(".stop();");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        final MotorDuration<Void> duration = driveAction.getParam().getDuration();
        this.sb.append("_meDrive.drive(");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(driveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        final MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        this.sb.append("_meDrive.steer(");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().visit(this);
        this.sb.append(", ").append(curveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        final MotorDuration<Void> duration = turnAction.getParam().getDuration();
        this.sb.append("_meDrive.turn(");
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append(", ").append(turnAction.getDirection() == TurnDirection.LEFT ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        for ( final UsedActor actor : this.usedActors ) {
            if ( actor.getType().equals(SC.DIFFERENTIAL_DRIVE) ) {
                this.sb.append("_meDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("_meLight" + lightSensor.getPort() + ".read() * ANALOG2PERCENT");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("(analogRead(PORT_7) < 512)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("_meSoundSensor" + soundSensor.getPort() + ".strength()");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {

        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.sb.append("myGyro" + gyroSensor.getPort() + ".getGyro" + gyroSensor.getMode() + "()");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometer) {
        this.sb.append("myGyro" + accelerometer.getPort() + ".getAngle" + accelerometer.getMode().toString() + "()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("__meLineFollower" + infraredSensor.getPort() + ".readSensor" + infraredSensor.getSlot() + "()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("_meTemp" + temperatureSensor.getPort() + ".getTemperature()");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("_meTouch" + touchSensor.getPort() + ".touched()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("_meUltraSensor" + ultrasonicSensor.getPort() + ".distanceCm()");
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("_mePir" + motionSensor.getPort() + ".isHumanDetected()");
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> _meFlameSensor) {
        this.sb.append("_meFlameSensor" + _meFlameSensor.getPort() + ".readAnalog()");
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        this.sb.append("_meJoystick" + joystick.getPort() + ".read" + joystick.getAxis() + "()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        if ( !mainTask.getVariables().toString().equals("") ) {
            nlIndent();
        }
        ;
        nlIndent();
        //generateConfigurationVariables();
        if ( this.isTimerSensorUsed ) {
            this.sb.append("unsigned long __time = millis();");
            nlIndent();
        }
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> (phrase.getKind().getCategory() == Category.METHOD) && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( (this.configuration.getConfigurationComponents().isEmpty() || this.isTimerSensorUsed) && (numberConf == 0) ) {
            nlIndent();
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
        this.sb.append("Serial.begin(9600); ");
        //      TODO test the following lines when these sensors are available
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".begin();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
        nlIndent();
        generateUsedVars();
        //      TODO test the following lines when these sensors are available
        //        this.sb.append("\n}");
        //        decrIndentation();
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
        //                    break;
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
        //                    break;
        //                case SC.TEMPERATURE:
        //                    nlIndent();
        //                    this.sb.append("myTemp" + usedSensor.getPort() + ".update();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
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
        this.sb.append("#define ANALOG2PERCENT 0.0978");
        nlIndent();
        nlIndent();
        this.sb.append("#include <math.h>");
        nlIndent();
        this.sb.append("#include <MeMCore.h>");
        nlIndent();
        this.sb.append("#include <Wire.h>");
        nlIndent();
        this.sb.append("#include <SoftwareSerial.h>");
        nlIndent();
        this.sb.append("#include <MeDrive.h>");
        nlIndent();
        this.sb.append("#include <NEPODefs.h>");
        nlIndent();
        this.sb.append("#include <RobertaFunctions.h>");
        nlIndent();
        generateSensors();
        generateActors();
        nlIndent();
        this.sb.append("RobertaFunctions rob;");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
    }

    private void generateSensors() {
        LinkedHashSet<Integer> usedInfraredPort = new LinkedHashSet<>();
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case SC.BUTTON:
                    nlIndent();
                    this.sb.append("pinMode(7, INPUT);");
                    break;
                case SC.COLOR:
                    break;
                case SC.ULTRASONIC:
                    nlIndent();
                    this.sb.append("MeUltrasonicSensor _meUltraSensor" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    break;
                case SC.MOTION:
                    nlIndent();
                    this.sb.append("MePIRMotionSensor _mePir" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TEMPERATURE:
                    nlIndent();
                    this.sb.append("MeHumiture _meTemp" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TOUCH:
                    nlIndent();
                    this.sb.append("MeTouchSensor _meTouch" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.LIGHT:
                    nlIndent();
                    this.sb.append("MeLightSensor _meLight" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    break;
                case SC.INFRARED:
                    int port = Integer.parseInt(usedSensor.getPort());
                    if ( !usedInfraredPort.contains(port) ) {
                        nlIndent();
                        this.sb.append("MeLineFollower __meLineFollower" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    }
                    usedInfraredPort.add(port);
                    break;
                case SC.COMPASS:
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    nlIndent();
                    this.sb.append("MeGyro myGyro" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.SOUND:
                    nlIndent();
                    this.sb.append("MeSoundSensor _meSoundSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.JOYSTICK:
                    nlIndent();
                    this.sb.append("MeJoystick _meJoystick" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.FLAMESENSOR:
                    nlIndent();
                    this.sb.append("MeFlameSensor _meFlameSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.VOLTAGE:
                    nlIndent();
                    this.sb.append("MePotentiometer _mePotentiometer" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TIMER:
                    break;
                default:
                    throw new DbcException("Sensor is not supported! " + usedSensor.getType());
            }
        }
    }

    private void generateActors() {
        for ( final UsedActor usedActor : this.usedActors ) {
            switch ( usedActor.getType() ) {
                case SC.LED_ON_BOARD:
                    nlIndent();
                    this.sb.append("MeRGBLed _meRgbLed(7, 2);");
                    break;
                case SC.GEARED_MOTOR:
                    nlIndent();
                    this.sb.append("MeDCMotor _meDCmotor" + usedActor.getPort() + "(M" + usedActor.getPort() + ");");
                    break;
                case SC.DIFFERENTIAL_DRIVE:
                    nlIndent();
                    this.sb
                        .append(
                            "MeDrive _meDrive(M"
                                + this.configuration.getFirstMotorPort(SC.LEFT)
                                + ", M"
                                + this.configuration.getFirstMotorPort(SC.RIGHT)
                                + ");");
                    break;
                case SC.LED_MATRIX:
                    nlIndent();
                    this.sb.append("MeLEDMatrix _meLEDMatrix_" + usedActor.getPort() + "(" + usedActor.getPort() + ");");
                    break;
                case SC.BUZZER:
                    nlIndent();
                    this.sb.append("MeBuzzer _meBuzzer;");
                    break;
                case SC.IR_TRANSMITTER:
                    nlIndent();
                    this.sb.append("MeIR _meIr;");
                    break;
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        //RatedVoltage: 5V
        //Signal type: Analog (range from 0 to 970)
        this.sb.append("_mePotentiometer" + voltageSensor.getPort() + ".read()*5/970");
        return null;
    }

    @Override
    public Void visitImage(LedMatrix<Void> ledMatrix) {
        ledMatrix.getImage();
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
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append("_meIr.sendString(");
        sendIRAction.getMessage().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append("_meIr.getString()");
        return null;
    }
}

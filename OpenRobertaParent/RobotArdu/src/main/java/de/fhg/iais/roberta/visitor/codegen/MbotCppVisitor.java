package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
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
        this.sb.append("rgbled_7.setColor(" + lightAction.getPort());
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
        this.sb.append("rgbled_7.show();");
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("rgbled_7.setColor(" + lightStatusAction.getPort());
        this.sb.append(", 0, 0, 0);");
        nlIndent();
        this.sb.append("rgbled_7.show();");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //8 - sound port
        this.sb.append("buzzer.tone(8, ");
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
        this.sb.append("buzzer.tone(8, ");
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
        this.sb.append(motorOnAction.getUserDefinedPort()).append(".run(");
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
            this.sb.append(motorOnAction.getUserDefinedPort()).append(".stop();");
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
        this.sb.append(motorStopAction.getUserDefinedPort()).append(".stop();");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        final MotorDuration<Void> duration = driveAction.getParam().getDuration();
        this.sb.append("myDrive.drive(");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append("*255/100, ").append(driveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
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
        this.sb.append("myDrive.steer(");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append("*255/100, ");
        curveAction.getParamRight().getSpeed().visit(this);
        this.sb.append("*255/100, ").append(curveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
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
        this.sb.append("myDrive.turn(");
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append("*255/100, ").append(turnAction.getDirection() == TurnDirection.LEFT ? 1 : 0);
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
                this.sb.append("myDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("myLight" + lightSensor.getPort() + ".read()*100/1023");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("(analogRead(7) < 100)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {

        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {

        this.sb.append("mySound" + soundSensor.getPort() + ".strength()");
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
        this.sb.append("lineFinder" + infraredSensor.getPort() + ".readSensors" + "()&" + infraredSensor.getMode());
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("myTemp" + temperatureSensor.getPort() + ".getTemperature()");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("myTouch" + touchSensor.getPort() + ".touched()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("ultraSensor" + ultrasonicSensor.getPort() + ".distanceCm()");
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("pir" + motionSensor.getPort() + ".isHumanDetected()");
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        this.sb.append("flameSensor" + flameSensor.getPort() + ".readAnalog()");
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        /*
         * after understanding how to implement modes this also works:
         * this.sb.append("myJoystick" + joystick.getPort().getPortNumber() + ".read" + joystick.getMode().getValues()[0] + "()");
         */
        this.sb.append("myJoystick" + joystick.getPort() + ".read" + joystick.getAxis() + "()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("unsigned long __time = millis(); \n");
        }
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\nvoid setup() \n");
        this.sb.append("{");
        nlIndent();

        this.sb.append("Serial.begin(9600); ");
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort() + ".begin();");
                    break;
                default:
                    break;
            }
        }
        nlIndent();
        generateUsedVars();
        this.sb.append("\n}");
        decrIndentation();
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case SC.GYRO:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
                    break;
                case SC.ACCELEROMETER:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
                    break;
                case SC.TEMPERATURE:
                    nlIndent();
                    this.sb.append("myTemp" + usedSensor.getPort() + ".update();");
                    break;
                default:
                    break;
            }
        }
        decrIndentation();
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <MeMCore.h> \n");
        this.sb.append("#include <Wire.h>\n");
        this.sb.append("#include <SoftwareSerial.h>\n");
        this.sb.append("#include <RobertaFunctions.h>\n");
        this.sb.append("#include <NEPODefs.h>\n");
        this.sb.append("#include \"MeDrive.h\"\n\n");
        this.sb.append("RobertaFunctions rob;\n");

        generateSensors();
        generateActors();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
    }

    private void generateSensors() {
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case SC.BUTTON:
                    this.sb.append("pinMode(7, INPUT);\n");
                    break;
                case SC.COLOR:
                    break;
                case SC.ULTRASONIC:
                    this.sb.append("MeUltrasonicSensor ultraSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.MOTION:
                    this.sb.append("MePIRMotionSensor pir" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.TEMPERATURE:
                    this.sb.append("MeHumiture myTemp" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.TOUCH:
                    this.sb.append("MeTouchSensor myTouch" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.LIGHT:
                    this.sb.append("MeLightSensor myLight" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.INFRARED:
                    this.sb.append("MeLineFollower lineFinder" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.COMPASS:
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    this.sb.append("MeGyro myGyro" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.SOUND:
                    this.sb.append("MeSoundSensor mySound" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.JOYSTICK:
                    this.sb.append("MeJoystick myJoystick" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.FLAMESENSOR:
                    this.sb.append("MeFlameSensor flameSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.VOLTAGE:
                    this.sb.append("MePotentiometer myVoltageSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SC.TIMER:
                    break;
                default:
                    throw new DbcException("Sensor is not supported! " + usedSensor.getType());
            }
        }
    }

    private void generateActors() {
        decrIndentation();
        for ( final UsedActor usedActor : this.usedActors ) {
            switch ( usedActor.getType() ) {
                case SC.LED_ON_BOARD:
                    this.sb.append("MeRGBLed rgbled_7(7, 7==7?2:4);\n");
                    break;
                case SC.GEARED_MOTOR:
                    this.sb.append("MeDCMotor " + usedActor.getPort() + "(" + usedActor.getPort() + ");\n");
                    break;
                case SC.DIFFERENTIAL_DRIVE:
                    this.sb
                        .append(
                            "MeDrive myDrive("
                                + this.configuration.getFirstMotorPort(SC.LEFT)
                                + ", "
                                + this.configuration.getFirstMotorPort(SC.RIGHT)
                                + ");\n");
                    break;
                case SC.LED_MATRIX:
                    this.sb.append("MeLEDMatrix myLEDMatrix_" + usedActor.getPort() + "(" + usedActor.getPort() + ");\n");
                    break;
                case SC.BUZZER:
                    this.sb.append("MeBuzzer buzzer;\n");
                    break;
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }
    //
    //    @Override
    //    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
    //        this.sb.append("rgbled_7.setColor(");
    //        if ( ledOnAction.getSide().equals("Left") ) {
    //            this.sb.append("1, ");
    //        } else {
    //            this.sb.append("2, ");
    //        }
    //        switch ( ledOnAction.getLedColor().toString() ) {
    //            case "ColorConst [RED]":
    //                this.sb.append("255, 0, 0");
    //                break;
    //            case "ColorConst [BLACK]":
    //                this.sb.append("0, 0, 0");
    //                break;
    //            case "ColorConst [BLUE]":
    //                this.sb.append("0, 0, 255");
    //                break;
    //            case "ColorConst [GREEN]":
    //                this.sb.append("0, 255, 0");
    //                break;
    //            case "ColorConst [YELLOW]":
    //                this.sb.append("255, 255, 0");
    //                break;
    //            case "ColorConst [WHITE]":
    //                this.sb.append("255, 255, 255");
    //                break;
    //            case "ColorConst [BROWN]":
    //                this.sb.append("102, 51, 0");
    //                break;
    //            default:
    //                ledOnAction.getLedColor().visit(this);
    //                break;
    //
    //        }
    //        this.sb.append(");");
    //        nlIndent();
    //        this.sb.append("rgbled_7.show();");
    //        return null;
    //    }
    //
    //    @Override
    //    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
    //        this.sb.append("rgbled_7.setColor(");
    //        if ( ledOffAction.getSide().equals("Left") ) {
    //            this.sb.append("1, ");
    //        } else {
    //            this.sb.append("2, ");
    //        }
    //        this.sb.append("0, 0, 0);");
    //        nlIndent();
    //        this.sb.append("rgbled_7.show();");
    //        return null;
    //    }

    //    @Override
    //    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
    //        this.sb.append("rgbled_").append(externalLedOnAction.getPort().getValues()[0]).append(".setColor(").append(externalLedOnAction.getLedNo() + ", ");
    //        switch ( externalLedOnAction.getLedColor().toString() ) {
    //            case "ColorConst [RED]":
    //                this.sb.append("255, 0, 0");
    //                break;
    //            case "ColorConst [BLACK]":
    //                this.sb.append("0, 0, 0");
    //                break;
    //            case "ColorConst [BLUE]":
    //                this.sb.append("0, 0, 255");
    //                break;
    //            case "ColorConst [GREEN]":
    //                this.sb.append("0, 255, 0");
    //                break;
    //            case "ColorConst [YELLOW]":
    //                this.sb.append("255, 255, 0");
    //                break;
    //            case "ColorConst [WHITE]":
    //                this.sb.append("255, 255, 255");
    //                break;
    //            case "ColorConst [BROWN]":
    //                this.sb.append("102, 51, 0");
    //                break;
    //            default:
    //                externalLedOnAction.getLedColor().visit(this);
    //                break;
    //
    //        }
    //        this.sb.append(");");
    //        nlIndent();
    //        this.sb.append("rgbled_" + externalLedOnAction.getPort().getValues()[0] + ".show();");
    //        return null;
    //    }

    //    @Override
    //    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
    //        this.sb.append("rgbled_" + externalLedOffAction.getPort().getValues()[0] + ".setColor(");
    //        this.sb.append(externalLedOffAction.getLedNo());
    //        this.sb.append(", 0, 0, 0);");
    //        nlIndent();
    //        this.sb.append("rgbled_" + externalLedOffAction.getPort().getValues()[0] + ".show();");
    //        return null;
    //    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        //RatedVoltage: 5V
        //Signal type: Analog (range from 0 to 970)
        this.sb.append("myVoltageSensor" + voltageSensor.getPort() + ".read()*5/970");
        return null;
    }

    @Override
    public Void visitRgbColor(RgbColor<Void> rgbColor) {
        rgbColor.getR().visit(this);
        this.sb.append(", ");
        rgbColor.getG().visit(this);
        this.sb.append(", ");
        rgbColor.getB().visit(this);
        return null;
    }

    @Override
    public Void visitImage(LedMatrix<Void> ledMatrix) {
        ledMatrix.getImage();
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        return null;
    }

    //    @Override
    //    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
    //        String valuesToDisplay = displayImageAction.getValuesToDisplay().toString();
    //        valuesToDisplay = valuesToDisplay.replaceAll("   ", "-");
    //        valuesToDisplay = valuesToDisplay.replaceAll("  ", "-");
    //        valuesToDisplay = valuesToDisplay.replaceAll(" #", "#");
    //        final String[] valuesToDisplayArray = valuesToDisplay.split("\n");
    //        final char[][] imageCharacterMatrix = new char[8][16];
    //        valuesToDisplayArray[0] = valuesToDisplayArray[0].split("Image \\[ \\[")[1].split("]")[0];
    //        imageCharacterMatrix[0] = valuesToDisplayArray[0].replaceAll(",", "").toCharArray();
    //        for ( int i = 1; i < 8; i++ ) {
    //            valuesToDisplayArray[i] = valuesToDisplayArray[i].replaceAll("\\[|\\]|\\] \\]", "");
    //            imageCharacterMatrix[i] = valuesToDisplayArray[i].replaceAll(",", "").toCharArray();
    //        }
    //        final int[] imageBitmap = new int[16];
    //        for ( int i = 0; i < 16; i++ ) {
    //            for ( int j = 0; j < 8; j++ ) {
    //                if ( imageCharacterMatrix[j][i] == '#' ) {
    //                    imageBitmap[i] += Util1.pow2(7 - j);
    //                }
    //            }
    //        }
    //        this.sb.append("unsigned char drawBuffer[16];");
    //        nlIndent();
    //        this.sb.append("unsigned char *drawTemp;");
    //        nlIndent();
    //
    //        this.sb.append("drawTemp = new unsigned char[16]{");
    //        for ( int i = 0; i < 15; i++ ) {
    //            this.sb.append(imageBitmap[i]);
    //            this.sb.append(", ");
    //        }
    //        this.sb.append(imageBitmap[15]);
    //        this.sb.append("};");
    //        nlIndent();
    //        this.sb.append("memcpy(drawBuffer,drawTemp,16);");
    //        nlIndent();
    //        this.sb.append("free(drawTemp);");
    //        nlIndent();
    //        this.sb.append("myLEDMatrix_" + displayImageAction.getPort().getValues()[0] + ".drawBitmap(0, 0, 16, drawBuffer);");
    //        nlIndent();
    //        return null;
    //    }

    //    @Override
    //    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
    //        this.sb.append("myLEDMatrix_" + displayTextAction.getPort().getValues()[0] + ".drawStr(0, 7, ");
    //        displayTextAction.getMsg().visit(this);
    //        this.sb.append(");");
    //        return null;
    //    }

}

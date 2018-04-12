package de.fhg.iais.roberta.syntax.codegen.arduino.mbot;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.DisplayImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.DisplayTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.arduino.mbot.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.arduino.ArduinoVisitor;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.PIRMotionSensor;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a hussentation of a phrase to a StringBuilder. <b>This
 * representation is correct C code for Arduino.</b> <br>
 */
public class CppVisitor extends ArduinoVisitor implements MbotAstVisitor<Void> {
    private final MbotConfiguration brickConfiguration;
    private final boolean isTimerSensorUsed;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private CppVisitor(MbotConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(phrases, indentation);
        this.brickConfiguration = brickConfiguration;
        final UsedHardwareCollectorVisitor codePreprocessVisitor = new UsedHardwareCollectorVisitor(phrases, brickConfiguration);
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
    public static String generate(MbotConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        final CppVisitor astVisitor = new CppVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
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
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
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
        this.sb.append(motorOnAction.getPort().getValues()[1]).append(".run(");
        if ( this.brickConfiguration.getRightMotorPort().equals(motorOnAction.getPort()) ) {
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
            this.sb.append(motorOnAction.getPort().getValues()[1]).append(".stop();");
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
        this.sb.append(motorStopAction.getPort().getValues()[1]).append(".stop();");
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
            if ( actor.getType().equals(ActorType.DIFFERENTIAL_DRIVE) ) {
                this.sb.append("myDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        switch ( (LightSensorMode) lightSensor.getMode() ) {
            case LEFT:
                this.sb.append("lineFinder" + lightSensor.getPort().getPortNumber() + ".readSensors" + "()&2");
                break;
            case RIGHT:
                this.sb.append("lineFinder" + lightSensor.getPort().getPortNumber() + ".readSensors" + "()&1");
                break;

        }
        return null;
    }

    @Override
    public Void visitAmbientLightSensor(AmbientLightSensor<Void> lightSensor) {
        this.sb.append("myLight" + lightSensor.getPort().getPortNumber() + ".read()");
        return null;

    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {

        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {

        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {

        this.sb.append("mySound" + soundSensor.getPort().getPortNumber() + ".strength()");
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
        this.sb.append("myGyro" + gyroSensor.getPort().getPortNumber() + ".getGyro" + gyroSensor.getMode().toString() + "()");
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometer) {
        this.sb.append("myGyro" + accelerometer.getPort().getPortNumber() + ".getAngle" + accelerometer.getMode() + "()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("myTemp" + temperatureSensor.getPort().getPortNumber() + ".getTemperature()");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("myTouch" + touchSensor.getPort().getPortNumber() + ".touched()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("ultraSensor" + ultrasonicSensor.getPort().getPortNumber() + ".distanceCm()");
        return null;
    }

    @Override
    public Void visitPIRMotionSensor(PIRMotionSensor<Void> motionSensor) {
        this.sb.append("pir" + motionSensor.getPort().getPortNumber() + ".isHumanDetected()");
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        this.sb.append("flameSensor" + flameSensor.getPort().getPortNumber() + ".readAnalog()");
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        /*
         * after understanding how to implement modes this also works:
         * this.sb.append("myJoystick" + joystick.getPort().getPortNumber() + ".read" + joystick.getMode().getValues()[0] + "()");
         */
        this.sb.append("myJoystick" + joystick.getPort().getPortNumber() + ".read" + joystick.getAxis() + "()");
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
            switch ( (SensorType) usedSensor.getType() ) {
                case GYRO:
                case ACCELEROMETER:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort().getPortNumber() + ".begin();");
                    break;
            }
        }
        nlIndent();
        generateUsedVars();
        this.sb.append("\n}");
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( (SensorType) usedSensor.getType() ) {
                case GYRO:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort().getPortNumber() + ".update();");
                    break;
                case ACCELEROMETER:
                    nlIndent();
                    this.sb.append("myGyro" + usedSensor.getPort().getPortNumber() + ".update();");
                    break;
                case TEMPERATURE:
                    nlIndent();
                    this.sb.append("myTemp" + usedSensor.getPort().getPortNumber() + ".update();");
                    break;
            }
        }
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
        this.sb.append("#include \"MeDrive.h\"\n\n");
        this.sb.append("RobertaFunctions rob;\n");

        generateSensors();
        generateActors();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
    }

    private void generateSensors() {
        for ( final UsedSensor usedSensor : this.usedSensors ) {
            switch ( (SensorType) usedSensor.getType() ) {
                case COLOR:
                    break;
                case INFRARED:
                    break;
                case ULTRASONIC:
                    this.sb.append("MeUltrasonicSensor ultraSensor" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case PIR_MOTION:
                    this.sb.append("MePIRMotionSensor pir" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case TEMPERATURE:
                    this.sb.append("MeHumiture myTemp" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case TOUCH:
                    this.sb.append("MeTouchSensor myTouch" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case AMBIENT_LIGHT:
                    this.sb.append("MeLightSensor myLight" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case LINE_FOLLOWER:
                    this.sb.append("MeLineFollower lineFinder" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case COMPASS:
                    break;
                case GYRO:
                case ACCELEROMETER:
                    this.sb.append("MeGyro myGyro" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case SOUND:
                    this.sb.append("MeSoundSensor mySound" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case JOYSTICK:
                    this.sb.append("MeJoystick myJoystick" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case FLAMESENSOR:
                    this.sb.append("MeFlameSensor flameSensor" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case VOLTAGE:
                    this.sb.append("MePotentiometer myVoltageSensor" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case TIMER:
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
                case LED_ON_BOARD:
                    this.sb.append("MeRGBLed rgbled_7(7, 7==7?2:4);\n");
                    break;
                case GEARED_MOTOR:
                    this.sb.append("MeDCMotor " + usedActor.getPort().getValues()[1] + "(" + usedActor.getPort().getValues()[0] + ");\n");
                    break;
                case DIFFERENTIAL_DRIVE:
                    this.sb.append(
                        "MeDrive myDrive("
                            + this.brickConfiguration.getLeftMotorPort().getValues()[0]
                            + ", "
                            + this.brickConfiguration.getRightMotorPort().getValues()[0]
                            + ");\n");
                    break;
                case EXTERNAL_LED:
                    this.sb.append("MeRGBLed rgbled_" + usedActor.getPort().getValues()[0] + "(" + usedActor.getPort().getValues()[0] + ", 4);\n");
                    break;
                case LED_MATRIX:
                    this.sb.append("MeLEDMatrix myLEDMatrix_" + usedActor.getPort().getValues()[0] + "(" + usedActor.getPort().getValues()[0] + ");\n");
                    break;
                case BUZZER:
                    this.sb.append("MeBuzzer buzzer;\n");
                    break;
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.sb.append("rgbled_7.setColor(");
        if ( ledOnAction.getSide().equals("Left") ) {
            this.sb.append("1, ");
        } else {
            this.sb.append("2, ");
        }
        switch ( ledOnAction.getLedColor().toString() ) {
            case "ColorConst [RED]":
                this.sb.append("255, 0, 0");
                break;
            case "ColorConst [BLACK]":
                this.sb.append("0, 0, 0");
                break;
            case "ColorConst [BLUE]":
                this.sb.append("0, 0, 255");
                break;
            case "ColorConst [GREEN]":
                this.sb.append("0, 255, 0");
                break;
            case "ColorConst [YELLOW]":
                this.sb.append("255, 255, 0");
                break;
            case "ColorConst [WHITE]":
                this.sb.append("255, 255, 255");
                break;
            case "ColorConst [BROWN]":
                this.sb.append("102, 51, 0");
                break;
            default:
                ledOnAction.getLedColor().visit(this);
                break;

        }
        this.sb.append(");");
        nlIndent();
        this.sb.append("rgbled_7.show();");
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        this.sb.append("rgbled_7.setColor(");
        if ( ledOffAction.getSide().equals("Left") ) {
            this.sb.append("1, ");
        } else {
            this.sb.append("2, ");
        }
        this.sb.append("0, 0, 0);");
        nlIndent();
        this.sb.append("rgbled_7.show();");
        return null;
    }

    @Override
    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
        this.sb.append("rgbled_").append(externalLedOnAction.getPort().getValues()[0]).append(".setColor(").append(externalLedOnAction.getLedNo() + ", ");
        switch ( externalLedOnAction.getLedColor().toString() ) {
            case "ColorConst [RED]":
                this.sb.append("255, 0, 0");
                break;
            case "ColorConst [BLACK]":
                this.sb.append("0, 0, 0");
                break;
            case "ColorConst [BLUE]":
                this.sb.append("0, 0, 255");
                break;
            case "ColorConst [GREEN]":
                this.sb.append("0, 255, 0");
                break;
            case "ColorConst [YELLOW]":
                this.sb.append("255, 255, 0");
                break;
            case "ColorConst [WHITE]":
                this.sb.append("255, 255, 255");
                break;
            case "ColorConst [BROWN]":
                this.sb.append("102, 51, 0");
                break;
            default:
                externalLedOnAction.getLedColor().visit(this);
                break;

        }
        this.sb.append(");");
        nlIndent();
        this.sb.append("rgbled_" + externalLedOnAction.getPort().getValues()[0] + ".show();");
        return null;
    }

    @Override
    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
        this.sb.append("rgbled_" + externalLedOffAction.getPort().getValues()[0] + ".setColor(");
        this.sb.append(externalLedOffAction.getLedNo());
        this.sb.append(", 0, 0, 0);");
        nlIndent();
        this.sb.append("rgbled_" + externalLedOffAction.getPort().getValues()[0] + ".show();");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        this.sb.append("myVoltageSensor" + voltageSensor.getPort().getPortNumber() + ".read()");
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
    public Void visitDisplayImageAction(DisplayImageAction<Void> displayImageAction) {
        String valuesToDisplay = displayImageAction.getValuesToDisplay().toString();
        valuesToDisplay = valuesToDisplay.replaceAll("   ", "-");
        valuesToDisplay = valuesToDisplay.replaceAll("  ", "-");
        valuesToDisplay = valuesToDisplay.replaceAll(" #", "#");
        final String[] valuesToDisplayArray = valuesToDisplay.split("\n");
        final char[][] imageCharacterMatrix = new char[8][16];
        valuesToDisplayArray[0] = valuesToDisplayArray[0].split("Image \\[ \\[")[1].split("]")[0];
        imageCharacterMatrix[0] = valuesToDisplayArray[0].replaceAll(",", "").toCharArray();
        for ( int i = 1; i < 8; i++ ) {
            valuesToDisplayArray[i] = valuesToDisplayArray[i].replaceAll("\\[|\\]|\\] \\]", "");
            imageCharacterMatrix[i] = valuesToDisplayArray[i].replaceAll(",", "").toCharArray();
        }
        final int[] imageBitmap = new int[16];
        for ( int i = 0; i < 16; i++ ) {
            for ( int j = 0; j < 8; j++ ) {
                if ( imageCharacterMatrix[j][i] == '#' ) {
                    imageBitmap[i] += Util1.pow2(7 - j);
                }
            }
        }
        this.sb.append("unsigned char drawBuffer[16];");
        nlIndent();
        this.sb.append("unsigned char *drawTemp;");
        nlIndent();

        this.sb.append("drawTemp = new unsigned char[16]{");
        for ( int i = 0; i < 15; i++ ) {
            this.sb.append(imageBitmap[i]);
            this.sb.append(", ");
        }
        this.sb.append(imageBitmap[15]);
        this.sb.append("};");
        nlIndent();
        this.sb.append("memcpy(drawBuffer,drawTemp,16);");
        nlIndent();
        this.sb.append("free(drawTemp);");
        nlIndent();
        this.sb.append("myLEDMatrix_" + displayImageAction.getPort().getValues()[0] + ".drawBitmap(0, 0, 16, drawBuffer);");
        nlIndent();
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction<Void> displayTextAction) {
        this.sb.append("myLEDMatrix_" + displayTextAction.getPort().getValues()[0] + ".drawStr(0, 7, ");
        displayTextAction.getMsg().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMbotGetSampleSensor(GetSampleSensor<Void> getSampleSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}

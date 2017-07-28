package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.MakeBlockConfiguration;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.makeblock.LightSensorMode;
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
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.program.MakeBlockCodePreprocessVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.makeblock.FlameSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.Joystick;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.MakeblockAstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a
 * StringBuilder. <b>This representation is correct C code for Arduino.</b> <br>
 */
public class Ast2MakeBlockVisitor extends Ast2ArduVisitor implements MakeblockAstVisitor<Void> {
    private final MakeBlockConfiguration brickConfiguration;
    private final boolean isTimerSensorUsed;
    //    private final boolean isInfraredSensorUsed;
    private final boolean isTemperatureSensorUsed;
    private String temperatureSensorPort;
    private final boolean isToneActionUsed;

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ast2MakeBlockVisitor(MakeBlockConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases, int indentation) {
        super(phrases, indentation);
        this.brickConfiguration = brickConfiguration;
        MakeBlockCodePreprocessVisitor codePreprocessVisitor = new MakeBlockCodePreprocessVisitor(phrases, brickConfiguration);
        this.usedSensors = codePreprocessVisitor.getUsedSensors();
        this.usedActors = codePreprocessVisitor.getUsedActors();
        this.isTimerSensorUsed = codePreprocessVisitor.isTimerSensorUsed();
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        //        this.isInfraredSensorUsed = usedHardwareVisitor.isInfraredSensorUsed();
        this.isTemperatureSensorUsed = codePreprocessVisitor.isTemperatureSensorUsed();
        this.isToneActionUsed = codePreprocessVisitor.isToneActionUsed();
        this.loopsLabels = codePreprocessVisitor.getloopsLabelContainer();
    }

    /**
     * factory method to generate C++ code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param withWrapping if false the generated code will be without the surrounding configuration code
     */
    public static String generate(MakeBlockConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, boolean withWrapping) {
        Assert.notNull(brickConfiguration);

        Ast2MakeBlockVisitor astVisitor = new Ast2MakeBlockVisitor(brickConfiguration, programPhrases, withWrapping ? 1 : 0);
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
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        this.sb.append(motorOnAction.getPort().getValues()[1]);
        if ( duration != null ) {
            if ( duration.getType() == MotorMoveMode.ROTATIONS ) {
                this.sb.append(".runTurns(");
            } else if ( duration.getType() == MotorMoveMode.DEGREE ) {
                this.sb.append(".move(");
            }
            duration.getValue().visit(this);
            this.sb.append(", ");
        } else {
            this.sb.append(".run(");
        }
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(");");
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
        this.sb.append(motorStopAction.getPort().getValues()[1] + ".stop()");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        if ( driveAction.getDirection() == DriveDirection.FOREWARD ) {
            this.sb.append("myDrive.drive(");
            driveAction.getParam().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 1, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 1);");
            }
        } else {
            this.sb.append("myDrive.drive(");
            driveAction.getParam().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 0, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 0);");
            }
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        if ( curveAction.getDirection() == DriveDirection.FOREWARD ) {
            this.sb.append("myDrive.steer(");
            curveAction.getParamLeft().getSpeed().visit(this);
            this.sb.append(", ");
            curveAction.getParamRight().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 1, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 1);");
            }
        } else {
            this.sb.append("myDrive.steer(");
            curveAction.getParamLeft().getSpeed().visit(this);
            this.sb.append(", ");
            curveAction.getParamRight().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 0, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 0);");
            }
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        if ( turnAction.getDirection() == TurnDirection.LEFT ) {
            this.sb.append("myDrive.turn(");
            turnAction.getParam().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 1, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 1);");
            }
        } else {
            this.sb.append("myDrive.turn(");
            turnAction.getParam().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", 0, ");
                duration.getValue().visit(this);
                this.sb.append(");");
            } else {
                this.sb.append(", 0);");
            }
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        for ( UsedActor actor : this.usedActors ) {
            this.sb.append(actor.getPort().getValues()[1] + ".stop();");
            nlIndent();
        }
        return null;
    }

    // TODO: separate the block:
    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        switch ( (LightSensorMode) lightSensor.getMode() ) {
            // should go to ambientlight sensor block
            case RED:
                this.sb.append("myLight" + lightSensor.getPort().getPortNumber() + ".read()");
                break;
            //should go to light sensor block
            case AMBIENTLIGHT:
                this.sb.append("lineFinder.readSensor" + lightSensor.getPort().getPortNumber() + "()");
                break;
            default:
                break;
        }

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
        this.sb.append("myGyro.getGyro" + gyroSensor.getMode().toString() + "()");
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        this.sb.append("myGyro.getAngle" + accelerometer.getCoordinate() + "()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        //        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
        //            case DISTANCE: // TODO: change to send or create actor
        //
        //                break;
        //            case SEEK: // TODO: change to receive or remove mode
        //                //  ">> 16 & 0xff" add
        //                this.sb.append("ir.value");
        //                break;
        //            default:
        //                throw new DbcException("Invalid Infrared Sensor Mode!");
        //        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("myTemp" + temperatureSensor.getPort().getPortNumber() + ".getTemperature()");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("T.ShowSeconds()");
                break;
            case RESET:
                this.sb.append("T.ResetTimer();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("myTouch" + touchSensor.getPort().getPortNumber() + ".touched()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("ultraSensor.distanceCm()");
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
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\nvoid setup() \n");
        this.sb.append("{");
        nlIndent();
        this.generateActors();
        this.sb.append("Serial.begin(9600); ");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.StartTimer();");
        }
        //        if ( this.isInfraredSensorUsed ) {
        //            nlIndent();
        //            this.sb.append("ir.begin();");
        //        }
        nlIndent();
        generateUsedVars();
        this.sb.append("\n}");
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.Timer();");
        }

        if ( this.isTemperatureSensorUsed ) {
            for ( UsedSensor sensor : this.usedSensors ) {

                if ( sensor.getType().toString().equalsIgnoreCase("TEMPERATURE") ) {
                    this.temperatureSensorPort = sensor.getPort().getPortNumber();
                }
            }

            nlIndent();
            this.sb.append("myTemp" + this.temperatureSensorPort + ".update();");
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
        this.sb.append("#include <CountUpDownTimer.h>\n");
        this.sb.append("#include <RobertaFunctions.h>\n");
        this.sb.append("#include \"MeDrive.h\"\n\n");

        if ( this.isTimerSensorUsed ) {
            this.sb.append("#include <CountUpDown.h>\n\n");
            this.sb.append("CountUpDownTimer T(UP, HIGH);\n");
        }

        if ( this.isToneActionUsed ) {
            this.sb.append("MeBuzzer buzzer;\n");
        }

        this.sb.append("RobertaFunctions rob;\n");

        String actorPorts = "";
        for ( UsedActor actor : this.usedActors ) {
            actorPorts += actor.getPort().getValues()[0] + ", ";
            this.sb.append("MeDCMotor " + actor.getPort().getValues()[1] + "(" + actor.getPort().getValues()[0] + ");\n");
        }

        if ( this.usedActors.size() > 1 ) {
            actorPorts = actorPorts.substring(0, actorPorts.length() - 2);
            this.sb.append(
                "MeDrive myDrive("
                    + this.brickConfiguration.getLeftMotorPort().getValues()[0]
                    + ", "
                    + this.brickConfiguration.getRightMotorPort().getValues()[0]
                    + ");\n");
        }
        this.generateSensors();
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            this.sb.append("\n}\n");
        }
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case COLOR:
                    break;
                case INFRARED:
                    break;
                case ULTRASONIC:
                    this.sb.append("MeUltrasonicSensor ultraSensor(" + usedSensor.getPort() + ");\n");
                    break;
                case TEMPERATURE:
                    this.sb.append("MeHumiture myTemp" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case TOUCH:
                    this.sb.append("MeTouchSensor myTouch" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case LIGHT:
                    //TODO: change it according to new blocks
                    if ( usedSensor.getMode() == LightSensorMode.RED ) {
                        this.sb.append("MeLightSensor myLight" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    } else if ( usedSensor.getMode() == LightSensorMode.AMBIENTLIGHT ) {
                        this.sb.append("MeLineFollower lineFinder(" + usedSensor.getPort() + ");\n");
                    }
                    break;
                case COMPASS:
                    break;
                case GYRO:
                    this.sb.append("MEGyro myGyro(" + usedSensor.getPort() + ");\n");
                    break;
                case ACCELEROMETER:
                    this.sb.append("MEGyro myGyro(" + usedSensor.getPort() + ");\n");
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
                default:
                    throw new DbcException("Sensor is not supported!");
            }
        }
    }

    private void generateActors() {
        for ( UsedActor usedActor : this.usedActors ) {
            //this.sb.append(usedActor.getPort().getValues()[1] + ".begin();");
            //nlIndent();
        }
    }
}

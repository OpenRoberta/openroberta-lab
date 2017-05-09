package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.MakeBlockConfiguration;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
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
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.hardwarecheck.arduino.MakeBlockUsedHardwareVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.makeblock.TemperatureSensor;
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
    private boolean isTimerSensorUsed;

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
        MakeBlockUsedHardwareVisitor usedHardwareVisitor = new MakeBlockUsedHardwareVisitor(phrases, brickConfiguration);
        this.usedSensors = usedHardwareVisitor.getUsedSensors();
        this.usedActors = usedHardwareVisitor.getUsedActors();
        this.isTimerSensorUsed = usedHardwareVisitor.isTimerSensorUsed();
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

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
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
        //the axis names(getAxis) should be taken as input for gyro sensor however the implementations for that don't exist in GyroSensor.java
        //this.sb.append("myGyro.getAngle" + "(" + gyroSensor.getAxis() + ")");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {

        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("myTemp" + temperatureSensor.getPort().getPortNumber() + ".temperature()");
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
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        incrIndentation();
        generateUserDefinedMethods();
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.Timer();");
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = indexOfFunct.getLocation() == IndexLocation.LAST ? "rob.arrFindLast(" : "rob.arrFindFirst(";
        this.sb.append(methodName);
        arrayLen((Var<Void>) indexOfFunct.getParam().get(0));
        this.sb.append(", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("NULL");
            return null;
        }
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            this.sb.append("(");
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
            this.sb.append(" == 0)");
        } else {
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            return null;
        }
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(1).visit(this);
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("rob.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                this.sb.append("rob.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case WHOLE:
                this.sb.append("rob.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(",");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("rob.arrSum(");
                break;
            case MIN:
                this.sb.append("rob.arrMin(");
                break;
            case MAX:
                this.sb.append("rob.arrMax(");
                break;
            case AVERAGE:
                this.sb.append("rob.arrMean(");
                break;
            case MEDIAN:
                this.sb.append("rob.arrMedian(");
                break;
            case STD_DEV:
                this.sb.append("rob.arrStandardDeviatioin(");
                break;
            case RANDOM:
                this.sb.append("rob.arrRand(");
                break;
            case MODE:
                this.sb.append("rob.arrMode(");
                break;
            default:
                break;
        }
        arrayLen((Var<Void>) mathOnListFunct.getParam().get(0));
        this.sb.append(", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("rob.randomFloat()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("rob.randomIntegerInRange(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <MeOrion.h> \n");
        this.sb.append("#include <Wire.h>\n");
        this.sb.append("#include <SoftwareSerial.h>\n");
        this.sb.append("#include <CountUpDownTimer.h>\n");
        this.sb.append("#include <RobertaFunctions.h>\n");
        this.sb.append("#include \"MeDrive.h\"\n\n");

        if ( this.isTimerSensorUsed ) {
            this.sb.append("#include <CountUpDown.h>\n\n");
            this.sb.append("CountUpDownTimer T(UP, HIGH);\n");
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
        this.sb.append("\nvoid setup() \n");
        this.sb.append("{");
        nlIndent();
        this.generateActors();
        this.sb.append("Serial.begin(9600);");
        if ( this.isTimerSensorUsed ) {
            nlIndent();
            this.sb.append("T.StartTimer();");
        }
        this.sb.append("\n}");
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
                    this.sb.append("MeTemperature myTemp" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case TOUCH:
                    this.sb.append("MeTouchSensor myTouch" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case LIGHT:
                    this.sb.append("MeLightSensor myLight" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
                    break;
                case COMPASS:
                case GYRO:

                    this.sb.append("MEGyro myGyro");
                    break;
                case SOUND:
                    this.sb.append("MeSoundSensor mySound" + usedSensor.getPort().getPortNumber() + "(" + usedSensor.getPort() + ");\n");
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

package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ArduConfiguration;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.arduino.ActorPort;
import de.fhg.iais.roberta.mode.action.arduino.BlinkMode;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.arduino.InfraredSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.check.LoopsCounterVisitor;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.hardwarecheck.arduino.UsedHardwareVisitor;
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
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable C representation of a phrase to a
 * StringBuilder. <b>This representation is correct C code.</b> <br>
 */
public class Ast2ArduVisitor extends Ast2CppVisitor {
    public static final String INDENT = "    ";

    private final boolean isTimeSensorUsed;
    private final ArrayList<ArrayList<Phrase<Void>>> phrases;

    /**
     * initialize the C code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param usedFunctions in the current program
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    public Ast2ArduVisitor(
        ArrayList<ArrayList<Phrase<Void>>> phrases,
        ArduConfiguration brickConfiguration,
        UsedHardwareVisitor usedHardwareVisitor,
        int indentation) {
        super(brickConfiguration, usedHardwareVisitor.getUsedSensors(), indentation);
        this.isTimeSensorUsed = usedHardwareVisitor.isTimerSensorUsed();
        this.phrases = phrases;
        this.loopsLabels = new LoopsCounterVisitor(phrases).getloopsLabelContainer();
    }

    /**
     * factory method to generate C code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(ArduConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);
        UsedHardwareVisitor usedHardwareVisitor = new UsedHardwareVisitor(phrasesSet);
        Ast2ArduVisitor astVisitor = new Ast2ArduVisitor(phrasesSet, brickConfiguration, usedHardwareVisitor, withWrapping ? 1 : 0);
        astVisitor.generatePrefix(withWrapping, phrasesSet);
        astVisitor.generateCodeFromPhrases(phrasesSet, withWrapping, astVisitor);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(whitespace() + binary.getOp().getOpSymbol() + whitespace());

        switch ( binary.getOp() ) {
            case TEXT_APPEND:
                if ( binary.getRight().getVarType() == BlocklyType.BOOLEAN ) {
                    this.sb.append("rob.boolToString(");
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                    this.sb.append(")");
                } else {
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                }
                break;
            case DIVIDE:
                this.sb.append("((float) ");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);

        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT || repeatStmt.getMode() == RepeatStmt.Mode.FOREVER_ARDU;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                increaseLoopCounter();
                //generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                String varType;
                String expression = repeatStmt.getExpr().toString();
                String segments[] = expression.split(",");
                String element = segments[2];
                String arr = null;
                if ( expression.contains("NUMBER") ) {
                    varType = "double";
                } else if ( expression.contains("BOOLEAN") ) {
                    varType = "bool";
                } else {
                    varType = "String";
                }
                if ( !segments[6].contains("java.util") ) {
                    arr = segments[6].substring(segments[6].indexOf("[") + 1, segments[6].indexOf("]"));
                    this.sb.append(
                        "for("
                            + varType
                            + whitespace()
                            + element
                            + " = 0;"
                            + element
                            + " < sizeof("
                            + arr
                            + "Raw"
                            + ") / sizeof("
                            + arr
                            + "Raw"
                            + "[0]); "
                            + element
                            + "++) {");
                } else {
                    this.sb.append("while(false){");
                }
                break;

            case FOREVER_ARDU:
                repeatStmt.getList().visit(this);
                return null;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt(repeatStmt);
        }
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("delay(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("delay(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("rob.lcdClear();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        BlinkMode blinkingMode = (BlinkMode) lightAction.getBlinkMode();

        switch ( blinkingMode ) {
            case ON:
                this.sb.append("one.led(HIGH);");
                break;
            case OFF:
                this.sb.append("one.led(LOW);");
                break;
            default:
                throw new DbcException("Invalide blinking mode: " + blinkingMode);
        }
        return null;

    }

    //won't be used
    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    //won't be used
    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    //won't be used
    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String toChar = "";
        String varType = showTextAction.getMsg().getVarType().toString();
        boolean isVar = showTextAction.getMsg().getKind().getName().toString().equals("VAR");
        IColorSensorMode mode = null;
        Expr<Void> tt = showTextAction.getMsg();
        if ( tt.getKind().hasName("SENSOR_EXPR") ) {
            de.fhg.iais.roberta.syntax.sensor.Sensor<Void> sens = ((SensorExpr<Void>) tt).getSens();
            if ( sens.getKind().hasName("COLOR_SENSING") ) {
                mode = ((ColorSensor<Void>) sens).getMode();
            }
        }

        this.sb.append("one.lcd");
        if ( showTextAction.getY().toString().equals("NumConst [1]") || showTextAction.getY().toString().equals("NumConst [2]") ) {
            showTextAction.getY().visit(this);
        } else {
            this.sb.append("1");
        }

        this.sb.append("(");

        if ( isVar && (varType.equals("STRING") || varType.equals("COLOR"))
            || mode != null && !mode.toString().equals("RED") && !mode.toString().equals("RGB") ) {
            toChar = ".c_str()";
        }

        if ( varType.equals("BOOLEAN") ) {
            this.sb.append("rob.boolToString(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }

        this.sb.append(toChar + ");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //9 - sound port
        this.sb.append("tone(9, ");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        String methodName;
        String port = null;
        final boolean isDuration = motorOnAction.getParam().getDuration() != null;
        final boolean isServo = motorOnAction.getPort() == ActorPort.A || motorOnAction.getPort() == ActorPort.D;
        if ( isServo ) {
            methodName = motorOnAction.getPort() == ActorPort.A ? "one.servo1(" : "one.servo2(";
        } else {
            methodName = isDuration ? "rob.move1mTime(" : "one.move1m(";
            port = motorOnAction.getPort() == ActorPort.B ? "1" : "2";
        }
        this.sb.append(methodName);
        if ( !isServo ) {
            this.sb.append(port + ", ");
        }
        if ( reverse ) {
            this.sb.append("-");
        }
        motorOnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    // not needed
    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    //impossible to implement it without encoder
    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String port = motorStopAction.getPort() == ActorPort.B ? "1" : "2";
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            this.sb.append("one.stop1m(");

        } else {
            this.sb.append("one.brake1m(");
        }
        this.sb.append(port + ")");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        //this.sb.append(this.brickConfiguration.generateText("q") + "\n");
        final boolean isRegulatedDrive =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated()
                || this.brickConfiguration.getActorOnPort(ActorPort.A).isRegulated();
        final boolean isDuration = driveAction.getParam().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = driveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "rob.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        if ( (!reverse && localReverse) || (reverse && !localReverse) ) {
            sign = "-";
        }
        this.sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(sign);
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        final boolean isRegulatedDrive =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated()
                || this.brickConfiguration.getActorOnPort(ActorPort.A).isRegulated();
        final boolean isDuration = curveAction.getParamLeft().getDuration() != null;
        final boolean reverse =
            this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection() == DriveDirection.BACKWARD
                || this.brickConfiguration.getActorOnPort(ActorPort.A).getRotationDirection() == DriveDirection.BACKWARD;
        final boolean localReverse = curveAction.getDirection() == DriveDirection.BACKWARD;
        String methodName;
        String sign = "";
        if ( isDuration ) {
            methodName = "rob.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        if ( (!reverse && localReverse) || (reverse && !localReverse) ) {
            sign = "-";
        }
        this.sb.append(sign);
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(sign);
        curveAction.getParamRight().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            curveAction.getParamLeft().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    // TURN ACTIONS
    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        Actor leftMotor = this.brickConfiguration.getLeftMotor();
        Actor rightMotor = this.brickConfiguration.getRightMotor();
        boolean isRegulatedDrive = leftMotor.isRegulated() || rightMotor.isRegulated();
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isReverseLeftMotor = leftMotor.getRotationDirection() == DriveDirection.BACKWARD;
        boolean isReverseRightMotor = rightMotor.getRotationDirection() == DriveDirection.BACKWARD;
        boolean isTurnRight = turnAction.getDirection() == TurnDirection.RIGHT;

        String methodName;
        String rightMotorSign = "";
        String leftMotorSign = "";

        if ( isTurnRight && !isReverseRightMotor ) {
            rightMotorSign = "-";
        }

        if ( !isTurnRight && !isReverseLeftMotor ) {
            leftMotorSign = "-";
        }

        if ( isDuration ) {
            methodName = "rob.moveTime";
        } else {
            methodName = "one.move";
        }
        if ( isRegulatedDrive ) {
            methodName = methodName + "PID";
        }
        methodName = methodName + "(";
        this.sb.append(methodName);
        this.sb.append(leftMotorSign);
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append(", ");
        this.sb.append(rightMotorSign);
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");

        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("one.stop();");
        return null;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("one.readAdc(");
        //ports from 0 to 7
        this.sb.append(lightSensor.getPort().getPortNumber()); // we could add "-1" so the number of ports would be 1-8 for users
        this.sb.append(") / 10.23");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        IBrickKey button = brickSensor.getKey();
        String btnNumber;
        switch ( button.toString() ) {
            case "ENTER":
                btnNumber = "2";
                break;
            case "LEFT":
                btnNumber = "1";
                break;
            case "RIGHT":
                btnNumber = "3";
                break;
            default:
                btnNumber = "123";
                break;
        }
        this.sb.append("rob.buttonIsPressed(" + btnNumber + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String port = colorSensor.getPort().getPortNumber();
        String colors;
        if ( port == "1" ) {
            colors = "colorsLeft, ";
        } else {
            colors = "colorsRight, ";
        }
        switch ( getEnumCode(colorSensor.getMode()) ) {
            case "ColorSensorMode.COLOUR":
                this.sb.append("rob.colorSensorColor(");
                this.sb.append(colors);
                this.sb.append(colorSensor.getPort().getPortNumber());
                this.sb.append(")");
                break;
            case "ColorSensorMode.RGB":
                this.sb.append("{(double) rob.colorSensorRGB(" + colors + port);
                this.sb.append(")[0], (double) rob.colorSensorRGB(" + colors + port);
                this.sb.append(")[1], (double) rob.colorSensorRGB(" + colors + port);
                this.sb.append(")[2]}");
                break;
            case "ColorSensorMode.RED":
                this.sb.append("rob.colorSensorLight(" + colors + port);
                this.sb.append(")");
                break;
        }
        return null;
    }

    //no such sensor
    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.sb.append("rob.readBearing()");
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        this.sb.append("one.readBattery()");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort().getPortNumber();
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            case OBSTACLE:
                this.sb.append("rob.infraredSensorObstacle(");
                break;
            case SEEK:
                this.sb.append("rob.infraredSensorPresence(");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }
        this.sb.append(port + ")");
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

    //no such sensor
    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort().getPortNumber();
        if ( ultrasonicSensor.getPort().getPortNumber().equals("3") ) {
            this.sb.append("rob.sonar()");
        } else {
            this.sb.append("rob.ultrasonicDistance(" + port + ")");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        decrIndentation();
        mainTask.getVariables().visit(this);
        incrIndentation();
        generateUserDefinedMethods(this.phrases);
        this.sb.append("\n").append("void loop() \n");
        this.sb.append("{");
        if ( this.isTimeSensorUsed ) {
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

    //TODO: check the change of the list
    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("{");
        listCreate.getValue().visit(this);
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
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
        switch ( getEnumCode(listGetIndex.getLocation()) ) {
            case "IndexLocation.FROM_START":
                listGetIndex.getParam().get(1).visit(this);
                break;
            case "IndexLocation.FROM_END":
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case "IndexLocation.FIRST":
                this.sb.append("0");
                break;
            case "IndexLocation.LAST":
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case "IndexLocation.RANDOM":
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
        switch ( getEnumCode(listSetIndex.getLocation()) ) {
            case "IndexLocation.FROM_START":
                listSetIndex.getParam().get(2).visit(this);
                break;
            case "IndexLocation.FROM_END":
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case "IndexLocation.FIRST":
                this.sb.append("0");
                break;
            case "IndexLocation.LAST":
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case "IndexLocation.RANDOM":
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
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("pow(");
        mathPowerFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind().hasName("BINARY") && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && !expr.getKind().hasName("BINARY") ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("(");
            expr.visit(this);
            sb.append(")");
        }
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(");
        expr.visit(this);
        this.sb.append(")" + whitespace() + "{");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + "float" + whitespace());
        final ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace() + "=" + whitespace());
        expressions.get().get(1).visit(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace());
        this.sb.append("<" + whitespace());
        expressions.get().get(2).visit(this);
        this.sb.append(";" + whitespace());
        expressions.get().get(0).visit(this);
        this.sb.append(whitespace());
        this.sb.append("+=" + whitespace());
        expressions.get().get(3).visit(this);
        this.sb.append(")" + whitespace() + "{");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        nlIndent();
        this.sb.append("break;");
    }

    private void generateSensors() {
        for ( UsedSensor usedSensor : this.usedSensors ) {
            switch ( usedSensor.getType() ) {
                case COLOR:
                    nlIndent();
                    this.sb.append("brm.setRgbStatus(ENABLE);");
                    break;
                case INFRARED:
                    nlIndent();
                    this.sb.append("one.obstacleEmitters(ON);");
                    break;
                case ULTRASONIC:
                    nlIndent();
                    this.sb.append("brm.setSonarStatus(ENABLE);");
                    break;
                case LIGHT:
                case COMPASS:
                case SOUND:
                case TOUCH:
                    break;
                default:
                    throw new DbcException("Sensor is not supported!");
            }
        }
    }

    private void generatePrefix(boolean withWrapping, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("#include <math.h> \n");
        this.sb.append("#include <CountUpDownTimer.h> \n");
        // Bot'n Roll ONE A library:
        this.sb.append("#include <BnrOneA.h>   // Bot'n Roll ONE A library \n");
        //Bot'n Roll CoSpace Rescue Module library (for the additional sonar kit):
        this.sb.append("#include <BnrRescue.h>   // Bot'n Roll CoSpace Rescue Module library \n");
        //additional Roberta functions:
        this.sb.append("#include <RobertaFunctions.h>   // Open Roberta library \n");
        // SPI communication library required by BnrOne.cpp"
        this.sb.append("#include <SPI.h>   // SPI communication library required by BnrOne.cpp \n");
        // required by BnrRescue.cpp (for the additional sonar kit):
        this.sb.append("#include <Wire.h>   //a library required by BnrRescue.cpp for the additional sonar  \n");
        // declaration of object variable to control the Bot'n Roll ONE A and Rescue:
        this.sb.append("BnrOneA one; \n");
        this.sb.append("BnrRescue brm; \n");
        this.sb.append("RobertaFunctions rob(one, brm);  \n");
        if ( this.isTimeSensorUsed ) {
            this.sb.append("CountUpDownTimer T(UP, HIGH); \n");
        }
        this.sb.append("#define SSPIN  2 \n");
        this.sb.append("#define MODULE_ADDRESS 0x2C \n");
        this.sb.append("byte colorsLeft[3]={0,0,0}; \n");
        this.sb.append("byte colorsRight[3]={0,0,0}; \n \n");
        this.sb.append("void setup() \n");
        this.sb.append("{");
        nlIndent();
        this.sb.append("Wire.begin();");
        nlIndent();
        //set baud rate to 9600 for printing values at serial monitor:
        this.sb.append("Serial.begin(9600);   // sets baud rate to 9600bps for printing values at serial monitor.");
        nlIndent();
        // start the communication module:
        this.sb.append("one.spiConnect(SSPIN);   // starts the SPI communication module");
        nlIndent();
        this.sb.append("brm.i2cConnect(MODULE_ADDRESS);   // starts I2C communication");
        nlIndent();
        this.sb.append("brm.setModuleAddress(0x2C);");
        nlIndent();
        // stop motors:
        this.sb.append("one.stop();");
        nlIndent();
        this.sb.append("rob.setOne(one);");
        nlIndent();
        this.sb.append("rob.setBrm(brm);");
        nlIndent();
        this.generateSensors();
        if ( this.isTimeSensorUsed ) {
            nlIndent();
            this.sb.append("T.StartTimer();");
        }
        this.sb.append("\n}\n");
    }

    private void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, Ast2ArduVisitor astVisitor) {
        boolean mainBlock = false;
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            boolean isCreateMethodPhrase = phrases.get(1).getKind().getCategory() != Category.METHOD;
            if ( isCreateMethodPhrase ) {
                for ( Phrase<Void> phrase : phrases ) {
                    mainBlock = handleMainBlocks(astVisitor, mainBlock, phrase);
                    phrase.visit(astVisitor);
                }
                if ( mainBlock ) {
                    generateSuffix(withWrapping, astVisitor);
                    mainBlock = false;
                }
            }
        }
    }

    private boolean handleMainBlocks(Ast2ArduVisitor astVisitor, boolean mainBlock, Phrase<Void> phrase) {
        //        if (phrase.getProperty().isInTask() != false ) { //TODO: old unit tests have no inTask property
        if ( phrase.getKind().getCategory() != Category.TASK ) {
            astVisitor.nlIndent();
        } else if ( !phrase.getKind().hasName("LOCATION") ) {
            mainBlock = true;
        }
        //        }
        return mainBlock;
    }

    private void generateSuffix(boolean withWrapping, Ast2ArduVisitor astVisitor) {
        if ( withWrapping ) {
            astVisitor.sb.append("\n}\n");
        }
    }

    private void arrayLen(Var<Void> arr) {
        this.sb.append("sizeof(" + arr.getValue() + "Raw" + ")/sizeof(" + arr.getValue() + "Raw" + "[0])");
    }

    private void addContinueLabelToLoop() {
        if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
            nlIndent();
            this.sb.append("continue_loop" + this.currenLoop.getLast() + ":");
        }
    }

    private void addBreakLabelToLoop(boolean isWaitStmt) {
        if ( !isWaitStmt ) {
            if ( this.loopsLabels.get(this.currenLoop.getLast()) ) {
                this.sb.append("break_loop" + this.loopCounter + ":");
                nlIndent();
            }
            this.currenLoop.removeLast();
        }
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;
    }
}

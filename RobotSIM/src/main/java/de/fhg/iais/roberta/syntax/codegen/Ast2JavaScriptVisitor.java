package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.action.sim.ActorPort;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.sim.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.sim.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.sim.TimerSensorMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
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
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

public class Ast2JavaScriptVisitor implements AstVisitor<Void> {
    private static final String MOTOR_LEFT = "CONST.MOTOR_LEFT";
    private static final String MOTOR_RIGHT = "CONST.MOTOR_RIGHT";
    private final StringBuilder sb = new StringBuilder();
    private int stmtsNumber = 0;
    private int methodsNumber = 0;
    private ArrayList<Boolean> inStmt = new ArrayList<>();
    private Configuration brickConfiguration;

    private Ast2JavaScriptVisitor(Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;

    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(phrasesSet.size() >= 1);
        Assert.notNull(brickConfiguration);

        Ast2JavaScriptVisitor astVisitor = new Ast2JavaScriptVisitor(brickConfiguration);
        generateCodeFromPhrases(phrasesSet, astVisitor);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        sb.append("createConstant(CONST." + numConst.getKind() + ", " + numConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        sb.append("createMathConstant('" + mathConst.getMathConst() + "')");
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        sb.append("createConstant(CONST." + boolConst.getKind() + ", " + boolConst.isValue() + ")");
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        sb.append("createConstant(CONST." + stringConst.getKind() + ", '" + stringConst.getValue() + "')");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        sb.append("createConstant(CONST." + nullConst.getKind() + ", undefined)");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        sb.append("createConstant(CONST." + colorConst.getKind() + ", CONST.COLOR_ENUM." + colorConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        sb.append("createVarReference(CONST." + var.getTypeVar() + ", \"" + var.getValue() + "\")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        sb.append("createVarDeclaration(CONST." + var.getTypeVar() + ", \"" + var.getName() + "\", ");
        if ( var.getValue().getKind() == BlockType.EXPR_LIST ) {
            ExprList<Void> list = (ExprList<Void>) var.getValue();
            if ( list.get().size() == 2 ) {
                list.get().get(1).visit(this);
            } else {
                list.get().get(0).visit(this);
            }
        } else {
            var.getValue().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        sb.append("createUnaryExpr(CONST." + unary.getOp() + ", ");
        unary.getExpr().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        String method = "createBinaryExpr(CONST." + binary.getOp() + ", ";
        String end = ")";
        // FIXME: The math change should be removed from the binary expression since it is a statement
        switch ( binary.getOp() ) {
            case MATH_CHANGE:
                method = "createMathChange(";
                //                end = createClosingBracket();
                break;
            case TEXT_APPEND:
                method = "createTextAppend(";
                end = createClosingBracket();
                break;
            default:
                break;
        }
        sb.append(method);
        binary.getLeft().visit(this);
        sb.append(", ");
        binary.getRight().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        sb.append("createBinaryExpr(CONST." + mathPowerFunct.getFunctName() + ", ");
        mathPowerFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                sb.append("createConstant(CONST.STRING_CONST, '')");
                break;
            case "java.lang.Boolean":
                sb.append("createConstant(CONST.BOOL_CONST, true)");
                break;
            case "java.lang.Integer":
                sb.append("createConstant(CONST.NUM_CONST, 0)");
                break;
            case "java.util.ArrayList":
                sb.append("[]");
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                sb.append("createConstant(CONST.NULL_CONST, null)");
                break;
            default:
                sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( expr.getKind() != BlockType.EMPTY_EXPR ) {
                if ( first ) {
                    first = false;
                } else {
                    sb.append(", ");
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        String end = createClosingBracket();
        sb.append("createAssignStmt(\"" + assignStmt.getName().getValue());
        sb.append("\", ");
        assignStmt.getExpr().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        String end = "";
        if ( !isInStmt() ) {
            sb.append("var stmt" + stmtsNumber + " = ");
            increaseStmt();
            end = ";";
        }
        exprStmt.getExpr().visit(this);
        sb.append(end);

        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.isTernary() ) {
            sb.append("createTernaryExpr(");
            ifStmt.getExpr().get(0).visit(this);
            sb.append(", ");
            ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
            sb.append(", ");
            ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
            sb.append(")");
        } else {
            String end = createClosingBracket();
            sb.append("createIfStmt([");
            appendIfStmtConditions(ifStmt);
            sb.append("], [");
            appendThenStmts(ifStmt);
            sb.append("]");
            appendElseStmt(ifStmt);
            sb.append(end);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        String end = createClosingBracket();
        appendRepeatStmtCondition(repeatStmt);
        addInStmt();
        appendRepeatStmtStatements(repeatStmt);
        sb.append("]");
        sb.append(end);
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        if ( stmtList.get().size() == 0 ) {
            return null;
        }
        String symbol = isInStmt() ? ", " : "\n";
        for ( int i = 0; i < stmtList.get().size(); i++ ) {
            stmtList.get().get(i).visit(this);
            sb.append(symbol);
        }
        removeLastComma();
        removeInStmt();
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        String end = createClosingBracket();
        sb.append("createDriveAction(");
        driveAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).getRotationDirection();
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        sb.append(", CONST." + driveDirection);
        appenDriveDuration(driveAction);
        sb.append(end);
        return null;
    }

    private DriveDirection getDriveDirection(boolean isReverse) {
        if ( isReverse ) {
            return DriveDirection.BACKWARD;
        }
        return DriveDirection.FOREWARD;
    }

    private void appenDriveDuration(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        if ( isDuration ) {
            sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        String end = createClosingBracket();
        sb.append("createTurnAction(");
        turnAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).getRotationDirection();
        ITurnDirection turnDirection = turnAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }
        sb.append(", CONST." + turnDirection);
        appenTurnDuration(turnAction);
        sb.append(end);
        return null;
    }

    private TurnDirection getTurnDirection(boolean isReverse) {
        if ( isReverse ) {
            return TurnDirection.RIGHT;
        }
        return TurnDirection.LEFT;
    }

    private void appenTurnDuration(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        if ( isDuration ) {
            sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String end = createClosingBracket();
        sb.append("createTurnLight(CONST." + lightAction.getColor() + ", CONST." + lightAction.getBlinkMode());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String end = createClosingBracket();
        sb.append("createStatusLight(CONST." + lightStatusAction.getStatus());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        sb.append("createGetMotorPower(" + (motorGetPowerAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        boolean isDuration = motorOnAction.getParam().getDuration() != null;
        String end = createClosingBracket();
        sb.append("createMotorOnAction(");
        motorOnAction.getParam().getSpeed().visit(this);
        sb.append(", " + (motorOnAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        if ( isDuration ) {
            sb.append(", createDuration(CONST.");
            sb.append(motorOnAction.getParam().getDuration().getType().toString() + ", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
            sb.append(")");
        }
        sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String end = createClosingBracket();
        sb.append("createSetMotorPowerAction(" + (motorSetPowerAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ", ");
        motorSetPowerAction.getPower().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String end = createClosingBracket();
        sb.append("createStopMotorAction(");
        sb.append((motorStopAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        String end = createClosingBracket();
        sb.append("createClearDisplayAction(");
        sb.append(end);
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            String end = createClosingBracket();
            sb.append("createSetVolumeAction(CONST." + volumeAction.getMode() + ", ");
            volumeAction.getVolume().visit(this);
            sb.append(end);
        } else {
            sb.append("createGetVolume()");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        String end = createClosingBracket();
        sb.append("createPlayFileAction(CONST." + playFileAction.getFileName());
        sb.append(end);
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        String end = createClosingBracket();
        sb.append("createShowPictureAction('" + showPictureAction.getPicture() + "', ");
        showPictureAction.getX().visit(this);
        sb.append(", ");
        showPictureAction.getY().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String end = createClosingBracket();
        sb.append("createShowTextAction(");
        showTextAction.getMsg().visit(this);
        sb.append(", ");
        showTextAction.getX().visit(this);
        sb.append(", ");
        showTextAction.getY().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        String end = createClosingBracket();
        sb.append("createStopDrive(");
        sb.append(end);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        String end = createClosingBracket();
        sb.append("createToneAction(");
        toneAction.getFrequency().visit(this);
        sb.append(", ");
        toneAction.getDuration().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        sb.append("createGetSample(CONST.BUTTONS, CONST." + brickSensor.getKey() + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        sb.append("createGetSample(CONST.COLOR, CONST." + colorSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderMotor = (encoderSensor.getMotorPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString();
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String end = createClosingBracket();
            sb.append("createResetEncoderSensor(" + encoderMotor);
            sb.append(end);
        } else {
            sb.append("createGetSampleEncoderSensor(" + encoderMotor + ", CONST." + encoderSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            String end = createClosingBracket();
            sb.append("createResetGyroSensor(");
            sb.append(end);
        } else {
            sb.append("createGetSample(CONST.GYRO, CONST." + gyroSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        sb.append("createGetSample(CONST.INFRARED, CONST." + infraredSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                sb.append("createGetSample(CONST.TIMER, 'timer" + timerSensor.getTimer() + "')");
                break;
            case RESET:
                String end = createClosingBracket();
                sb.append("createResetTimer('timer" + timerSensor.getTimer() + "'");
                sb.append(end);
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        sb.append("createGetSample(CONST.TOUCH)");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        sb.append("createGetSample(CONST.ULTRASONIC, CONST." + ultrasonicSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        sensorGetSample.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        if ( mainTask.getDebug().equals("TRUE") ) {
            String end = createClosingBracket();
            sb.append("createDebugAction(");
            sb.append(end);
        }
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        String end = createClosingBracket();
        sb.append("createWaitStmt([");
        addInStmt();
        visitStmtList(waitStmt.getStatements());
        removeInStmt();
        sb.append("]");
        sb.append(end);
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        String end = createClosingBracket();
        sb.append("createWaitTimeStmt(");
        waitTimeStmt.getTime().visit(this);
        sb.append(end);
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        sb.append("createGetSubList({list: ");
        getSubFunct.getParam().get(0).visit(this);
        sb.append(", where1: CONST.");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        sb.append(where1);
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            sb.append(", at1: ");
            getSubFunct.getParam().get(1).visit(this);
        }
        sb.append(", where2: CONST.");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        sb.append(where2);
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            sb.append(", at2: ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        sb.append("})");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        sb.append("createListFindItem(CONST." + indexOfFunct.getLocation() + ", ");
        indexOfFunct.getParam().get(0).visit(this);
        sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        String methodName = "createListLength(";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "createListIsEmpty(";
        }
        sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        sb.append("createCreateListWith(CONST.ARRAY_" + listCreate.getTypeVar() + ", [");
        listCreate.getValue().visit(this);
        sb.append("])");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        String end = createClosingBracket();
        sb.append("createListsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        sb.append(", CONST.");
        sb.append(listSetIndex.getElementOperation());
        sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        sb.append(", CONST.");
        sb.append(listSetIndex.getLocation());
        if ( listSetIndex.getParam().size() == 3 ) {
            sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        sb.append(end);
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        String methodName = "createListsGetIndex(";
        String end = ")";
        if ( listGetIndex.getElementOperation().isStatment() ) {
            methodName = "createListsGetIndexStmt(";
            end = createClosingBracket();
        }
        sb.append(methodName);
        listGetIndex.getParam().get(0).visit(this);
        sb.append(", CONST.");
        sb.append(listGetIndex.getElementOperation());
        sb.append(", CONST.");
        sb.append(listGetIndex.getLocation());
        if ( listGetIndex.getParam().size() == 2 ) {
            sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        sb.append(end);
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        sb.append("createCreateListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        sb.append("createMathConstrainFunct(");
        mathConstrainFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        sb.append("createMathPropFunct('" + mathNumPropFunct.getFunctName() + "', ");
        mathNumPropFunct.getParam().get(0).visit(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            sb.append(", ");
            mathNumPropFunct.getParam().get(1).visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        sb.append("createMathOnList(CONST." + mathOnListFunct.getFunctName() + ", ");
        mathOnListFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        sb.append("createRandDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        sb.append("createRandInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        sb.append("createSingleFunction('" + mathSingleFunct.getFunctName() + "', ");
        mathSingleFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        sb.append("createTextJoin([");
        textJoinFunct.getParam().visit(this);
        sb.append("])");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        sb.append("var method" + methodsNumber + " = createMethodVoid('" + methodVoid.getMethodName() + "', [");
        methodVoid.getParameters().visit(this);
        sb.append("], [");
        addInStmt();
        methodVoid.getBody().visit(this);
        sb.append("]);\n");
        increaseMethods();
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        sb.append("var method" + methodsNumber + " = createMethodReturn('" + methodReturn.getMethodName() + "', [");
        addInStmt();
        methodReturn.getBody().visit(this);
        sb.append("], ");
        methodReturn.getReturnValue().visit(this);
        sb.append(");\n");
        increaseMethods();
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        sb.append("createIfReturn(");
        methodIfReturn.getCondition().visit(this);
        sb.append(", ");
        methodIfReturn.getReturnValue().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        String end = ")";
        String name = "createMethodCallReturn('";
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            name = "createMethodCallVoid('";
            end = createClosingBracket();
        }
        sb.append(name + methodCall.getMethodName() + "', [");
        methodCall.getParameters().visit(this);
        sb.append("], [");
        methodCall.getParametersValues().visit(this);
        sb.append("]" + end);
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
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

    private void increaseStmt() {
        stmtsNumber++;
    }

    private void increaseMethods() {
        methodsNumber++;
    }

    /**
     * @return the inStmt
     */
    private boolean isInStmt() {
        if ( inStmt.size() == 0 ) {
            return false;
        }
        return inStmt.get(inStmt.size() - 1);
    }

    /**
     * @param inStmt the inStmt to set
     */
    private void addInStmt() {
        inStmt.add(true);
    }

    private void removeInStmt() {
        if ( inStmt.size() != 0 ) {
            inStmt.remove(inStmt.size() - 1);
        }
    }

    private static void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Ast2JavaScriptVisitor astVisitor) {
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(astVisitor);
            }
        }
        appendProgramInitialization(astVisitor);
    }

    private static void appendProgramInitialization(Ast2JavaScriptVisitor astVisitor) {
        astVisitor.sb.append("var blocklyProgram = {");
        appendMethodsInitialization(astVisitor);
        appendStmtsInitialization(astVisitor);
        astVisitor.sb.append("};");
    }

    private static void appendStmtsInitialization(Ast2JavaScriptVisitor astVisitor) {
        astVisitor.sb.append("'programStmts': [");
        if ( astVisitor.stmtsNumber > 0 ) {
            for ( int i = 0; i < astVisitor.stmtsNumber; i++ ) {
                astVisitor.sb.append("stmt" + i);
                if ( i != astVisitor.stmtsNumber - 1 ) {
                    astVisitor.sb.append(",");
                }

            }
        }
        astVisitor.sb.append("]");
    }

    private static void appendMethodsInitialization(Ast2JavaScriptVisitor astVisitor) {
        if ( astVisitor.methodsNumber > 0 ) {
            astVisitor.sb.append("'programMethods': [");
            for ( int i = 0; i < astVisitor.methodsNumber; i++ ) {
                astVisitor.sb.append("method" + i);
                if ( i != astVisitor.methodsNumber - 1 ) {
                    astVisitor.sb.append(",");
                } else {
                    astVisitor.sb.append("], ");
                }
            }
        }
    }

    private void appendIfStmtConditions(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            ifStmt.getExpr().get(i).visit(this);
            if ( i < ifStmt.getExpr().size() - 1 ) {
                sb.append(", ");
            }
        }
    }

    private void appendElseStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            sb.append(", [");
            addInStmt();
            ifStmt.getElseList().visit(this);
            sb.append("]");
        }
    }

    private void appendThenStmts(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getThenList().size(); i++ ) {
            addInStmt();
            sb.append("[");
            ifStmt.getThenList().get(i).visit(this);
            boolean isLastStmt = i < ifStmt.getThenList().size() - 1;
            sb.append("]");
            if ( isLastStmt ) {
                sb.append(", ");
            }
        }
    }

    private void appendRepeatStmtStatements(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            if ( repeatStmt.getList().get().size() != 0 ) {
                sb.append("[");
                repeatStmt.getList().visit(this);
                sb.append("]");
            }
        } else {
            repeatStmt.getList().visit(this);
        }
    }

    private void appendRepeatStmtCondition(RepeatStmt<Void> repeatStmt) {
        switch ( repeatStmt.getMode() ) {
            case WAIT:
                sb.append("createIfStmt([");
                repeatStmt.getExpr().visit(this);
                sb.append("], [");
                break;
            case TIMES:
                sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                ((NumConst<Void>) ((ExprList<Void>) repeatStmt.getExpr()).get().get(2)).visit(this);
                sb.append(", [");
                break;
            case FOREVER:
            case WHILE:
            case UNTIL:
                sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                repeatStmt.getExpr().visit(this);
                sb.append(", [");
                break;
            case FOR:
                sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", [");
                repeatStmt.getExpr().visit(this);
                sb.append("], [");
                break;
            case FOR_EACH:
                sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                repeatStmt.getExpr().visit(this);
                sb.append(", [");
                break;

            default:
                throw new DbcException("Invalid repeat mode");

        }
    }

    private String createClosingBracket() {
        String end = ")";
        if ( !isInStmt() ) {
            sb.append("var stmt" + stmtsNumber + " = ");
            increaseStmt();
            end = ");\n";
        }
        return end;
    }

    private void removeLastComma() {
        if ( isInStmt() ) {
            sb.setLength(sb.length() - 2);
        }
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> driveAction) {
        // TODO Auto-generated method stub
        return null;
    }

}

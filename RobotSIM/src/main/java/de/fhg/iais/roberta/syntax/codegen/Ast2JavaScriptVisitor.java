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
import de.fhg.iais.roberta.syntax.MotorDuration;
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
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
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
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
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
        this.sb.append("createConstant(CONST." + numConst.getKind().getName() + ", " + numConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        this.sb.append("createMathConstant('" + mathConst.getMathConst() + "')");
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append("createConstant(CONST." + boolConst.getKind().getName() + ", " + boolConst.isValue() + ")");
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("createConstant(CONST." + stringConst.getKind().getName() + ", '" + stringConst.getValue() + "')");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("createConstant(CONST." + nullConst.getKind().getName() + ", undefined)");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append("createConstant(CONST." + colorConst.getKind().getName() + ", CONST.COLOR_ENUM." + colorConst.getValue() + ")");
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
        this.sb.append("createVarReference(CONST." + var.getTypeVar() + ", \"" + var.getValue() + "\")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append("createVarDeclaration(CONST." + var.getTypeVar() + ", \"" + var.getName() + "\", ");
        if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
            ExprList<Void> list = (ExprList<Void>) var.getValue();
            if ( list.get().size() == 2 ) {
                list.get().get(1).visit(this);
            } else {
                list.get().get(0).visit(this);
            }
        } else {
            var.getValue().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        this.sb.append("createUnaryExpr(CONST." + unary.getOp() + ", ");
        unary.getExpr().visit(this);
        this.sb.append(")");
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
        this.sb.append(method);
        binary.getLeft().visit(this);
        this.sb.append(", ");
        binary.getRight().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("createBinaryExpr(CONST." + mathPowerFunct.getFunctName() + ", ");
        mathPowerFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        this.sb.append(")");
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
                this.sb.append("createConstant(CONST.STRING_CONST, '')");
                break;
            case "java.lang.Boolean":
                this.sb.append("createConstant(CONST.BOOL_CONST, true)");
                break;
            case "java.lang.Integer":
                this.sb.append("createConstant(CONST.NUM_CONST, 0)");
                break;
            case "java.util.ArrayList":
                this.sb.append("[]");
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                this.sb.append("createConstant(CONST.NULL_CONST, null)");
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                if ( first ) {
                    first = false;
                } else {
                    this.sb.append(", ");
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
        this.sb.append("createAssignStmt(\"" + assignStmt.getName().getValue());
        this.sb.append("\", ");
        assignStmt.getExpr().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        String end = "";
        if ( !isInStmt() ) {
            this.sb.append("var stmt" + this.stmtsNumber + " = ");
            increaseStmt();
            end = ";";
        }
        exprStmt.getExpr().visit(this);
        this.sb.append(end);

        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.isTernary() ) {
            this.sb.append("createTernaryExpr(");
            ifStmt.getExpr().get(0).visit(this);
            this.sb.append(", ");
            ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
            this.sb.append(", ");
            ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
            this.sb.append(")");
        } else {
            String end = createClosingBracket();
            this.sb.append("createIfStmt([");
            appendIfStmtConditions(ifStmt);
            this.sb.append("], [");
            appendThenStmts(ifStmt);
            this.sb.append("]");
            appendElseStmt(ifStmt);
            this.sb.append(end);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        String end = createClosingBracket();
        appendRepeatStmtCondition(repeatStmt);
        addInStmt();
        appendRepeatStmtStatements(repeatStmt);
        this.sb.append("]");
        this.sb.append(end);
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
            this.sb.append(symbol);
        }
        removeLastComma();
        removeInStmt();
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        String end = createClosingBracket();
        this.sb.append("createDriveAction(");
        driveAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        this.sb.append(", CONST." + driveDirection);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        String end = createClosingBracket();
        this.sb.append("createCurveAction(");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(curveAction.getDirection() == DriveDirection.FOREWARD);
        }
        this.sb.append(", CONST." + driveDirection);
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    private void appendDuration(MotorDuration<Void> duration) {
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
    }

    private DriveDirection getDriveDirection(boolean isReverse) {
        if ( isReverse ) {
            return DriveDirection.BACKWARD;
        }
        return DriveDirection.FOREWARD;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnAction(");
        turnAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        ITurnDirection turnDirection = turnAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }
        this.sb.append(", CONST." + turnDirection);
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    private TurnDirection getTurnDirection(boolean isReverse) {
        if ( isReverse ) {
            return TurnDirection.RIGHT;
        }
        return TurnDirection.LEFT;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnLight(CONST." + lightAction.getColor() + ", CONST." + lightAction.getBlinkMode());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        String end = createClosingBracket();
        this.sb.append("createLightSensorAction(CONST.COLOR_ENUM." + lightSensorAction.getLight() + ", CONST." + lightSensorAction.getState());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String end = createClosingBracket();
        this.sb.append("createStatusLight(CONST." + lightStatusAction.getStatus());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        this.sb.append("createGetMotorPower(" + (motorGetPowerAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        boolean isDuration = motorOnAction.getParam().getDuration() != null;
        String end = createClosingBracket();
        this.sb.append("createMotorOnAction(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(", " + (motorOnAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        if ( isDuration ) {
            this.sb.append(", createDuration(CONST.");
            this.sb.append(motorOnAction.getParam().getDuration().getType().toString() + ", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
            this.sb.append(")");
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String end = createClosingBracket();
        this.sb.append("createSetMotorPowerAction(" + (motorSetPowerAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ", ");
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String end = createClosingBracket();
        this.sb.append("createStopMotorAction(");
        this.sb.append((motorStopAction.getPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        String end = createClosingBracket();
        this.sb.append("createClearDisplayAction(");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            String end = createClosingBracket();
            this.sb.append("createSetVolumeAction(CONST." + volumeAction.getMode() + ", ");
            volumeAction.getVolume().visit(this);
            this.sb.append(end);
        } else {
            this.sb.append("createGetVolume()");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        String end = createClosingBracket();
        this.sb.append("createPlayFileAction(CONST." + playFileAction.getFileName());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        String end = createClosingBracket();
        this.sb.append("createShowPictureAction('" + showPictureAction.getPicture() + "', ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String end = createClosingBracket();
        this.sb.append("createShowTextAction(");
        showTextAction.getMsg().visit(this);
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        String end = createClosingBracket();
        this.sb.append("createStopDrive(");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        String end = createClosingBracket();
        this.sb.append("createToneAction(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        this.sb.append("createGetSample(CONST.BUTTONS, CONST." + brickSensor.getKey() + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.sb.append("createGetSample(CONST.COLOR, CONST." + colorSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("createGetSample(CONST.LIGHT, CONST." + lightSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderMotor = (encoderSensor.getMotorPort() == ActorPort.B ? MOTOR_RIGHT : MOTOR_LEFT).toString();
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String end = createClosingBracket();
            this.sb.append("createResetEncoderSensor(" + encoderMotor);
            this.sb.append(end);
        } else {
            this.sb.append("createGetSampleEncoderSensor(" + encoderMotor + ", CONST." + encoderSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            String end = createClosingBracket();
            this.sb.append("createResetGyroSensor(");
            this.sb.append(end);
        } else {
            this.sb.append("createGetGyroSensorSample(CONST.GYRO, CONST." + gyroSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("createGetSample(CONST.INFRARED, CONST." + infraredSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("createGetSample(CONST.TIMER, 'timer" + timerSensor.getTimer() + "')");
                break;
            case RESET:
                String end = createClosingBracket();
                this.sb.append("createResetTimer('timer" + timerSensor.getTimer() + "'");
                this.sb.append(end);
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("createGetSample(CONST.TOUCH)");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("createGetSample(CONST.ULTRASONIC, CONST." + ultrasonicSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("createGetSample(CONST.SOUND)");
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
            this.sb.append("createDebugAction(");
            this.sb.append(end);
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
        this.sb.append("createWaitStmt([");
        addInStmt();
        visitStmtList(waitStmt.getStatements());
        removeInStmt();
        this.sb.append("]");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        String end = createClosingBracket();
        this.sb.append("createWaitTimeStmt(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(end);
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
        this.sb.append("createGetSubList({list: ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", where1: CONST.");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(where1);
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", at1: ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", where2: CONST.");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(where2);
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", at2: ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append("})");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        this.sb.append("createListFindItem(CONST." + indexOfFunct.getLocation() + ", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        String methodName = "createListLength(";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "createListIsEmpty(";
        }
        this.sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("createCreateListWith(CONST.ARRAY_" + listCreate.getTypeVar() + ", [");
        listCreate.getValue().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        String end = createClosingBracket();
        this.sb.append("createListsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listSetIndex.getElementOperation());
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listSetIndex.getLocation());
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(end);
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
        this.sb.append(methodName);
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listGetIndex.getElementOperation());
        this.sb.append(", CONST.");
        this.sb.append(listGetIndex.getLocation());
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("createCreateListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("createMathConstrainFunct(");
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
        this.sb.append("createMathPropFunct('" + mathNumPropFunct.getFunctName() + "', ");
        mathNumPropFunct.getParam().get(0).visit(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            this.sb.append(", ");
            mathNumPropFunct.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        this.sb.append("createMathOnList(CONST." + mathOnListFunct.getFunctName() + ", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("createRandDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("createRandInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        this.sb.append("createSingleFunction('" + mathSingleFunct.getFunctName() + "', ");
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("createTextJoin([");
        textJoinFunct.getParam().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.sb.append("var method" + this.methodsNumber + " = createMethodVoid('" + methodVoid.getMethodName() + "', [");
        methodVoid.getParameters().visit(this);
        this.sb.append("], [");
        addInStmt();
        methodVoid.getBody().visit(this);
        this.sb.append("]);\n");
        increaseMethods();
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.sb.append("var method" + this.methodsNumber + " = createMethodReturn('" + methodReturn.getMethodName() + "', [");
        addInStmt();
        methodReturn.getBody().visit(this);
        this.sb.append("], ");
        methodReturn.getReturnValue().visit(this);
        this.sb.append(");\n");
        increaseMethods();
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("createIfReturn(");
        methodIfReturn.getCondition().visit(this);
        this.sb.append(", ");
        methodIfReturn.getReturnValue().visit(this);
        this.sb.append(")");
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
        this.sb.append(name + methodCall.getMethodName() + "', [");
        methodCall.getParameters().visit(this);
        this.sb.append("], [");
        methodCall.getParametersValues().visit(this);
        this.sb.append("]" + end);
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
        this.stmtsNumber++;
    }

    private void increaseMethods() {
        this.methodsNumber++;
    }

    /**
     * @return the inStmt
     */
    private boolean isInStmt() {
        if ( this.inStmt.size() == 0 ) {
            return false;
        }
        return this.inStmt.get(this.inStmt.size() - 1);
    }

    /**
     * @param inStmt the inStmt to set
     */
    private void addInStmt() {
        this.inStmt.add(true);
    }

    private void removeInStmt() {
        if ( this.inStmt.size() != 0 ) {
            this.inStmt.remove(this.inStmt.size() - 1);
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
                this.sb.append(", ");
            }
        }
    }

    private void appendElseStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            this.sb.append(", [");
            addInStmt();
            ifStmt.getElseList().visit(this);
            this.sb.append("]");
        }
    }

    private void appendThenStmts(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getThenList().size(); i++ ) {
            addInStmt();
            this.sb.append("[");
            ifStmt.getThenList().get(i).visit(this);
            boolean isLastStmt = i < ifStmt.getThenList().size() - 1;
            this.sb.append("]");
            if ( isLastStmt ) {
                this.sb.append(", ");
            }
        }
    }

    private void appendRepeatStmtStatements(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            if ( repeatStmt.getList().get().size() != 0 ) {
                this.sb.append("[");
                repeatStmt.getList().visit(this);
                this.sb.append("]");
            }
        } else {
            repeatStmt.getList().visit(this);
        }
    }

    private void appendRepeatStmtCondition(RepeatStmt<Void> repeatStmt) {
        switch ( repeatStmt.getMode() ) {
            case WAIT:
                this.sb.append("createIfStmt([");
                repeatStmt.getExpr().visit(this);
                this.sb.append("], [");
                break;
            case TIMES:
                this.sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                ((NumConst<Void>) ((ExprList<Void>) repeatStmt.getExpr()).get().get(2)).visit(this);
                this.sb.append(", [");
                break;
            case FOREVER:
            case WHILE:
            case UNTIL:
                this.sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                repeatStmt.getExpr().visit(this);
                this.sb.append(", [");
                break;
            case FOR:
                this.sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", [");
                repeatStmt.getExpr().visit(this);
                this.sb.append("], [");
                break;
            case FOR_EACH:
                this.sb.append("createRepeatStmt(CONST." + repeatStmt.getMode() + ", ");
                repeatStmt.getExpr().visit(this);
                this.sb.append(", [");
                break;

            default:
                throw new DbcException("Invalid repeat mode");

        }
    }

    private String createClosingBracket() {
        String end = ")";
        if ( !isInStmt() ) {
            this.sb.append("var stmt" + this.stmtsNumber + " = ");
            increaseStmt();
            end = ");\n";
        }
        return end;
    }

    private void removeLastComma() {
        if ( isInStmt() ) {
            this.sb.setLength(this.sb.length() - 2);
        }
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}

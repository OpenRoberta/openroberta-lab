package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.ArrayList;

import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.ev3.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.ev3.DriveAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ToneAction;
import de.fhg.iais.roberta.syntax.action.ev3.TurnAction;
import de.fhg.iais.roberta.syntax.action.ev3.VolumeAction;
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
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LenghtOfIsEmptyFunct;
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
import de.fhg.iais.roberta.syntax.sensor.ev3.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.UltrasonicSensor;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

public class Ast2Ev3JavaScriptVisitor implements AstVisitor<Void> {
    private static final String MOTOR_LEFT = "MOTOR_LEFT";
    private static final String MOTOR_RIGHT = "MOTOR_RIGHT";
    private final StringBuilder sb = new StringBuilder();
    private int stmtCount = 0;
    private ArrayList<Boolean> inStmt = new ArrayList<Boolean>();

    private Ast2Ev3JavaScriptVisitor() {

    }

    public static String generate(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(phrasesSet.size() >= 1);
        Ast2Ev3JavaScriptVisitor astVisitor = new Ast2Ev3JavaScriptVisitor();
        generateCodeFromPhrases(phrasesSet, astVisitor);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        this.sb.append("createConstant(" + numConst.getKind() + ", " + numConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append("createConstant(" + boolConst.getKind() + ", " + boolConst.isValue() + ")");
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("createConstant(" + stringConst.getKind() + ", " + stringConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("createConstant(" + nullConst.getKind() + ", undefined)");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append("createConstant(" + colorConst.getKind() + ", COLOR_ENUM." + colorConst.getValue() + ")");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        this.sb.append("createVarReference(" + var.getTypeVar() + ", \"" + var.getValue() + "\")");
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append("createVarDeclaration(" + var.getTypeVar() + ", \"" + var.getName() + "\", ");
        var.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        this.sb.append("createBinaryExpr(" + binary.getOp() + ", ");
        binary.getLeft().visit(this);
        this.sb.append(", ");
        binary.getRight().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFunc(MathPowerFunct<Void> func) {
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
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
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
        this.sb.append("var stmt" + this.stmtCount + " = ");
        increaseStmt();
        exprStmt.getExpr().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        String end = createClosingBracket();
        this.sb.append("createIfStmt([");
        appendIfStmtConditions(ifStmt);
        this.sb.append("], [");
        appendThenStmts(ifStmt);
        this.sb.append("]");
        appendElseStmt(ifStmt);
        this.sb.append(end);
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
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String end = createClosingBracket();
        this.sb.append("createDriveAction(");
        driveAction.getParam().getSpeed().visit(this);
        this.sb.append(", " + driveAction.getDirection());
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        String end = createClosingBracket();
        this.sb.append("createTurnAction(");
        turnAction.getParam().getSpeed().visit(this);
        this.sb.append(", " + turnAction.getDirection());
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnLight(" + lightAction.getColor() + ", " + lightAction.getBlinkMode());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String end = createClosingBracket();
        this.sb.append("createResetLight(");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
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
            this.sb.append(", createDuration(");
            this.sb.append(motorOnAction.getParam().getDuration().getType().toString() + ", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
            this.sb.append(")");
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
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
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
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
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.sb.append("createGetSample(" + colorSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("createGetSample(TOUCH)");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("createGetSample(ULTRASONIC)");
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        sensorGetSample.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
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
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        return null;
    }

    @Override
    public Void visitLenghtOfIsEmptyFunct(LenghtOfIsEmptyFunct<Void> lenghtOfIsEmptyFunct) {
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
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
        this.stmtCount++;
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

    private static void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Ast2Ev3JavaScriptVisitor astVisitor) {
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(astVisitor);
            }
        }
        appendInitStmt(astVisitor);

    }

    private static void appendInitStmt(Ast2Ev3JavaScriptVisitor astVisitor) {
        astVisitor.sb.append("initProgram([");
        for ( int i = 0; i < astVisitor.stmtCount; i++ ) {
            astVisitor.sb.append("stmt" + i);
            if ( i != astVisitor.stmtCount - 1 ) {
                astVisitor.sb.append(",");
            } else {
                astVisitor.sb.append("]);");
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
                this.sb.append("createRepeatStmt(" + repeatStmt.getMode() + ", ");
                ((NumConst<Void>) ((ExprList<Void>) repeatStmt.getExpr()).get().get(2)).visit(this);
                this.sb.append(", [");
                break;
            case FOREVER:
            case WHILE:
                this.sb.append("createRepeatStmt(" + repeatStmt.getMode() + ", ");
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
            this.sb.append("var stmt" + this.stmtCount + " = ");
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

}

package de.fhg.iais.roberta.ast.typecheck;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.functions.Func;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.Location;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class InfoCollector<T> implements AstVisitor<T> {

    private final List<NepoInfo> infos = new ArrayList<>();

    /**
     * initialize the info collector visitor.
     */
    private InfoCollector() {
    }

    /**
     * collects the infos generated during typechecking for an AST. This is done by a visitor, which is an instance of this class<br>
     * 
     * @param phrase whose infos should be collected
     * @return list of collected infos
     */
    public static <T> List<NepoInfo> collectInfos(Phrase<T> phrase) //
    {
        InfoCollector<T> astVisitor = new InfoCollector<T>();
        phrase.visit(astVisitor);
        return astVisitor.infos;
    }

    @Override
    public T visitNumConst(NumConst<T> numConst) {
        extractInfos(numConst);
        return null;
    }

    @Override
    public T visitMathConst(MathConst<T> mathConst) {
        extractInfos(mathConst);
        return null;
    }

    @Override
    public T visitBoolConst(BoolConst<T> boolConst) {
        extractInfos(boolConst);
        return null;
    }

    @Override
    public T visitStringConst(StringConst<T> stringConst) {
        extractInfos(stringConst);
        return null;
    }

    @Override
    public T visitNullConst(NullConst<T> nullConst) {
        extractInfos(nullConst);
        return null;
    }

    @Override
    public T visitColorConst(ColorConst<T> colorConst) {
        extractInfos(colorConst);
        return null;
    }

    @Override
    public T visitVar(Var<T> var) {
        extractInfos(var);
        return null;
    }

    @Override
    public T visitUnary(Unary<T> unary) {
        extractInfos(unary);
        return null;
    }

    @Override
    public T visitBinary(Binary<T> binary) {
        extractInfos(binary);
        return null;
    }

    @Override
    public T visitFunc(Func<T> func) {
        extractInfos(func);
        return null;
    }

    @Override
    public T visitActionExpr(ActionExpr<T> actionExpr) {
        extractInfos(actionExpr);
        return null;
    }

    @Override
    public T visitSensorExpr(SensorExpr<T> sensorExpr) {
        extractInfos(sensorExpr);
        return null;
    }

    @Override
    public T visitEmptyExpr(EmptyExpr<T> emptyExpr) {
        extractInfos(emptyExpr);
        return null;
    }

    @Override
    public T visitExprList(ExprList<T> exprList) {
        extractInfos(exprList);
        return null;
    }

    @Override
    public T visitActionStmt(ActionStmt<T> actionStmt) {
        extractInfos(actionStmt);
        return null;
    }

    @Override
    public T visitAssignStmt(AssignStmt<T> assignStmt) {
        extractInfos(assignStmt);
        return null;
    }

    @Override
    public T visitExprStmt(ExprStmt<T> exprStmt) {
        extractInfos(exprStmt);
        return null;
    }

    @Override
    public T visitIfStmt(IfStmt<T> ifStmt) {
        extractInfos(ifStmt);
        return null;
    }

    @Override
    public T visitRepeatStmt(RepeatStmt<T> repeatStmt) {
        extractInfos(repeatStmt);
        return null;
    }

    @Override
    public T visitSensorStmt(SensorStmt<T> sensorStmt) {
        extractInfos(sensorStmt);
        return null;
    }

    @Override
    public T visitStmtFlowCon(StmtFlowCon<T> stmtFlowCon) {
        extractInfos(stmtFlowCon);
        return null;
    }

    @Override
    public T visitStmtList(StmtList<T> stmtList) {
        extractInfos(stmtList);
        return null;
    }

    @Override
    public T visitDriveAction(DriveAction<T> driveAction) {
        extractInfos(driveAction);
        return null;
    }

    @Override
    public T visitTurnAction(TurnAction<T> turnAction) {
        extractInfos(turnAction);
        return null;
    }

    @Override
    public T visitLightAction(LightAction<T> lightAction) {
        extractInfos(lightAction);
        return null;
    }

    @Override
    public T visitLightStatusAction(LightStatusAction<T> lightStatusAction) {
        extractInfos(lightStatusAction);
        return null;
    }

    @Override
    public T visitMotorGetPowerAction(MotorGetPowerAction<T> motorGetPowerAction) {
        extractInfos(motorGetPowerAction);
        return null;
    }

    @Override
    public T visitMotorOnAction(MotorOnAction<T> motorOnAction) {
        extractInfos(motorOnAction);
        return null;
    }

    @Override
    public T visitMotorSetPowerAction(MotorSetPowerAction<T> motorSetPowerAction) {
        extractInfos(motorSetPowerAction);
        return null;
    }

    @Override
    public T visitMotorStopAction(MotorStopAction<T> motorStopAction) {
        extractInfos(motorStopAction);
        return null;
    }

    @Override
    public T visitClearDisplayAction(ClearDisplayAction<T> clearDisplayAction) {
        extractInfos(clearDisplayAction);
        return null;
    }

    @Override
    public T visitVolumeAction(VolumeAction<T> volumeAction) {
        extractInfos(volumeAction);
        return null;
    }

    @Override
    public T visitPlayFileAction(PlayFileAction<T> playFileAction) {
        extractInfos(playFileAction);
        return null;
    }

    @Override
    public T visitShowPictureAction(ShowPictureAction<T> showPictureAction) {
        extractInfos(showPictureAction);
        return null;
    }

    @Override
    public T visitShowTextAction(ShowTextAction<T> showTextAction) {
        extractInfos(showTextAction);
        return null;
    }

    @Override
    public T visitMotorDriveStopAction(MotorDriveStopAction<T> stopAction) {
        extractInfos(stopAction);
        return null;
    }

    @Override
    public T visitToneAction(ToneAction<T> toneAction) {
        extractInfos(toneAction);
        return null;
    }

    @Override
    public T visitBrickSensor(BrickSensor<T> brickSensor) {
        extractInfos(brickSensor);
        return null;
    }

    @Override
    public T visitColorSensor(ColorSensor<T> colorSensor) {
        extractInfos(colorSensor);
        return null;
    }

    @Override
    public T visitEncoderSensor(EncoderSensor<T> encoderSensor) {
        extractInfos(encoderSensor);
        return null;
    }

    @Override
    public T visitGyroSensor(GyroSensor<T> gyroSensor) {
        extractInfos(gyroSensor);
        return null;
    }

    @Override
    public T visitInfraredSensor(InfraredSensor<T> infraredSensor) {
        extractInfos(infraredSensor);
        return null;
    }

    @Override
    public T visitTimerSensor(TimerSensor<T> timerSensor) {
        extractInfos(timerSensor);
        return null;
    }

    @Override
    public T visitTouchSensor(TouchSensor<T> touchSensor) {
        extractInfos(touchSensor);
        return null;
    }

    @Override
    public T visitUltrasonicSensor(UltrasonicSensor<T> ultrasonicSensor) {
        extractInfos(ultrasonicSensor);
        return null;
    }

    @Override
    public T visitMainTask(MainTask<T> mainTask) {
        extractInfos(mainTask);
        return null;
    }

    @Override
    public T visitActivityTask(ActivityTask<T> activityTask) {
        extractInfos(activityTask);
        return null;
    }

    @Override
    public T visitStartActivityTask(StartActivityTask<T> startActivityTask) {
        extractInfos(startActivityTask);
        return null;
    }

    @Override
    public T visitWaitStmt(WaitStmt<T> waitStmt) {
        extractInfos(waitStmt);
        return null;
    }

    private void extractInfos(Phrase<T> numConst) {
        this.infos.addAll(numConst.getInfos().getInfos());
    }

    @Override
    public T visitLocation(Location<T> location) {
        // TODO Auto-generated method stub
        return null;
    }

}

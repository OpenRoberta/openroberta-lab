package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.NxtConfiguration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.ShowPicture;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
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
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
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
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class UsedTimerVisitor implements AstVisitor<Void> {
	
	 private Set<FunctionNames> functionWasMet = new HashSet<>();

	    public static Set<FunctionNames> check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
	        Assert.isTrue(phrasesSet.size() >= 1);
	        UsedTimerVisitor checkVisitor = new UsedTimerVisitor();
	        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
	            for ( Phrase<Void> phrase : phrases ) {
	                phrase.visit(checkVisitor);
	            }
	        }
	        return checkVisitor.getUsedFuctions();
	    }

	    public Set<FunctionNames> getUsedFuctions() {
	        return this.functionWasMet;
	    }

	@Override
	public Void visitNumConst(NumConst<Void> numConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathConst(MathConst<Void> mathConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBoolConst(BoolConst<Void> boolConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStringConst(StringConst<Void> stringConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitNullConst(NullConst<Void> nullConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitColorConst(ColorConst<Void> colorConst) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVar(Var<Void> var) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVarDeclaration(VarDeclaration<Void> var) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitUnary(Unary<Void> unary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBinary(Binary<Void> binary) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitActionExpr(ActionExpr<Void> actionExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitEmptyList(EmptyList<Void> emptyList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitExprList(ExprList<Void> exprList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitActionStmt(ActionStmt<Void> actionStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitExprStmt(ExprStmt<Void> exprStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitIfStmt(IfStmt<Void> ifStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStmtList(StmtList<Void> stmtList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitDriveAction(DriveAction<Void> driveAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTurnAction(TurnAction<Void> turnAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightAction(LightAction<Void> lightAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitToneAction(ToneAction<Void> toneAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitColorSensor(ColorSensor<Void> colorSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLightSensor(LightSensor<Void> lightSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
		  this.functionWasMet.add(FunctionNames.TIME);
		  return null;
	}

	@Override
	public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMainTask(MainTask<Void> mainTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitActivityTask(ActivityTask<Void> activityTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLocation(Location<Void> location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitListCreate(ListCreate<Void> listCreate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitListRepeat(ListRepeat<Void> listRepeat) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitMethodCall(MethodCall<Void> methodCall) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitBluetoothWaitForConnectionAction(
			BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
		// TODO Auto-generated method stub
		return null;
	}
    

}
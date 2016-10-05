package de.fhg.iais.roberta.typecheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
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
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.NepoInfo.Severity;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class TypecheckVisitor implements AstVisitor<BlocklyType> {
    private final int ERROR_LIMIT_FOR_TYPECHECK = 10;

    private final Configuration brickConfiguration;
    private final String programName;
    private final Phrase<BlocklyType> phrase;

    private List<NepoInfo> infos = null;
    private int errorCount = 0;
    private BlocklyType resultType;

    /**
     * initialize the typecheck visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrase
     */
    TypecheckVisitor(String programName, Configuration brickConfiguration, Phrase<BlocklyType> phrase) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.phrase = phrase;
    }

    /**
     * typecheck an AST. This is done ba an visitor, which is an instance of this class<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrase to typecheck
     * @return the typecheck visitor (to get information about errors and the derived type)
     */
    public static TypecheckVisitor makeVisitorAndTypecheck(String programName, Configuration brickConfiguration, Phrase<BlocklyType> phrase) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.notNull(phrase);

        TypecheckVisitor astVisitor = new TypecheckVisitor(programName, brickConfiguration, phrase);
        astVisitor.resultType = phrase.visit(astVisitor);
        return astVisitor;
    }

    /**
     * get the number of <b>errors</b>
     *
     * @return the number of <b>errors</b> detected during this type check visit
     */
    public int getErrorCount() {
        if ( this.infos == null ) {
            this.infos = InfoCollector.collectInfos(this.phrase);
            for ( NepoInfo info : this.infos ) {
                if ( info.getSeverity() == Severity.ERROR ) {
                    this.errorCount++;
                }
            }
        }
        return this.errorCount;
    }

    /**
     * get the list of all infos (errors, warnings) generated during this typecheck visit
     *
     * @return the list of all infos
     */
    public List<NepoInfo> getInfos() {
        getErrorCount(); // for the side effect
        return this.infos;
    }

    /**
     * return the type that was inferenced by the typechecker for the given phrase
     *
     * @return the resulting type.May be <code>null</code> if type errors occurred
     */
    public BlocklyType getResultType() {
        return this.resultType;
    }

    @Override
    public BlocklyType visitNumConst(NumConst<BlocklyType> numConst) {
        Assert.isTrue(numConst.getKind().hasName("NUM_CONST"));
        return BlocklyType.NUMBER;
    }

    @Override
    public BlocklyType visitMathConst(MathConst<BlocklyType> mathConst) {
        Assert.isTrue(mathConst.getKind().hasName("MATH_CONST"));
        String name = mathConst.getMathConst().name();
        BlocklyType type = TypeTransformations.getConstantSignature(name);
        checkLookupNotNull(mathConst, name, type, "invalid mathematical constant");
        return type;
    }

    @Override
    public BlocklyType visitBoolConst(BoolConst<BlocklyType> boolConst) {
        Assert.isTrue(boolConst.getKind().hasName("BOOL_CONST"));
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitStringConst(StringConst<BlocklyType> stringConst) {
        Assert.isTrue(stringConst.getKind().hasName("STRING_CONST"));
        return BlocklyType.STRING;
    }

    @Override
    public BlocklyType visitNullConst(NullConst<BlocklyType> nullConst) {
        Assert.isTrue(nullConst.getKind().hasName("NULL_CONST"));
        return BlocklyType.NULL;
    }

    @Override
    public BlocklyType visitColorConst(ColorConst<BlocklyType> colorConst) {
        Assert.isTrue(colorConst.getKind().hasName("COLOR_CONST"));
        return BlocklyType.COLOR;
    }

    @Override
    public BlocklyType visitVar(Var<BlocklyType> var) {
        return null;
    }

    @Override
    public BlocklyType visitUnary(Unary<BlocklyType> unary) {
        return null;
    }

    @Override
    public BlocklyType visitBinary(Binary<BlocklyType> binary) {
        BlocklyType left = binary.getLeft().visit(this);
        BlocklyType right = binary.getRight().visit(this);
        //        Sig signature = TypeTransformations.getBinarySignature(binary.getOp().getOpSymbol());
        Sig signature = binary.getOp().getSignature();
        return signature.typeCheck(binary, Arrays.asList(left, right));
    }

    @Override
    public BlocklyType visitMathPowerFunct(MathPowerFunct<BlocklyType> func) {
        List<BlocklyType> paramTypes = typecheckList(func.getParam());
        Sig signature = TypeTransformations.getFunctionSignature(func.getFunctName().name());
        BlocklyType resultType = signature.typeCheck(func, paramTypes);
        return resultType;
    }

    @Override
    public BlocklyType visitActionExpr(ActionExpr<BlocklyType> actionExpr) {
        return null;
    }

    @Override
    public BlocklyType visitSensorExpr(SensorExpr<BlocklyType> sensorExpr) {
        return null;
    }

    @Override
    public BlocklyType visitEmptyExpr(EmptyExpr<BlocklyType> emptyExpr) {
        return null;
    }

    @Override
    public BlocklyType visitExprList(ExprList<BlocklyType> exprList) {
        return null;
    }

    @Override
    public BlocklyType visitActionStmt(ActionStmt<BlocklyType> actionStmt) {
        return null;
    }

    @Override
    public BlocklyType visitAssignStmt(AssignStmt<BlocklyType> assignStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitExprStmt(ExprStmt<BlocklyType> exprStmt) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitIfStmt(IfStmt<BlocklyType> ifStmt) {
        return null;
    }

    @Override
    public BlocklyType visitRepeatStmt(RepeatStmt<BlocklyType> repeatStmt) {
        return null;
    }

    @Override
    public BlocklyType visitSensorStmt(SensorStmt<BlocklyType> sensorStmt) {
        return null;
    }

    @Override
    public BlocklyType visitStmtFlowCon(StmtFlowCon<BlocklyType> stmtFlowCon) {
        return null;
    }

    @Override
    public BlocklyType visitStmtList(StmtList<BlocklyType> stmtList) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitDriveAction(DriveAction<BlocklyType> driveAction) {
        return null;
    }

    @Override
    public BlocklyType visitTurnAction(TurnAction<BlocklyType> turnAction) {
        return null;
    }

    @Override
    public BlocklyType visitLightAction(LightAction<BlocklyType> lightAction) {
        return null;
    }

    @Override
    public BlocklyType visitLightStatusAction(LightStatusAction<BlocklyType> lightStatusAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorGetPowerAction(MotorGetPowerAction<BlocklyType> motorGetPowerAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorOnAction(MotorOnAction<BlocklyType> motorOnAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorSetPowerAction(MotorSetPowerAction<BlocklyType> motorSetPowerAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorStopAction(MotorStopAction<BlocklyType> motorStopAction) {
        return null;
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction<BlocklyType> clearDisplayAction) {
        return null;
    }

    @Override
    public BlocklyType visitVolumeAction(VolumeAction<BlocklyType> volumeAction) {
        return null;
    }

    @Override
    public BlocklyType visitPlayFileAction(PlayFileAction<BlocklyType> playFileAction) {
        return null;
    }

    @Override
    public BlocklyType visitShowPictureAction(ShowPictureAction<BlocklyType> showPictureAction) {
        return null;
    }

    @Override
    public BlocklyType visitShowTextAction(ShowTextAction<BlocklyType> showTextAction) {
        return null;
    }

    @Override
    public BlocklyType visitMotorDriveStopAction(MotorDriveStopAction<BlocklyType> stopAction) {
        return null;
    }

    @Override
    public BlocklyType visitToneAction(ToneAction<BlocklyType> toneAction) {
        return null;
    }

    @Override
    public BlocklyType visitBrickSensor(BrickSensor<BlocklyType> brickSensor) {
        return null;
    }

    @Override
    public BlocklyType visitColorSensor(ColorSensor<BlocklyType> colorSensor) {
        return null;
    }

    @Override
    public BlocklyType visitEncoderSensor(EncoderSensor<BlocklyType> encoderSensor) {
        return null;
    }

    @Override
    public BlocklyType visitGyroSensor(GyroSensor<BlocklyType> gyroSensor) {
        return null;
    }

    @Override
    public BlocklyType visitInfraredSensor(InfraredSensor<BlocklyType> infraredSensor) {
        return null;
    }

    @Override
    public BlocklyType visitTimerSensor(TimerSensor<BlocklyType> timerSensor) {
        return null;
    }

    @Override
    public BlocklyType visitTouchSensor(TouchSensor<BlocklyType> touchSensor) {
        return null;
    }

    @Override
    public BlocklyType visitUltrasonicSensor(UltrasonicSensor<BlocklyType> ultrasonicSensor) {
        return null;
    }

    @Override
    public BlocklyType visitMainTask(MainTask<BlocklyType> mainTask) {
        return null;
    }

    @Override
    public BlocklyType visitActivityTask(ActivityTask<BlocklyType> activityTask) {
        return null;
    }

    @Override
    public BlocklyType visitStartActivityTask(StartActivityTask<BlocklyType> startActivityTask) {
        return null;
    }

    @Override
    public BlocklyType visitWaitStmt(WaitStmt<BlocklyType> waitStmt) {
        return null;
    }

    private List<BlocklyType> typecheckList(List<Expr<BlocklyType>> params) {
        List<BlocklyType> paramTypes = new ArrayList<>(params.size());
        for ( Expr<BlocklyType> param : params ) {
            paramTypes.add(param.visit(this));
        }
        return paramTypes;
    }

    private void checkFor(Phrase<BlocklyType> phrase, boolean condition, String message) {
        if ( !condition ) {
            if ( this.errorCount >= this.ERROR_LIMIT_FOR_TYPECHECK ) {
                throw new RuntimeException("aborting typecheck. More than " + this.ERROR_LIMIT_FOR_TYPECHECK + " found.");
            } else {
                this.errorCount++;
                NepoInfo error = NepoInfo.error(message);
                phrase.addInfo(error);
            }
        }
    }

    private void checkLookupNotNull(Phrase<BlocklyType> phrase, String name, Object supposedToBeNotNull, String message) {
        if ( supposedToBeNotNull == null ) {
            checkFor(phrase, false, message + ": " + name);
        }
    }

    @Override
    public BlocklyType visitLocation(Location<BlocklyType> location) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitEmptyList(EmptyList<BlocklyType> emptyList) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitGetSampleSensor(GetSampleSensor<BlocklyType> sensorGetSample) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextPrintFunct(TextPrintFunct<BlocklyType> textPrintFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitFunctionStmt(FunctionStmt<BlocklyType> functionStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitFunctionExpr(FunctionExpr<BlocklyType> functionExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitGetSubFunct(GetSubFunct<BlocklyType> getSubFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitIndexOfFunct(IndexOfFunct<BlocklyType> indexOfFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<BlocklyType> lengthOfIsEmptyFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListCreate(ListCreate<BlocklyType> listCreate) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListGetIndex(ListGetIndex<BlocklyType> listGetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListRepeat(ListRepeat<BlocklyType> listRepeat) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitListSetIndex(ListSetIndex<BlocklyType> listSetIndex) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathConstrainFunct(MathConstrainFunct<BlocklyType> mathConstrainFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathNumPropFunct(MathNumPropFunct<BlocklyType> mathNumPropFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathOnListFunct(MathOnListFunct<BlocklyType> mathOnListFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathRandomFloatFunct(MathRandomFloatFunct<BlocklyType> mathRandomFloatFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathRandomIntFunct(MathRandomIntFunct<BlocklyType> mathRandomIntFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMathSingleFunct(MathSingleFunct<BlocklyType> mathSingleFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitTextJoinFunct(TextJoinFunct<BlocklyType> textJoinFunct) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitWaitTimeStmt(WaitTimeStmt<BlocklyType> waitTimeStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitVarDeclaration(VarDeclaration<BlocklyType> var) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodVoid(MethodVoid<BlocklyType> methodVoid) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodReturn(MethodReturn<BlocklyType> methodReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodIfReturn(MethodIfReturn<BlocklyType> methodIfReturn) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodStmt(MethodStmt<BlocklyType> methodStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodCall(MethodCall<BlocklyType> methodCall) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitMethodExpr(MethodExpr<BlocklyType> methodExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitBluetoothReceiveAction(BluetoothReceiveAction<BlocklyType> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitBluetoothConnectAction(BluetoothConnectAction<BlocklyType> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitBluetoothSendAction(BluetoothSendAction<BlocklyType> clearDisplayAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<BlocklyType> bluetoothWaitForConnection) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitStmtExpr(StmtExpr<BlocklyType> stmtExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitShadowExpr(ShadowExpr<BlocklyType> shadowExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLightSensor(LightSensor<BlocklyType> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitSoundSensor(SoundSensor<BlocklyType> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitLightSensorAction(LightSensorAction<BlocklyType> lightSensorAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitCurveAction(CurveAction<BlocklyType> driveAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitCompassSensor(CompassSensor<BlocklyType> compassSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitConnectConst(ConnectConst<BlocklyType> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BlocklyType visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<BlocklyType> bluetoothCheckConnectAction) {
        // TODO Auto-generated method stub
        return null;
    }
}

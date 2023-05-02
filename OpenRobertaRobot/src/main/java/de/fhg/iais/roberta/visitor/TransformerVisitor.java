package de.fhg.iais.roberta.visitor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.EvalExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.TernaryExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassCalibrate;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.util.syntax.MotorDuration;

/**
 * A visitor to be used to potentially modify all phrases in the AST. Every visit implementation should return a new phrase using the static make method of the
 * individual phrase, or even other phrases. All subphrases should be called with {@link Phrase#modify(TransformerVisitor)} )} to ensure all leaves are
 * visited.
 */
@SuppressWarnings("unchecked")
public abstract class TransformerVisitor implements IVisitor<Phrase> {

    /**
     * Delegates visitable to matching visitT(T t) method from substructure
     *
     * @param visitable meant to be visited
     * @return output of delegated visit-method
     */
    public Phrase visit(Phrase visitable) {
        try {
            java.lang.reflect.Method m = getVisitMethodFor(visitable.getClass());
            @SuppressWarnings("unchecked")
            Phrase result = (Phrase) m.invoke(this, new Object[] {visitable});
            return result;
        } catch ( NoSuchMethodException e ) {
            throw new DbcException(String.format("visit Method not found for phrase \"%s\"", visitable.getClass()), e);
        } catch ( IllegalAccessException e ) {
            throw new DbcException(e);
        } catch ( InvocationTargetException e ) {
            // rethrow cause
            Throwable cause = e.getCause();
            if ( cause instanceof RuntimeException ) {
                throw (RuntimeException) cause;
            }
            throw new DbcException(String.format("visit method for phrase \"%s\" threw exception", visitable.getClass()), e);
        }
    }

    private java.lang.reflect.Method getVisitMethodFor(Class<?> clazz) throws NoSuchMethodException {
        java.lang.reflect.Method m = null;
        try {
            return getClass().getMethod("visit", clazz);
        } catch ( NoSuchMethodException e ) {
            String methodName = "visit" + clazz.getSimpleName();
            return getClass().getMethod("visit" + clazz.getSimpleName(), clazz);
        }
    }

    /**
     * Returns the dropdown factory. Necessary for the {@link GetSampleSensor} visit. {@link BlocklyDropdownFactory} is required by
     * {@link GetSampleSensor#make(String, String, String, boolean, BlocklyProperties, BlocklyComment, BlocklyDropdownFactory)} in order to recreate
     * itself.
     *
     * @return the dropdown factory
     */
    abstract BlocklyDropdownFactory getBlocklyDropdownFactory();

    public Phrase visitDriveAction(DriveAction driveAction) {
        return new DriveAction(driveAction.direction, modifyMotionParam(driveAction.param), BlocklyConstants.EMPTY_PORT, null, driveAction.getProperty());
    }

    public Phrase visitCurveAction(CurveAction curveAction) {
        return new CurveAction(curveAction.direction, modifyMotionParam(curveAction.paramLeft), modifyMotionParam(curveAction.paramRight), BlocklyConstants.EMPTY_PORT, null, curveAction.getProperty());
    }

    public Phrase visitTurnAction(TurnAction turnAction) {
        return new TurnAction(turnAction.direction, modifyMotionParam(turnAction.param), BlocklyConstants.EMPTY_PORT, null, turnAction.getProperty());
    }

    public Phrase visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        return new MotorDriveStopAction(stopAction.getProperty(), BlocklyConstants.EMPTY_PORT, null);
    }

    public Phrase visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return new MotorGetPowerAction(motorGetPowerAction.getProperty(), motorGetPowerAction.getUserDefinedPort());
    }

    public Phrase visitMotorOnAction(MotorOnAction motorOnAction) {
        return new MotorOnAction(motorOnAction.getUserDefinedPort(), modifyMotionParam(motorOnAction.param), motorOnAction.getProperty());
    }

    public Phrase visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return new MotorSetPowerAction(motorSetPowerAction.getProperty(), (Expr) motorSetPowerAction.power.modify(this), motorSetPowerAction.getUserDefinedPort());
    }

    public Phrase visitMotorStopAction(MotorStopAction motorStopAction) {
        return new MotorStopAction(motorStopAction.getUserDefinedPort(), motorStopAction.mode, motorStopAction.getProperty());
    }

    public Phrase visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return new ClearDisplayAction(clearDisplayAction.getProperty(), clearDisplayAction.port, null);
    }

    public Phrase visitShowTextAction(ShowTextAction showTextAction) {
        return new ShowTextAction(showTextAction.getProperty(), (Expr) showTextAction.msg.modify(this), (Expr) showTextAction.x.modify(this), (Expr) showTextAction.y.modify(this), showTextAction.port, null);
    }

    public Phrase visitLightAction(LightAction lightAction) {
        return new LightAction(lightAction.port, lightAction.color, lightAction.mode, (Expr) lightAction.rgbLedColor.modify(this), lightAction.getProperty());
    }

    public Phrase visitLightStatusAction(LightStatusAction lightStatusAction) {
        return new LightStatusAction(lightStatusAction.getUserDefinedPort(), lightStatusAction.status, lightStatusAction.getProperty());
    }

    public Phrase visitToneAction(ToneAction toneAction) {
        return new ToneAction(toneAction.getProperty(), (Expr) toneAction.frequency.modify(this), (Expr) toneAction.duration.modify(this), toneAction.port, toneAction.hide);
    }

    public Phrase visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return new PlayNoteAction(playNoteAction.getProperty(), playNoteAction.duration, playNoteAction.frequency, playNoteAction.port, playNoteAction.hide);
    }

    public Phrase visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        return new GetVolumeAction(getVolumeAction.getProperty(), BlocklyConstants.EMPTY_PORT, null);
    }

    public Phrase visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return new SetVolumeAction(setVolumeAction.getProperty(), BlocklyConstants.EMPTY_PORT, (Expr) setVolumeAction.volume.modify(this), null);
    }

    public Phrase visitPlayFileAction(PlayFileAction playFileAction) {
        return new PlayFileAction(playFileAction.getProperty(), playFileAction.port, playFileAction.fileName, playFileAction.hide);
    }

    public Phrase visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        return new SetLanguageAction(setLanguageAction.getProperty(), setLanguageAction.language);
    }

    public Phrase visitSayTextAction(SayTextAction sayTextAction) {
        return new SayTextAction(sayTextAction.getProperty(), (Expr) sayTextAction.msg.modify(this));
    }

    public Phrase visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        return new SayTextWithSpeedAndPitchAction(sayTextAction.getProperty(), (Expr) sayTextAction.msg.modify(this), (Expr) sayTextAction.speed.modify(this), (Expr) sayTextAction.pitch.modify(this));
    }

    public Phrase visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        return new SerialWriteAction(serialWriteAction.getProperty(), (Expr) serialWriteAction.value.modify(this));
    }

    public Phrase visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        return new PinWriteValueAction(pinWriteValueAction.getProperty(), pinWriteValueAction.mutation, pinWriteValueAction.pinValue, pinWriteValueAction.port, (Expr) pinWriteValueAction.value.modify(this));
    }

    public Phrase visitNumConst(NumConst numConst) {
        return new NumConst(numConst.getProperty(), numConst.value);
    }

    public Phrase visitMathConst(MathConst mathConst) {
        return new MathConst(mathConst.getProperty(), mathConst.mathConst);
    }

    public Phrase visitBoolConst(BoolConst boolConst) {
        return new BoolConst(boolConst.getProperty(), boolConst.value);
    }

    public Phrase visitStringConst(StringConst stringConst) {
        return new StringConst(stringConst.getProperty(), stringConst.value);
    }

    public Phrase visitNullConst(NullConst nullConst) {
        return new NullConst(nullConst.getProperty());
    }

    public Phrase visitColorConst(ColorConst colorConst) {
        return new ColorConst(colorConst.getProperty(), colorConst.getHexValueAsString());
    }

    public Phrase visitRgbColor(RgbColor rgbColor) {
        return new RgbColor(rgbColor.getProperty(), (Expr) rgbColor.R.modify(this), (Expr) rgbColor.G.modify(this), (Expr) rgbColor.B.modify(this), (Expr) rgbColor.A.modify(this));
    }

    public Phrase visitStmtTextComment(StmtTextComment stmtTextComment) {
        return new StmtTextComment(stmtTextComment.getProperty(), stmtTextComment.textComment);
    }

    public Phrase visitConnectConst(ConnectConst connectConst) {
        return new ConnectConst(connectConst.getProperty(), connectConst.value, connectConst.value);
    }

    public Phrase visitVar(Var var) {
        return new Var(var.getVarType(), var.name, var.getProperty());
    }

    public Phrase visitVarDeclaration(VarDeclaration var) {
        return new VarDeclaration(var.typeVar, var.name, var.value.modify(this), var.next, var.global, var.getProperty());
    }

    public Phrase visitUnary(Unary unary) {
        return new Unary(unary.op, (Expr) unary.expr.modify(this), unary.getProperty());
    }

    public Phrase visitBinary(Binary binary) {
        return new Binary(binary.op, (Expr) binary.left.modify(this), (Expr) binary.getRight().modify(this), binary.operationRange, binary.getProperty());
    }

    public Phrase visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        List<Expr> newParam = new ArrayList();
        mathPowerFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathPowerFunct(mathPowerFunct.getProperty(), mathPowerFunct.functName, newParam);
    }

    public Phrase visitEmptyList(EmptyList emptyList) {
        return new EmptyList(emptyList.getProperty(), emptyList.typeVar);
    }

    public Phrase visitAssignStmt(AssignStmt assignStmt) {
        return new AssignStmt(assignStmt.getProperty(), (Var) assignStmt.name.modify(this), (Expr) assignStmt.expr.modify(this));
    }

    public Phrase visitEmptyExpr(EmptyExpr emptyExpr) {
        return new EmptyExpr(emptyExpr.getDefVal());
    }

    public Phrase visitExprList(ExprList exprList) {
        ExprList newExprList = new ExprList();
        exprList.get().forEach(phraseExpr -> newExprList.addExpr((Expr) phraseExpr.modify(this)));
        newExprList.setReadOnly();
        return newExprList;
    }

    public Phrase visitIfStmt(IfStmt ifStmt) {
        List<Expr> newExpr = new ArrayList();
        ifStmt.expr.forEach(phraseExpr -> newExpr.add((Expr) phraseExpr.modify(this)));
        List<StmtList> newThenList = new ArrayList();
        ifStmt.thenList.forEach(phraseStmtList -> newThenList.add((StmtList) phraseStmtList.modify(this)));
        StmtList newElseList = (StmtList) ifStmt.elseList.modify(this);

        return new IfStmt(ifStmt.getProperty(), newExpr, newThenList, newElseList, ifStmt._else, ifStmt._elseIf);
    }

    public Phrase visitTernaryExpr(TernaryExpr ternaryExpr) {
        Expr condition = (Expr) ternaryExpr.condition.modify(this);
        Expr thenPart = (Expr) ternaryExpr.thenPart.modify(this);
        Expr elsePart = (Expr) ternaryExpr.elsePart.modify(this);

        return new TernaryExpr(ternaryExpr.getProperty(), condition, thenPart, elsePart);
    }

    public Phrase visitNNStepStmt(NNStepStmt nnStepStmt) {
        return new NNStepStmt(nnStepStmt.getProperty());
    }

    public Phrase visitNNSetInputNeuronVal(NNSetInputNeuronVal setVal) {
        return new NNSetBiasStmt(setVal.getProperty(), setVal.name, (Expr) setVal.modify(this));
    }

    public Phrase visitNNGetOutputNeuronVal(NNGetOutputNeuronVal getStmt) {
        return new NNGetOutputNeuronVal(getStmt.getProperty(), getStmt.name);
    }

    public Phrase visitNNSetWeightStmt(NNSetWeightStmt chgStmt) {
        return new NNSetWeightStmt(chgStmt.getProperty(), chgStmt.from, chgStmt.to, (Expr) chgStmt.modify(this));
    }

    public Phrase visitNNSetBiasStmt(NNSetBiasStmt chgStmt) {
        return new NNSetBiasStmt(chgStmt.getProperty(), chgStmt.name, (Expr) chgStmt.modify(this));
    }

    public Phrase visitNNGetWeight(NNGetWeight nnGetWeight) {
        return new NNGetWeight(nnGetWeight.getProperty(), nnGetWeight.from, nnGetWeight.to);
    }

    public Phrase visitNNGetBias(NNGetBias nnGetBias) {
        return new NNGetBias(nnGetBias.getProperty(), nnGetBias.name);
    }

    public Phrase visitRepeatStmt(RepeatStmt repeatStmt) {
        return new RepeatStmt(repeatStmt.mode, (Expr) repeatStmt.expr.modify(this), (StmtList) repeatStmt.list.modify(this), repeatStmt.getProperty());
    }

    public Phrase visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        return new StmtFlowCon(stmtFlowCon.getProperty(), stmtFlowCon.flow);
    }

    public Phrase visitStmtList(StmtList stmtList) {
        StmtList newPhrase = new StmtList();
        stmtList.get().forEach(stmt -> newPhrase.addStmt((Stmt) stmt.modify(this)));
        newPhrase.setReadOnly();
        return newPhrase;
    }

    public Phrase visitMainTask(MainTask mainTask) {
        return new MainTask(mainTask.getProperty(), (StmtList) mainTask.variables.modify(this), mainTask.debug, mainTask.data);
    }

    public Phrase visitWaitStmt(WaitStmt waitStmt) {
        return new WaitStmt(waitStmt.getProperty(), (StmtList) waitStmt.statements.modify(this));
    }

    public Phrase visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return new WaitTimeStmt(waitTimeStmt.getProperty(), (Expr) waitTimeStmt.time.modify(this));
    }

    public Phrase visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        List<Expr> newParam = new ArrayList();
        textPrintFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new TextPrintFunct(newParam, textPrintFunct.getProperty());
    }

    public Phrase visitGetSubFunct(GetSubFunct getSubFunct) {
        List<Expr> newParam = new ArrayList();
        getSubFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new GetSubFunct(getSubFunct.functName, getSubFunct.strParam, newParam, getSubFunct.getProperty());
    }

    public Phrase visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        List<Expr> newParam = new ArrayList();
        indexOfFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new IndexOfFunct(indexOfFunct.location, newParam, indexOfFunct.getProperty());
    }

    public Phrase visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        List<Expr> newParam = new ArrayList();
        lengthOfIsEmptyFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new LengthOfIsEmptyFunct(lengthOfIsEmptyFunct.functName, newParam, lengthOfIsEmptyFunct.getProperty());
    }

    public Phrase visitListCreate(ListCreate listCreate) {
        return new ListCreate(listCreate.typeVar, (ExprList) listCreate.exprList.modify(this), listCreate.getProperty());
    }

    public Phrase visitListGetIndex(ListGetIndex listGetIndex) {
        List<Expr> newParam = new ArrayList();
        listGetIndex.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListGetIndex(listGetIndex.getElementOperation(), listGetIndex.location, newParam, listGetIndex.dataType, listGetIndex.getProperty());
    }

    public Phrase visitListRepeat(ListRepeat listRepeat) {
        List<Expr> newParam = new ArrayList();
        listRepeat.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListRepeat(listRepeat.typeVar, newParam, listRepeat.getProperty());
    }

    public Phrase visitListSetIndex(ListSetIndex listSetIndex) {
        List<Expr> newParam = new ArrayList();
        listSetIndex.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListSetIndex(listSetIndex.mode, listSetIndex.location, newParam, listSetIndex.getProperty());
    }

    public Phrase visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        List<Expr> newParam = new ArrayList();
        mathConstrainFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathConstrainFunct(newParam, mathConstrainFunct.getProperty());
    }

    public Phrase visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        List<Expr> newParam = new ArrayList();
        mathNumPropFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathNumPropFunct(mathNumPropFunct.functName, newParam, mathNumPropFunct.getProperty());
    }

    public Phrase visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        List<Expr> newParam = new ArrayList();
        mathOnListFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathOnListFunct(mathOnListFunct.functName, newParam, mathOnListFunct.getProperty());
    }

    public Phrase visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        return new MathRandomFloatFunct(mathRandomFloatFunct.getProperty());
    }

    public Phrase visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        List<Expr> newParam = new ArrayList();
        mathRandomIntFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathRandomIntFunct(newParam, mathRandomIntFunct.getProperty());
    }

    public Phrase visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        List<Expr> newParam = new ArrayList();
        mathSingleFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathSingleFunct(mathSingleFunct.functName, newParam, mathSingleFunct.getProperty());
    }

    public Phrase visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        List<Expr> newParam = new ArrayList();
        mathCastStringFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, mathCastStringFunct.getProperty());
    }

    public Phrase visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        List<Expr> newParam = new ArrayList();
        mathCastCharFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, mathCastCharFunct.getProperty());
    }

    public Phrase visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        List<Expr> newParam = new ArrayList();
        textStringCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, textStringCastNumberFunct.getProperty());
    }

    public Phrase visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        List<Expr> newParam = new ArrayList();
        textCharCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, textCharCastNumberFunct.getProperty());
    }

    public Phrase visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        return new TextJoinFunct((ExprList) textJoinFunct.param.modify(this), textJoinFunct.getProperty());
    }

    public Phrase visitMethodVoid(MethodVoid methodVoid) {
        return new MethodVoid(methodVoid.getMethodName(), (ExprList) methodVoid.getParameters().modify(this), (StmtList) methodVoid.body.modify(this), methodVoid.getProperty());
    }

    public Phrase visitMethodReturn(MethodReturn methodReturn) {
        return new MethodReturn(methodReturn.getMethodName(), (ExprList) methodReturn.getParameters().modify(this), (StmtList) methodReturn.body.modify(this), methodReturn.getReturnType(), (Expr) methodReturn.returnValue.modify(this), methodReturn.getProperty());
    }

    public Phrase visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        return new MethodIfReturn((Expr) methodIfReturn.oraCondition.modify(this), methodIfReturn.getReturnType(), (Expr) methodIfReturn.oraReturnValue.modify(this), methodIfReturn.getValue(), methodIfReturn.getProperty());
    }

    public Phrase visitMethodStmt(MethodStmt methodStmt) {
        return new MethodStmt((Method) methodStmt.method.modify(this));
    }

    public Phrase visitMethodCall(MethodCall methodCall) {
        return new MethodCall(methodCall.getMethodName(), (ExprList) methodCall.getParameters().modify(this), (ExprList) methodCall.getParametersValues().modify(this), methodCall.getReturnType(), methodCall.getProperty());
    }

    public Phrase visitSensorExpr(SensorExpr sensorExpr) {
        return new SensorExpr((Sensor) sensorExpr.sensor.modify(this));
    }

    public Phrase visitMethodExpr(MethodExpr methodExpr) {
        return new MethodExpr((Method) methodExpr.getMethod().modify(this));
    }

    public Phrase visitActionStmt(ActionStmt actionStmt) {
        return new ActionStmt((Action) actionStmt.action.modify(this));
    }

    public Phrase visitActionExpr(ActionExpr actionExpr) {
        return new ActionExpr((Action) actionExpr.action.modify(this));
    }

    public Phrase visitExprStmt(ExprStmt exprStmt) {
        return new ExprStmt((Expr) exprStmt.expr.modify(this));
    }

    public Phrase visitStmtExpr(StmtExpr stmtExpr) {
        return new StmtExpr((Stmt) stmtExpr.stmt.modify(this));
    }

    public Phrase visitShadowExpr(ShadowExpr shadowExpr) {
        return new ShadowExpr((Expr) shadowExpr.shadow.modify(this), (Expr) shadowExpr.block.modify(this));
    }

    public Phrase visitSensorStmt(SensorStmt sensorStmt) {
        return new SensorStmt((Sensor) sensorStmt.sensor.modify(this));
    }

    public Phrase visitActivityTask(ActivityTask activityTask) {
        return new ActivityTask(activityTask.getProperty(), (Expr) activityTask.activityName.modify(this));
    }

    public Phrase visitStartActivityTask(StartActivityTask startActivityTask) {
        return new StartActivityTask(startActivityTask.getProperty(), (Expr) startActivityTask.activityName.modify(this));
    }

    public Phrase visitLocation(Location location) {
        return new Location(location.x, location.y);
    }

    public Phrase visitFunctionStmt(FunctionStmt functionStmt) {
        return new FunctionStmt((Function) functionStmt.function.modify(this));
    }

    public Phrase visitFunctionExpr(FunctionExpr functionExpr) {
        return new FunctionExpr((Function) functionExpr.getFunction().modify(this));
    }

    public Phrase visitEvalExpr(EvalExpr evalExpr) {
        try {
            return EvalExpr.make(evalExpr.expr, evalExpr.type, evalExpr.getProperty());
        } catch ( Exception e ) {
            throw new DbcException("Could not modify EvalExpr!", e);
        }
    }

    public Phrase visitAssertStmt(AssertStmt assertStmt) {
        return new AssertStmt(assertStmt.getProperty(), (Expr) assertStmt.asserts.modify(this), assertStmt.msg);
    }

    public Phrase visitDebugAction(DebugAction debugAction) {
        return new DebugAction(debugAction.getProperty(), (Expr) debugAction.value.modify(this));
    }

    public Phrase visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        return new BluetoothReceiveAction(bluetoothReceiveAction.getProperty(), bluetoothReceiveAction.mutation, bluetoothReceiveAction.dataType, bluetoothReceiveAction.protocol, bluetoothReceiveAction.channel, bluetoothReceiveAction.dataValue, (Expr) bluetoothReceiveAction.connection.modify(this));
    }

    public Phrase visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        return new BluetoothConnectAction(bluetoothConnectAction.getProperty(), (Expr) bluetoothConnectAction.address.modify(this));
    }

    public Phrase visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        return new BluetoothSendAction(bluetoothSendAction.getProperty(), bluetoothSendAction.mutation, bluetoothSendAction.dataType, bluetoothSendAction.protocol, bluetoothSendAction.channel, bluetoothSendAction.dataValue, (Expr) bluetoothSendAction.msg.modify(this), (Expr) bluetoothSendAction.connection.modify(this));
    }

    public Phrase visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        return new BluetoothWaitForConnectionAction(bluetoothWaitForConnection.getProperty());
    }

    public Phrase visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return new BluetoothCheckConnectAction(bluetoothCheckConnectAction.getProperty(), (Expr) bluetoothCheckConnectAction.connection.modify(this));
    }

    public Phrase visitKeysSensor(KeysSensor keysSensor) {
        return new KeysSensor(keysSensor.getProperty(), new ExternalSensorBean(keysSensor.getUserDefinedPort(), keysSensor.getMode(), keysSensor.getSlot(), keysSensor.getMutation()));
    }

    public Phrase visitColorSensor(ColorSensor colorSensor) {
        return new ColorSensor(colorSensor.getProperty(), new ExternalSensorBean(colorSensor.getUserDefinedPort(), colorSensor.getMode(), colorSensor.getSlot(), colorSensor.getMutation()));
    }

    public Phrase visitLightSensor(LightSensor lightSensor) {
        return new LightSensor(lightSensor.getProperty(), new ExternalSensorBean(lightSensor.getUserDefinedPort(), lightSensor.getMode(), lightSensor.getSlot(), lightSensor.getMutation()));
    }

    public Phrase visitSoundSensor(SoundSensor soundSensor) {
        return new SoundSensor(soundSensor.getProperty(), new ExternalSensorBean(soundSensor.getUserDefinedPort(), soundSensor.getMode(), soundSensor.getSlot(), soundSensor.getMutation()));
    }

    public Phrase visitEncoderSensor(EncoderSensor encoderSensor) {
        return new EncoderSensor(encoderSensor.getProperty(), new ExternalSensorBean(encoderSensor.getUserDefinedPort(), encoderSensor.getMode(), encoderSensor.getSlot(), encoderSensor.getMutation()));
    }

    public Phrase visitEncoderReset(EncoderReset encoderReset) {
        return new EncoderReset(encoderReset.getProperty(), encoderReset.sensorPort);
    }

    public Phrase visitGyroSensor(GyroSensor gyroSensor) {
        return new GyroSensor(gyroSensor.getProperty(), new ExternalSensorBean(gyroSensor.getUserDefinedPort(), gyroSensor.getMode(), gyroSensor.getSlot(), gyroSensor.getMutation()));
    }

    public Phrase visitGyroReset(GyroReset gyroReset) {
        return new GyroReset(gyroReset.getProperty(), gyroReset.sensorPort);
    }


    public Phrase visitInfraredSensor(InfraredSensor infraredSensor) {
        return new InfraredSensor(infraredSensor.getProperty(), new ExternalSensorBean(infraredSensor.getUserDefinedPort(), infraredSensor.getMode(), infraredSensor.getSlot(), infraredSensor.getMutation()));
    }

    public Phrase visitTimerSensor(TimerSensor timerSensor) {
        return new TimerSensor(timerSensor.getProperty(), new ExternalSensorBean(timerSensor.getUserDefinedPort(), timerSensor.getMode(), timerSensor.getSlot(), timerSensor.getMutation()));
    }

    public Phrase visitTimerReset(TimerReset timerReset) {
        return new TimerReset(timerReset.getProperty(), timerReset.sensorPort);
    }

    public Phrase visitTouchSensor(TouchSensor touchSensor) {
        return new TouchSensor(touchSensor.getProperty(), new ExternalSensorBean(touchSensor.getUserDefinedPort(), touchSensor.getMode(), touchSensor.getSlot(), touchSensor.getMutation()));
    }

    public Phrase visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        return new UltrasonicSensor(ultrasonicSensor.getProperty(), new ExternalSensorBean(ultrasonicSensor.getUserDefinedPort(), ultrasonicSensor.getMode(), ultrasonicSensor.getSlot(), ultrasonicSensor.getMutation()));
    }

    public Phrase visitCompassSensor(CompassSensor compassSensor) {
        return new CompassSensor(compassSensor.getProperty(), new ExternalSensorBean(compassSensor.getUserDefinedPort(), compassSensor.getMode(), compassSensor.getSlot(), compassSensor.getMutation()));
    }

    public Phrase visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        return new CompassCalibrate(compassCalibrate.getProperty(), new ExternalSensorBean(compassCalibrate.getUserDefinedPort(), compassCalibrate.getMode(), compassCalibrate.getSlot(), compassCalibrate.getMutation()));
    }

    public Phrase visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return new TemperatureSensor(temperatureSensor.getProperty(),
            new ExternalSensorBean(
                temperatureSensor.getUserDefinedPort(),
                temperatureSensor.getMode(),
                temperatureSensor.getSlot(),
                temperatureSensor.getMutation()));
    }

    public Phrase visitVoltageSensor(VoltageSensor voltageSensor) {
        return new VoltageSensor(voltageSensor.getProperty(), new ExternalSensorBean(voltageSensor.getUserDefinedPort(), voltageSensor.getMode(), voltageSensor.getSlot(), voltageSensor.getMutation()));
    }

    public Phrase visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return new AccelerometerSensor(accelerometerSensor.getProperty(),
            new ExternalSensorBean(
                accelerometerSensor.getUserDefinedPort(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getSlot(),
                accelerometerSensor.getMutation()));
    }

    public Phrase visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        return new PinTouchSensor(pinTouchSensor.getProperty(), new ExternalSensorBean(pinTouchSensor.getUserDefinedPort(), pinTouchSensor.getMode(), pinTouchSensor.getSlot(), pinTouchSensor.getMutation()));
    }

    public Phrase visitGestureSensor(GestureSensor gestureSensor) {
        return new GestureSensor(gestureSensor.getProperty(), new ExternalSensorBean(gestureSensor.getUserDefinedPort(), gestureSensor.getMode(), gestureSensor.getSlot(), gestureSensor.getMutation()));
    }

    public Phrase visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        return new PinGetValueSensor(pinGetValueSensor.getProperty(),
            new ExternalSensorBean(
                pinGetValueSensor.getUserDefinedPort(),
                pinGetValueSensor.getMode(),
                pinGetValueSensor.getSlot(),
                pinGetValueSensor.getMutation()));
    }

    public Phrase visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, sensorGetSample.sensorPort, sensorGetSample.slot, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
    }

    public Phrase visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        return new IRSeekerSensor(irSeekerSensor.getProperty(), new ExternalSensorBean(irSeekerSensor.getUserDefinedPort(), irSeekerSensor.getMode(), irSeekerSensor.getSlot(), irSeekerSensor.getMutation()));
    }

    public Phrase visitMoistureSensor(MoistureSensor moistureSensor) {
        return new MoistureSensor(moistureSensor.getProperty(), new ExternalSensorBean(moistureSensor.getUserDefinedPort(), moistureSensor.getMode(), moistureSensor.getSlot(), moistureSensor.getMutation()));
    }

    public Phrase visitHumiditySensor(HumiditySensor humiditySensor) {
        return new HumiditySensor(humiditySensor.getProperty(), new ExternalSensorBean(humiditySensor.getUserDefinedPort(), humiditySensor.getMode(), humiditySensor.getSlot(), humiditySensor.getMutation()));
    }

    public Phrase visitMotionSensor(MotionSensor motionSensor) {
        return new MotionSensor(motionSensor.getProperty(), new ExternalSensorBean(motionSensor.getUserDefinedPort(), motionSensor.getMode(), motionSensor.getSlot(), motionSensor.getMutation()));
    }

    public Phrase visitDropSensor(DropSensor dropSensor) {
        return new DropSensor(dropSensor.getProperty(), new ExternalSensorBean(dropSensor.getUserDefinedPort(), dropSensor.getMode(), dropSensor.getSlot(), dropSensor.getMutation()));
    }

    public Phrase visitPulseSensor(PulseSensor pulseSensor) {
        return new PulseSensor(pulseSensor.getProperty(), new ExternalSensorBean(pulseSensor.getUserDefinedPort(), pulseSensor.getMode(), pulseSensor.getSlot(), pulseSensor.getMutation()));
    }

    public Phrase visitRfidSensor(RfidSensor rfidSensor) {
        return new RfidSensor(rfidSensor.getProperty(), new ExternalSensorBean(rfidSensor.getUserDefinedPort(), rfidSensor.getMode(), rfidSensor.getSlot(), rfidSensor.getMutation()));
    }

    public Phrase visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        return new VemlLightSensor(vemlLightSensor.getProperty(), new ExternalSensorBean(vemlLightSensor.getUserDefinedPort(), vemlLightSensor.getMode(), vemlLightSensor.getSlot(), vemlLightSensor.getMutation()));
    }

    public Phrase visitParticleSensor(ParticleSensor particleSensor) {
        return new ParticleSensor(particleSensor.getProperty(), new ExternalSensorBean(particleSensor.getUserDefinedPort(), particleSensor.getMode(), particleSensor.getSlot(), particleSensor.getMutation()));
    }

    public Phrase visitHTColorSensor(HTColorSensor htColorSensor) {
        return new HTColorSensor(htColorSensor.getProperty(), new ExternalSensorBean(htColorSensor.getUserDefinedPort(), htColorSensor.getMode(), htColorSensor.getSlot(), htColorSensor.getMutation()));
    }

    public Phrase visitAccelerometer(AccelerometerSensor accelerometerSensor) {
        return new AccelerometerSensor(accelerometerSensor.getProperty(), new ExternalSensorBean(accelerometerSensor.getUserDefinedPort(), accelerometerSensor.getMode(), accelerometerSensor.getSlot(), accelerometerSensor.getMutation()));
    }

    // Helper methods

    protected MotionParam modifyMotionParam(MotionParam param) {
        MotionParam.Builder motionParamBuilder = new MotionParam.Builder();
        motionParamBuilder.speed((Expr) param.getSpeed().modify(this));
        if ( param.getDuration() != null ) { // duration may be null
            motionParamBuilder.duration(new MotorDuration(param.getDuration().getType(), (Expr) param.getDuration().getValue().modify(this)));
        }
        return motionParamBuilder.build();
    }
}

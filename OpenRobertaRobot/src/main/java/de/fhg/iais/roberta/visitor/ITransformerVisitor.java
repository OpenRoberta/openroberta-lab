package de.fhg.iais.roberta.visitor;

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
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNChangeBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNChangeWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNInputNeuronStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNOutputNeuronStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNOutputNeuronWoVarStmt;
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
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
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
import de.fhg.iais.roberta.visitor.hardware.actor.IAllActorsVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * A visitor to be used to potentially modify all phrases in the AST. Every visit implementation should return a new phrase using the static make method of the
 * individual phrase, or even other phrases. All subphrases should be called with {@link Phrase#modify(ITransformerVisitor)} )} to ensure all leaves are
 * visited.
 */
@SuppressWarnings("unchecked")
public interface ITransformerVisitor extends ISensorVisitor<Phrase>, IAllActorsVisitor<Phrase>, ILanguageVisitor<Phrase> {

    /**
     * Returns the dropdown factory. Necessary for the {@link GetSampleSensor} visit. {@link BlocklyDropdownFactory} is required by
     * {@link GetSampleSensor#make(String, String, String, boolean, BlocklyProperties, BlocklyComment, BlocklyDropdownFactory)} in order to recreate
     * itself.
     *
     * @return the dropdown factory
     */
    BlocklyDropdownFactory getBlocklyDropdownFactory();

    @Override
    default Phrase visitDriveAction(DriveAction driveAction) {
        return new DriveAction(driveAction.direction, modifyMotionParam(driveAction.param), BlocklyConstants.EMPTY_PORT, null, driveAction.getProperty());
    }

    @Override
    default Phrase visitCurveAction(CurveAction curveAction) {
        return new CurveAction(curveAction.direction, modifyMotionParam(curveAction.paramLeft), modifyMotionParam(curveAction.paramRight), BlocklyConstants.EMPTY_PORT, null, curveAction.getProperty());
    }

    @Override
    default Phrase visitTurnAction(TurnAction turnAction) {
        return new TurnAction(turnAction.direction, modifyMotionParam(turnAction.param), BlocklyConstants.EMPTY_PORT, null, turnAction.getProperty());
    }

    @Override
    default Phrase visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        return new MotorDriveStopAction(stopAction.getProperty(), BlocklyConstants.EMPTY_PORT, null);
    }

    @Override
    default Phrase visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return new MotorGetPowerAction(motorGetPowerAction.getProperty(), motorGetPowerAction.getUserDefinedPort());
    }

    @Override
    default Phrase visitMotorOnAction(MotorOnAction motorOnAction) {
        return new MotorOnAction(motorOnAction.getUserDefinedPort(), modifyMotionParam(motorOnAction.param), motorOnAction.getProperty());
    }

    @Override
    default Phrase visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return new MotorSetPowerAction(motorSetPowerAction.getProperty(), (Expr) motorSetPowerAction.power.modify(this), motorSetPowerAction.getUserDefinedPort());
    }

    @Override
    default Phrase visitMotorStopAction(MotorStopAction motorStopAction) {
        return new MotorStopAction(motorStopAction.getUserDefinedPort(), motorStopAction.mode, motorStopAction.getProperty());
    }

    @Override
    default Phrase visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return new ClearDisplayAction(clearDisplayAction.getProperty(), clearDisplayAction.port, null);
    }

    @Override
    default Phrase visitShowTextAction(ShowTextAction showTextAction) {
        return new ShowTextAction(showTextAction.getProperty(), (Expr) showTextAction.msg.modify(this), (Expr) showTextAction.x.modify(this), (Expr) showTextAction.y.modify(this), showTextAction.port, null);
    }

    @Override
    default Phrase visitLightAction(LightAction lightAction) {
        return new LightAction(lightAction.port, lightAction.color, lightAction.mode, (Expr) lightAction.rgbLedColor.modify(this), lightAction.getProperty());
    }

    @Override
    default Phrase visitLightStatusAction(LightStatusAction lightStatusAction) {
        return new LightStatusAction(lightStatusAction.getUserDefinedPort(), lightStatusAction.status, lightStatusAction.getProperty());
    }

    @Override
    default Phrase visitToneAction(ToneAction toneAction) {
        return new ToneAction(toneAction.getProperty(), (Expr) toneAction.frequency.modify(this), (Expr) toneAction.duration.modify(this), toneAction.port, toneAction.hide);
    }

    @Override
    default Phrase visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return new PlayNoteAction(playNoteAction.getProperty(), playNoteAction.duration, playNoteAction.frequency, playNoteAction.port, playNoteAction.hide);
    }

    @Override
    default Phrase visitVolumeAction(VolumeAction volumeAction) {
        return new VolumeAction(volumeAction.mode, (Expr) volumeAction.volume.modify(this), BlocklyConstants.EMPTY_PORT, null, volumeAction.getProperty());
    }

    @Override
    default Phrase visitPlayFileAction(PlayFileAction playFileAction) {
        return new PlayFileAction(playFileAction.getProperty(), playFileAction.fileName);
    }

    @Override
    default Phrase visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        return new SetLanguageAction(setLanguageAction.language, setLanguageAction.getProperty());
    }

    @Override
    default Phrase visitSayTextAction(SayTextAction sayTextAction) {
        return new SayTextAction(sayTextAction.getProperty(), (Expr) sayTextAction.msg.modify(this));
    }

    @Override
    default Phrase visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        return new SayTextWithSpeedAndPitchAction(sayTextAction.getProperty(), (Expr) sayTextAction.msg.modify(this), (Expr) sayTextAction.speed.modify(this), (Expr) sayTextAction.pitch.modify(this));
    }

    @Override
    default Phrase visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        return new SerialWriteAction(serialWriteAction.getProperty(), (Expr) serialWriteAction.value.modify(this));
    }

    @Override
    default Phrase visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        return new PinWriteValueAction(pinWriteValueAction.pinValue, pinWriteValueAction.port, (Expr) pinWriteValueAction.value.modify(this), true, pinWriteValueAction.getProperty());
    }

    @Override
    default Phrase visitNumConst(NumConst numConst) {
        return new NumConst(numConst.getProperty(), numConst.value);
    }

    @Override
    default Phrase visitMathConst(MathConst mathConst) {
        return new MathConst(mathConst.getProperty(), mathConst.mathConst);
    }

    @Override
    default Phrase visitBoolConst(BoolConst boolConst) {
        return new BoolConst(boolConst.getProperty(), boolConst.value);
    }

    @Override
    default Phrase visitStringConst(StringConst stringConst) {
        return new StringConst(stringConst.getProperty(), stringConst.value);
    }

    @Override
    default Phrase visitNullConst(NullConst nullConst) {
        return new NullConst(nullConst.getProperty());
    }

    @Override
    default Phrase visitColorConst(ColorConst colorConst) {
        return new ColorConst(colorConst.getProperty(), colorConst.getHexValueAsString());
    }

    @Override
    default Phrase visitRgbColor(RgbColor rgbColor) {
        return new RgbColor(rgbColor.getProperty(), (Expr) rgbColor.R.modify(this), (Expr) rgbColor.G.modify(this), (Expr) rgbColor.B.modify(this), (Expr) rgbColor.A.modify(this));
    }

    @Override
    default Phrase visitStmtTextComment(StmtTextComment stmtTextComment) {
        return new StmtTextComment(stmtTextComment.getProperty(), stmtTextComment.textComment);
    }

    @Override
    default Phrase visitConnectConst(ConnectConst connectConst) {
        return new ConnectConst(connectConst.getProperty(), connectConst.value, connectConst.value);
    }

    @Override
    default Phrase visitVar(Var var) {
        return new Var(var.getVarType(), var.name, var.getProperty());
    }

    @Override
    default Phrase visitVarDeclaration(VarDeclaration var) {
        return new VarDeclaration(var.typeVar, var.name, (Expr) var.value.modify(this), var.next, var.global, var.getProperty());
    }

    @Override
    default Phrase visitUnary(Unary unary) {
        return new Unary(unary.op, (Expr) unary.expr.modify(this), unary.getProperty());
    }

    @Override
    default Phrase visitBinary(Binary binary) {
        return new Binary(binary.op, (Expr) binary.left.modify(this), (Expr) binary.getRight().modify(this), binary.operationRange, binary.getProperty());
    }

    @Override
    default Phrase visitMathPowerFunct(MathPowerFunct mathPowerFunct) {
        List<Expr> newParam = new ArrayList();
        mathPowerFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathPowerFunct(mathPowerFunct.getProperty(), mathPowerFunct.functName, newParam);
    }

    @Override
    default Phrase visitEmptyList(EmptyList emptyList) {
        return new EmptyList(emptyList.getProperty(), emptyList.typeVar);
    }

    @Override
    default Phrase visitAssignStmt(AssignStmt assignStmt) {
        return new AssignStmt(assignStmt.getProperty(), (Var) assignStmt.name.modify(this), (Expr) assignStmt.expr.modify(this));
    }

    @Override
    default Phrase visitEmptyExpr(EmptyExpr emptyExpr) {
        return new EmptyExpr(emptyExpr.getDefVal());
    }

    @Override
    default Phrase visitExprList(ExprList exprList) {
        ExprList newExprList = new ExprList();
        exprList.get().forEach(phraseExpr -> newExprList.addExpr((Expr) phraseExpr.modify(this)));
        newExprList.setReadOnly();
        return newExprList;
    }

    @Override
    default Phrase visitIfStmt(IfStmt ifStmt) {
        List<Expr> newExpr = new ArrayList();
        ifStmt.expr.forEach(phraseExpr -> newExpr.add((Expr) phraseExpr.modify(this)));
        List<StmtList> newThenList = new ArrayList();
        ifStmt.thenList.forEach(phraseStmtList -> newThenList.add((StmtList) phraseStmtList.modify(this)));
        StmtList newElseList = (StmtList) ifStmt.elseList.modify(this);

        return new IfStmt(ifStmt.getProperty(), newExpr, newThenList, newElseList, ifStmt._else, ifStmt._elseIf);
    }

    default Phrase visitTernaryExpr(TernaryExpr ternaryExpr) {
        Expr condition = (Expr) ternaryExpr.condition.modify(this);
        Expr thenPart = (Expr) ternaryExpr.thenPart.modify(this);
        Expr elsePart = (Expr) ternaryExpr.elsePart.modify(this);

        return new TernaryExpr(ternaryExpr.getProperty(), condition, thenPart, elsePart);
    }

    @Override
    default Phrase visitNNStepStmt(NNStepStmt nnStepStmt) {
        StmtList newIoNeurons = new StmtList();
        for ( Stmt e : nnStepStmt.getIoNeurons().get() ) {
            newIoNeurons.get().add((Stmt) e.modify(this));
        }
        return new NNStepStmt(nnStepStmt.getProperty(), newIoNeurons, nnStepStmt.netDefinition);
    }

    @Override
    default Phrase visitNNInputNeuronStmt(NNInputNeuronStmt inNeuron) {
        return new NNInputNeuronStmt(inNeuron.getProperty(), inNeuron.name, (Expr) inNeuron.modify(this));
    }

    @Override
    default Phrase visitNNOutputNeuronStmt(NNOutputNeuronStmt outNeuron) {
        return new NNOutputNeuronStmt(outNeuron.getProperty(), outNeuron.name, (Expr) outNeuron.modify(this));
    }

    @Override
    default Phrase visitNNOutputNeuronWoVarStmt(NNOutputNeuronWoVarStmt outNeuron) {
        return new NNOutputNeuronWoVarStmt(outNeuron.getProperty(), outNeuron.name);
    }

    @Override
    default Phrase visitNNChangeWeightStmt(NNChangeWeightStmt chgStmt) {
        return new NNChangeWeightStmt(chgStmt.getProperty(), chgStmt.from, chgStmt.to, chgStmt.change, (Expr) chgStmt.modify(this));
    }

    @Override
    default Phrase visitNNChangeBiasStmt(NNChangeBiasStmt chgStmt) {
        return new NNChangeBiasStmt(chgStmt.getProperty(), chgStmt.name, chgStmt.change, (Expr) chgStmt.modify(this));
    }

    @Override
    default Phrase visitNNGetOutputNeuronVal(NNGetOutputNeuronVal getStmt) {
        return new NNGetOutputNeuronVal(getStmt.getProperty(), getStmt.name);
    }

    @Override
    default Phrase visitNNGetWeight(NNGetWeight nnGetWeight) {
        return new NNGetWeight(nnGetWeight.getProperty(), nnGetWeight.from, nnGetWeight.to);
    }

    @Override
    default Phrase visitNNGetBias(NNGetBias nnGetBias) {
        return new NNGetBias(nnGetBias.getProperty(), nnGetBias.name);
    }

    @Override
    default Phrase visitRepeatStmt(RepeatStmt repeatStmt) {
        return new RepeatStmt(repeatStmt.mode, (Expr) repeatStmt.expr.modify(this), (StmtList) repeatStmt.list.modify(this), repeatStmt.getProperty());
    }

    @Override
    default Phrase visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        return new StmtFlowCon(stmtFlowCon.getProperty(), stmtFlowCon.flow);
    }

    @Override
    default Phrase visitStmtList(StmtList stmtList) {
        StmtList newPhrase = new StmtList();
        stmtList.get().forEach(stmt -> newPhrase.addStmt((Stmt) stmt.modify(this)));
        newPhrase.setReadOnly();
        return newPhrase;
    }

    @Override
    default Phrase visitMainTask(MainTask mainTask) {
        return new MainTask(mainTask.getProperty(), (StmtList) mainTask.variables.modify(this), mainTask.debug, mainTask.data);
    }

    @Override
    default Phrase visitWaitStmt(WaitStmt waitStmt) {
        return new WaitStmt(waitStmt.getProperty(), (StmtList) waitStmt.statements.modify(this));
    }

    @Override
    default Phrase visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        return new WaitTimeStmt(waitTimeStmt.getProperty(), (Expr) waitTimeStmt.time.modify(this));
    }

    @Override
    default Phrase visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        List<Expr> newParam = new ArrayList();
        textPrintFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new TextPrintFunct(newParam, textPrintFunct.getProperty());
    }

    @Override
    default Phrase visitGetSubFunct(GetSubFunct getSubFunct) {
        List<Expr> newParam = new ArrayList();
        getSubFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new GetSubFunct(getSubFunct.functName, getSubFunct.strParam, newParam, getSubFunct.getProperty());
    }

    @Override
    default Phrase visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        List<Expr> newParam = new ArrayList();
        indexOfFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new IndexOfFunct(indexOfFunct.location, newParam, indexOfFunct.getProperty());
    }

    @Override
    default Phrase visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct lengthOfIsEmptyFunct) {
        List<Expr> newParam = new ArrayList();
        lengthOfIsEmptyFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new LengthOfIsEmptyFunct(lengthOfIsEmptyFunct.functName, newParam, lengthOfIsEmptyFunct.getProperty());
    }

    @Override
    default Phrase visitListCreate(ListCreate listCreate) {
        return new ListCreate(listCreate.typeVar, (ExprList) listCreate.exprList.modify(this), listCreate.getProperty());
    }

    @Override
    default Phrase visitListGetIndex(ListGetIndex listGetIndex) {
        List<Expr> newParam = new ArrayList();
        listGetIndex.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListGetIndex(listGetIndex.getElementOperation(), listGetIndex.location, newParam, listGetIndex.dataType, listGetIndex.getProperty());
    }

    @Override
    default Phrase visitListRepeat(ListRepeat listRepeat) {
        List<Expr> newParam = new ArrayList();
        listRepeat.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListRepeat(listRepeat.typeVar, newParam, listRepeat.getProperty());
    }

    @Override
    default Phrase visitListSetIndex(ListSetIndex listSetIndex) {
        List<Expr> newParam = new ArrayList();
        listSetIndex.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new ListSetIndex(listSetIndex.mode, listSetIndex.location, newParam, listSetIndex.getProperty());
    }

    @Override
    default Phrase visitMathConstrainFunct(MathConstrainFunct mathConstrainFunct) {
        List<Expr> newParam = new ArrayList();
        mathConstrainFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathConstrainFunct(newParam, mathConstrainFunct.getProperty());
    }

    @Override
    default Phrase visitMathNumPropFunct(MathNumPropFunct mathNumPropFunct) {
        List<Expr> newParam = new ArrayList();
        mathNumPropFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathNumPropFunct(mathNumPropFunct.functName, newParam, mathNumPropFunct.getProperty());
    }

    @Override
    default Phrase visitMathOnListFunct(MathOnListFunct mathOnListFunct) {
        List<Expr> newParam = new ArrayList();
        mathOnListFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathOnListFunct(mathOnListFunct.functName, newParam, mathOnListFunct.getProperty());
    }

    @Override
    default Phrase visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        return new MathRandomFloatFunct(mathRandomFloatFunct.getProperty());
    }

    @Override
    default Phrase visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        List<Expr> newParam = new ArrayList();
        mathRandomIntFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathRandomIntFunct(newParam, mathRandomIntFunct.getProperty());
    }

    @Override
    default Phrase visitMathSingleFunct(MathSingleFunct mathSingleFunct) {
        List<Expr> newParam = new ArrayList();
        mathSingleFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathSingleFunct(mathSingleFunct.functName, newParam, mathSingleFunct.getProperty());
    }

    @Override
    default Phrase visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        List<Expr> newParam = new ArrayList();
        mathCastStringFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, mathCastStringFunct.getProperty());
    }

    @Override
    default Phrase visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        List<Expr> newParam = new ArrayList();
        mathCastCharFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, mathCastCharFunct.getProperty());
    }

    @Override
    default Phrase visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        List<Expr> newParam = new ArrayList();
        textStringCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, textStringCastNumberFunct.getProperty());
    }

    @Override
    default Phrase visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        List<Expr> newParam = new ArrayList();
        textCharCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr) phraseExpr.modify(this)));
        return new MathCastStringFunct(newParam, textCharCastNumberFunct.getProperty());
    }

    @Override
    default Phrase visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        return new TextJoinFunct((ExprList) textJoinFunct.param.modify(this), textJoinFunct.getProperty());
    }

    @Override
    default Phrase visitMethodVoid(MethodVoid methodVoid) {
        return new MethodVoid(methodVoid.getMethodName(), (ExprList) methodVoid.getParameters().modify(this), (StmtList) methodVoid.body.modify(this), methodVoid.getProperty());
    }

    @Override
    default Phrase visitMethodReturn(MethodReturn methodReturn) {
        return new MethodReturn(methodReturn.getMethodName(), (ExprList) methodReturn.getParameters().modify(this), (StmtList) methodReturn.body.modify(this), methodReturn.getReturnType(), (Expr) methodReturn.returnValue.modify(this), methodReturn.getProperty());
    }

    @Override
    default Phrase visitMethodIfReturn(MethodIfReturn methodIfReturn) {
        return new MethodIfReturn((Expr) methodIfReturn.oraCondition.modify(this), methodIfReturn.getReturnType(), (Expr) methodIfReturn.oraReturnValue.modify(this), methodIfReturn.getValue(), methodIfReturn.getProperty());
    }

    @Override
    default Phrase visitMethodStmt(MethodStmt methodStmt) {
        return new MethodStmt((Method) methodStmt.method.modify(this));
    }

    @Override
    default Phrase visitMethodCall(MethodCall methodCall) {
        return new MethodCall(methodCall.getMethodName(), (ExprList) methodCall.getParameters().modify(this), (ExprList) methodCall.getParametersValues().modify(this), methodCall.getReturnType(), methodCall.getProperty());
    }

    @Override
    default Phrase visitSensorExpr(SensorExpr sensorExpr) {
        return new SensorExpr((Sensor) sensorExpr.sensor.modify(this));
    }

    @Override
    default Phrase visitMethodExpr(MethodExpr methodExpr) {
        return new MethodExpr((Method) methodExpr.getMethod().modify(this));
    }

    @Override
    default Phrase visitActionStmt(ActionStmt actionStmt) {
        return new ActionStmt((Action) actionStmt.action.modify(this));
    }

    @Override
    default Phrase visitActionExpr(ActionExpr actionExpr) {
        return new ActionExpr((Action) actionExpr.action.modify(this));
    }

    @Override
    default Phrase visitExprStmt(ExprStmt exprStmt) {
        return new ExprStmt((Expr) exprStmt.expr.modify(this));
    }

    @Override
    default Phrase visitStmtExpr(StmtExpr stmtExpr) {
        return new StmtExpr((Stmt) stmtExpr.stmt.modify(this));
    }

    @Override
    default Phrase visitShadowExpr(ShadowExpr shadowExpr) {
        return new ShadowExpr((Expr) shadowExpr.shadow.modify(this), (Expr) shadowExpr.block.modify(this));
    }

    @Override
    default Phrase visitSensorStmt(SensorStmt sensorStmt) {
        return new SensorStmt((Sensor) sensorStmt.sensor.modify(this));
    }

    @Override
    default Phrase visitActivityTask(ActivityTask activityTask) {
        return new ActivityTask(activityTask.getProperty(), (Expr) activityTask.activityName.modify(this));
    }

    @Override
    default Phrase visitStartActivityTask(StartActivityTask startActivityTask) {
        return new StartActivityTask(startActivityTask.getProperty(), (Expr) startActivityTask.activityName.modify(this));
    }

    @Override
    default Phrase visitLocation(Location location) {
        return new Location(location.x, location.y);
    }

    @Override
    default Phrase visitFunctionStmt(FunctionStmt functionStmt) {
        return new FunctionStmt((Function) functionStmt.function.modify(this));
    }

    @Override
    default Phrase visitFunctionExpr(FunctionExpr functionExpr) {
        return new FunctionExpr((Function) functionExpr.getFunction().modify(this));
    }

    @Override
    default Phrase visitEvalExpr(EvalExpr evalExpr) {
        try {
            return EvalExpr.make(evalExpr.expr, evalExpr.type, evalExpr.getProperty());
        } catch ( Exception e ) {
            throw new DbcException("Could not modify EvalExpr!", e);
        }
    }

    @Override
    default Phrase visitAssertStmt(AssertStmt assertStmt) {
        return new AssertStmt(assertStmt.getProperty(), (Expr) assertStmt.asserts.modify(this), assertStmt.msg);
    }

    @Override
    default Phrase visitDebugAction(DebugAction debugAction) {
        return new DebugAction(debugAction.getProperty(), (Expr) debugAction.value.modify(this));
    }

    @Override
    default Phrase visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        return new BluetoothReceiveAction(bluetoothReceiveAction.dataValue, (Expr) bluetoothReceiveAction.connection.modify(this), bluetoothReceiveAction.channel, bluetoothReceiveAction.dataType, bluetoothReceiveAction.getProperty());
    }

    @Override
    default Phrase visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        return new BluetoothConnectAction(bluetoothConnectAction.getProperty(), (Expr) bluetoothConnectAction.address.modify(this));
    }

    @Override
    default Phrase visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        return new BluetoothSendAction(bluetoothSendAction.dataValue, (Expr) bluetoothSendAction.connection.modify(this), (Expr) bluetoothSendAction.msg.modify(this), bluetoothSendAction.channel, bluetoothSendAction.dataType, bluetoothSendAction.getProperty());
    }

    @Override
    default Phrase visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        return new BluetoothWaitForConnectionAction(bluetoothWaitForConnection.getProperty());
    }

    @Override
    default Phrase visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return new BluetoothCheckConnectAction(bluetoothCheckConnectAction.getProperty(), (Expr) bluetoothCheckConnectAction.connection.modify(this));
    }

    @Override
    default Phrase visitKeysSensor(KeysSensor keysSensor) {
        return new KeysSensor(keysSensor.getProperty(), new ExternalSensorBean(keysSensor.getUserDefinedPort(), keysSensor.getMode(), keysSensor.getSlot(), keysSensor.getMutation()));
    }

    @Override
    default Phrase visitColorSensor(ColorSensor colorSensor) {
        return new ColorSensor(colorSensor.getProperty(), new ExternalSensorBean(colorSensor.getUserDefinedPort(), colorSensor.getMode(), colorSensor.getSlot(), colorSensor.getMutation()));
    }

    @Override
    default Phrase visitLightSensor(LightSensor lightSensor) {
        return new LightSensor(lightSensor.getProperty(), new ExternalSensorBean(lightSensor.getUserDefinedPort(), lightSensor.getMode(), lightSensor.getSlot(), lightSensor.getMutation()));
    }

    @Override
    default Phrase visitSoundSensor(SoundSensor soundSensor) {
        return new SoundSensor(soundSensor.getProperty(), new ExternalSensorBean(soundSensor.getUserDefinedPort(), soundSensor.getMode(), soundSensor.getSlot(), soundSensor.getMutation()));
    }

    @Override
    default Phrase visitEncoderSensor(EncoderSensor encoderSensor) {
        return new EncoderSensor(encoderSensor.getProperty(), new ExternalSensorBean(encoderSensor.getUserDefinedPort(), encoderSensor.getMode(), encoderSensor.getSlot(), encoderSensor.getMutation()));
    }

    @Override
    default Phrase visitGyroSensor(GyroSensor gyroSensor) {
        return new GyroSensor(gyroSensor.getProperty(), new ExternalSensorBean(gyroSensor.getUserDefinedPort(), gyroSensor.getMode(), gyroSensor.getSlot(), gyroSensor.getMutation()));
    }

    @Override
    default Phrase visitInfraredSensor(InfraredSensor infraredSensor) {
        return new InfraredSensor(infraredSensor.getProperty(), new ExternalSensorBean(infraredSensor.getUserDefinedPort(), infraredSensor.getMode(), infraredSensor.getSlot(), infraredSensor.getMutation()));
    }

    @Override
    default Phrase visitTimerSensor(TimerSensor timerSensor) {
        return new TimerSensor(timerSensor.getProperty(), new ExternalSensorBean(timerSensor.getUserDefinedPort(), timerSensor.getMode(), timerSensor.getSlot(), timerSensor.getMutation()));
    }

    @Override
    default Phrase visitTouchSensor(TouchSensor touchSensor) {
        return new TouchSensor(touchSensor.getProperty(), new ExternalSensorBean(touchSensor.getUserDefinedPort(), touchSensor.getMode(), touchSensor.getSlot(), touchSensor.getMutation()));
    }

    @Override
    default Phrase visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        return new UltrasonicSensor(ultrasonicSensor.getProperty(), new ExternalSensorBean(ultrasonicSensor.getUserDefinedPort(), ultrasonicSensor.getMode(), ultrasonicSensor.getSlot(), ultrasonicSensor.getMutation()));
    }

    @Override
    default Phrase visitCompassSensor(CompassSensor compassSensor) {
        return new CompassSensor(compassSensor.getProperty(), new ExternalSensorBean(compassSensor.getUserDefinedPort(), compassSensor.getMode(), compassSensor.getSlot(), compassSensor.getMutation()));
    }

    @Override
    default Phrase visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return new TemperatureSensor(temperatureSensor.getProperty(),
            new ExternalSensorBean(
                temperatureSensor.getUserDefinedPort(),
                temperatureSensor.getMode(),
                temperatureSensor.getSlot(),
                temperatureSensor.getMutation()));
    }

    @Override
    default Phrase visitVoltageSensor(VoltageSensor voltageSensor) {
        return new VoltageSensor(voltageSensor.getProperty(), new ExternalSensorBean(voltageSensor.getUserDefinedPort(), voltageSensor.getMode(), voltageSensor.getSlot(), voltageSensor.getMutation()));
    }

    @Override
    default Phrase visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return new AccelerometerSensor(accelerometerSensor.getProperty(),
            new ExternalSensorBean(
                accelerometerSensor.getUserDefinedPort(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getSlot(),
                accelerometerSensor.getMutation()));
    }

    @Override
    default Phrase visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        return new PinTouchSensor(pinTouchSensor.getProperty(), new ExternalSensorBean(pinTouchSensor.getUserDefinedPort(), pinTouchSensor.getMode(), pinTouchSensor.getSlot(), pinTouchSensor.getMutation()));
    }

    @Override
    default Phrase visitGestureSensor(GestureSensor gestureSensor) {
        return new GestureSensor(gestureSensor.getProperty(), new ExternalSensorBean(gestureSensor.getUserDefinedPort(), gestureSensor.getMode(), gestureSensor.getSlot(), gestureSensor.getMutation()));
    }

    @Override
    default Phrase visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        return new PinGetValueSensor(pinGetValueSensor.getProperty(),
            new ExternalSensorBean(
                pinGetValueSensor.getUserDefinedPort(),
                pinGetValueSensor.getMode(),
                pinGetValueSensor.getSlot(),
                pinGetValueSensor.getMutation()));
    }

    @Override
    default Phrase visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, sensorGetSample.sensorPort, sensorGetSample.slot, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
    }

    @Override
    default Phrase visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        return new IRSeekerSensor(irSeekerSensor.getProperty(), new ExternalSensorBean(irSeekerSensor.getUserDefinedPort(), irSeekerSensor.getMode(), irSeekerSensor.getSlot(), irSeekerSensor.getMutation()));
    }

    @Override
    default Phrase visitMoistureSensor(MoistureSensor moistureSensor) {
        return new MoistureSensor(moistureSensor.getProperty(), new ExternalSensorBean(moistureSensor.getUserDefinedPort(), moistureSensor.getMode(), moistureSensor.getSlot(), moistureSensor.getMutation()));
    }

    @Override
    default Phrase visitHumiditySensor(HumiditySensor humiditySensor) {
        return new HumiditySensor(humiditySensor.getProperty(), new ExternalSensorBean(humiditySensor.getUserDefinedPort(), humiditySensor.getMode(), humiditySensor.getSlot(), humiditySensor.getMutation()));
    }

    @Override
    default Phrase visitMotionSensor(MotionSensor motionSensor) {
        return new MotionSensor(motionSensor.getProperty(), new ExternalSensorBean(motionSensor.getUserDefinedPort(), motionSensor.getMode(), motionSensor.getSlot(), motionSensor.getMutation()));
    }

    @Override
    default Phrase visitDropSensor(DropSensor dropSensor) {
        return new DropSensor(dropSensor.getProperty(), new ExternalSensorBean(dropSensor.getUserDefinedPort(), dropSensor.getMode(), dropSensor.getSlot(), dropSensor.getMutation()));
    }

    @Override
    default Phrase visitPulseSensor(PulseSensor pulseSensor) {
        return new PulseSensor(pulseSensor.getProperty(), new ExternalSensorBean(pulseSensor.getUserDefinedPort(), pulseSensor.getMode(), pulseSensor.getSlot(), pulseSensor.getMutation()));
    }

    @Override
    default Phrase visitRfidSensor(RfidSensor rfidSensor) {
        return new RfidSensor(rfidSensor.getProperty(), new ExternalSensorBean(rfidSensor.getUserDefinedPort(), rfidSensor.getMode(), rfidSensor.getSlot(), rfidSensor.getMutation()));
    }

    @Override
    default Phrase visitVemlLightSensor(VemlLightSensor vemlLightSensor) {
        return new VemlLightSensor(vemlLightSensor.getProperty(), new ExternalSensorBean(vemlLightSensor.getUserDefinedPort(), vemlLightSensor.getMode(), vemlLightSensor.getSlot(), vemlLightSensor.getMutation()));
    }

    @Override
    default Phrase visitParticleSensor(ParticleSensor particleSensor) {
        return new ParticleSensor(particleSensor.getProperty(), new ExternalSensorBean(particleSensor.getUserDefinedPort(), particleSensor.getMode(), particleSensor.getSlot(), particleSensor.getMutation()));
    }

    @Override
    default Phrase visitHTColorSensor(HTColorSensor htColorSensor) {
        return new HTColorSensor(htColorSensor.getProperty(), new ExternalSensorBean(htColorSensor.getUserDefinedPort(), htColorSensor.getMode(), htColorSensor.getSlot(), htColorSensor.getMutation()));
    }

    // Helper methods

    default MotionParam modifyMotionParam(MotionParam param) {
        MotionParam.Builder motionParamBuilder = new MotionParam.Builder();
        motionParamBuilder.speed((Expr) param.getSpeed().modify(this));
        if ( param.getDuration() != null ) { // duration may be null
            motionParamBuilder.duration(new MotorDuration(param.getDuration().getType(), (Expr) param.getDuration().getValue().modify(this)));
        }
        return motionParamBuilder.build();
    }
}

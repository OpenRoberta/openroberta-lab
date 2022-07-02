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
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
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
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
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
public interface ITransformerVisitor<V> extends ISensorVisitor<Phrase<V>>, IAllActorsVisitor<Phrase<V>>, ILanguageVisitor<Phrase<V>> {

    /**
     * Returns the dropdown factory. Necessary for the {@link GetSampleSensor} visit. {@link BlocklyDropdownFactory} is required by
     * {@link GetSampleSensor#make(String, String, String, boolean, BlocklyBlockProperties, BlocklyComment, BlocklyDropdownFactory)} in order to recreate
     * itself.
     *
     * @return the dropdown factory
     */
    BlocklyDropdownFactory getBlocklyDropdownFactory();

    @Override
    default Phrase<V> visitDriveAction(DriveAction<Phrase<V>> driveAction) {
        return new DriveAction<>(driveAction.direction, modifyMotionParam(driveAction.param), BlocklyConstants.EMPTY_PORT, null, driveAction.getProperty(), driveAction.getComment());
    }

    @Override
    default Phrase<V> visitCurveAction(CurveAction<Phrase<V>> curveAction) {
        return new CurveAction<>(curveAction.direction, modifyMotionParam(curveAction.paramLeft), modifyMotionParam(curveAction.paramRight), BlocklyConstants.EMPTY_PORT, null, curveAction.getProperty(), curveAction.getComment());
    }

    @Override
    default Phrase<V> visitTurnAction(TurnAction<Phrase<V>> turnAction) {
        return new TurnAction<>(turnAction.direction, modifyMotionParam(turnAction.param), BlocklyConstants.EMPTY_PORT, null, turnAction.getProperty(), turnAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorDriveStopAction(MotorDriveStopAction<Phrase<V>> stopAction) {
        return new MotorDriveStopAction<>(stopAction.getProperty(), stopAction.getComment(), BlocklyConstants.EMPTY_PORT, null);
    }

    @Override
    default Phrase<V> visitMotorGetPowerAction(MotorGetPowerAction<Phrase<V>> motorGetPowerAction) {
        return new MotorGetPowerAction<V>(motorGetPowerAction.getProperty(), motorGetPowerAction.getComment(), motorGetPowerAction.getUserDefinedPort());
    }

    @Override
    default Phrase<V> visitMotorOnAction(MotorOnAction<Phrase<V>> motorOnAction) {
        return new MotorOnAction<>(motorOnAction.getUserDefinedPort(), modifyMotionParam(motorOnAction.param), motorOnAction.getProperty(), motorOnAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorSetPowerAction(MotorSetPowerAction<Phrase<V>> motorSetPowerAction) {
        return new MotorSetPowerAction<V>(motorSetPowerAction.getProperty(), motorSetPowerAction.getComment(), (Expr<V>) motorSetPowerAction.power.modify(this), motorSetPowerAction.getUserDefinedPort());
    }

    @Override
    default Phrase<V> visitMotorStopAction(MotorStopAction<Phrase<V>> motorStopAction) {
        return new MotorStopAction<V>(motorStopAction.getUserDefinedPort(), motorStopAction.mode, motorStopAction.getProperty(), motorStopAction.getComment());
    }

    @Override
    default Phrase<V> visitClearDisplayAction(ClearDisplayAction<Phrase<V>> clearDisplayAction) {
        return new ClearDisplayAction(clearDisplayAction.getProperty(), clearDisplayAction.getComment(), clearDisplayAction.port, null);
    }

    @Override
    default Phrase<V> visitShowTextAction(ShowTextAction<Phrase<V>> showTextAction) {
        return new ShowTextAction<V>(showTextAction.getProperty(), showTextAction.getComment(), (Expr<V>) showTextAction.msg.modify(this), (Expr<V>) showTextAction.x.modify(this), (Expr<V>) showTextAction.y.modify(this), showTextAction.port, null);
    }

    @Override
    default Phrase<V> visitLightAction(LightAction<Phrase<V>> lightAction) {
        return new LightAction<>(lightAction.port, lightAction.color, lightAction.mode, (Expr<V>) lightAction.rgbLedColor.modify(this), lightAction.getProperty(), lightAction.getComment());
    }

    @Override
    default Phrase<V> visitLightStatusAction(LightStatusAction<Phrase<V>> lightStatusAction) {
        return new LightStatusAction<>(lightStatusAction.getUserDefinedPort(), lightStatusAction.status, lightStatusAction.getProperty(), lightStatusAction.getComment());
    }

    @Override
    default Phrase<V> visitToneAction(ToneAction<Phrase<V>> toneAction) {
        return new ToneAction<>(toneAction.getProperty(), toneAction.getComment(), (Expr<V>) toneAction.frequency.modify(this), (Expr<V>) toneAction.duration.modify(this), toneAction.port, toneAction.hide);
    }

    @Override
    default Phrase<V> visitPlayNoteAction(PlayNoteAction<Phrase<V>> playNoteAction) {
        return new PlayNoteAction<>(playNoteAction.getProperty(), playNoteAction.getComment(), playNoteAction.duration, playNoteAction.frequency, playNoteAction.port, playNoteAction.hide);
    }

    @Override
    default Phrase<V> visitVolumeAction(VolumeAction<Phrase<V>> volumeAction) {
        return new VolumeAction<>(volumeAction.mode, (Expr<V>) volumeAction.volume.modify(this), BlocklyConstants.EMPTY_PORT, null, volumeAction.getProperty(), volumeAction.getComment());
    }

    @Override
    default Phrase<V> visitPlayFileAction(PlayFileAction<Phrase<V>> playFileAction) {
        return new PlayFileAction<V>(playFileAction.getProperty(), playFileAction.getComment(), playFileAction.fileName);
    }

    @Override
    default Phrase<V> visitSetLanguageAction(SetLanguageAction<Phrase<V>> setLanguageAction) {
        return new SetLanguageAction<V>(setLanguageAction.language, setLanguageAction.getProperty(), setLanguageAction.getComment());
    }

    @Override
    default Phrase<V> visitSayTextAction(SayTextAction<Phrase<V>> sayTextAction) {
        return new SayTextAction<>(sayTextAction.getProperty(), sayTextAction.getComment(), (Expr<V>) sayTextAction.msg.modify(this));
    }

    @Override
    default Phrase<V> visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction<Phrase<V>> sayTextAction) {
        return new SayTextWithSpeedAndPitchAction<>(sayTextAction.getProperty(), sayTextAction.getComment(), (Expr<V>) sayTextAction.msg.modify(this), (Expr<V>) sayTextAction.speed.modify(this), (Expr<V>) sayTextAction.pitch.modify(this));
    }

    @Override
    default Phrase<V> visitSerialWriteAction(SerialWriteAction<Phrase<V>> serialWriteAction) {
        return new SerialWriteAction<>(serialWriteAction.getProperty(), serialWriteAction.getComment(), (Expr<V>) serialWriteAction.value.modify(this));
    }

    @Override
    default Phrase<V> visitPinWriteValueAction(PinWriteValueAction<Phrase<V>> pinWriteValueAction) {
        return new PinWriteValueAction<>(pinWriteValueAction.pinValue, pinWriteValueAction.port, (Expr<V>) pinWriteValueAction.value.modify(this), true, pinWriteValueAction.getProperty(), pinWriteValueAction.getComment());
    }

    @Override
    default Phrase<V> visitNumConst(NumConst<Phrase<V>> numConst) {
        return new NumConst<>(numConst.getProperty(), numConst.getComment(), numConst.value);
    }

    @Override
    default Phrase<V> visitMathConst(MathConst<Phrase<V>> mathConst) {
        return new MathConst<V>(mathConst.getProperty(), mathConst.getComment(), mathConst.mathConst);
    }

    @Override
    default Phrase<V> visitBoolConst(BoolConst<Phrase<V>> boolConst) {
        return new BoolConst<>(boolConst.getProperty(), boolConst.getComment(), boolConst.value);
    }

    @Override
    default Phrase<V> visitStringConst(StringConst<Phrase<V>> stringConst) {
        return new StringConst<V>(stringConst.getProperty(), stringConst.getComment(), stringConst.value);
    }

    @Override
    default Phrase<V> visitNullConst(NullConst<Phrase<V>> nullConst) {
        return new NullConst<V>(nullConst.getProperty(), nullConst.getComment());
    }

    @Override
    default Phrase<V> visitColorConst(ColorConst<Phrase<V>> colorConst) {
        return new ColorConst<>(colorConst.getProperty(), colorConst.getComment(), colorConst.getHexValueAsString());
    }

    @Override
    default Phrase<V> visitRgbColor(RgbColor<Phrase<V>> rgbColor) {
        return new RgbColor(rgbColor.getProperty(), rgbColor.getComment(), (Expr<V>) rgbColor.R.modify(this), (Expr<V>) rgbColor.G.modify(this), (Expr<V>) rgbColor.B.modify(this), (Expr<V>) rgbColor.A.modify(this));
    }

    @Override
    default Phrase<V> visitStmtTextComment(StmtTextComment<Phrase<V>> stmtTextComment) {
        return new StmtTextComment<V>(stmtTextComment.getProperty(), stmtTextComment.getComment(), stmtTextComment.textComment);
    }

    @Override
    default Phrase<V> visitConnectConst(ConnectConst<Phrase<V>> connectConst) {
        return new ConnectConst<V>(connectConst.getProperty(), connectConst.getComment(), connectConst.value, connectConst.value);
    }

    @Override
    default Phrase<V> visitVar(Var<Phrase<V>> var) {
        return new Var<>(var.getVarType(), var.name, var.getProperty(), var.getComment());
    }

    @Override
    default Phrase<V> visitVarDeclaration(VarDeclaration<Phrase<V>> var) {
        return new VarDeclaration<>(var.typeVar, var.name, (Expr<V>) var.value.modify(this), var.next, var.global, var.getProperty(), var.getComment());
    }

    @Override
    default Phrase<V> visitUnary(Unary<Phrase<V>> unary) {
        return new Unary<V>(unary.op, (Expr<V>) unary.expr.modify(this), unary.getProperty(), unary.getComment());
    }

    @Override
    default Phrase<V> visitBinary(Binary<Phrase<V>> binary) {
        return new Binary<>(binary.op, (Expr<V>) binary.left.modify(this), (Expr<V>) binary.getRight().modify(this), binary.operationRange, binary.getProperty(), binary.getComment());
    }

    @Override
    default Phrase<V> visitMathPowerFunct(MathPowerFunct<Phrase<V>> mathPowerFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathPowerFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathPowerFunct<V>(mathPowerFunct.getProperty(), mathPowerFunct.getComment(), mathPowerFunct.functName, newParam);
    }

    @Override
    default Phrase<V> visitEmptyList(EmptyList<Phrase<V>> emptyList) {
        return new EmptyList<V>(emptyList.getProperty(), emptyList.getComment(), emptyList.typeVar);
    }

    @Override
    default Phrase<V> visitAssignStmt(AssignStmt<Phrase<V>> assignStmt) {
        return new AssignStmt<V>((Var<V>) assignStmt.name.modify(this), (Expr<V>) assignStmt.expr.modify(this), assignStmt.getProperty(), assignStmt.getComment());
    }

    @Override
    default Phrase<V> visitEmptyExpr(EmptyExpr<Phrase<V>> emptyExpr) {
        return new EmptyExpr<V>(emptyExpr.getDefVal());
    }

    @Override
    default Phrase<V> visitExprList(ExprList<Phrase<V>> exprList) {
        ExprList<V> newExprList = new ExprList<V>();
        exprList.get().forEach(phraseExpr -> newExprList.addExpr((Expr<V>) phraseExpr.modify(this)));
        newExprList.setReadOnly();
        return newExprList;
    }

    @Override
    default Phrase<V> visitIfStmt(IfStmt<Phrase<V>> ifStmt) {
        List<Expr<V>> newExpr = new ArrayList<>();
        ifStmt.expr.forEach(phraseExpr -> newExpr.add((Expr<V>) phraseExpr.modify(this)));
        List<StmtList<V>> newThenList = new ArrayList<>();
        ifStmt.thenList.forEach(phraseStmtList -> newThenList.add((StmtList<V>) phraseStmtList.modify(this)));
        StmtList<V> newElseList = (StmtList<V>) ifStmt.elseList.modify(this);

        return new IfStmt<V>(newExpr, newThenList, newElseList, ifStmt.getProperty(), ifStmt.getComment(), ifStmt._else, ifStmt._elseIf);
    }

    default Phrase<V> visitTernaryExpr(TernaryExpr<Phrase<V>> ternaryExpr) {
        Expr<V> condition = (Expr<V>) ternaryExpr.condition.modify(this);
        Expr<V> thenPart = (Expr<V>) ternaryExpr.thenPart.modify(this);
        Expr<V> elsePart = (Expr<V>) ternaryExpr.elsePart.modify(this);

        return new TernaryExpr<V>(ternaryExpr.getProperty(), ternaryExpr.getComment(), condition, thenPart, elsePart);
    }

    @Override
    default Phrase<V> visitNNStepStmt(NNStepStmt<Phrase<V>> nnStepStmt) {
        StmtList<V> newIoNeurons = new StmtList<V>();
        for ( Stmt<Phrase<V>> e : nnStepStmt.getIoNeurons().get() ) {
            newIoNeurons.get().add((Stmt<V>) e.modify(this));
        }
        return new NNStepStmt<>(nnStepStmt.netDefinition, newIoNeurons, nnStepStmt.getProperty(), nnStepStmt.getComment());
    }

    @Override
    default Phrase<V> visitNNInputNeuronStmt(NNInputNeuronStmt<Phrase<V>> inNeuron) {
        return new NNInputNeuronStmt<V>(inNeuron.getProperty(), inNeuron.getComment(), inNeuron.name, (Expr<V>) inNeuron.modify(this));
    }

    @Override
    default Phrase<V> visitNNOutputNeuronStmt(NNOutputNeuronStmt<Phrase<V>> outNeuron) {
        return new NNOutputNeuronStmt<V>(outNeuron.getProperty(), outNeuron.getComment(), outNeuron.name, (Expr<V>) outNeuron.modify(this));
    }

    @Override
    default Phrase<V> visitNNOutputNeuronWoVarStmt(NNOutputNeuronWoVarStmt<Phrase<V>> outNeuron) {
        return new NNOutputNeuronWoVarStmt<V>(outNeuron.getProperty(), outNeuron.getComment(), outNeuron.name);
    }

    @Override
    default Phrase<V> visitNNChangeWeightStmt(NNChangeWeightStmt chgStmt) {
        return new NNChangeWeightStmt<V>(chgStmt.getProperty(), chgStmt.getComment(), chgStmt.from, chgStmt.to, chgStmt.change, (Expr<V>) chgStmt.modify(this));
    }

    @Override
    default Phrase<V> visitNNChangeBiasStmt(NNChangeBiasStmt chgStmt) {
        return new NNChangeBiasStmt<V>(chgStmt.getProperty(), chgStmt.getComment(), chgStmt.name, chgStmt.change, (Expr<V>) chgStmt.modify(this));
    }

    @Override
    default Phrase<V> visitNNGetOutputNeuronVal(NNGetOutputNeuronVal getStmt) {
        return new NNGetOutputNeuronVal<>(getStmt.getProperty(), getStmt.getComment(), getStmt.name);
    }

    @Override
    default Phrase<V> visitRepeatStmt(RepeatStmt<Phrase<V>> repeatStmt) {
        return new RepeatStmt<>(repeatStmt.mode, (Expr<V>) repeatStmt.expr.modify(this), (StmtList<V>) repeatStmt.list.modify(this), repeatStmt.getProperty(), repeatStmt.getComment());
    }

    @Override
    default Phrase<V> visitStmtFlowCon(StmtFlowCon<Phrase<V>> stmtFlowCon) {
        return new StmtFlowCon<V>(stmtFlowCon.getProperty(), stmtFlowCon.getComment(), stmtFlowCon.flow);
    }

    @Override
    default Phrase<V> visitStmtList(StmtList<Phrase<V>> stmtList) {
        StmtList<V> newPhrase = new StmtList<V>();
        stmtList.get().forEach(stmt -> newPhrase.addStmt((Stmt<V>) stmt.modify(this)));
        newPhrase.setReadOnly();
        return newPhrase;
    }

    @Override
    default Phrase<V> visitMainTask(MainTask<Phrase<V>> mainTask) {
        return new MainTask<V>((StmtList<V>) mainTask.variables.modify(this), mainTask.debug, mainTask.getProperty(), mainTask.getComment());
    }

    @Override
    default Phrase<V> visitWaitStmt(WaitStmt<Phrase<V>> waitStmt) {
        return new WaitStmt<>((StmtList<V>) waitStmt.statements.modify(this), waitStmt.getProperty(), waitStmt.getComment());
    }

    @Override
    default Phrase<V> visitWaitTimeStmt(WaitTimeStmt<Phrase<V>> waitTimeStmt) {
        return new WaitTimeStmt<>(waitTimeStmt.getProperty(), waitTimeStmt.getComment(), (Expr<V>) waitTimeStmt.time.modify(this));
    }

    @Override
    default Phrase<V> visitTextPrintFunct(TextPrintFunct<Phrase<V>> textPrintFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textPrintFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new TextPrintFunct<V>(newParam, textPrintFunct.getProperty(), textPrintFunct.getComment());
    }

    @Override
    default Phrase<V> visitGetSubFunct(GetSubFunct<Phrase<V>> getSubFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        getSubFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new GetSubFunct<V>(getSubFunct.functName, getSubFunct.strParam, newParam, getSubFunct.getProperty(), getSubFunct.getComment());
    }

    @Override
    default Phrase<V> visitIndexOfFunct(IndexOfFunct<Phrase<V>> indexOfFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        indexOfFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new IndexOfFunct<V>(indexOfFunct.location, newParam, indexOfFunct.getProperty(), indexOfFunct.getComment());
    }

    @Override
    default Phrase<V> visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Phrase<V>> lengthOfIsEmptyFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        lengthOfIsEmptyFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new LengthOfIsEmptyFunct<V>(lengthOfIsEmptyFunct.functName, newParam, lengthOfIsEmptyFunct.getProperty(), lengthOfIsEmptyFunct.getComment());
    }

    @Override
    default Phrase<V> visitListCreate(ListCreate<Phrase<V>> listCreate) {
        return new ListCreate<V>(listCreate.typeVar, (ExprList<V>) listCreate.exprList.modify(this), listCreate.getProperty(), listCreate.getComment());
    }

    @Override
    default Phrase<V> visitListGetIndex(ListGetIndex<Phrase<V>> listGetIndex) {
        List<Expr<V>> newParam = new ArrayList<>();
        listGetIndex.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new ListGetIndex<>(listGetIndex.getElementOperation(), listGetIndex.location, newParam, listGetIndex.dataType, listGetIndex.getProperty(), listGetIndex.getComment());
    }

    @Override
    default Phrase<V> visitListRepeat(ListRepeat<Phrase<V>> listRepeat) {
        List<Expr<V>> newParam = new ArrayList<>();
        listRepeat.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new ListRepeat<V>(listRepeat.typeVar, newParam, listRepeat.getProperty(), listRepeat.getComment());
    }

    @Override
    default Phrase<V> visitListSetIndex(ListSetIndex<Phrase<V>> listSetIndex) {
        List<Expr<V>> newParam = new ArrayList<>();
        listSetIndex.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new ListSetIndex<V>(listSetIndex.mode, listSetIndex.location, newParam, listSetIndex.getProperty(), listSetIndex.getComment());
    }

    @Override
    default Phrase<V> visitMathConstrainFunct(MathConstrainFunct<Phrase<V>> mathConstrainFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathConstrainFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathConstrainFunct<V>(newParam, mathConstrainFunct.getProperty(), mathConstrainFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathNumPropFunct(MathNumPropFunct<Phrase<V>> mathNumPropFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathNumPropFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathNumPropFunct<V>(mathNumPropFunct.functName, newParam, mathNumPropFunct.getProperty(), mathNumPropFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathOnListFunct(MathOnListFunct<Phrase<V>> mathOnListFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathOnListFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathOnListFunct<V>(mathOnListFunct.functName, newParam, mathOnListFunct.getProperty(), mathOnListFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathRandomFloatFunct(MathRandomFloatFunct<Phrase<V>> mathRandomFloatFunct) {
        return new MathRandomFloatFunct<V>(mathRandomFloatFunct.getProperty(), mathRandomFloatFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathRandomIntFunct(MathRandomIntFunct<Phrase<V>> mathRandomIntFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathRandomIntFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathRandomIntFunct<V>(newParam, mathRandomIntFunct.getProperty(), mathRandomIntFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathSingleFunct(MathSingleFunct<Phrase<V>> mathSingleFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathSingleFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathSingleFunct<V>(mathSingleFunct.functName, newParam, mathSingleFunct.getProperty(), mathSingleFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathCastStringFunct(MathCastStringFunct<Phrase<V>> mathCastStringFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathCastStringFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathCastStringFunct<V>(newParam, mathCastStringFunct.getProperty(), mathCastStringFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathCastCharFunct(MathCastCharFunct<Phrase<V>> mathCastCharFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathCastCharFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathCastStringFunct<V>(newParam, mathCastCharFunct.getProperty(), mathCastCharFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextStringCastNumberFunct(TextStringCastNumberFunct<Phrase<V>> textStringCastNumberFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textStringCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathCastStringFunct<V>(newParam, textStringCastNumberFunct.getProperty(), textStringCastNumberFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextCharCastNumberFunct(TextCharCastNumberFunct<Phrase<V>> textCharCastNumberFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textCharCastNumberFunct.param.forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return new MathCastStringFunct<V>(newParam, textCharCastNumberFunct.getProperty(), textCharCastNumberFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextJoinFunct(TextJoinFunct<Phrase<V>> textJoinFunct) {
        return new TextJoinFunct<V>((ExprList<V>) textJoinFunct.param.modify(this), textJoinFunct.getProperty(), textJoinFunct.getComment());
    }

    @Override
    default Phrase<V> visitMethodVoid(MethodVoid<Phrase<V>> methodVoid) {
        return new MethodVoid<V>(methodVoid.getMethodName(), (ExprList<V>) methodVoid.getParameters().modify(this), (StmtList<V>) methodVoid.body.modify(this), methodVoid.getProperty(), methodVoid.getComment());
    }

    @Override
    default Phrase<V> visitMethodReturn(MethodReturn<Phrase<V>> methodReturn) {
        return new MethodReturn<V>(methodReturn.getMethodName(), (ExprList<V>) methodReturn.getParameters().modify(this), (StmtList<V>) methodReturn.body.modify(this), methodReturn.getReturnType(), (Expr<V>) methodReturn.returnValue.modify(this), methodReturn.getProperty(), methodReturn.getComment());
    }

    @Override
    default Phrase<V> visitMethodIfReturn(MethodIfReturn<Phrase<V>> methodIfReturn) {
        return new MethodIfReturn<>((Expr<V>) methodIfReturn.oraCondition.modify(this), methodIfReturn.getReturnType(), (Expr<V>) methodIfReturn.oraReturnValue.modify(this), methodIfReturn.getValue(), methodIfReturn.getProperty(), methodIfReturn.getComment());
    }

    @Override
    default Phrase<V> visitMethodStmt(MethodStmt<Phrase<V>> methodStmt) {
        return new MethodStmt<V>((Method<V>) methodStmt.method.modify(this));
    }

    @Override
    default Phrase<V> visitMethodCall(MethodCall<Phrase<V>> methodCall) {
        return new MethodCall<>(methodCall.getMethodName(), (ExprList<V>) methodCall.getParameters().modify(this), (ExprList<V>) methodCall.getParametersValues().modify(this), methodCall.getReturnType(), methodCall.getProperty(), methodCall.getComment());
    }

    @Override
    default Phrase<V> visitSensorExpr(SensorExpr<Phrase<V>> sensorExpr) {
        return new SensorExpr<V>((Sensor<V>) sensorExpr.sensor.modify(this));
    }

    @Override
    default Phrase<V> visitMethodExpr(MethodExpr<Phrase<V>> methodExpr) {
        return new MethodExpr<>((Method<V>) methodExpr.getMethod().modify(this));
    }

    @Override
    default Phrase<V> visitActionStmt(ActionStmt<Phrase<V>> actionStmt) {
        return new ActionStmt<V>((Action<V>) actionStmt.action.modify(this));
    }

    @Override
    default Phrase<V> visitActionExpr(ActionExpr<Phrase<V>> actionExpr) {
        return new ActionExpr<V>((Action<V>) actionExpr.action.modify(this));
    }

    @Override
    default Phrase<V> visitExprStmt(ExprStmt<Phrase<V>> exprStmt) {
        return new ExprStmt<V>((Expr<V>) exprStmt.expr.modify(this));
    }

    @Override
    default Phrase<V> visitStmtExpr(StmtExpr<Phrase<V>> stmtExpr) {
        return new StmtExpr<>((Stmt<V>) stmtExpr.stmt.modify(this));
    }

    @Override
    default Phrase<V> visitShadowExpr(ShadowExpr<Phrase<V>> shadowExpr) {
        return new ShadowExpr<V>((Expr<V>) shadowExpr.shadow.modify(this), (Expr<V>) shadowExpr.block.modify(this));
    }

    @Override
    default Phrase<V> visitSensorStmt(SensorStmt<Phrase<V>> sensorStmt) {
        return new SensorStmt<V>((Sensor<V>) sensorStmt.sensor.modify(this));
    }

    @Override
    default Phrase<V> visitActivityTask(ActivityTask<Phrase<V>> activityTask) {
        return new ActivityTask<>(activityTask.getProperty(), activityTask.getComment(), (Expr<V>) activityTask.activityName.modify(this));
    }

    @Override
    default Phrase<V> visitStartActivityTask(StartActivityTask<Phrase<V>> startActivityTask) {
        return new StartActivityTask<V>(startActivityTask.getProperty(), startActivityTask.getComment(), (Expr<V>) startActivityTask.activityName.modify(this));
    }

    @Override
    default Phrase<V> visitLocation(Location<Phrase<V>> location) {
        return new Location<V>(location.x, location.y);
    }

    @Override
    default Phrase<V> visitFunctionStmt(FunctionStmt<Phrase<V>> functionStmt) {
        return new FunctionStmt<V>((Function<V>) functionStmt.function.modify(this));
    }

    @Override
    default Phrase<V> visitFunctionExpr(FunctionExpr<Phrase<V>> functionExpr) {
        return new FunctionExpr<V>((Function<V>) functionExpr.getFunction().modify(this));
    }

    @Override
    default Phrase<V> visitEvalExpr(EvalExpr<Phrase<V>> evalExpr) {
        try {
            return EvalExpr.make(evalExpr.expr, evalExpr.type, evalExpr.getProperty(), evalExpr.getComment());
        } catch ( Exception e ) {
            throw new DbcException("Could not modify EvalExpr!", e);
        }
    }

    @Override
    default Phrase<V> visitAssertStmt(AssertStmt<Phrase<V>> assertStmt) {
        return new AssertStmt<>(assertStmt.getProperty(), assertStmt.getComment(), (Expr<V>) assertStmt.asserts.modify(this), assertStmt.msg);
    }

    @Override
    default Phrase<V> visitDebugAction(DebugAction<Phrase<V>> debugAction) {
        return new DebugAction<>(debugAction.getProperty(), debugAction.getComment(), (Expr<V>) debugAction.value.modify(this));
    }

    @Override
    default Phrase<V> visitBluetoothReceiveAction(BluetoothReceiveAction<Phrase<V>> bluetoothReceiveAction) {
        return new BluetoothReceiveAction<V>(bluetoothReceiveAction.dataValue, (Expr<V>) bluetoothReceiveAction.connection.modify(this), bluetoothReceiveAction.channel, bluetoothReceiveAction.dataType, bluetoothReceiveAction.getProperty(), bluetoothReceiveAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothConnectAction(BluetoothConnectAction<Phrase<V>> bluetoothConnectAction) {
        return new BluetoothConnectAction<V>(bluetoothConnectAction.getProperty(), bluetoothConnectAction.getComment(), (Expr<V>) bluetoothConnectAction.address.modify(this));
    }

    @Override
    default Phrase<V> visitBluetoothSendAction(BluetoothSendAction<Phrase<V>> bluetoothSendAction) {
        return new BluetoothSendAction<>(bluetoothSendAction.dataValue, (Expr<V>) bluetoothSendAction.connection.modify(this), (Expr<V>) bluetoothSendAction.msg.modify(this), bluetoothSendAction.channel, bluetoothSendAction.dataType, bluetoothSendAction.getProperty(), bluetoothSendAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Phrase<V>> bluetoothWaitForConnection) {
        return new BluetoothWaitForConnectionAction<V>(bluetoothWaitForConnection.getProperty(), bluetoothWaitForConnection.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Phrase<V>> bluetoothCheckConnectAction) {
        return new BluetoothCheckConnectAction<V>(bluetoothCheckConnectAction.getProperty(), bluetoothCheckConnectAction.getComment(), (Expr<V>) bluetoothCheckConnectAction.connection.modify(this));
    }

    @Override
    default Phrase<V> visitKeysSensor(KeysSensor<Phrase<V>> keysSensor) {
        return new KeysSensor<V>(keysSensor.getProperty(), keysSensor.getComment(), new ExternalSensorBean(keysSensor.getUserDefinedPort(), keysSensor.getMode(), keysSensor.getSlot(), keysSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitColorSensor(ColorSensor<Phrase<V>> colorSensor) {
        return new ColorSensor<V>(colorSensor.getProperty(), colorSensor.getComment(), new ExternalSensorBean(colorSensor.getUserDefinedPort(), colorSensor.getMode(), colorSensor.getSlot(), colorSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitLightSensor(LightSensor<Phrase<V>> lightSensor) {
        return new LightSensor<V>(lightSensor.getProperty(), lightSensor.getComment(), new ExternalSensorBean(lightSensor.getUserDefinedPort(), lightSensor.getMode(), lightSensor.getSlot(), lightSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitSoundSensor(SoundSensor<Phrase<V>> soundSensor) {
        return new SoundSensor<V>(soundSensor.getProperty(), soundSensor.getComment(), new ExternalSensorBean(soundSensor.getUserDefinedPort(), soundSensor.getMode(), soundSensor.getSlot(), soundSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitEncoderSensor(EncoderSensor<Phrase<V>> encoderSensor) {
        return new EncoderSensor<V>(encoderSensor.getProperty(), encoderSensor.getComment(), new ExternalSensorBean(encoderSensor.getUserDefinedPort(), encoderSensor.getMode(), encoderSensor.getSlot(), encoderSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitGyroSensor(GyroSensor<Phrase<V>> gyroSensor) {
        return new GyroSensor<>(gyroSensor.getProperty(), gyroSensor.getComment(), new ExternalSensorBean(gyroSensor.getUserDefinedPort(), gyroSensor.getMode(), gyroSensor.getSlot(), gyroSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitInfraredSensor(InfraredSensor<Phrase<V>> infraredSensor) {
        return new InfraredSensor<V>(infraredSensor.getProperty(), infraredSensor.getComment(), new ExternalSensorBean(infraredSensor.getUserDefinedPort(), infraredSensor.getMode(), infraredSensor.getSlot(), infraredSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitTimerSensor(TimerSensor<Phrase<V>> timerSensor) {
        return new TimerSensor<>(timerSensor.getProperty(), timerSensor.getComment(), new ExternalSensorBean(timerSensor.getUserDefinedPort(), timerSensor.getMode(), timerSensor.getSlot(), timerSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitTouchSensor(TouchSensor<Phrase<V>> touchSensor) {
        return new TouchSensor<V>(touchSensor.getProperty(), touchSensor.getComment(), new ExternalSensorBean(touchSensor.getUserDefinedPort(), touchSensor.getMode(), touchSensor.getSlot(), touchSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitUltrasonicSensor(UltrasonicSensor<Phrase<V>> ultrasonicSensor) {
        return new UltrasonicSensor<V>(ultrasonicSensor.getProperty(), ultrasonicSensor.getComment(), new ExternalSensorBean(ultrasonicSensor.getUserDefinedPort(), ultrasonicSensor.getMode(), ultrasonicSensor.getSlot(), ultrasonicSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitCompassSensor(CompassSensor<Phrase<V>> compassSensor) {
        return new CompassSensor<>(compassSensor.getProperty(), compassSensor.getComment(), new ExternalSensorBean(compassSensor.getUserDefinedPort(), compassSensor.getMode(), compassSensor.getSlot(), compassSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitTemperatureSensor(TemperatureSensor<Phrase<V>> temperatureSensor) {
        return new TemperatureSensor<V>(temperatureSensor.getProperty(), temperatureSensor.getComment(),
            new ExternalSensorBean(
                temperatureSensor.getUserDefinedPort(),
                temperatureSensor.getMode(),
                temperatureSensor.getSlot(),
                temperatureSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitVoltageSensor(VoltageSensor<Phrase<V>> voltageSensor) {
        return new VoltageSensor<V>(voltageSensor.getProperty(), voltageSensor.getComment(), new ExternalSensorBean(voltageSensor.getUserDefinedPort(), voltageSensor.getMode(), voltageSensor.getSlot(), voltageSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitAccelerometerSensor(AccelerometerSensor<Phrase<V>> accelerometerSensor) {
        return new AccelerometerSensor<V>(accelerometerSensor.getProperty(), accelerometerSensor.getComment(),
            new ExternalSensorBean(
                accelerometerSensor.getUserDefinedPort(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getSlot(),
                accelerometerSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitPinTouchSensor(PinTouchSensor<Phrase<V>> pinTouchSensor) {
        return new PinTouchSensor<V>(pinTouchSensor.getProperty(), pinTouchSensor.getComment(), new ExternalSensorBean(pinTouchSensor.getUserDefinedPort(), pinTouchSensor.getMode(), pinTouchSensor.getSlot(), pinTouchSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitGestureSensor(GestureSensor<Phrase<V>> gestureSensor) {
        return new GestureSensor<V>(gestureSensor.getProperty(), gestureSensor.getComment(), new ExternalSensorBean(gestureSensor.getUserDefinedPort(), gestureSensor.getMode(), gestureSensor.getSlot(), gestureSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitPinGetValueSensor(PinGetValueSensor<Phrase<V>> pinGetValueSensor) {
        return new PinGetValueSensor<V>(pinGetValueSensor.getProperty(), pinGetValueSensor.getComment(),
            new ExternalSensorBean(
                pinGetValueSensor.getUserDefinedPort(),
                pinGetValueSensor.getMode(),
                pinGetValueSensor.getSlot(),
                pinGetValueSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitGetSampleSensor(GetSampleSensor<Phrase<V>> sensorGetSample) {
        return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, sensorGetSample.sensorPort, sensorGetSample.slot, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), sensorGetSample.getComment(), getBlocklyDropdownFactory());
    }

    @Override
    default Phrase<V> visitIRSeekerSensor(IRSeekerSensor<Phrase<V>> irSeekerSensor) {
        return new IRSeekerSensor<V>(irSeekerSensor.getProperty(), irSeekerSensor.getComment(), new ExternalSensorBean(irSeekerSensor.getUserDefinedPort(), irSeekerSensor.getMode(), irSeekerSensor.getSlot(), irSeekerSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitMoistureSensor(MoistureSensor<Phrase<V>> moistureSensor) {
        return new MoistureSensor<V>(moistureSensor.getProperty(), moistureSensor.getComment(), new ExternalSensorBean(moistureSensor.getUserDefinedPort(), moistureSensor.getMode(), moistureSensor.getSlot(), moistureSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitHumiditySensor(HumiditySensor<Phrase<V>> humiditySensor) {
        return new HumiditySensor<V>(humiditySensor.getProperty(), humiditySensor.getComment(), new ExternalSensorBean(humiditySensor.getUserDefinedPort(), humiditySensor.getMode(), humiditySensor.getSlot(), humiditySensor.getMutation()));
    }

    @Override
    default Phrase<V> visitMotionSensor(MotionSensor<Phrase<V>> motionSensor) {
        return new MotionSensor<V>(motionSensor.getProperty(), motionSensor.getComment(), new ExternalSensorBean(motionSensor.getUserDefinedPort(), motionSensor.getMode(), motionSensor.getSlot(), motionSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitDropSensor(DropSensor<Phrase<V>> dropSensor) {
        return new DropSensor<V>(dropSensor.getProperty(), dropSensor.getComment(), new ExternalSensorBean(dropSensor.getUserDefinedPort(), dropSensor.getMode(), dropSensor.getSlot(), dropSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitPulseSensor(PulseSensor<Phrase<V>> pulseSensor) {
        return new PulseSensor<V>(pulseSensor.getProperty(), pulseSensor.getComment(), new ExternalSensorBean(pulseSensor.getUserDefinedPort(), pulseSensor.getMode(), pulseSensor.getSlot(), pulseSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitRfidSensor(RfidSensor<Phrase<V>> rfidSensor) {
        return new RfidSensor<V>(rfidSensor.getProperty(), rfidSensor.getComment(), new ExternalSensorBean(rfidSensor.getUserDefinedPort(), rfidSensor.getMode(), rfidSensor.getSlot(), rfidSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitVemlLightSensor(VemlLightSensor<Phrase<V>> vemlLightSensor) {
        return new VemlLightSensor<V>(vemlLightSensor.getProperty(), vemlLightSensor.getComment(), new ExternalSensorBean(vemlLightSensor.getUserDefinedPort(), vemlLightSensor.getMode(), vemlLightSensor.getSlot(), vemlLightSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitParticleSensor(ParticleSensor<Phrase<V>> particleSensor) {
        return new ParticleSensor<V>(particleSensor.getProperty(), particleSensor.getComment(), new ExternalSensorBean(particleSensor.getUserDefinedPort(), particleSensor.getMode(), particleSensor.getSlot(), particleSensor.getMutation()));
    }

    @Override
    default Phrase<V> visitHTColorSensor(HTColorSensor<Phrase<V>> htColorSensor) {
        return new HTColorSensor<V>(htColorSensor.getProperty(), htColorSensor.getComment(), new ExternalSensorBean(htColorSensor.getUserDefinedPort(), htColorSensor.getMode(), htColorSensor.getSlot(), htColorSensor.getMutation()));
    }

    // Helper methods

    default MotionParam<V> modifyMotionParam(MotionParam<Phrase<V>> param) {
        MotionParam.Builder<V> motionParamBuilder = new MotionParam.Builder<>();
        motionParamBuilder.speed((Expr<V>) param.getSpeed().modify(this));
        if ( param.getDuration() != null ) { // duration may be null
            motionParamBuilder.duration(new MotorDuration<>(param.getDuration().getType(), (Expr<V>) param.getDuration().getValue().modify(this)));
        }
        return motionParamBuilder.build();
    }
}

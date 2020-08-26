package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
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
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.actor.IAllActorsVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * A visitor to be used to potentially modify all phrases in the AST. Every visit implementation should return a new phrase using the static make method of the
 * individual phrase, or even other phrases. All subphrases should be called with {@link Phrase#modify(ITransformerVisitor)} )} to ensure all leaves are
 * visited.
 */
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
        return DriveAction.make(driveAction.getDirection(), modifyMotionParam(driveAction.getParam()), driveAction.getProperty(), driveAction.getComment());
    }

    @Override
    default Phrase<V> visitCurveAction(CurveAction<Phrase<V>> curveAction) {
        return CurveAction
            .make(
                curveAction.getDirection(),
                modifyMotionParam(curveAction.getParamLeft()),
                modifyMotionParam(curveAction.getParamRight()),
                curveAction.getProperty(),
                curveAction.getComment());
    }

    @Override
    default Phrase<V> visitTurnAction(TurnAction<Phrase<V>> turnAction) {
        return TurnAction.make(turnAction.getDirection(), modifyMotionParam(turnAction.getParam()), turnAction.getProperty(), turnAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorDriveStopAction(MotorDriveStopAction<Phrase<V>> stopAction) {
        return MotorDriveStopAction.make(stopAction.getProperty(), stopAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorGetPowerAction(MotorGetPowerAction<Phrase<V>> motorGetPowerAction) {
        return MotorGetPowerAction.make(motorGetPowerAction.getUserDefinedPort(), motorGetPowerAction.getProperty(), motorGetPowerAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorOnAction(MotorOnAction<Phrase<V>> motorOnAction) {
        return MotorOnAction
            .make(motorOnAction.getUserDefinedPort(), modifyMotionParam(motorOnAction.getParam()), motorOnAction.getProperty(), motorOnAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorSetPowerAction(MotorSetPowerAction<Phrase<V>> motorSetPowerAction) {
        return MotorSetPowerAction
            .make(
                motorSetPowerAction.getUserDefinedPort(),
                (Expr<V>) motorSetPowerAction.getPower().modify(this),
                motorSetPowerAction.getProperty(),
                motorSetPowerAction.getComment());
    }

    @Override
    default Phrase<V> visitMotorStopAction(MotorStopAction<Phrase<V>> motorStopAction) {
        return MotorStopAction
            .make(motorStopAction.getUserDefinedPort(), motorStopAction.getMode(), motorStopAction.getProperty(), motorStopAction.getComment());
    }

    @Override
    default Phrase<V> visitClearDisplayAction(ClearDisplayAction<Phrase<V>> clearDisplayAction) {
        return ClearDisplayAction.make(clearDisplayAction.getPort(), clearDisplayAction.getProperty(), clearDisplayAction.getComment());
    }

    @Override
    default Phrase<V> visitShowTextAction(ShowTextAction<Phrase<V>> showTextAction) {
        return ShowTextAction
            .make(
                (Expr<V>) showTextAction.getMsg().modify(this),
                (Expr<V>) showTextAction.getX().modify(this),
                (Expr<V>) showTextAction.getY().modify(this),
                showTextAction.getPort(),
                showTextAction.getProperty(),
                showTextAction.getComment());
    }

    @Override
    default Phrase<V> visitLightAction(LightAction<Phrase<V>> lightAction) {
        return LightAction
            .make(
                lightAction.getPort(),
                lightAction.getColor(),
                lightAction.getMode(),
                (Expr<V>) lightAction.getRgbLedColor().modify(this),
                lightAction.getProperty(),
                lightAction.getComment());
    }

    @Override
    default Phrase<V> visitLightStatusAction(LightStatusAction<Phrase<V>> lightStatusAction) {
        return LightStatusAction
            .make(lightStatusAction.getPort(), lightStatusAction.getStatus(), lightStatusAction.getProperty(), lightStatusAction.getComment());
    }

    @Override
    default Phrase<V> visitToneAction(ToneAction<Phrase<V>> toneAction) {
        return ToneAction
            .make(
                (Expr<V>) toneAction.getFrequency().modify(this),
                (Expr<V>) toneAction.getDuration().modify(this),
                toneAction.getPort(),
                toneAction.getProperty(),
                toneAction.getComment());
    }

    @Override
    default Phrase<V> visitPlayNoteAction(PlayNoteAction<Phrase<V>> playNoteAction) {
        return PlayNoteAction
            .make(
                playNoteAction.getPort(),
                playNoteAction.getDuration(),
                playNoteAction.getFrequency(),
                playNoteAction.getProperty(),
                playNoteAction.getComment());
    }

    @Override
    default Phrase<V> visitVolumeAction(VolumeAction<Phrase<V>> volumeAction) {
        return VolumeAction
            .make(volumeAction.getMode(), (Expr<V>) volumeAction.getVolume().modify(this), volumeAction.getProperty(), volumeAction.getComment());
    }

    @Override
    default Phrase<V> visitPlayFileAction(PlayFileAction<Phrase<V>> playFileAction) {
        return PlayFileAction.make(playFileAction.getFileName(), playFileAction.getProperty(), playFileAction.getComment());
    }

    @Override
    default Phrase<V> visitSetLanguageAction(SetLanguageAction<Phrase<V>> setLanguageAction) {
        return SetLanguageAction.make(setLanguageAction.getLanguage(), setLanguageAction.getProperty(), setLanguageAction.getComment());
    }

    @Override
    default Phrase<V> visitSayTextAction(SayTextAction<Phrase<V>> sayTextAction) {
        return SayTextAction
            .make(
                (Expr<V>) sayTextAction.getMsg().modify(this),
                (Expr<V>) sayTextAction.getSpeed().modify(this),
                (Expr<V>) sayTextAction.getPitch().modify(this),
                sayTextAction.getProperty(),
                sayTextAction.getComment());
    }

    @Override
    default Phrase<V> visitSerialWriteAction(SerialWriteAction<Phrase<V>> serialWriteAction) {
        return SerialWriteAction.make((Expr<V>) serialWriteAction.getValue().modify(this), serialWriteAction.getProperty(), serialWriteAction.getComment());
    }

    @Override
    default Phrase<V> visitPinWriteValueAction(PinWriteValueAction<Phrase<V>> pinWriteValueAction) {
        return PinWriteValueAction
            .make(
                pinWriteValueAction.getMode(),
                pinWriteValueAction.getPort(),
                (Expr<V>) pinWriteValueAction.getValue().modify(this),
                true,
                pinWriteValueAction.getProperty(),
                pinWriteValueAction.getComment());
    }

    @Override
    default Phrase<V> visitNumConst(NumConst<Phrase<V>> numConst) {
        return NumConst.make(numConst.getValue(), numConst.getProperty(), numConst.getComment());
    }

    @Override
    default Phrase<V> visitMathConst(MathConst<Phrase<V>> mathConst) {
        return MathConst.make(mathConst.getMathConst(), mathConst.getProperty(), mathConst.getComment());
    }

    @Override
    default Phrase<V> visitBoolConst(BoolConst<Phrase<V>> boolConst) {
        return BoolConst.make(boolConst.getValue(), boolConst.getProperty(), boolConst.getComment());
    }

    @Override
    default Phrase<V> visitStringConst(StringConst<Phrase<V>> stringConst) {
        return StringConst.make(stringConst.getValue(), stringConst.getProperty(), stringConst.getComment());
    }

    @Override
    default Phrase<V> visitNullConst(NullConst<Phrase<V>> nullConst) {
        return NullConst.make(nullConst.getProperty(), nullConst.getComment());
    }

    @Override
    default Phrase<V> visitColorConst(ColorConst<Phrase<V>> colorConst) {
        return ColorConst.make(colorConst.getHexValueAsString(), colorConst.getProperty(), colorConst.getComment());
    }

    @Override
    default Phrase<V> visitRgbColor(RgbColor<Phrase<V>> rgbColor) {
        return RgbColor
            .make(
                (Expr<V>) rgbColor.getR().modify(this),
                (Expr<V>) rgbColor.getG().modify(this),
                (Expr<V>) rgbColor.getB().modify(this),
                (Expr<V>) rgbColor.getA().modify(this),
                rgbColor.getProperty(),
                rgbColor.getComment());
    }

    @Override
    default Phrase<V> visitStmtTextComment(StmtTextComment<Phrase<V>> stmtTextComment) {
        return StmtTextComment.make(stmtTextComment.getTextComment(), stmtTextComment.getProperty(), stmtTextComment.getComment());
    }

    @Override
    default Phrase<V> visitConnectConst(ConnectConst<Phrase<V>> connectConst) {
        return ConnectConst.make(connectConst.getValue(), connectConst.getValue(), connectConst.getProperty(), connectConst.getComment());
    }

    @Override
    default Phrase<V> visitVar(Var<Phrase<V>> var) {
        return Var.make(var.getVarType(), var.getValue(), var.getProperty(), var.getComment());
    }

    @Override
    default Phrase<V> visitVarDeclaration(VarDeclaration<Phrase<V>> var) {
        return VarDeclaration
            .make(var.getTypeVar(), var.getName(), (Expr<V>) var.getValue().modify(this), var.isNext(), var.isGlobal(), var.getProperty(), var.getComment());
    }

    @Override
    default Phrase<V> visitUnary(Unary<Phrase<V>> unary) {
        return Unary.make(unary.getOp(), (Expr<V>) unary.getExpr().modify(this), unary.getProperty(), unary.getComment());
    }

    @Override
    default Phrase<V> visitBinary(Binary<Phrase<V>> binary) {
        return Binary
            .make(
                binary.getOp(),
                (Expr<V>) binary.getLeft().modify(this),
                (Expr<V>) binary.getRight().modify(this),
                binary.getOperationRange(),
                binary.getProperty(),
                binary.getComment());
    }

    @Override
    default Phrase<V> visitMathPowerFunct(MathPowerFunct<Phrase<V>> mathPowerFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathPowerFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathPowerFunct.make(mathPowerFunct.getFunctName(), newParam, mathPowerFunct.getProperty(), mathPowerFunct.getComment());
    }

    @Override
    default Phrase<V> visitEmptyList(EmptyList<Phrase<V>> emptyList) {
        return EmptyList.make(emptyList.getTypeVar(), emptyList.getProperty(), emptyList.getComment());
    }

    @Override
    default Phrase<V> visitAssignStmt(AssignStmt<Phrase<V>> assignStmt) {
        return AssignStmt
            .make((Var<V>) assignStmt.getName().modify(this), (Expr<V>) assignStmt.getExpr().modify(this), assignStmt.getProperty(), assignStmt.getComment());
    }

    @Override
    default Phrase<V> visitEmptyExpr(EmptyExpr<Phrase<V>> emptyExpr) {
        return EmptyExpr.make(emptyExpr.getDefVal());
    }

    @Override
    default Phrase<V> visitExprList(ExprList<Phrase<V>> exprList) {
        ExprList<V> newExprList = ExprList.make();
        exprList.get().forEach(phraseExpr -> newExprList.addExpr((Expr<V>) phraseExpr.modify(this)));
        newExprList.setReadOnly();
        return newExprList;
    }

    @Override
    default Phrase<V> visitIfStmt(IfStmt<Phrase<V>> ifStmt) {
        List<Expr<V>> newExpr = new ArrayList<>();
        ifStmt.getExpr().forEach(phraseExpr -> newExpr.add((Expr<V>) phraseExpr.modify(this)));
        List<StmtList<V>> newThenList = new ArrayList<>();
        ifStmt.getThenList().forEach(phraseStmtList -> newThenList.add((StmtList<V>) phraseStmtList.modify(this)));
        StmtList<V> newElseList = (StmtList<V>) ifStmt.getElseList().modify(this);

        return IfStmt.make(newExpr, newThenList, newElseList, ifStmt.getProperty(), ifStmt.getComment(), ifStmt.get_else(), ifStmt.get_elseIf());
    }

    @Override
    default Phrase<V> visitNNStepStmt(NNStepStmt<Phrase<V>> nnStepStmt) {
        List<Expr<V>> newIl = new ArrayList<>();
        for ( Expr<Phrase<V>> e : nnStepStmt.getIl() ) {
            newIl.add((Expr<V>) e.modify(this));
        }
        List<Var<V>> newOl = new ArrayList<>();
        for ( Var<Phrase<V>> e : nnStepStmt.getOl() ) {
            newOl.add((Var<V>) e.modify(this));
        }
        return NNStepStmt.make(newIl, newOl, nnStepStmt.getProperty(), nnStepStmt.getComment());
    }

    @Override
    default Phrase<V> visitRepeatStmt(RepeatStmt<Phrase<V>> repeatStmt) {
        return RepeatStmt
            .make(
                repeatStmt.getMode(),
                (Expr<V>) repeatStmt.getExpr().modify(this),
                (StmtList<V>) repeatStmt.getList().modify(this),
                repeatStmt.getProperty(),
                repeatStmt.getComment());
    }

    @Override
    default Phrase<V> visitStmtFlowCon(StmtFlowCon<Phrase<V>> stmtFlowCon) {
        return StmtFlowCon.make(stmtFlowCon.getFlow(), stmtFlowCon.getProperty(), stmtFlowCon.getComment());
    }

    @Override
    default Phrase<V> visitStmtList(StmtList<Phrase<V>> stmtList) {
        StmtList<V> newPhrase = StmtList.make();
        stmtList.get().forEach(stmt -> newPhrase.addStmt((Stmt<V>) stmt.modify(this)));
        newPhrase.setReadOnly();
        return newPhrase;
    }

    @Override
    default Phrase<V> visitMainTask(MainTask<Phrase<V>> mainTask) {
        return MainTask.make((StmtList<V>) mainTask.getVariables().modify(this), mainTask.getDebug(), mainTask.getProperty(), mainTask.getComment());
    }

    @Override
    default Phrase<V> visitWaitStmt(WaitStmt<Phrase<V>> waitStmt) {
        return WaitStmt.make((StmtList<V>) waitStmt.getStatements().modify(this), waitStmt.getProperty(), waitStmt.getComment());
    }

    @Override
    default Phrase<V> visitWaitTimeStmt(WaitTimeStmt<Phrase<V>> waitTimeStmt) {
        return WaitTimeStmt.make((Expr<V>) waitTimeStmt.getTime().modify(this), waitTimeStmt.getProperty(), waitTimeStmt.getComment());
    }

    @Override
    default Phrase<V> visitTextPrintFunct(TextPrintFunct<Phrase<V>> textPrintFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textPrintFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return TextPrintFunct.make(newParam, textPrintFunct.getProperty(), textPrintFunct.getComment());
    }

    @Override
    default Phrase<V> visitGetSubFunct(GetSubFunct<Phrase<V>> getSubFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        getSubFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return GetSubFunct.make(getSubFunct.getFunctName(), getSubFunct.getStrParam(), newParam, getSubFunct.getProperty(), getSubFunct.getComment());
    }

    @Override
    default Phrase<V> visitIndexOfFunct(IndexOfFunct<Phrase<V>> indexOfFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        indexOfFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return IndexOfFunct.make(indexOfFunct.getLocation(), newParam, indexOfFunct.getProperty(), indexOfFunct.getComment());
    }

    @Override
    default Phrase<V> visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Phrase<V>> lengthOfIsEmptyFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        lengthOfIsEmptyFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return LengthOfIsEmptyFunct.make(lengthOfIsEmptyFunct.getFunctName(), newParam, lengthOfIsEmptyFunct.getProperty(), lengthOfIsEmptyFunct.getComment());
    }

    @Override
    default Phrase<V> visitListCreate(ListCreate<Phrase<V>> listCreate) {
        return ListCreate.make(listCreate.getTypeVar(), (ExprList<V>) listCreate.getValue().modify(this), listCreate.getProperty(), listCreate.getComment());
    }

    @Override
    default Phrase<V> visitListGetIndex(ListGetIndex<Phrase<V>> listGetIndex) {
        List<Expr<V>> newParam = new ArrayList<>();
        listGetIndex.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return ListGetIndex
            .make(
                listGetIndex.getElementOperation(),
                listGetIndex.getLocation(),
                newParam,
                listGetIndex.getDataType(),
                listGetIndex.getProperty(),
                listGetIndex.getComment());
    }

    @Override
    default Phrase<V> visitListRepeat(ListRepeat<Phrase<V>> listRepeat) {
        List<Expr<V>> newParam = new ArrayList<>();
        listRepeat.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return ListRepeat.make(listRepeat.getTypeVar(), newParam, listRepeat.getProperty(), listRepeat.getComment());
    }

    @Override
    default Phrase<V> visitListSetIndex(ListSetIndex<Phrase<V>> listSetIndex) {
        List<Expr<V>> newParam = new ArrayList<>();
        listSetIndex.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return ListSetIndex
            .make(listSetIndex.getElementOperation(), listSetIndex.getLocation(), newParam, listSetIndex.getProperty(), listSetIndex.getComment());
    }

    @Override
    default Phrase<V> visitMathConstrainFunct(MathConstrainFunct<Phrase<V>> mathConstrainFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathConstrainFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathConstrainFunct.make(newParam, mathConstrainFunct.getProperty(), mathConstrainFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathNumPropFunct(MathNumPropFunct<Phrase<V>> mathNumPropFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathNumPropFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathNumPropFunct.make(mathNumPropFunct.getFunctName(), newParam, mathNumPropFunct.getProperty(), mathNumPropFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathOnListFunct(MathOnListFunct<Phrase<V>> mathOnListFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathOnListFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathOnListFunct.make(mathOnListFunct.getFunctName(), newParam, mathOnListFunct.getProperty(), mathOnListFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathRandomFloatFunct(MathRandomFloatFunct<Phrase<V>> mathRandomFloatFunct) {
        return MathRandomFloatFunct.make(mathRandomFloatFunct.getProperty(), mathRandomFloatFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathRandomIntFunct(MathRandomIntFunct<Phrase<V>> mathRandomIntFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathRandomIntFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathRandomIntFunct.make(newParam, mathRandomIntFunct.getProperty(), mathRandomIntFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathSingleFunct(MathSingleFunct<Phrase<V>> mathSingleFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathSingleFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathSingleFunct.make(mathSingleFunct.getFunctName(), newParam, mathSingleFunct.getProperty(), mathSingleFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathCastStringFunct(MathCastStringFunct<Phrase<V>> mathCastStringFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathCastStringFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathCastStringFunct.make(newParam, mathCastStringFunct.getProperty(), mathCastStringFunct.getComment());
    }

    @Override
    default Phrase<V> visitMathCastCharFunct(MathCastCharFunct<Phrase<V>> mathCastCharFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        mathCastCharFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathCastStringFunct.make(newParam, mathCastCharFunct.getProperty(), mathCastCharFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextStringCastNumberFunct(TextStringCastNumberFunct<Phrase<V>> textStringCastNumberFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textStringCastNumberFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathCastStringFunct.make(newParam, textStringCastNumberFunct.getProperty(), textStringCastNumberFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextCharCastNumberFunct(TextCharCastNumberFunct<Phrase<V>> textCharCastNumberFunct) {
        List<Expr<V>> newParam = new ArrayList<>();
        textCharCastNumberFunct.getParam().forEach(phraseExpr -> newParam.add((Expr<V>) phraseExpr.modify(this)));
        return MathCastStringFunct.make(newParam, textCharCastNumberFunct.getProperty(), textCharCastNumberFunct.getComment());
    }

    @Override
    default Phrase<V> visitTextJoinFunct(TextJoinFunct<Phrase<V>> textJoinFunct) {
        return TextJoinFunct.make((ExprList<V>) textJoinFunct.getParam().modify(this), textJoinFunct.getProperty(), textJoinFunct.getComment());
    }

    @Override
    default Phrase<V> visitMethodVoid(MethodVoid<Phrase<V>> methodVoid) {
        return MethodVoid
            .make(
                methodVoid.getMethodName(),
                (ExprList<V>) methodVoid.getParameters().modify(this),
                (StmtList<V>) methodVoid.getBody().modify(this),
                methodVoid.getProperty(),
                methodVoid.getComment());
    }

    @Override
    default Phrase<V> visitMethodReturn(MethodReturn<Phrase<V>> methodReturn) {
        return MethodReturn
            .make(
                methodReturn.getMethodName(),
                (ExprList<V>) methodReturn.getParameters().modify(this),
                (StmtList<V>) methodReturn.getBody().modify(this),
                methodReturn.getReturnType(),
                (Expr<V>) methodReturn.getReturnValue().modify(this),
                methodReturn.getProperty(),
                methodReturn.getComment());
    }

    @Override
    default Phrase<V> visitMethodIfReturn(MethodIfReturn<Phrase<V>> methodIfReturn) {
        return MethodIfReturn
            .make(
                (Expr<V>) methodIfReturn.getCondition().modify(this),
                methodIfReturn.getReturnType(),
                (Expr<V>) methodIfReturn.getReturnValue().modify(this),
                methodIfReturn.getProperty(),
                methodIfReturn.getComment());
    }

    @Override
    default Phrase<V> visitMethodStmt(MethodStmt<Phrase<V>> methodStmt) {
        return MethodStmt.make((Method<V>) methodStmt.getMethod().modify(this));
    }

    @Override
    default Phrase<V> visitMethodCall(MethodCall<Phrase<V>> methodCall) {
        return MethodCall
            .make(
                methodCall.getMethodName(),
                (ExprList<V>) methodCall.getParameters().modify(this),
                (ExprList<V>) methodCall.getParametersValues().modify(this),
                methodCall.getReturnType(),
                methodCall.getProperty(),
                methodCall.getComment());
    }

    @Override
    default Phrase<V> visitSensorExpr(SensorExpr<Phrase<V>> sensorExpr) {
        return SensorExpr.make((Sensor<V>) sensorExpr.getSens().modify(this));
    }

    @Override
    default Phrase<V> visitMethodExpr(MethodExpr<Phrase<V>> methodExpr) {
        return MethodExpr.make((Method<V>) methodExpr.getMethod().modify(this));
    }

    @Override
    default Phrase<V> visitActionStmt(ActionStmt<Phrase<V>> actionStmt) {
        return ActionStmt.make((Action<V>) actionStmt.getAction().modify(this));
    }

    @Override
    default Phrase<V> visitActionExpr(ActionExpr<Phrase<V>> actionExpr) {
        return ActionExpr.make((Action<V>) actionExpr.getAction().modify(this));
    }

    @Override
    default Phrase<V> visitExprStmt(ExprStmt<Phrase<V>> exprStmt) {
        return ExprStmt.make((Expr<V>) exprStmt.getExpr().modify(this));
    }

    @Override
    default Phrase<V> visitStmtExpr(StmtExpr<Phrase<V>> stmtExpr) {
        return StmtExpr.make((Stmt<V>) stmtExpr.getStmt().modify(this));
    }

    @Override
    default Phrase<V> visitShadowExpr(ShadowExpr<Phrase<V>> shadowExpr) {
        return ShadowExpr.make((Expr<V>) shadowExpr.getShadow().modify(this), (Expr<V>) shadowExpr.getBlock().modify(this));
    }

    @Override
    default Phrase<V> visitSensorStmt(SensorStmt<Phrase<V>> sensorStmt) {
        return SensorStmt.make((Sensor<V>) sensorStmt.getSensor().modify(this));
    }

    @Override
    default Phrase<V> visitActivityTask(ActivityTask<Phrase<V>> activityTask) {
        return ActivityTask.make((Expr<V>) activityTask.getActivityName().modify(this), activityTask.getProperty(), activityTask.getComment());
    }

    @Override
    default Phrase<V> visitStartActivityTask(StartActivityTask<Phrase<V>> startActivityTask) {
        return StartActivityTask
            .make((Expr<V>) startActivityTask.getActivityName().modify(this), startActivityTask.getProperty(), startActivityTask.getComment());
    }

    @Override
    default Phrase<V> visitLocation(Location<Phrase<V>> location) {
        return Location.make(location.getX(), location.getY());
    }

    @Override
    default Phrase<V> visitFunctionStmt(FunctionStmt<Phrase<V>> functionStmt) {
        return FunctionStmt.make((Function<V>) functionStmt.getFunction().modify(this));
    }

    @Override
    default Phrase<V> visitFunctionExpr(FunctionExpr<Phrase<V>> functionExpr) {
        return FunctionExpr.make((Function<V>) functionExpr.getFunction().modify(this));
    }

    @Override
    default Phrase<V> visitEvalExpr(EvalExpr<Phrase<V>> evalExpr) {
        try {
            return EvalExpr.make(evalExpr.getExprStr(), evalExpr.getType(), evalExpr.getProperty(), evalExpr.getComment());
        } catch ( Exception e ) {
            throw new DbcException("Could not modify EvalExpr!", e);
        }
    }

    @Override
    default Phrase<V> visitAssertStmt(AssertStmt<Phrase<V>> assertStmt) {
        return AssertStmt.make((Expr<V>) assertStmt.getAssert().modify(this), assertStmt.getMsg(), assertStmt.getProperty(), assertStmt.getComment());
    }

    @Override
    default Phrase<V> visitDebugAction(DebugAction<Phrase<V>> debugAction) {
        return DebugAction.make((Expr<V>) debugAction.getValue().modify(this), debugAction.getProperty(), debugAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothReceiveAction(BluetoothReceiveAction<Phrase<V>> bluetoothReceiveAction) {
        return BluetoothReceiveAction
            .make(
                (Expr<V>) bluetoothReceiveAction.getConnection().modify(this),
                bluetoothReceiveAction.getChannel(),
                bluetoothReceiveAction.getDataType(),
                bluetoothReceiveAction.getProperty(),
                bluetoothReceiveAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothConnectAction(BluetoothConnectAction<Phrase<V>> bluetoothConnectAction) {
        return BluetoothConnectAction
            .make((Expr<V>) bluetoothConnectAction.getAddress().modify(this), bluetoothConnectAction.getProperty(), bluetoothConnectAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothSendAction(BluetoothSendAction<Phrase<V>> bluetoothSendAction) {
        return BluetoothSendAction
            .make(
                bluetoothSendAction.getDataValue(),
                (Expr<V>) bluetoothSendAction.getConnection().modify(this),
                (Expr<V>) bluetoothSendAction.getMsg().modify(this),
                bluetoothSendAction.getChannel(),
                bluetoothSendAction.getDataType(),
                bluetoothSendAction.getProperty(),
                bluetoothSendAction.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Phrase<V>> bluetoothWaitForConnection) {
        return BluetoothWaitForConnectionAction.make(bluetoothWaitForConnection.getProperty(), bluetoothWaitForConnection.getComment());
    }

    @Override
    default Phrase<V> visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Phrase<V>> bluetoothCheckConnectAction) {
        return BluetoothCheckConnectAction
            .make(
                (Expr<V>) bluetoothCheckConnectAction.getConnection().modify(this),
                bluetoothCheckConnectAction.getProperty(),
                bluetoothCheckConnectAction.getComment());
    }

    @Override
    default Phrase<V> visitKeysSensor(KeysSensor<Phrase<V>> keysSensor) {
        return KeysSensor
            .make(
                new SensorMetaDataBean(keysSensor.getPort(), keysSensor.getMode(), keysSensor.getSlot(), keysSensor.isPortInMutation()),
                keysSensor.getProperty(),
                keysSensor.getComment());
    }

    @Override
    default Phrase<V> visitColorSensor(ColorSensor<Phrase<V>> colorSensor) {
        return ColorSensor
            .make(
                new SensorMetaDataBean(colorSensor.getPort(), colorSensor.getMode(), colorSensor.getSlot(), colorSensor.isPortInMutation()),
                colorSensor.getProperty(),
                colorSensor.getComment());
    }

    @Override
    default Phrase<V> visitLightSensor(LightSensor<Phrase<V>> lightSensor) {
        return LightSensor
            .make(
                new SensorMetaDataBean(lightSensor.getPort(), lightSensor.getMode(), lightSensor.getSlot(), lightSensor.isPortInMutation()),
                lightSensor.getProperty(),
                lightSensor.getComment());
    }

    @Override
    default Phrase<V> visitSoundSensor(SoundSensor<Phrase<V>> soundSensor) {
        return SoundSensor
            .make(
                new SensorMetaDataBean(soundSensor.getPort(), soundSensor.getMode(), soundSensor.getSlot(), soundSensor.isPortInMutation()),
                soundSensor.getProperty(),
                soundSensor.getComment());
    }

    @Override
    default Phrase<V> visitEncoderSensor(EncoderSensor<Phrase<V>> encoderSensor) {
        return EncoderSensor
            .make(
                new SensorMetaDataBean(encoderSensor.getPort(), encoderSensor.getMode(), encoderSensor.getSlot(), encoderSensor.isPortInMutation()),
                encoderSensor.getProperty(),
                encoderSensor.getComment());
    }

    @Override
    default Phrase<V> visitGyroSensor(GyroSensor<Phrase<V>> gyroSensor) {
        return GyroSensor
            .make(
                new SensorMetaDataBean(gyroSensor.getPort(), gyroSensor.getMode(), gyroSensor.getSlot(), gyroSensor.isPortInMutation()),
                gyroSensor.getProperty(),
                gyroSensor.getComment());
    }

    @Override
    default Phrase<V> visitInfraredSensor(InfraredSensor<Phrase<V>> infraredSensor) {
        return InfraredSensor
            .make(
                new SensorMetaDataBean(infraredSensor.getPort(), infraredSensor.getMode(), infraredSensor.getSlot(), infraredSensor.isPortInMutation()),
                infraredSensor.getProperty(),
                infraredSensor.getComment());
    }

    @Override
    default Phrase<V> visitTimerSensor(TimerSensor<Phrase<V>> timerSensor) {
        return TimerSensor
            .make(
                new SensorMetaDataBean(timerSensor.getPort(), timerSensor.getMode(), timerSensor.getSlot(), timerSensor.isPortInMutation()),
                timerSensor.getProperty(),
                timerSensor.getComment());
    }

    @Override
    default Phrase<V> visitTouchSensor(TouchSensor<Phrase<V>> touchSensor) {
        return TouchSensor
            .make(
                new SensorMetaDataBean(touchSensor.getPort(), touchSensor.getMode(), touchSensor.getSlot(), touchSensor.isPortInMutation()),
                touchSensor.getProperty(),
                touchSensor.getComment());
    }

    @Override
    default Phrase<V> visitUltrasonicSensor(UltrasonicSensor<Phrase<V>> ultrasonicSensor) {
        return UltrasonicSensor
            .make(
                new SensorMetaDataBean(ultrasonicSensor.getPort(), ultrasonicSensor.getMode(), ultrasonicSensor.getSlot(), ultrasonicSensor.isPortInMutation()),
                ultrasonicSensor.getProperty(),
                ultrasonicSensor.getComment());
    }

    @Override
    default Phrase<V> visitCompassSensor(CompassSensor<Phrase<V>> compassSensor) {
        return CompassSensor
            .make(
                new SensorMetaDataBean(compassSensor.getPort(), compassSensor.getMode(), compassSensor.getSlot(), compassSensor.isPortInMutation()),
                compassSensor.getProperty(),
                compassSensor.getComment());
    }

    @Override
    default Phrase<V> visitTemperatureSensor(TemperatureSensor<Phrase<V>> temperatureSensor) {
        return TemperatureSensor
            .make(
                new SensorMetaDataBean(
                    temperatureSensor.getPort(),
                    temperatureSensor.getMode(),
                    temperatureSensor.getSlot(),
                    temperatureSensor.isPortInMutation()),
                temperatureSensor.getProperty(),
                temperatureSensor.getComment());
    }

    @Override
    default Phrase<V> visitVoltageSensor(VoltageSensor<Phrase<V>> voltageSensor) {
        return VoltageSensor
            .make(
                new SensorMetaDataBean(voltageSensor.getPort(), voltageSensor.getMode(), voltageSensor.getSlot(), voltageSensor.isPortInMutation()),
                voltageSensor.getProperty(),
                voltageSensor.getComment());
    }

    @Override
    default Phrase<V> visitAccelerometer(AccelerometerSensor<Phrase<V>> accelerometerSensor) {
        return AccelerometerSensor
            .make(
                new SensorMetaDataBean(
                    accelerometerSensor.getPort(),
                    accelerometerSensor.getMode(),
                    accelerometerSensor.getSlot(),
                    accelerometerSensor.isPortInMutation()),
                accelerometerSensor.getProperty(),
                accelerometerSensor.getComment());
    }

    @Override
    default Phrase<V> visitPinTouchSensor(PinTouchSensor<Phrase<V>> pinTouchSensor) {
        return PinTouchSensor
            .make(
                new SensorMetaDataBean(pinTouchSensor.getPort(), pinTouchSensor.getMode(), pinTouchSensor.getSlot(), pinTouchSensor.isPortInMutation()),
                pinTouchSensor.getProperty(),
                pinTouchSensor.getComment());
    }

    @Override
    default Phrase<V> visitGestureSensor(GestureSensor<Phrase<V>> gestureSensor) {
        return GestureSensor
            .make(
                new SensorMetaDataBean(gestureSensor.getPort(), gestureSensor.getMode(), gestureSensor.getSlot(), gestureSensor.isPortInMutation()),
                gestureSensor.getProperty(),
                gestureSensor.getComment());
    }

    @Override
    default Phrase<V> visitPinGetValueSensor(PinGetValueSensor<Phrase<V>> pinGetValueSensor) {
        return PinGetValueSensor
            .make(
                new SensorMetaDataBean(
                    pinGetValueSensor.getPort(),
                    pinGetValueSensor.getMode(),
                    pinGetValueSensor.getSlot(),
                    pinGetValueSensor.isPortInMutation()),
                pinGetValueSensor.getProperty(),
                pinGetValueSensor.getComment());
    }

    @Override
    default Phrase<V> visitGetSampleSensor(GetSampleSensor<Phrase<V>> sensorGetSample) {
        return GetSampleSensor
            .make(
                sensorGetSample.getSensorTypeAndMode(),
                sensorGetSample.getSensorPort(),
                sensorGetSample.getSlot(),
                sensorGetSample.isPortInMutation(),
                sensorGetSample.getProperty(),
                sensorGetSample.getComment(),
                getBlocklyDropdownFactory());
    }

    @Override
    default Phrase<V> visitIRSeekerSensor(IRSeekerSensor<Phrase<V>> irSeekerSensor) {
        return IRSeekerSensor
            .make(
                new SensorMetaDataBean(irSeekerSensor.getPort(), irSeekerSensor.getMode(), irSeekerSensor.getSlot(), irSeekerSensor.isPortInMutation()),
                irSeekerSensor.getProperty(),
                irSeekerSensor.getComment());
    }

    @Override
    default Phrase<V> visitMoistureSensor(MoistureSensor<Phrase<V>> moistureSensor) {
        return MoistureSensor
            .make(
                new SensorMetaDataBean(moistureSensor.getPort(), moistureSensor.getMode(), moistureSensor.getSlot(), moistureSensor.isPortInMutation()),
                moistureSensor.getProperty(),
                moistureSensor.getComment());
    }

    @Override
    default Phrase<V> visitHumiditySensor(HumiditySensor<Phrase<V>> humiditySensor) {
        return HumiditySensor
            .make(
                new SensorMetaDataBean(humiditySensor.getPort(), humiditySensor.getMode(), humiditySensor.getSlot(), humiditySensor.isPortInMutation()),
                humiditySensor.getProperty(),
                humiditySensor.getComment());
    }

    @Override
    default Phrase<V> visitMotionSensor(MotionSensor<Phrase<V>> motionSensor) {
        return MotionSensor
            .make(
                new SensorMetaDataBean(motionSensor.getPort(), motionSensor.getMode(), motionSensor.getSlot(), motionSensor.isPortInMutation()),
                motionSensor.getProperty(),
                motionSensor.getComment());
    }

    @Override
    default Phrase<V> visitDropSensor(DropSensor<Phrase<V>> dropSensor) {
        return DropSensor
            .make(
                new SensorMetaDataBean(dropSensor.getPort(), dropSensor.getMode(), dropSensor.getSlot(), dropSensor.isPortInMutation()),
                dropSensor.getProperty(),
                dropSensor.getComment());
    }

    @Override
    default Phrase<V> visitPulseSensor(PulseSensor<Phrase<V>> pulseSensor) {
        return PulseSensor
            .make(
                new SensorMetaDataBean(pulseSensor.getPort(), pulseSensor.getMode(), pulseSensor.getSlot(), pulseSensor.isPortInMutation()),
                pulseSensor.getProperty(),
                pulseSensor.getComment());
    }

    @Override
    default Phrase<V> visitRfidSensor(RfidSensor<Phrase<V>> rfidSensor) {
        return RfidSensor
            .make(
                new SensorMetaDataBean(rfidSensor.getPort(), rfidSensor.getMode(), rfidSensor.getSlot(), rfidSensor.isPortInMutation()),
                rfidSensor.getProperty(),
                rfidSensor.getComment());
    }

    @Override
    default Phrase<V> visitVemlLightSensor(VemlLightSensor<Phrase<V>> vemlLightSensor) {
        return VemlLightSensor
            .make(
                new SensorMetaDataBean(vemlLightSensor.getPort(), vemlLightSensor.getMode(), vemlLightSensor.getSlot(), vemlLightSensor.isPortInMutation()),
                vemlLightSensor.getProperty(),
                vemlLightSensor.getComment());
    }

    @Override
    default Phrase<V> visitParticleSensor(ParticleSensor<Phrase<V>> particleSensor) {
        return ParticleSensor
            .make(
                new SensorMetaDataBean(particleSensor.getPort(), particleSensor.getMode(), particleSensor.getSlot(), particleSensor.isPortInMutation()),
                particleSensor.getProperty(),
                particleSensor.getComment());
    }

    @Override
    default Phrase<V> visitHTColorSensor(HTColorSensor<Phrase<V>> htColorSensor) {
        return HTColorSensor
            .make(
                new SensorMetaDataBean(htColorSensor.getPort(), htColorSensor.getMode(), htColorSensor.getSlot(), htColorSensor.isPortInMutation()),
                htColorSensor.getProperty(),
                htColorSensor.getComment());
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

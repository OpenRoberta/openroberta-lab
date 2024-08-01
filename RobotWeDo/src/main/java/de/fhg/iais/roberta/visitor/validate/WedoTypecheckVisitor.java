package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.visitor.IWeDoVisitor;

public class WedoTypecheckVisitor extends TypecheckCommonLanguageVisitor implements IWeDoVisitor<BlocklyType> {
    public WedoTypecheckVisitor(UsedHardwareBean usedHardwareBean) {
        super(usedHardwareBean);
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(clearDisplayAction, this);
    }

    @Override
    public BlocklyType visitShowTextAction(ShowTextAction showTextAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY).typeCheckPhrases(showTextAction, this,
            showTextAction.msg);
    }

    @Override
    public BlocklyType visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.COLOR).typeCheckPhrases(rgbLedOnAction, this, rgbLedOnAction.colour);
    }

    @Override
    public BlocklyType visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(rgbLedOffAction, this);
    }

    @Override
    public BlocklyType visitMotorOnAction(MotorOnAction motorOnAction) {
        if ( motorOnAction.param.getDuration() != null ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(motorOnAction, this,
                motorOnAction.param.getSpeed(), motorOnAction.param.getDuration().getValue());
        } else {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(motorOnAction, this,
                motorOnAction.param.getSpeed());
        }

    }

    @Override
    public BlocklyType visitMotorStopAction(MotorStopAction motorStopAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(motorStopAction, this);
    }

    @Override
    public BlocklyType visitToneAction(ToneAction toneAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(toneAction, this, toneAction.frequency, toneAction.duration);
    }

    @Override
    public BlocklyType visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(playNoteAction, this);
    }

    @Override
    public BlocklyType visitKeysSensor(KeysSensor keysSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(keysSensor, this);
    }

    @Override
    public BlocklyType visitGyroSensor(GyroSensor gyroSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(gyroSensor, this);
    }

    @Override
    public BlocklyType visitInfraredSensor(InfraredSensor infraredSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(infraredSensor, this);
    }
}

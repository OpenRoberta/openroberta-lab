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
import de.fhg.iais.roberta.visitor.IWeDoVisitor;

public class WedoTypecheckVisitor extends TypecheckCommonLanguageVisitor implements IWeDoVisitor<BlocklyType> {
    public WedoTypecheckVisitor(UsedHardwareBean usedHardwareBean) {
        super(usedHardwareBean);
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitShowTextAction(ShowTextAction showTextAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMotorOnAction(MotorOnAction motorOnAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitMotorStopAction(MotorStopAction motorStopAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitToneAction(ToneAction toneAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return BlocklyType.VOID;
    }

    @Override
    public BlocklyType visitKeysSensor(KeysSensor keysSensor) {
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitGyroSensor(GyroSensor gyroSensor) {
        return BlocklyType.BOOLEAN;
    }

    @Override
    public BlocklyType visitInfraredSensor(InfraredSensor infraredSensor) {
        return BlocklyType.NUMBER;
    }
}

package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;

public class MicrobitValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMicrobitVisitor<Void> {

    private final boolean isSim;
    private final boolean displaySwitchUsed;

    public MicrobitValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed) //
    {
        super(brickConfiguration, beanBuilders, displaySwitchUsed);
        this.isSim = isSim;
        this.displaySwitchUsed = displaySwitchUsed;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        addToPhraseIfUnsupportedInSim(radioSendAction, false, isSim);
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        addToPhraseIfUnsupportedInSim(radioReceiveAction, true, isSim);
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        addToPhraseIfUnsupportedInSim(radioSetChannelAction, false, isSim);
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addToPhraseIfUnsupportedInSim(accelerometerSensor, true, isSim);
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addToPhraseIfUnsupportedInSim(playFileAction, false, isSim);
        checkActorByTypeExists(playFileAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return null;
    }
}

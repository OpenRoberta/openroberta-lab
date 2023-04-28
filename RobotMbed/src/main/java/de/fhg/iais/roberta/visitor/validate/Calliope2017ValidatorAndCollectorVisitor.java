package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;

/**
 * Calliope 2017 has Bluetooth Support => The radio block must be disabled!
 */
public final class Calliope2017ValidatorAndCollectorVisitor extends CalliopeValidatorAndCollectorVisitor {

    public Calliope2017ValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders, boolean isSim) {
        super(brickConfiguration, beanBuilders, isSim);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        addErrorToPhrase(radioSendAction, "BLOCK_NOT_SUPPORTED");
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        addErrorToPhrase(radioReceiveAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        addErrorToPhrase(radioSetChannelAction, "BLOCK_NOT_SUPPORTED");
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitRadioRssiSensor(RadioRssiSensor radioRssiSensor) {
        addErrorToPhrase(radioRssiSensor, "BLOCK_NOT_SUPPORTED");
        return super.visitRadioRssiSensor(radioRssiSensor);
    }
}

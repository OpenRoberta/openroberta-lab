package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;

public class MicrobitSimValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMicrobitVisitor<Void> {

    public MicrobitSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        addWarningToPhrase(radioSendAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        addErrorToPhrase(radioReceiveAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        if ( pinValueSensor.getMode().equals(SC.PULSEHIGH) || pinValueSensor.getMode().equals(SC.PULSELOW) || pinValueSensor.getMode().equals(SC.PULSE) ) {
            addErrorToPhrase(pinValueSensor, "SIM_BLOCK_NOT_SUPPORTED");
        }
        return super.visitPinGetValueSensor(pinValueSensor);
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        addWarningToPhrase(radioSetChannelAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }
    
    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addErrorToPhrase(accelerometerSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addWarningToPhrase(playFileAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

}

package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actor.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actor.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public class MbotSimValidatorAndCollectorVisitor extends MbotValidatorAndCollectorVisitor implements IMbotVisitor<Void> {
    public MbotSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        addErrorToPhrase(irSeekerSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        addErrorToPhrase(lightSensor, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        addWarningToPhrase(sendIRAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        super.visitReceiveIRAction(receiveIRAction);
        addErrorToPhrase(receiveIRAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }
}

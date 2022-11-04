package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actor.arduino.bob3.SendIRAction;

public class Rob3rtaSimValidatorAndCollectorVisitor extends Rob3rtaValidatorAndCollectorVisitor {
    public Rob3rtaSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        addWarningToPhrase(sendIRAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        addErrorToPhrase(receiveIRAction, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }
}

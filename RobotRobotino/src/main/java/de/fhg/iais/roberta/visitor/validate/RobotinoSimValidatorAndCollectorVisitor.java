package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.sensor.robotino.ColourBlob;

public class RobotinoSimValidatorAndCollectorVisitor extends AbstractRobotinoValidatorAndCollectorVisitor {
    public RobotinoSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        addWarningToPhrase(pinWriteValueAction, "SIM_BLOCK_NOT_SUPPORTED");
        return super.visitPinWriteValueAction(pinWriteValueAction);
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        super.visitColourBlob(colourBlob);
        addErrorToPhrase(colourBlob, "SIM_BLOCK_NOT_SUPPORTED");
        return null;
    }
}

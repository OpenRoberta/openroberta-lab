package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;

public class WedoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor {

    public WedoUsedHardwareCollectorVisitor(UsedHardwareBean.Builder builder, ConfigurationAst configuration) {
        super(builder, configuration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        return null;
    }
}

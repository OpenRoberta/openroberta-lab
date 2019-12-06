package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbotBrickValidatorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorWorker;

public class MbotConfigurationValidatorWorker extends AbstractValidatorWorker {
    
    @Override
    protected AbstractBrickValidatorVisitor getVisitor(UsedHardwareBean.Builder builder, Project project) {
        return new MbotBrickValidatorVisitor(builder, project.getConfigurationAst());
    }

    @Override
    protected String getBeanName() {
        return "ProgramValidator";
    }
}
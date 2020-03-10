package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbotBrickValidatorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorWorker;

public class MbotConfigurationValidatorWorker extends AbstractValidatorWorker {
    
    @Override
    protected AbstractBrickValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new MbotBrickValidatorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}
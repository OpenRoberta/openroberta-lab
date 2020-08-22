package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbotSimValidatorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorWorker;

public class MbotSimValidatorWorker extends AbstractValidatorWorker {
    @Override
    protected AbstractProgramValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new MbotSimValidatorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

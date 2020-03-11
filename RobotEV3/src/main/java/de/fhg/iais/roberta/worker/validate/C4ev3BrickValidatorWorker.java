package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.C4ev3BrickValidatorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorWorker;

public final class C4ev3BrickValidatorWorker extends AbstractValidatorWorker {

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new C4ev3BrickValidatorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

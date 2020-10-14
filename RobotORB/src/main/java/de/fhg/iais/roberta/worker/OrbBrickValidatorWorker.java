package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.OrbBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;

public class OrbBrickValidatorWorker extends AbstractValidatorWorker {

    @Override
    protected AbstractProgramValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new OrbBrickValidatorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}
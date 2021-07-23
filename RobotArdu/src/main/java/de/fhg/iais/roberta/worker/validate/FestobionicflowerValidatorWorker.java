package de.fhg.iais.roberta.worker.validate;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.FestobionicflowerValidatorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorWorker;

public class FestobionicflowerValidatorWorker extends AbstractValidatorWorker {

    @Override
    protected FestobionicflowerValidatorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new FestobionicflowerValidatorVisitor(project.getConfigurationAst(), beanBuilders);
    }

}
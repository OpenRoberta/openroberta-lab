package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.NIBOValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.Rob3rtaValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public class Rob3rtaValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected NIBOValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Rob3rtaValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false);
    }
}
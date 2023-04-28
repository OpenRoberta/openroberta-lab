package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.Bob3ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.NIBOValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public class Bob3ValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected NIBOValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Bob3ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false);
    }
}

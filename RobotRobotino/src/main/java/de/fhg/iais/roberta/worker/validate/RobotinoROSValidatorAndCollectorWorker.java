package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.RobotinoROSValidatorAndCollectorVisitor;

public class RobotinoROSValidatorAndCollectorWorker extends AbstractRobotinoValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new RobotinoROSValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

}

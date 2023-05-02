package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.RobotinoValidatorAndCollectorVisitor;

public class RobotinoValidatorAndCollectorWorker extends AbstractRobotinoValidatorAndCollectorWorker {
    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new RobotinoValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false);
    }

}

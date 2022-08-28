package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.WedoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractValidatorAndCollectorWorker;

public class WedoValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new WedoValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

    @Override
    public void execute(Project project) {
        WedoConfigurationValidatorWorker wedoConfigurationValidatorWorker = new WedoConfigurationValidatorWorker(project);
        wedoConfigurationValidatorWorker.validateConfiguration();
        super.execute(project);
    }
}

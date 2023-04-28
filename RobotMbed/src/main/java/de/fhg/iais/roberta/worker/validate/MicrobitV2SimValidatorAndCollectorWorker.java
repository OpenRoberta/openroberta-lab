package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.MicrobitV2ValidatorAndCollectorVisitor;

public class MicrobitV2SimValidatorAndCollectorWorker extends MicrobitV2ValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new MicrobitV2ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, true);
    }
}

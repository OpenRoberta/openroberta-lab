package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CalliopeV3ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class CalliopeV3ValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new CalliopeV3ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false, isDisplaySwitchUsed(project), false);
    }
}

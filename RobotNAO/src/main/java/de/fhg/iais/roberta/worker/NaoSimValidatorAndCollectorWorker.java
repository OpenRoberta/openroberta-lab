package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.NaoSimMethods;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.NaoSimValidatorAndCollectorVisitor;

public class NaoSimValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new NaoSimValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(NaoSimMethods.class);
    }
}

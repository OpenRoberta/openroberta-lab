package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.Calliope2016ValidatorAndCollectorVisitor;

import java.util.Collections;
import java.util.List;

public class Calliope2016ValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new Calliope2016ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

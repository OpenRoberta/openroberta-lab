package de.fhg.iais.roberta.worker;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.Calliope2016ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class Calliope2016ValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    public Calliope2016ValidatorAndCollectorWorker() {
        super();
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new Calliope2016ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

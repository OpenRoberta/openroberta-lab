package de.fhg.iais.roberta.worker.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.Calliope2017ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class Calliope2017ValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    public Calliope2017ValidatorAndCollectorWorker() {
        super();
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Calliope2017ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false);
    }
}

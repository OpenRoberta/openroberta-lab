package de.fhg.iais.roberta.worker;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.Calliope2017NoBlueValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class Calliope2017NoBlueValidatorAndCollectorWorker extends CalliopeValidatorAndCollectorWorker {

    public Calliope2017NoBlueValidatorAndCollectorWorker() {
        super();
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new Calliope2017NoBlueValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

package de.fhg.iais.roberta.worker.spike;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.spike.SpikeMethods;
import de.fhg.iais.roberta.visitor.spike.SpikeValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractSpikeValidatorAndCollectorWorker;

public class SpikeValidatorAndCollectorWorker extends AbstractSpikeValidatorAndCollectorWorker {


    @Override
    final protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new SpikeValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

    @Override
    final protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(SpikeMethods.class);
    }
}
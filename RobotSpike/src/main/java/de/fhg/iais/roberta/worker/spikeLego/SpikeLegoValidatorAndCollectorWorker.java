package de.fhg.iais.roberta.worker.spikeLego;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.spikeLego.SpikeLegoMethods;
import de.fhg.iais.roberta.visitor.spikeLego.SpikeLegoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractSpikeValidatorAndCollectorWorker;

public class SpikeLegoValidatorAndCollectorWorker extends AbstractSpikeValidatorAndCollectorWorker {


    @Override
    final protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new SpikeLegoValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

    @Override
    final protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(SpikeLegoMethods.class);
    }
}
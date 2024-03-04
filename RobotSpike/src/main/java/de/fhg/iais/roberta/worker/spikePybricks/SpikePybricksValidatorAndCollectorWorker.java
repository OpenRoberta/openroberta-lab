package de.fhg.iais.roberta.worker.spikePybricks;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.spikePybricks.SpikePybricksMethods;
import de.fhg.iais.roberta.visitor.spikePybricks.SpikePybricksValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractSpikeValidatorAndCollectorWorker;

public class SpikePybricksValidatorAndCollectorWorker extends AbstractSpikeValidatorAndCollectorWorker {
    @Override
    final protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new SpikePybricksValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }

    @Override
    final protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(SpikePybricksMethods.class);
    }
}
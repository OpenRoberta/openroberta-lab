package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.MbedValidatorAndCollectorVisitor;

public abstract class MbedValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {
    private final List<String> FREE_PINS;
    private final List<String> DEFAULT_PROPERTIES;

    public MbedValidatorAndCollectorWorker(List<String> freePins, List<String> defaultProperties) {
        this.FREE_PINS = Collections.unmodifiableList(freePins);
        this.DEFAULT_PROPERTIES = Collections.unmodifiableList(defaultProperties);
    }

    @Override
    public void execute(Project project) {
        MbedConfigurationValidatorWorker mbedConfigurationValidatorWorker = new MbedConfigurationValidatorWorker(project);
        mbedConfigurationValidatorWorker.validateConfiguration(FREE_PINS, DEFAULT_PROPERTIES);
        super.execute(project);
    }

    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(Project project, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        return new MbedValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders);
    }
}

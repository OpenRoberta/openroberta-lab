package de.fhg.iais.roberta.visitor.spike;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.visitor.AbstractSpikeValidatorAndCollectorVisitor;

public class SpikeSimValidatorAndCollectorVisitor extends AbstractSpikeValidatorAndCollectorVisitor {

    public SpikeSimValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }
}


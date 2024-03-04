package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;

public class CalliopeV3ValidatorAndCollectorVisitor extends CalliopeValidatorAndCollectorVisitor{
    public CalliopeV3ValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean hasBlueTooth) {
        super(brickConfiguration, beanBuilders, isSim, hasBlueTooth);
    }
}

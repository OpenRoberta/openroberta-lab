package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.visitor.IRaspberryPiVisitor;

public class RaspberryPiValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IRaspberryPiVisitor<Void> {

    public RaspberryPiValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }
}

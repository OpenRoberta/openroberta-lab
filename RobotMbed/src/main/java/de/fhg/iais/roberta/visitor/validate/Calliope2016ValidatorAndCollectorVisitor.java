package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.sensor.mbed.RadioRssiSensor;

public final class Calliope2016ValidatorAndCollectorVisitor extends CalliopeValidatorAndCollectorVisitor {

    public Calliope2016ValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }
}

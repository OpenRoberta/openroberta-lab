package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;

public class Rob3rtaValidatorAndCollectorVisitor extends NIBOValidatorAndCollectorVisitor {

    public Rob3rtaValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        switch ( pinTouchSensor.getUserDefinedPort() ) {
            case "EAR":
            case "WHEEL":
                break;
            default:
                addErrorToPhrase(pinTouchSensor, "BLOCK_NOT_SUPPORTED");
        }
        return null;
    }
}

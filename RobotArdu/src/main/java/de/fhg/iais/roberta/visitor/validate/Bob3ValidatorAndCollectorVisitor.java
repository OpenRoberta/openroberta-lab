package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;

public class Bob3ValidatorAndCollectorVisitor extends NIBOValidatorAndCollectorVisitor {

    public Bob3ValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        switch ( pinTouchSensor.getUserDefinedPort() ) {
            case "1":
            case "2":
                break;
            default:
                addErrorToPhrase(pinTouchSensor, "BLOCK_NOT_SUPPORTED");
        }
        return null;
    }
}

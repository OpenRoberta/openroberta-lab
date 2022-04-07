package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public class MbotSimValidatorAndCollectorVisitor extends MbotValidatorAndCollectorVisitor implements IMbotVisitor<Void> {
    public MbotSimValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        irSeekerSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        lightSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        sendIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        receiveIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent("ORT_" + sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            if ( sensor.getKind().hasName("INFRARED_SENSING") ) {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
            } else {
                sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_SENSOR_MISSING"));
            }
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( !type.equals("COLOR") && !type.equals("HT_COLOR") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "TOUCH_SENSING":
                    if ( !type.equals("TOUCH") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !type.equals("ULTRASONIC") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( !type.equals("INFRARED") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_INFRARED_SENSOR_PORT"));
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !type.equals("GYRO") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( !type.equals("COMPASS") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                case "IRSEEKER_SENSING":
                    if ( !type.equals("IRSEEKER_SENSING") ) {
                        sensor.addInfo(NepoInfo.warning("SIM_CONFIGURATION_WARNING_WRONG_SENSOR_PORT"));
                    }
                    break;
                default:
                    break;
            }
        }
    }

}

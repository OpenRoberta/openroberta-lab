package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;
import org.json.JSONObject;

public class MbotSimValidatorVisitor extends AbstractSimValidatorVisitor implements IMbotVisitor<Void> {
    public MbotSimValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
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
    protected void checkDiffDrive(Phrase<Void> driveAction) {
        checkLeftRightMotorPortMbed(driveAction);
    }

    private void checkLeftRightMotorPortMbed(Phrase<Void> driveAction) {
        if ( validNumberOfMotors(driveAction) ) {
            ConfigurationComponent leftMotor = this.robotConfiguration.getFirstMotor(SC.LEFT);
            ConfigurationComponent rightMotor = this.robotConfiguration.getFirstMotor(SC.RIGHT);
            if ( leftMotor == null ) {
                driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
                this.errorCount++;

                if ( rightMotor == null ) {
                    driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
                    this.errorCount++;

                }
            }
        }
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent("ORT_" + sensor.getPort());
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

    //TODO Implement validation for LED Matrix
    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        return null;
    }
}

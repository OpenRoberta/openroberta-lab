package de.fhg.iais.roberta.visitor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.FischertechnikConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffStopAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.MotorOmniDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ServoOnForAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;


public class Txt4ValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements ITxt4Visitor<Void> {


    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
    }});

    public Txt4ValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.power);
        if ( checkActorPort(motorOnAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorOnAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), motor.componentType));
        }
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);
        return null;
    }

    @Override
    public Void visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);

        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVE);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVE);
        }

        return null;
    }

    @Override
    public Void visitMotorOmniDiffOnForAction(MotorOmniDiffOnForAction motorOmniDiffOnForAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnForAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);

        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnForAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            addOmnidriveDistanceMethods(motorOmniDiffOnForAction.direction);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffOnForAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVE);
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVEDISTANCE);
        }

        return null;
    }

    @Override
    public Void visitMotorOmniDiffCurveAction(MotorOmniDiffCurveAction motorOmniDiffCurveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);

        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVE);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVE);
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffCurveForAction(MotorOmniDiffCurveForAction motorOmniDiffCurveForAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveForAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);

        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveForAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVE);
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVECURVEDISTANCE);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffCurveForAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVE);
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFERENTIALDRIVEDISTANCE);
        }
        return null;
    }

    private void addOmnidriveDistanceMethods(String direction) {
        switch ( direction ) {
            case "HEART": //forward
            case "HEART_SMALL": //backward
            case "HAPPY": //left
            case "SMILE": //right
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE);
                break;
            case "CONFUSED": //forward left
            case "SUPRISED": //backward right
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE);
                break;
            case "ASLEEP": //forward right
            case "ANGRY": //backward left
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE);
                break;
            default:
                break;
        }
    }

    @Override
    public Void visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);
        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVETURN);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFDRIVETURN);
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffTurnForAction(MotorOmniDiffTurnForAction motorOmniDiffTurnForAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnForAction.getUserDefinedPort(), SC.ENCODER));
        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnForAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVETURNDEGREES);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffTurnForAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
            usedMethodBuilder.addUsedMethod(Txt4Methods.DIFFDRIVETURNDEGREES);
        }
        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnForAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTARTFOR);

        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), SC.ENCODER));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(encoderSensor.getUserDefinedPort(), SC.ENCODER, encoderSensor.getMode()));
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        usedHardwareBuilder.addUsedActor(new UsedActor(encoderReset.sensorPort, SC.ENCODER));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(encoderReset.sensorPort, SC.ENCODER, SC.RESET));
        return null;
    }

    @Override
    public Void visitServoOnForAction(ServoOnForAction servoOnForAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(servoOnForAction.getUserDefinedPort(), SC.SERVOMOTOR));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(keysSensor.getUserDefinedPort(), SC.KEY, keysSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        if ( checkActorPort(motorStopAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorStopAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), motor.componentType));
        }
        return null;
    }

    @Override
    public Void visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), SC.ENCODER));
        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
        }
        return null;
    }

    @Override
    public Void visitGetLineSensor(GetLineSensor getLineSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(getLineSensor.getUserDefinedPort(), SC.INFRARED, getLineSensor.getMode()));
        return null;
    }

    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct mathRandomFloatFunct) {
        usedHardwareBuilder.addUsedActor(new UsedActor(null, C.RANDOM_DOUBLE));
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        super.visitMathRandomIntFunct(mathRandomIntFunct);
        usedHardwareBuilder.addUsedActor(new UsedActor(null, C.RANDOM));
        return null;
    }

    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
    }

    private ConfigurationComponent getMotorFromUserName(String userName) {
        for ( Map.Entry<String, ConfigurationComponent> entry : this.robotConfiguration.getConfigurationComponents().entrySet() ) {
            ConfigurationComponent component = entry.getValue();
            String comProp = component.componentProperties.get("PORT");
            if ( comProp != null && comProp.equals(userName) ) {
                return component;
            }
        }
        return null;
    }

    private ConfigurationComponent getMotorFromPort(String portName) {
        for ( Map.Entry<String, ConfigurationComponent> entry : this.robotConfiguration.getConfigurationComponents().entrySet() ) {
            ConfigurationComponent component = entry.getValue();
            if ( component.userDefinedPortName.equals(portName) ) {
                return component;
            }
        }
        throw new DbcException("port " + portName + " is missing");
    }

    private Void checkDiffDrive(Phrase phrase) {
        ConfigurationComponent diffDrive = this.robotConfiguration.optConfigurationComponentByType("DIFFERENTIALDRIVE");
        if ( diffDrive == null ) {
            addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else {
            String leftUserPort = diffDrive.getComponentProperties().get("MOTOR_L");
            String rightUserPort = diffDrive.getComponentProperties().get("MOTOR_R");
            ConfigurationComponent leftMotor = getMotorFromUserName(leftUserPort);
            ConfigurationComponent rightMotor = getMotorFromUserName(rightUserPort);
            if ( leftMotor == null || rightMotor == null ) {
                addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else {
                usedHardwareBuilder.addUsedActor(new UsedActor("DIFFERENTIALDRIVE", SC.DIFFERENTIALDRIVE));
            }
        }
        return null;
    }

    private Boolean configHasOmnidrive() {
        return this.robotConfiguration.optConfigurationComponentByType(FischertechnikConstants.OMNIDRIVE) != null;
    }

    private boolean checkActorPort(WithUserDefinedPort action) {
        Assert.isTrue(action instanceof Phrase, "checking Port of a non Phrase");
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = (Phrase) action;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
            return false;
        }
        return true;
    }
}

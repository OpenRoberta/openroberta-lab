package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOffActionJoycar;
import de.fhg.iais.roberta.syntax.action.mbed.joycar.RgbLedOnActionJoycar;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.IJoyCarVisitor;
import de.fhg.iais.roberta.visitor.JoycarMethods;
import de.fhg.iais.roberta.visitor.MicrobitMethods;

public class JoyCarValidatorAndCollectorVisitor extends MicrobitV2ValidatorAndCollectorVisitor implements IJoyCarVisitor<Void> {

    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
        put("ULTRASONIC_SENSING", SC.ULTRASONIC);
    }});

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        usedMethodBuilder.addUsedMethod(JoycarMethods.RECEIVE_MESSAGE);
        return null;
    }

    public JoyCarValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed) {
        super(brickConfiguration, beanBuilders, isSim, displaySwitchUsed);
        this.occupiedPins = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "10", "11", "12", "16", "19", "20");
        this.ledPins = Arrays.asList("3", "4", "6", "7", "10");
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        usedMethodBuilder.addUsedMethod(JoycarMethods.ULTRASONIC_GET_DISTANCE);
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        requiredComponentVisited(driveAction, driveAction.param.getSpeed());
        if ( driveAction.param.getDuration() != null ) {
            requiredComponentVisited(driveAction, driveAction.param.getDuration().getValue());
        }

        usedHardwareBuilder.addUsedActor(new UsedActor(driveAction.port, SC.DIFFERENTIALDRIVE));
        usedHardwareBuilder.addUsedActor(new UsedActor(driveAction.port, "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        requiredComponentVisited(turnAction, turnAction.param.getSpeed());
        if ( turnAction.param.getDuration() != null ) {
            requiredComponentVisited(turnAction, turnAction.param.getDuration().getValue());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(turnAction.port, SC.DIFFERENTIALDRIVE));
        usedHardwareBuilder.addUsedActor(new UsedActor(turnAction.port, "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        requiredComponentVisited(curveAction, curveAction.paramRight.getSpeed(), curveAction.paramLeft.getSpeed());
        if ( curveAction.paramRight.getDuration() != null ) {
            requiredComponentVisited(curveAction, curveAction.paramRight.getDuration().getValue());
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(curveAction.port, SC.DIFFERENTIALDRIVE));
        usedHardwareBuilder.addUsedActor(new UsedActor(curveAction.port, "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
        return null;
    }

    @Override
    public Void visitRgbLedOnActionJoycar(RgbLedOnActionJoycar rgbLedOnActionJoycar) {
        checkSensorType(rgbLedOnActionJoycar, checkActorPort(rgbLedOnActionJoycar));
        requiredComponentVisited(rgbLedOnActionJoycar, rgbLedOnActionJoycar.colour);
        usedMethodBuilder.addUsedMethod(JoycarMethods.LED_SET_COLOUR);
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedOnActionJoycar.port, SC.RGBLED));
        return null;
    }

    @Override
    public Void visitRgbLedOffActionJoycar(RgbLedOffActionJoycar rgbLedOffActionJoycar) {
        checkSensorType(rgbLedOffActionJoycar, checkActorPort(rgbLedOffActionJoycar));
        usedHardwareBuilder.addUsedActor(new UsedActor(rgbLedOffActionJoycar.port, SC.RGBLED));
        usedMethodBuilder.addUsedMethod(JoycarMethods.LED_SET_COLOUR);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.port, "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.SETSPEED);
        usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.port, SC.MOTOR));

        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.param.getSpeed());
        if ( motorOnAction.param.getDuration() != null ) {
            requiredComponentVisited(motorOnAction, motorOnAction.param.getDuration().getValue());
        }
        checkSensorType(motorOnAction, checkActorPort(motorOnAction));

        String blockName = motorOnAction.getUserDefinedPort();
        ConfigurationComponent block = this.robotConfiguration.getConfigurationComponent(blockName);
        String port = block.getComponentProperties().get("PORT");

        if ( port.contains("SERVO") ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), SC.SERVOMOTOR));
            usedMethodBuilder.addUsedMethod(JoycarMethods.SERVO_SET_ANGLE);
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.port, "I2C"));
            usedMethodBuilder.addUsedMethod(JoycarMethods.SETSPEED);
            usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.port, SC.MOTOR));
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction motorDriveStopAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(motorDriveStopAction.port, SC.DIFFERENTIALDRIVE));
        usedHardwareBuilder.addUsedActor(new UsedActor(motorDriveStopAction.port, "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.DIFFDRIVE);
        usedMethodBuilder.addUsedMethod(JoycarMethods.SCALE);
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        checkSensorPort(encoderSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.I2C_GET_SENSORDATA);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        checkSensorPort(infraredSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(infraredSensor.getUserDefinedPort(), "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.I2C_GET_SENSORDATA);
        return null;
    }

    @Override
    public Void visitGetLineSensor(GetLineSensor getLineSensor) {
        checkSensorPort(getLineSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(getLineSensor.getUserDefinedPort(), "I2C"));
        usedMethodBuilder.addUsedMethod(JoycarMethods.I2C_GET_SENSORDATA);
        return null;
    }


    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            configurationComponent = getSubComponent(sensor.getUserDefinedPort());
            if ( configurationComponent == null ) {
                addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
                return;
            }
        }
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
        }
        checkSensorType(sensor, configurationComponent);
    }

    private ConfigurationComponent getSubComponent(String userDefinedPort) {
        for ( ConfigurationComponent component : this.robotConfiguration.getConfigurationComponentsValues() ) {
            try {
                for ( List<ConfigurationComponent> subComponents : component.getSubComponents().values() ) {
                    for ( ConfigurationComponent subComponent : subComponents ) {
                        if ( subComponent.userDefinedPortName.equals(userDefinedPort) ) {
                            return subComponent;
                        }
                    }
                }
            } catch ( UnsupportedOperationException e ) {
                continue;
            }
        }
        return null;
    }

    private void checkSensorType(Phrase sensor, ConfigurationComponent configurationComponent) {
        String expectedComponentType = SENSOR_COMPONENT_TYPE_MAP.get(sensor.getKind().getName());
        String typeWithoutSensing = sensor.getKind().getName().replace("_SENSING", "");
        if ( !(typeWithoutSensing.equalsIgnoreCase(configurationComponent.componentType)) ) {
            if ( expectedComponentType != null && !expectedComponentType.equalsIgnoreCase(configurationComponent.componentType) ) {
                addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
            }
        }
    }

    private ConfigurationComponent checkActorPort(WithUserDefinedPort action) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort());
        if ( configurationComponent == null ) {
            configurationComponent = getSubComponent(action.getUserDefinedPort());
            if ( configurationComponent == null ) {
                addErrorToPhrase(configurationComponent, "CONFIGURATION_ERROR_SENSOR_MISSING");
                return null;
            }
            addErrorToPhrase(configurationComponent, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return configurationComponent;
    }
}

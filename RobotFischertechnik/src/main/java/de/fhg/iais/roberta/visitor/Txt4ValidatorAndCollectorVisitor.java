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
import de.fhg.iais.roberta.syntax.action.DisplayLedOffAction;
import de.fhg.iais.roberta.syntax.action.DisplayLedOnAction;
import de.fhg.iais.roberta.syntax.action.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.LedSetBrightnessAction;
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
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.logic.ColourCompare;
import de.fhg.iais.roberta.syntax.sensor.CameraBallSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineColourSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineInformationSensor;
import de.fhg.iais.roberta.syntax.sensor.CameraLineSensor;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.TouchKeySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetLineSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
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

    private final boolean isSim;
    private static final Map<String, String> SENSOR_COMPONENT_TYPE_MAP = Collections.unmodifiableMap(new HashMap<String, String>() {{
    }});
    private boolean driveWasChecked;

    public Txt4ValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders, boolean isSim) {
        super(robotConfiguration, beanBuilders);
        driveWasChecked = false;
        this.isSim = isSim;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        checkActorPort(motorOnAction);
        requiredComponentVisited(motorOnAction, motorOnAction.power);
        if ( checkActorPort(motorOnAction) ) {
            ConfigurationComponent motor = getMotorFromPort(motorOnAction.port);
            usedHardwareBuilder.addUsedActor(new UsedActor(motor.getOptProperty("PORT"), motor.componentType));
        }
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTART);
        return null;
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        checkActorPort(motorOnForAction);
        requiredComponentVisited(motorOnForAction, motorOnForAction.power, motorOnForAction.value);
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnForAction.getUserDefinedPort(), SC.ENCODER));
        usedMethodBuilder.addUsedMethod(Txt4Methods.MOTORSTARTFOR);

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
    public Void visitMotorOmniDiffOnAction(MotorOmniDiffOnAction motorOmniDiffOnAction) {
        requiredComponentVisited(motorOmniDiffOnAction, motorOmniDiffOnAction.power);
        checkDiffOrOmniDrive(motorOmniDiffOnAction);
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
        requiredComponentVisited(motorOmniDiffOnForAction, motorOmniDiffOnForAction.power, motorOmniDiffOnForAction.distance);
        checkDiffOrOmniDrive(motorOmniDiffOnForAction);
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
        requiredComponentVisited(motorOmniDiffCurveAction, motorOmniDiffCurveAction.powerLeft, motorOmniDiffCurveAction.powerRight);
        checkDiffOrOmniDrive(motorOmniDiffCurveAction);
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
        requiredComponentVisited(motorOmniDiffCurveForAction, motorOmniDiffCurveForAction.distance, motorOmniDiffCurveForAction.powerLeft, motorOmniDiffCurveForAction.powerRight);
        checkDiffOrOmniDrive(motorOmniDiffCurveForAction);
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
            case "FORWARD": //forward
            case "BACKWARD": //backward
            case "LEFT": //left
            case "RIGHT": //right
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVESTRAIGHTDISTANCE);
                break;
            case "FORWARDLEFT": //forward left
            case "BACKWARDRIGHT": //backward right
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVEDIAGONALTLDISTANCE);
                break;
            case "FORWARDRIGHT": //forward right
            case "BACKWARDLEFT": //backward left
                usedMethodBuilder.addUsedMethod(Txt4Methods.OMNIDRIVEDIAGONALTRDISTANCE);
                break;
            default:
                break;
        }
    }

    @Override
    public Void visitMotorOmniDiffTurnAction(MotorOmniDiffTurnAction motorOmniDiffTurnAction) {
        requiredComponentVisited(motorOmniDiffTurnAction, motorOmniDiffTurnAction.power);
        checkDiffOrOmniDrive(motorOmniDiffTurnAction);
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
        requiredComponentVisited(motorOmniDiffTurnForAction, motorOmniDiffTurnForAction.degrees, motorOmniDiffTurnForAction.power);
        checkDiffOrOmniDrive(motorOmniDiffTurnForAction);
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
    public Void visitMotorOmniDiffStopAction(MotorOmniDiffStopAction motorOmniDiffStopAction) {
        checkDiffOrOmniDrive(motorOmniDiffStopAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), SC.ENCODER));
        if ( configHasOmnidrive() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), FischertechnikConstants.OMNIDRIVE));
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(motorOmniDiffStopAction.getUserDefinedPort(), SC.DIFFERENTIALDRIVE));
        }
        return null;
    }

    @Override
    public Void visitServoOnForAction(ServoOnForAction servoOnForAction) {
        checkActorPort(servoOnForAction);
        requiredComponentVisited(servoOnForAction, servoOnForAction.value);
        usedHardwareBuilder.addUsedActor(new UsedActor(servoOnForAction.getUserDefinedPort(), SC.SERVOMOTOR));
        return null;
    }

    @Override
    public Void visitLedAction(LedAction ledAction) {
        checkActorPort(ledAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledAction.getUserDefinedPort(), SC.LED));
        return null;
    }

    @Override
    public Void visitLedSetBrightnessAction(LedSetBrightnessAction ledSetBrightnessAction) {
        checkActorPort(ledSetBrightnessAction);
        requiredComponentVisited(ledSetBrightnessAction, ledSetBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor(ledSetBrightnessAction.getUserDefinedPort(), SC.LED));
        return null;
    }

    @Override
    public Void visitDisplayLedOnAction(DisplayLedOnAction displayLedOnAction) {
        checkActorPort(displayLedOnAction);
        usedMethodBuilder.addUsedMethod(Txt4Methods.DISPLAYLEDON);
        usedHardwareBuilder.addUsedActor(new UsedActor(displayLedOnAction.port, SC.DISPLAY));
        usedHardwareBuilder.addUsedActor(new UsedActor(displayLedOnAction.port, FischertechnikConstants.DISPLAYLED));
        return null;
    }

    @Override
    public Void visitDisplayLedOffAction(DisplayLedOffAction displayLedOffAction) {
        checkActorPort(displayLedOffAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(displayLedOffAction.port, SC.DISPLAY));
        usedHardwareBuilder.addUsedActor(new UsedActor(displayLedOffAction.port, FischertechnikConstants.DISPLAYLED));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        checkActorPort(displayTextAction);
        usedHardwareBuilder.addUsedActor(new UsedActor(displayTextAction.port, SC.DISPLAY));
        requiredComponentVisited(displayTextAction, displayTextAction.text);
        requiredComponentVisited(displayTextAction, displayTextAction.row);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorPort(keysSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(keysSensor.getUserDefinedPort(), SC.KEY, keysSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTouchKeySensor(TouchKeySensor touchKeySensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.DISPLAY, SC.DISPLAY));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(SC.DISPLAY, SC.BUTTON, touchKeySensor.getMode()));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        checkSensorPort(encoderSensor);
        usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), SC.ENCODER));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(encoderSensor.getUserDefinedPort(), SC.ENCODER, encoderSensor.getMode()));
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderReset.sensorPort);
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderReset, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(encoderReset.sensorPort, SC.ENCODER));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(encoderReset.sensorPort, SC.ENCODER, SC.RESET));
        return null;
    }

    @Override
    public Void visitGetLineSensor(GetLineSensor getLineSensor) {
        checkSensorPort(getLineSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(getLineSensor.getUserDefinedPort(), SC.INFRARED, getLineSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor motionSensor) {
        checkSensorPort(motionSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(motionSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, SC.MOTION));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(motionSensor.getUserDefinedPort(), SC.MOTION, SC.MOTION));
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, SC.COLOUR));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOUR, SC.COLOUR));
        usedMethodBuilder.addUsedMethod(Txt4Methods.CAMERAGETCOLOUR);
        return null;
    }

    @Override
    public Void visitCameraLineSensor(CameraLineSensor cameraLineSensor) {
        checkSensorPort(cameraLineSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, FischertechnikConstants.LINE));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineSensor.getUserDefinedPort(), FischertechnikConstants.LINE, FischertechnikConstants.LINE));
        return null;
    }

    @Override
    public Void visitCameraLineInformationSensor(CameraLineInformationSensor cameraLineInformationSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(cameraLineInformationSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(cameraLineInformationSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        requiredComponentVisited(cameraLineInformationSensor, cameraLineInformationSensor.lineId);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineInformationSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, FischertechnikConstants.LINE));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineInformationSensor.getUserDefinedPort(), FischertechnikConstants.LINE, FischertechnikConstants.LINE));
        usedMethodBuilder.addUsedMethod(Txt4Methods.LINEINFORMATION);
        return null;
    }

    @Override
    public Void visitCameraLineColourSensor(CameraLineColourSensor cameraLineColourSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(cameraLineColourSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(cameraLineColourSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }

        requiredComponentVisited(cameraLineColourSensor, cameraLineColourSensor.lineId);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineColourSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, FischertechnikConstants.LINE));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraLineColourSensor.getUserDefinedPort(), FischertechnikConstants.LINE, FischertechnikConstants.LINE));
        usedMethodBuilder.addUsedMethod(Txt4Methods.LINEGETCOLOUR);
        return null;
    }

    @Override
    public Void visitCameraBallSensor(CameraBallSensor cameraBallSensor) {
        checkSensorPort(cameraBallSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraBallSensor.getUserDefinedPort(), FischertechnikConstants.CAMERA, FischertechnikConstants.BALL));
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraBallSensor.getUserDefinedPort(), FischertechnikConstants.BALL, FischertechnikConstants.BALL));
        usedMethodBuilder.addUsedMethod(Txt4Methods.BALLINFORMATION);
        return null;
    }

    @Override
    public Void visitColourCompare(ColourCompare colourCompare) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor("", SC.COLOUR, SC.COLOUR));
        usedMethodBuilder.addUsedMethod(Txt4Methods.COLOURCOMPARE);
        requiredComponentVisited(colourCompare, colourCompare.colour1, colourCompare.colour2, colourCompare.tolerance);
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

    private void checkDiffDrive(Phrase phrase) {
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
    }

    private void checkOmniDrive(Phrase phrase) {
        ConfigurationComponent omnidrive = this.robotConfiguration.optConfigurationComponentByType("OMNIDRIVE");
        if ( omnidrive == null ) {
            addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        } else {
            String flMotorPort = omnidrive.getComponentProperties().get("MOTOR_FL");
            String frMotorPort = omnidrive.getComponentProperties().get("MOTOR_FR");
            String rlMotorPort = omnidrive.getComponentProperties().get("MOTOR_RL");
            String rrMotorPort = omnidrive.getComponentProperties().get("MOTOR_RR");

            ConfigurationComponent flMotor = getMotorFromUserName(flMotorPort);
            ConfigurationComponent frMotor = getMotorFromUserName(frMotorPort);
            ConfigurationComponent rlMotor = getMotorFromUserName(rlMotorPort);
            ConfigurationComponent rrMotor = getMotorFromUserName(rrMotorPort);
            if ( flMotor == null || frMotor == null || rlMotor == null || rrMotor == null ) {
                addErrorToPhrase(phrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else {
                usedHardwareBuilder.addUsedActor(new UsedActor("OMNIDRIVE", FischertechnikConstants.OMNIDRIVE));
            }
        }
    }

    private Boolean configHasOmnidrive() {
        return this.robotConfiguration.optConfigurationComponentByType(FischertechnikConstants.OMNIDRIVE) != null;
    }

    private void checkDiffOrOmniDrive(Phrase phrase) {
        if ( driveWasChecked ) {
            return;
        }
        if ( configHasOmnidrive() ) {
            checkOmniDrive(phrase);
        } else {
            checkDiffDrive(phrase);
        }
        driveWasChecked = true;
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

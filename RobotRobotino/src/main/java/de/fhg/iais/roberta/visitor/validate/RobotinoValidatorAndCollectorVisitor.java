package de.fhg.iais.roberta.visitor.validate;

import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.constants.RobotinoConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraThreshold;
import de.fhg.iais.roberta.syntax.sensor.robotino.ColourBlob;
import de.fhg.iais.roberta.syntax.sensor.robotino.MarkerInformation;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensorReset;
import de.fhg.iais.roberta.syntax.sensor.robotino.OpticalSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.visitor.IRobotinoVisitor;
import de.fhg.iais.roberta.visitor.RobotinoMethods;

public class RobotinoValidatorAndCollectorVisitor extends MotorValidatorAndCollectorVisitor implements IRobotinoVisitor<Void> {

    private final boolean isSim;

    public RobotinoValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim) {
        super(robotConfiguration, beanBuilders);
        this.isSim = isSim;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        checkSensorPort(touchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(touchSensor.getUserDefinedPort(), SC.TOUCH, touchSensor.getMode()));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(pinGetValueSensor.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(pinGetValueSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }

        if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            usedHardwareBuilder.addUsedSensor(new UsedSensor(pinGetValueSensor.getUserDefinedPort(), SC.ANALOG_INPUT, pinGetValueSensor.getMode()));
        } else if ( pinGetValueSensor.getMode().equals(SC.DIGITAL) ) {
            usedHardwareBuilder.addUsedSensor(new UsedSensor(pinGetValueSensor.getUserDefinedPort(), SC.DIGITAL_INPUT, pinGetValueSensor.getMode()));
        }

        if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETANALOGPIN);
        } else if ( pinGetValueSensor.getMode().equals(SC.DIGITAL) ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDIGITALPIN);
        }
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        addToPhraseIfUnsupportedInSim(pinWriteValueAction, false, isSim);
        requiredComponentVisited(pinWriteValueAction, pinWriteValueAction.value);

        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(pinWriteValueAction.port);
        if ( usedConfigurationBlock == null ) {
            addErrorToPhrase(pinWriteValueAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }

        usedMethodBuilder.addUsedMethod(RobotinoMethods.SETDIGITALPIN);
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(pinWriteValueAction.port), SC.DIGITAL_PIN));

        return null;
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        checkSensorPort(odometrySensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(odometrySensor.getUserDefinedPort(), RobotinoConstants.ODOMETRY, odometrySensor.getSlot()));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        return null;
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        checkSensorPort(odometrySensorReset);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(odometrySensorReset.getUserDefinedPort(), RobotinoConstants.ODOMETRY, odometrySensorReset.slot));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        return null;
    }

    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(RobotinoConstants.OMNIDRIVE), RobotinoConstants.OMNIDRIVE));
        requiredComponentVisited(omnidriveAction, omnidriveAction.xVel, omnidriveAction.yVel);
        requiredComponentVisited(omnidriveAction, omnidriveAction.thetaVel);
        addMotorMethods();
        return null;
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        requiredComponentVisited(omnidriveDistanceAction, omnidriveDistanceAction.xVel, omnidriveDistanceAction.yVel);
        requiredComponentVisited(omnidriveDistanceAction, omnidriveDistanceAction.distance);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(getConfigPort(RobotinoConstants.ODOMETRY), RobotinoConstants.ODOMETRY, null));
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(RobotinoConstants.OMNIDRIVE), RobotinoConstants.OMNIDRIVE));

        checkIfBothZeroSpeed(omnidriveDistanceAction);

        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVEFORDISTANCE);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVETOPOSITION);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        requiredComponentVisited(turnAction, turnAction.param.getSpeed(), turnAction.param.getDuration().getValue());

        usedHardwareBuilder.addUsedSensor(new UsedSensor(getConfigPort(RobotinoConstants.ODOMETRY), RobotinoConstants.ODOMETRY, null));
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(RobotinoConstants.OMNIDRIVE), RobotinoConstants.OMNIDRIVE));

        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.TURNFORDEGREES);
        return null;
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        requiredComponentVisited(omnidrivePositionAction, omnidrivePositionAction.x, omnidrivePositionAction.y, omnidrivePositionAction.power);

        usedHardwareBuilder.addUsedSensor(new UsedSensor(getConfigPort(RobotinoConstants.ODOMETRY), RobotinoConstants.ODOMETRY, null));
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(RobotinoConstants.OMNIDRIVE), RobotinoConstants.OMNIDRIVE));

        checkForZeroSpeed(omnidrivePositionAction, omnidrivePositionAction.power);

        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVETOPOSITION);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        addMotorMethods();
        usedHardwareBuilder.addUsedActor(new UsedActor(getConfigPort(RobotinoConstants.OMNIDRIVE), RobotinoConstants.OMNIDRIVE));
        return null;
    }

    @Override
    public Void visitMarkerInformation(MarkerInformation markerInformation) {
        checkSensorPort(markerInformation);
        requiredComponentVisited(markerInformation, markerInformation.markerId);
        usedHardwareBuilder.addUsedSensor(new UsedSensor("", RobotinoConstants.CAMERA, "marker"));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETMARKERINFO);
        return null;
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        addToPhraseIfUnsupportedInSim(colourBlob, true, isSim);
        checkSensorPort(colourBlob);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colourBlob.getUserDefinedPort(), RobotinoConstants.CAMERA, colourBlob.mode));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETCOLOURBLOB);
        return null;
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        checkSensorPort(cameraSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraSensor.getUserDefinedPort(), RobotinoConstants.CAMERA, cameraSensor.getMode()));
        if ( cameraSensor.getMode().equals("LINE") ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETCAMERALINE);
        }
        return null;
    }

    private void addMotorMethods() {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.OMNIDRIVESPEED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.POSTVEL);
    }

    @Override
    public Void visitOpticalSensor(OpticalSensor opticalSensor) {
        checkSensorPort(opticalSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(opticalSensor.getUserDefinedPort(), RobotinoConstants.OPTICAL_SENSOR, opticalSensor.getMode()));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDIGITALPIN);
        return null;
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(detectMarkSensor.getUserDefinedPort(), RobotinoConstants.CAMERA, detectMarkSensor.getMode()));
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETMARKERS);
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }

    private void checkIfBothZeroSpeed(OmnidriveDistanceAction omnidriveDistanceAction) {
        if ( omnidriveDistanceAction.xVel.getKind().hasName("NUM_CONST") && omnidriveDistanceAction.yVel.getKind().hasName("NUM_CONST") ) {

            if ( Math.abs(Double.parseDouble(((NumConst) omnidriveDistanceAction.xVel).value)) < DOUBLE_EPS && Math.abs(Double.parseDouble(((NumConst) omnidriveDistanceAction.yVel).value)) < DOUBLE_EPS ) {
                addWarningToPhrase(omnidriveDistanceAction, "MOTOR_SPEED_0");
            }
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDISTANCE);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCameraThreshold(CameraThreshold cameraThreshold) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(cameraThreshold.getUserDefinedPort(), RobotinoConstants.CAMERA, cameraThreshold.mode));
        requiredComponentVisited(cameraThreshold, cameraThreshold.threshold);
        return null;
    }

    private void checkSensorPort(WithUserDefinedPort sensor) {
        Assert.isTrue(sensor instanceof Phrase, "checking Port of a non Phrase");
        Phrase sensorAsSensor = (Phrase) sensor;

        String userDefinedPort = sensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(userDefinedPort);
        if ( configurationComponent == null ) {
            addErrorToPhrase(sensorAsSensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
            return;
        }
    }

    private String getConfigPort(String name) {
        Map<String, ConfigurationComponent> configComponents = this.robotConfiguration.getConfigurationComponents();
        for ( ConfigurationComponent component : configComponents.values() ) {
            if ( component.componentType.equals(name) ) {
                return component.userDefinedPortName;
            }
        }
        return "";
    }
}

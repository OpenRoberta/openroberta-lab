package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.ColourBlob;
import de.fhg.iais.roberta.syntax.sensor.robotino.MarkerInformation;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensorReset;
import de.fhg.iais.roberta.syntax.sensor.robotino.OpticalSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.RobotinoMethods;

public class RobotinoViewValidatorAndCollectorVisitor extends AbstractRobotinoValidatorAndCollectorVisitor {

    public RobotinoViewValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        return super.visitTouchSensor(touchSensor);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        if ( pinGetValueSensor.getMode().equals(SC.ANALOG) ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETANALOGPIN);
        } else if ( pinGetValueSensor.getMode().equals(SC.DIGITAL) ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDIGITALPIN);
        }
        return super.visitPinGetValueSensor(pinGetValueSensor);
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        return super.visitOdometrySensor(odometrySensor);
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        return super.visitOdometrySensorReset(odometrySensorReset);
    }

    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        addMotorMethods();
        return super.visitOmnidriveAction(omnidriveAction);
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVEFORDISTANCE);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVETOPOSITION);
        return super.visitOmnidriveDistanceAction(omnidriveDistanceAction);
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.TURNFORDEGREES);
        return super.visitTurnAction(turnAction);
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        addMotorMethods();
        usedMethodBuilder.addUsedMethod(RobotinoMethods.ISBUMPED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.RESETODOMETRY);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.DRIVETOPOSITION);
        return super.visitOmnidrivePositionAction(omnidrivePositionAction);
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        addMotorMethods();
        return super.visitMotorDriveStopAction(stopAction);
    }

    @Override
    public Void visitMarkerInformation(MarkerInformation markerInformation) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETMARKERINFO);
        return super.visitMarkerInformation(markerInformation);
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETCOLOURBLOB);
        return super.visitColourBlob(colourBlob);
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        if ( cameraSensor.getMode().equals("LINE") ) {
            usedMethodBuilder.addUsedMethod(RobotinoMethods.GETCAMERALINE);
        }
        return super.visitCameraSensor(cameraSensor);
    }

    private void addMotorMethods() {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.OMNIDRIVESPEED);
        usedMethodBuilder.addUsedMethod(RobotinoMethods.POSTVEL);
    }

    @Override
    public Void visitOpticalSensor(OpticalSensor opticalSensor) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETDIGITALPIN);
        return super.visitOpticalSensor(opticalSensor);
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        usedMethodBuilder.addUsedMethod(RobotinoMethods.GETMARKERS);
        return super.visitDetectMarkSensor(detectMarkSensor);
    }
}

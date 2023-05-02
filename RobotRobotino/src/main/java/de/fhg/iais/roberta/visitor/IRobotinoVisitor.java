package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
import de.fhg.iais.roberta.syntax.sensor.generic.DetectMarkSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraSensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.CameraThreshold;
import de.fhg.iais.roberta.syntax.sensor.robotino.ColourBlob;
import de.fhg.iais.roberta.syntax.sensor.robotino.MarkerInformation;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensor;
import de.fhg.iais.roberta.syntax.sensor.robotino.OdometrySensorReset;
import de.fhg.iais.roberta.syntax.sensor.robotino.OpticalSensor;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface IRobotinoVisitor<V> extends IVisitor<V> {
    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }

    V visitTouchSensor(TouchSensor touchSensor);

    V visitOmnidriveAction(OmnidriveAction omnidriveAction);

    V visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction);

    V visitMotorDriveStopAction(MotorDriveStopAction stopAction);

    V visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction);

    V visitOdometrySensor(OdometrySensor odometrySensor);

    V visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitOdometrySensorReset(OdometrySensorReset odometrySensorReset);

    V visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction);

    V visitTurnAction(TurnAction turnAction);

    V visitMarkerInformation(MarkerInformation markerInformation);

    V visitDetectMarkSensor(DetectMarkSensor detectMarkSensor);

    V visitCameraSensor(CameraSensor cameraSensor);

    V visitColourBlob(ColourBlob colourBlob);

    V visitCameraThreshold(CameraThreshold cameraThreshold);

    V visitOpticalSensor(OpticalSensor opticalSensor);

}
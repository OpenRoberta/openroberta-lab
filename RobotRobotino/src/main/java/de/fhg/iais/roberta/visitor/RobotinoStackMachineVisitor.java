package de.fhg.iais.roberta.visitor;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidriveDistanceAction;
import de.fhg.iais.roberta.syntax.actor.robotino.OmnidrivePositionAction;
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
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class RobotinoStackMachineVisitor extends AbstractStackMachineVisitor implements IRobotinoVisitor<Void> {

    private int port;

    public RobotinoStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port);
        } else {
            o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port);
        }
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH);
        return add(o);
    }

    @Override
    public Void visitOmnidriveAction(OmnidriveAction omnidriveAction) {
        omnidriveAction.xVel.accept(this);
        omnidriveAction.yVel.accept(this);
        omnidriveAction.thetaVel.accept(this);
        return add(makeNode(C.OMNI_DRIVE));
    }

    @Override
    public Void visitOmnidriveDistanceAction(OmnidriveDistanceAction omnidriveDistanceAction) {
        omnidriveDistanceAction.xVel.accept(this);
        omnidriveDistanceAction.yVel.accept(this);
        omnidriveDistanceAction.distance.accept(this);
        return add(makeNode(C.OMNI_DRIVE_DIST));
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        return add(makeNode(C.OMNI_DRIVE_STOP));
    }

    @Override
    public Void visitOmnidrivePositionAction(OmnidrivePositionAction omnidrivePositionAction) {
        omnidrivePositionAction.x.accept(this);
        omnidrivePositionAction.y.accept(this);
        omnidrivePositionAction.power.accept(this);
        return add(makeNode(C.OMNI_DRIVE_POSITION));
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        turnAction.param.getSpeed().accept(this);
        // TODO change names: duration is used here as angle!
        turnAction.param.getDuration().getValue().accept(this);
        ITurnDirection turnDirection = turnAction.direction;
        JSONObject o =
            makeNode(C.OMNI_DRIVE_TURN)
                .put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase());
        return (add(o));
    }

    @Override
    public Void visitMarkerInformation(MarkerInformation markerInformation) {
        markerInformation.markerId.accept(this);
        JSONObject o = makeNode(C.MARKER).put(C.GET_SAMPLE, C.MARKER).put(C.MODE, C.INFO);
        return add(o);
    }

    @Override
    public Void visitDetectMarkSensor(DetectMarkSensor detectMarkSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.MARKER).put(C.MODE, C.ID);
        return add(o);
    }

    @Override
    public Void visitCameraSensor(CameraSensor cameraSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.CAMERA).put(C.MODE, cameraSensor.getMode().toLowerCase());
        return add(o);
    }

    @Override
    public Void visitColourBlob(ColourBlob colourBlob) {
        return null;
    }

    @Override
    public Void visitCameraThreshold(CameraThreshold cameraThreshold) {
        return null;
    }

    @Override
    public Void visitOpticalSensor(OpticalSensor opticalSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.OPTICAL).put(C.PORT, opticalSensor.getUserDefinedPort()).put(C.MODE, opticalSensor.getMode().toLowerCase());
        return add(o);
    }

    @Override
    public Void visitOdometrySensor(OdometrySensor odometrySensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ODOMETRY).put(C.SLOT, odometrySensor.getSlot().toLowerCase());
        return add(o);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinGetValueSensor) {
        return add(makeNode(C.ODOMETRY_SENSOR_RESET));
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.MODE, C.DISTANCE);
        return add(o);
    }

    @Override
    public Void visitOdometrySensorReset(OdometrySensorReset odometrySensorReset) {
        return add(makeNode(C.ODOMETRY_SENSOR_RESET).put(C.SLOT, odometrySensorReset.slot.toLowerCase()));
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction pinWriteValueAction) {
        return null;
    }
}

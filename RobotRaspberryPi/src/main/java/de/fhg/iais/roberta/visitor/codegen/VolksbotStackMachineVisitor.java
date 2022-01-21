package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.Dummy;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.IVolksbotVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class VolksbotStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IVolksbotVisitor<V> {
    private final static int SPEED = 100;
    private final static int STEP_CM = 45;
    private final static int STEP_DEGREE = 90;
    private final static int STEP_CORR_CM = 10;
    private final static int STEP_PAUSE = 500;

    public VolksbotStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public V visitStepForward(StepForward<V> stepForward) {
        DriveDirection driveDirection = DriveDirection.FOREWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_CM));
        JSONObject o =
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false);
        app(o);
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_PAUSE));
        return app(makeNode(C.WAIT_TIME_STMT));
    }

    @Override
    public V visitStepBackward(StepBackward<V> stepBackward) {
        DriveDirection driveDirection = DriveDirection.BACKWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_CM));
        app(
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_PAUSE));
        return app(makeNode(C.WAIT_TIME_STMT));
    }

    @Override
    public V visitRotateRight(RotateRight<V> rotateRight) {
        DriveDirection driveDirection = DriveDirection.BACKWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, 10));
        app(
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));

        driveDirection = DriveDirection.FOREWARD;
        ITurnDirection turnDirection = TurnDirection.RIGHT;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_DEGREE));
        app(
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase())
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        driveDirection = DriveDirection.FOREWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_CORR_CM));
        app(
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_PAUSE));
        return app(makeNode(C.WAIT_TIME_STMT));
    }

    @Override
    public V visitRotateLeft(RotateLeft<V> rotateLeft) {
        DriveDirection driveDirection = DriveDirection.BACKWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_CORR_CM));
        app(
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));

        driveDirection = DriveDirection.FOREWARD;
        ITurnDirection turnDirection = TurnDirection.LEFT;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_DEGREE));
        app(
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase())
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        driveDirection = DriveDirection.FOREWARD;
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, SPEED));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_CORR_CM));
        app(
            makeNode(C.DRIVE_ACTION)
                .put(C.DRIVE_DIRECTION, driveDirection)
                .put(C.NAME, "volksbot")
                .put(C.SPEED_ONLY, false)
                .put(C.SET_TIME, false));
        app(makeNode(C.STOP_DRIVE).put(C.NAME, "volksbot"));
        app(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, STEP_PAUSE));
        return app(makeNode(C.WAIT_TIME_STMT));
    }

    @Override
    public V visitMainTaskSimple(MainTaskSimple<V> mainTaskSimple) {
        return null;
    }

    @Override
    public V visitDummy(Dummy<V> dummy) {
        return null;
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        return null;
    }
}

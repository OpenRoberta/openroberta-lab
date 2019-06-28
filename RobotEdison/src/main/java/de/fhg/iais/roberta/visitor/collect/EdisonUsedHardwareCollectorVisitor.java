package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.actors.edison.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.edison.SendIRAction;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.syntax.sensors.edison.ResetSensor;
import de.fhg.iais.roberta.visitor.hardware.IEdisonVisitor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * This class visits all the sensors/actors of the Edison brick and collects information about them
 */
public class EdisonUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IEdisonVisitor<Void> {

    public EdisonUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, Configuration robotConfiguration) {
        super(robotConfiguration);
        check(programPhrases);
    }



    /**
     * Blockly Blocks that need an extra helper method in the source code
     */
    public enum Method {
        AVG, //Average of a list
        CREATE_REPEAT, //Create a list with an integer x repeated n times
        MAX, //maximum of a given list
        MIN, //minimum of a list
        PRIME, //check if the given number is a prime number
        SUM, //Sum of a list
        OBSTACLEDETECTION, //Obstacle detection
        IRSEND, //IR sender
        IRSEEK, //IR seeker
        MOTORON, //Motor on / motor on for... block
        SHORTEN, //shorten a number for Edisons drive() methods
        ROUND, ROUND_UP, ROUND_DOWN, //round a number
        ABSOLUTE, //absolute value of a number
        POW10, //10^number
        CURVE, //for the steer block
        DIFFDRIVE, //for driving
        DIFFTURN //for turning

    }

    private HashSet<Method> usedMethods; //All needed helper methods as a Set


    /**
     * Visit the "Create List with repeated item" function so that their helper methods can be appended to the end of the source code
     *
     * @param listRepeat
     * @return
     */
    @Override public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        setListsUsed(true);
        usedMethod(Method.CREATE_REPEAT);
        return null;
    }

    /**
     * Visit the Number Property function (number is even/odd/prime/...) so that their helper methods can be appended to the end of the source code
     *
     * @param mathNumPropFunct
     * @return
     */
    @Override public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        if (mathNumPropFunct.getFunctName().getOpSymbol().equals("PRIME")) {
            usedMethod(Method.PRIME);
        }

        return null;
    }

    /**
     * Visit the Math on list function (sum/average/min/max) so that their helper methods can be appended to the end of the source code
     *
     * @param mathOnListFunct
     * @return
     */
    @Override public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch (mathOnListFunct.getFunctName().getOpSymbol()) {
            case "SUM":
                this.usedMethod(Method.SUM);
                break;
            case "MIN":
                this.usedMethod(Method.MIN);
                break;
            case "MAX":
                this.usedMethod(Method.MAX);
                break;
            case "AVERAGE":
                this.usedMethod(Method.SUM);
                this.usedMethod(Method.AVG);
                break;
            default:
                break;
        }

        return null;
    }

    /**
     *
     * @param motorOnAction
     * @return
     */
    @Override public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        usedMethod(Method.MOTORON);
        return null;
    }

    /**
     *
     * @param irSeekerSensor
     * @return
     */
    @Override public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        usedMethod(Method.IRSEEK);
        return null;
    }

    /**
     *
     * @param bluetoothSendAction to be visited
     * @return
     */
    @Override public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        usedMethod(Method.IRSEND);
        return null;
    }

    /**
     *
     * @param infraredSensor to be visited
     * @return
     */
    @Override public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        usedMethod(Method.OBSTACLEDETECTION);
        return null;
    }

    @Override public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        usedMethod(Method.IRSEND);
        return null;
    }

    @Override public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        usedMethod(Method.IRSEEK);
        return null;
    }

    @Override public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        return null;
    }

    @Override protected void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        super.check(phrasesSet);
    }

    @Override public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }

    @Override public Void visitDriveAction(DriveAction<Void> driveAction) {
        usedMethod(Method.DIFFDRIVE);
        return null;
    }

    @Override public Void visitCurveAction(CurveAction<Void> curveAction) {
        if (curveAction.getParamLeft().getDuration() != null) {
            usedMethod(Method.CURVE);
        }
        return null;
    }

    //TODO-MAX helper methods for wait-until block
    //TODO-MAX JavaDoc
    @Override public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        return super.visitWaitStmt(waitStmt);
    }

    @Override public Void visitTurnAction(TurnAction<Void> turnAction) {
        usedMethod(Method.DIFFTURN);
        return null;
    }

    /**
     * Returns all used helper methods (see {@link EdisonUsedHardwareCollectorVisitor#usedMethod(Method)}). If no methods are used,
     * this method returns a new empty Set
     *
     * @return
     */
    public Set<Method> getUsedMethods() {
        if (this.usedMethods == null) {
            //If no helper methods have been used, an empty set will be returned
            return new HashSet<>();
        }
        return this.usedMethods;
    }

    @Override public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ABS:
                usedMethod(Method.ABSOLUTE);
                break;
            case POW10:
                usedMethod(Method.POW10);
                break;
            case ROUND:
                usedMethod(Method.ROUND);
                break;
            case ROUNDUP:
                usedMethod(Method.ROUND_UP);
                break;
            case ROUNDDOWN:
                usedMethod(Method.ROUND_DOWN);
                break;
            default:
                break;
        }

        return null;
    }

    /**
     * Helper method to list all used "NEPO helper methods" so that they can be appended to the end of the source code
     *
     * @param m the helper method that was called
     */
    private void usedMethod(Method m) {
        if (this.usedMethods == null) {
            this.usedMethods = new HashSet<>();
            this.usedMethods.add(m);
        } else {
            this.usedMethods.add(m);
        }
    }

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    @Override public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        switch (sensorGetSample.getSensorTypeAndMode()) {
            case "INFRARED_OBSTACLEAHEAD":
            case "INFRARED_OBSTACLELEFT":
            case "INFRARED_OBSTACLERIGHT":
            case "INFRARED_OBSTACLENONE":
                this.usedMethod(Method.OBSTACLEDETECTION);
                break;
            case "IRSEEKER_RCCODE":
                this.usedMethod(Method.IRSEEK);
                break;
            default:
                break;
        }

        return null;
    }


    @Override public Void visitSensorResetAction(ResetSensor<Void> voidResetSensor) {
        return null;
    }
}
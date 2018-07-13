package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.CheckVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public abstract class RobotCommonCheckVisitor extends CheckVisitor implements AstActorMotorVisitor<Void>, AstSensorsVisitor<Void>, AstActorDisplayVisitor<Void>,
    AstActorLightVisitor<Void>, AstActorSoundVisitor<Void>, AstActorCommunicationVisitor<Void> {

    protected ArrayList<ArrayList<Phrase<Void>>> checkedProgram;
    protected int errorCount = 0;
    protected int warningCount = 0;
    protected Configuration brickConfiguration;

    public RobotCommonCheckVisitor(Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        collectGlobalVariables(phrasesSet);
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
        this.checkedProgram = phrasesSet;
    }

    /**
     * @return the checkedProgram
     */
    public ArrayList<ArrayList<Phrase<Void>>> getCheckedProgram() {
        return this.checkedProgram;
    }

    /**
     * @return the countErrors
     */
    public int getErrorCount() {
        return this.errorCount;
    }

    /**
     * @return the warningCount
     */
    public int getWarningCount() {
        return this.warningCount;
    }

    protected abstract void checkSensorPort(ExternalSensor<Void> sensor);

    @Override
    public Void visitVar(Var<Void> var) {
        String name = var.getValue();
        if ( !this.declaredVariables.contains(name) ) {
            var.addInfo(NepoInfo.error("VARIABLE_USED_BEFORE_DECLARATION"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkDiffDrive(driveAction);
        Expr<Void> speed = driveAction.getParam().getSpeed();
        speed.visit(this);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, driveAction);
        }
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkDiffDrive(turnAction);
        Expr<Void> speed = turnAction.getParam().getSpeed();
        speed.visit(this);
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, turnAction);
        }
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPort(motorGetPowerAction);
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        checkMotorPort(motorOnAction);
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(motorOnAction.getParam().getSpeed(), motorOnAction);
        }
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
        motorSetPowerAction.getPower().visit(this);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkDiffDrive(stopAction);
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorPort(colorSensor);
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( this.brickConfiguration.getActorOnPort((IActorPort) encoderSensor.getPort()) == null ) {
            encoderSensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> driveAction) {
        visitMotorDuration(driveAction.getParamLeft().getDuration());
        visitMotorDuration(driveAction.getParamRight().getDuration());
        driveAction.getParamLeft().getSpeed().visit(this);
        driveAction.getParamRight().getSpeed().visit(this);
        checkDiffDrive(driveAction);
        checkForZeroSpeedInCurve(driveAction.getParamLeft().getSpeed(), driveAction.getParamRight().getSpeed(), driveAction);

        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorPort(gyroSensor);
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        checkSensorPort(infraredSensor);
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        checkSensorPort(irSeekerSensor);
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        checkSensorPort(touchSensor);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        checkSensorPort(accelerometerSensor);
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        checkSensorPort(lightSensor);
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorPort(soundSensor);
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        checkSensorPort(compassSensor);
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    @Override
    public Void visitPinTouchSensor(PinTouchSensor<Void> pinTouchSensor) {
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        toneAction.getDuration().visit(this);
        toneAction.getFrequency().visit(this);
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        volumeAction.getVolume().visit(this);
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        sayTextAction.getMsg().visit(this);
        sayTextAction.getSpeed().visit(this);
        sayTextAction.getPitch().visit(this);
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        showPictureAction.getX().visit(this);
        showPictureAction.getY().visit(this);
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        showTextAction.getMsg().visit(this);
        showTextAction.getX().visit(this);
        showTextAction.getY().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        if ( bluetoothReceiveAction.getConnection() instanceof EmptyExpr ) {
            bluetoothReceiveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
            this.errorCount++;
        }
        bluetoothReceiveAction.getConnection().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        bluetoothConnectAction.getAddress().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        if ( bluetoothSendAction.getConnection() instanceof EmptyExpr ) {
            bluetoothSendAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
            this.errorCount++;
        }
        bluetoothSendAction.getConnection().visit(this);
        bluetoothSendAction.getMsg().visit(this);
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        bluetoothCheckConnectAction.getConnection().visit(this);
        return null;
    }

    private void checkDiffDrive(Phrase<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);
    }

    protected void checkMotorPort(MoveAction<Void> action) {
        if ( this.brickConfiguration.getActorOnPort(action.getPort()) == null ) {
            action.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
            this.errorCount++;
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        Actor leftMotor = this.brickConfiguration.getLeftMotor();
        Actor rightMotor = this.brickConfiguration.getRightMotor();
        checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
        checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
        checkLeftAndRightMotorRotationDirection(driveAction, leftMotor, rightMotor);
        checkNumberOfMotors(driveAction);
    }

    private void checkRightMotorPresenceAndRegulation(Phrase<Void> driveAction, Actor rightMotor) {
        if ( rightMotor == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
            this.errorCount++;
        } else {
            checkIfMotorRegulated(driveAction, rightMotor, "CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED");
        }
    }

    private void checkLeftMotorPresenceAndRegulation(Phrase<Void> driveAction, Actor leftMotor) {
        if ( leftMotor == null ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
            this.errorCount++;
        } else {
            checkIfMotorRegulated(driveAction, leftMotor, "CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED");
        }
    }

    private void checkLeftAndRightMotorRotationDirection(Phrase<Void> driveAction, Actor leftMotor, Actor rightMotor) {
        if ( (leftMotor != null) && (rightMotor != null) && (leftMotor.getRotationDirection() != rightMotor.getRotationDirection()) ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION"));
            this.errorCount++;
        }
    }

    private void checkNumberOfMotors(Phrase<Void> driveAction) {
        if ( this.brickConfiguration.getNumberOfRightMotors() > 1 ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS"));
            this.errorCount++;
        }
        if ( this.brickConfiguration.getNumberOfLeftMotors() > 1 ) {
            driveAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS"));
            this.errorCount++;
        }
    }

    private void checkIfMotorRegulated(Phrase<Void> driveAction, Actor motor, String errorMsg) {
        if ( !motor.isRegulated() ) {
            driveAction.addInfo(NepoInfo.error(errorMsg));
            this.errorCount++;
        }
    }

    private void checkForZeroSpeed(Expr<Void> speed, Action<Void> action) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst<Void> speedNumConst = (NumConst<Void>) speed;
            if ( Integer.valueOf(speedNumConst.getValue()) == 0 ) {
                action.addInfo(NepoInfo.warning("MOTOR_SPEED_0"));
                this.warningCount++;
            }
        }
    }

    private void checkForZeroSpeedInCurve(Expr<Void> speedLeft, Expr<Void> speedRight, Action<Void> action) {
        if ( speedLeft.getKind().hasName("NUM_CONST") && speedRight.getKind().hasName("NUM_CONST") ) {
            Double speedLeftNumConst = Double.valueOf(((NumConst<Void>) speedLeft).getValue());
            Double speedRightNumConst = Double.valueOf(((NumConst<Void>) speedRight).getValue());
            int signLeft = (int) Math.signum(speedLeftNumConst);
            int signRight = (int) Math.signum(speedRightNumConst);
            boolean sameSpeed = Math.abs(speedLeftNumConst) == Math.abs(speedRightNumConst); //NOSONAR : TODO: supply an delta of 0.1 (speed is in [0,100] ?
            if ( sameSpeed && (signLeft != signRight) && (signLeft != 0) && (signRight != 0) ) {
                action.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
                this.warningCount++;
            }
        }
    }

    private void visitMotorDuration(MotorDuration<Void> duration) {
        if ( duration != null ) {
            duration.getValue().visit(this);
        }
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        super.visitMethodReturn(methodReturn);
        if ( methodReturn.getReturnValue() instanceof EmptyExpr ) {
            methodReturn.addInfo(NepoInfo.error("ERROR_MISSING_RETURN"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        super.visitMethodCall(methodCall);
        boolean oneParamEmpty = false;
        for ( Expr<Void> expr : methodCall.getParametersValues().get() ) {
            oneParamEmpty = oneParamEmpty ? true : expr instanceof EmptyExpr;
        }
        if ( oneParamEmpty ) {
            methodCall.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        super.visitRepeatStmt(repeatStmt);
        if ( repeatStmt.getExpr() instanceof EmptyExpr ) {
            repeatStmt.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        } else if ( repeatStmt.getExpr() instanceof Unary ) {
            if ( ((Unary<Void>) repeatStmt.getExpr()).getExpr() instanceof EmptyExpr ) {
                repeatStmt.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
                this.errorCount++;
            }
        } else if ( repeatStmt.getExpr() instanceof Binary ) {
            if ( ((Binary<Void>) repeatStmt.getExpr()).getRight() instanceof EmptyExpr ) {
                repeatStmt.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
                this.errorCount++;
            }
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        super.visitWaitStmt(waitStmt);
        for ( Stmt<Void> stmt : waitStmt.getStatements().get() ) {
            for ( NepoInfo info : stmt.getInfos().getInfos() ) {
                waitStmt.addInfo(info);
            }
        }
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        super.visitIndexOfFunct(indexOfFunct);
        boolean oneParamEmpty = false;
        for ( Expr<Void> expr : indexOfFunct.getParam() ) {
            oneParamEmpty = oneParamEmpty ? true : expr instanceof EmptyExpr;
        }
        if ( oneParamEmpty ) {
            indexOfFunct.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        super.visitMathOnListFunct(mathOnListFunct);
        if ( mathOnListFunct.getParam().get(0) instanceof EmptyExpr ) {
            mathOnListFunct.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        super.visitLengthOfIsEmptyFunct(lengthOfIsEmptyFunct);
        if ( lengthOfIsEmptyFunct.getParam().get(0) instanceof EmptyExpr ) {
            lengthOfIsEmptyFunct.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        super.visitListRepeat(listRepeat);
        boolean oneParamEmpty = false;
        for ( Expr<Void> expr : listRepeat.getParam() ) {
            oneParamEmpty = oneParamEmpty ? true : expr instanceof EmptyExpr;
        }
        if ( oneParamEmpty ) {
            listRepeat.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( listGetIndex.getParam().get(0) instanceof EmptyExpr ) {
            listGetIndex.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        super.visitGetSubFunct(getSubFunct);
        if ( getSubFunct.getParam().get(0) instanceof EmptyExpr ) {
            getSubFunct.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        boolean oneParamEmpty = false;
        for ( Expr<Void> expr : listSetIndex.getParam() ) {
            oneParamEmpty = oneParamEmpty ? true : expr instanceof EmptyExpr;
        }
        if ( oneParamEmpty ) {
            listSetIndex.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        super.visitBinary(binary);
        if ( ((binary.getOp() == Binary.Op.MATH_CHANGE) || (binary.getOp() == Binary.Op.TEXT_APPEND)) && (binary.getLeft() instanceof EmptyExpr) ) {
            binary.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }

        if ( ((binary.getOp() == Binary.Op.AND) || (binary.getOp() == Binary.Op.OR))
            && ((binary.getLeft() instanceof EmptyExpr) || (binary.getRight() instanceof EmptyExpr)) ) {
            binary.addInfo(NepoInfo.error("ERROR_MISSING_PARAMETER"));
            this.errorCount++;
        }
        return null;
    }
}

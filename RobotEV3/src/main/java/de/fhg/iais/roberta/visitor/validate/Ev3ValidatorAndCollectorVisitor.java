package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.MoveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

public class Ev3ValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IEv3Visitor<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    public Ev3ValidatorAndCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        requiredComponentVisited(bluetoothCheckConnectAction, bluetoothCheckConnectAction.getConnection());
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        requiredComponentVisited(bluetoothConnectAction, bluetoothConnectAction.getAddress());
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        requiredComponentVisited(bluetoothReceiveAction, bluetoothReceiveAction.getConnection());
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        requiredComponentVisited(bluetoothSendAction, bluetoothSendAction.getConnection(), bluetoothSendAction.getMsg());
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        checkSensorPort(compassSensor);
        if ( this.robotConfiguration.getRobotName().equals("ev3dev") && (compassSensor.getMode().equals(SC.CALIBRATE)) ) {
            addWarningToPhrase(compassSensor, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        visitMotorDuration(curveAction.getParamLeft().getDuration());
        visitMotorDuration(curveAction.getParamRight().getDuration());

        requiredComponentVisited(curveAction, curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed());
        checkLeftRightMotorPort(curveAction);
        checkForZeroSpeedInCurve(curveAction.getParamLeft().getSpeed(), curveAction.getParamRight().getSpeed(), curveAction);
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        checkLeftRightMotorPort(driveAction);
        Expr<Void> speed = driveAction.getParam().getSpeed();
        requiredComponentVisited(driveAction, driveAction.getParam().getSpeed());

        speed.accept(this);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, driveAction);
        }
        visitMotorDuration(duration);

        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderSensor, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), configurationComponent.getComponentType()));
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        checkSensorPort(gyroSensor);
        if ( !gyroSensor.getMode().equals(SC.RESET) ) { // TODO why is this necessary?
            usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
            return null;
        }
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        checkSensorPort(htColorSensor);
        String mode = htColorSensor.getMode();
        usedHardwareBuilder.addUsedSensor(new UsedSensor(htColorSensor.getUserDefinedPort(), SC.HT_COLOR, mode));
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        checkSensorPort(irSeekerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(irSeekerSensor.getUserDefinedPort(), SC.IRSEEKER, irSeekerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        checkSensorPort(infraredSensor);
        String mode = infraredSensor.getMode();
        if ( infraredSensor.getMode().equals(SC.PRESENCE) ) {
            // TODO WHATT ?????
            mode = SC.SEEK;
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, mode));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
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
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        checkLeftRightMotorPort(stopAction);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        checkMotorPort(motorGetPowerAction);
        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorGetPowerAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorGetPowerAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        requiredComponentVisited(motorOnAction, motorOnAction.getParam().getSpeed());
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        if ( duration != null ) {
            // Instead of visitDuration ??
            requiredComponentVisited(motorOnAction, motorOnAction.getParam().getDuration().getValue());
        } else {
            checkForZeroSpeed(motorOnAction.getParam().getSpeed(), motorOnAction);
        }

        checkMotorPort(motorOnAction);
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(motorOnAction.getUserDefinedPort());
            boolean hasDuration = motorOnAction.getParam().getDuration() != null;
            if ( usedConfigurationBlock == null ) {
                addErrorToPhrase(motorOnAction, "CONFIGURATION_ERROR_ACTOR_MISSING");
            } else if ( SC.OTHER.equals(usedConfigurationBlock.getComponentType()) && hasDuration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }

        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorOnAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorOnAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        checkMotorPort(motorSetPowerAction);
        requiredComponentVisited(motorSetPowerAction, motorSetPowerAction.getPower());

        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorSetPowerAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorSetPowerAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        checkMotorPort(motorStopAction);
        ConfigurationComponent actor = this.robotConfiguration.getConfigurationComponent(motorStopAction.getUserDefinedPort());
        usedHardwareBuilder.addUsedActor(new UsedActor(motorStopAction.getUserDefinedPort(), actor.getComponentType()));
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        optionalComponentVisited(sayTextAction.getSpeed());
        optionalComponentVisited(sayTextAction.getPitch());
        requiredComponentVisited(sayTextAction, sayTextAction.getMsg());
        if ( this.robotConfiguration.getRobotName().equals("ev3lejosv0") ) {
            addWarningToPhrase(sayTextAction, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.VOICE, SC.VOICE));
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        optionalComponentVisited(showPictureAction.getX());
        optionalComponentVisited(showPictureAction.getY());
        usedHardwareBuilder.addUsedImage(showPictureAction.getPicture().toString());
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        requiredComponentVisited(toneAction, toneAction.getDuration(), toneAction.getFrequency());

        if ( toneAction.getDuration().getKind().hasName("NUM_CONST") ) {
            Double toneActionConst = Double.valueOf(((NumConst<Void>) toneAction.getDuration()).getValue());
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        checkSensorPort(touchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(touchSensor.getUserDefinedPort(), SC.TOUCH, touchSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        checkLeftRightMotorPort(turnAction);
        requiredComponentVisited(turnAction, turnAction.getParam().getSpeed());

        Expr<Void> speed = turnAction.getParam().getSpeed();
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        if ( duration != null ) {
            checkForZeroSpeed(speed, turnAction);
        }
        visitMotorDuration(duration);
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        // TODO Ansonsten nicht visiten ??
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            volumeAction.getVolume().accept(this);
        }
        return null;
    }

    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.getComponentType();
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( !type.equals("COLOR") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "TOUCH_SENSING":
                    if ( !type.equals("TOUCH") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !type.equals("ULTRASONIC") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( !type.equals("INFRARED") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !type.equals("GYRO") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "SOUND_SENSING":
                    if ( !type.equals("SOUND") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( !type.equals("LIGHT") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( !type.equals("COMPASS") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "IRSEEKER_SENSING":
                    if ( !type.equals("IRSEEKER") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "HTCOLOR_SENSING":
                    if ( !type.equals("HT_COLOR") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void checkLeftRightMotorPort(Phrase<Void> driveAction) {
        if ( validNumberOfMotors(driveAction) ) {
            ConfigurationComponent leftMotor = this.robotConfiguration.getFirstMotor(SC.LEFT);
            ConfigurationComponent rightMotor = this.robotConfiguration.getFirstMotor(SC.RIGHT);
            checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
            checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
            checkMotorRotationDirection(driveAction, leftMotor, rightMotor);
        }
    }

    protected boolean validNumberOfMotors(Phrase<Void> driveAction) {
        if ( this.robotConfiguration.getMotors(SC.RIGHT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS");
            return false;
        }
        if ( this.robotConfiguration.getMotors(SC.LEFT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS");
            return false;
        }
        return true;
    }

    private void checkRightMotorPresenceAndRegulation(Phrase<Void> driveAction, ConfigurationComponent rightMotor) {
        if ( rightMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, rightMotor, "CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED");
        }
    }

    private void checkLeftMotorPresenceAndRegulation(Phrase<Void> driveAction, ConfigurationComponent leftMotor) {
        if ( leftMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_LEFT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, leftMotor, "CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED");
        }
    }

    private void checkIfMotorRegulated(Phrase<Void> driveAction, ConfigurationComponent motor, String errorMsg) {
        if ( !motor.getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE) ) {
            addErrorToPhrase(driveAction, errorMsg);
        }
    }

    private void checkMotorRotationDirection(Phrase<Void> driveAction, ConfigurationComponent m1, ConfigurationComponent m2) {
        if ( (m1 != null) && (m2 != null) && !m1.getProperty(SC.MOTOR_REVERSE).equals(m2.getProperty(SC.MOTOR_REVERSE)) ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION");
        }
    }

    protected void checkMotorPort(MoveAction<Void> action) {
        if ( this.robotConfiguration.optConfigurationComponent(action.getUserDefinedPort()) == null ) {
            addErrorToPhrase(action, "CONFIGURATION_ERROR_MOTOR_MISSING");
        }
    }

    private void checkForZeroSpeed(Expr<Void> speed, Action<Void> action) {
        if ( speed.getKind().hasName("NUM_CONST") ) {
            NumConst<Void> speedNumConst = (NumConst<Void>) speed;
            if ( Math.abs(Double.valueOf(speedNumConst.getValue())) < DOUBLE_EPS ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private void checkForZeroSpeedInCurve(Expr<Void> speedLeft, Expr<Void> speedRight, Action<Void> action) {
        if ( speedLeft.getKind().hasName("NUM_CONST") && speedRight.getKind().hasName("NUM_CONST") ) {
            Double speedLeftNumConst = Double.valueOf(((NumConst<Void>) speedLeft).getValue());
            Double speedRightNumConst = Double.valueOf(((NumConst<Void>) speedRight).getValue());
            if ( (Math.abs(speedLeftNumConst) < DOUBLE_EPS) && (Math.abs(speedRightNumConst) < DOUBLE_EPS) ) {
                addWarningToPhrase(action, "BLOCK_NOT_EXECUTED");
            }
        }
    }

    private void visitMotorDuration(MotorDuration<Void> duration) {
        // TODO
        if ( duration != null ) {
            duration.getValue().accept(this);
        }
    }

    private void addLeftAndRightMotorToUsedActors() {
        //TODO: remove the check
        if ( this.robotConfiguration != null ) {
            String userDefinedLeftPortName = this.robotConfiguration.getFirstMotor("LEFT").getUserDefinedPortName();
            String userDefinedRightPortName = this.robotConfiguration.getFirstMotor("RIGHT").getUserDefinedPortName();
            if ( (userDefinedLeftPortName != null) && (userDefinedRightPortName != null) ) {
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedLeftPortName, SC.LARGE));
                usedHardwareBuilder.addUsedActor(new UsedActor(userDefinedRightPortName, SC.LARGE));
            }
        }
    }
}

package de.fhg.iais.roberta.visitor.validate;

import java.util.Objects;
import java.util.Optional;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
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
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassCalibrate;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.EV3DevMethods;
import de.fhg.iais.roberta.visitor.IEv3Visitor;

public class Ev3ValidatorAndCollectorVisitor extends MotorValidatorAndCollectorVisitor implements IEv3Visitor<Void> {

    private final boolean isSim;

    public Ev3ValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders, boolean isSim) {
        super(robotConfiguration, beanBuilders);
        this.isSim = isSim;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        requiredComponentVisited(bluetoothCheckConnectAction, bluetoothCheckConnectAction.connection);
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        requiredComponentVisited(bluetoothConnectAction, bluetoothConnectAction.address);
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        addToPhraseIfUnsupportedInSim(bluetoothReceiveAction, true, isSim);
        requiredComponentVisited(bluetoothReceiveAction, bluetoothReceiveAction.connection);
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        requiredComponentVisited(bluetoothSendAction, bluetoothSendAction.connection, bluetoothSendAction.msg);
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        checkSensorPort(colorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(colorSensor.getUserDefinedPort(), SC.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        addToPhraseIfUnsupportedInSim(compassSensor, true, isSim);
        checkSensorPort(compassSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        addToPhraseIfUnsupportedInSim(compassCalibrate, true, isSim);
        checkSensorPort(compassCalibrate);
        if ( this.robotConfiguration.getRobotName().equals("ev3dev") ) {
            addWarningToPhrase(compassCalibrate, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassCalibrate.getUserDefinedPort(), SC.COMPASS, SC.CALIBRATE));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderSensor.getUserDefinedPort());
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderSensor, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderSensor.getUserDefinedPort(), configurationComponent.componentType));
        }
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(encoderReset.sensorPort);
        if ( configurationComponent == null ) {
            addErrorToPhrase(encoderReset, "CONFIGURATION_ERROR_MOTOR_MISSING");
        } else {
            usedHardwareBuilder.addUsedActor(new UsedActor(encoderReset.sensorPort, configurationComponent.componentType));
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        checkSensorPort(gyroSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gyroSensor.getUserDefinedPort(), SC.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        return null;
    }


    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        addToPhraseIfUnsupportedInSim(htColorSensor, true, isSim);
        checkSensorPort(htColorSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(htColorSensor.getUserDefinedPort(), SC.HT_COLOR, htColorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        checkSensorPort(irSeekerSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(irSeekerSensor.getUserDefinedPort(), SC.IRSEEKER, irSeekerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        checkSensorPort(infraredSensor);
        String mode = infraredSensor.getMode();
        if ( infraredSensor.getMode().equals(SC.PRESENCE) ) {
            // TODO Why do we do this ?????
            mode = SC.SEEK;
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(infraredSensor.getUserDefinedPort(), SC.INFRARED, mode));
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        // TODO Shouldn't we do this: checkSensorPort(keysSensor);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        optionalComponentVisited(lightAction.rgbLedColor);
        usedHardwareBuilder.addUsedActor(new UsedActor(lightAction.port, SC.LIGHT));
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(lightStatusAction.getUserDefinedPort(), SC.LIGHT));
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(playNoteAction.port, SC.SOUND));
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        requiredComponentVisited(sayTextAction, sayTextAction.msg);
        if ( this.robotConfiguration.getRobotName().equals("ev3lejosv0") ) {
            addWarningToPhrase(sayTextAction, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.VOICE));
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        requiredComponentVisited(sayTextAction, sayTextAction.speed);
        requiredComponentVisited(sayTextAction, sayTextAction.pitch);
        requiredComponentVisited(sayTextAction, sayTextAction.msg);
        if ( this.robotConfiguration.getRobotName().equals("ev3lejosv0") ) {
            addWarningToPhrase(sayTextAction, "BLOCK_NOT_SUPPORTED");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.VOICE));
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction showPictureAction) {
        optionalComponentVisited(showPictureAction.x);
        optionalComponentVisited(showPictureAction.y);
        usedHardwareBuilder.addUsedImage(showPictureAction.pic.toString());
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        requiredComponentVisited(showTextAction, showTextAction.msg, showTextAction.x, showTextAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor(showTextAction.port, SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        addToPhraseIfUnsupportedInSim(soundSensor, true, isSim);
        checkSensorPort(soundSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(soundSensor.getUserDefinedPort(), SC.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);

        if ( toneAction.duration.getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst) toneAction.duration).value);
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        usedHardwareBuilder.addUsedActor(new UsedActor(toneAction.port, SC.SOUND));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        checkSensorPort(touchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(touchSensor.getUserDefinedPort(), SC.TOUCH, touchSensor.getMode()));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        checkSensorPort(ultrasonicSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(ultrasonicSensor.getUserDefinedPort(), SC.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        requiredComponentVisited(setVolumeAction, setVolumeAction.volume);
        usedHardwareBuilder.addUsedActor(new UsedActor(BlocklyConstants.EMPTY_PORT, SC.SOUND));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        if ( Objects.equals(robotConfiguration.getRobotName(), "ev3dev") ) {
            /*
             Only add the helper methods for "ev3dev". Following methods are needed for visitCurveAction:
             -  EV3DevMethods.BUSY_WAIT, EV3DevMethods.CLAMP, EV3DevMethods.DRIVE_IN_CURVE, EV3DevMethods.SCALE_SPEED
            */
            usedMethodBuilder.addUsedMethod(EV3DevMethods.BUSY_WAIT);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.CLAMP);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.DRIVE_IN_CURVE);
            usedMethodBuilder.addUsedMethod(EV3DevMethods.SCALE_SPEED);
        }
        requiredComponentVisited(curveAction, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed());
        Optional.ofNullable(curveAction.paramLeft.getDuration())
            .ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        Optional.ofNullable(curveAction.paramRight.getDuration())
            .ifPresent(duration -> requiredComponentVisited(curveAction, duration.getValue()));
        checkForZeroSpeedInCurve(curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed(), curveAction);
        checkLeftRightMotorPort(curveAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        checkAndVisitMotionParam(turnAction, turnAction.param);
        checkLeftRightMotorPort(turnAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        checkAndVisitMotionParam(driveAction, driveAction.param);
        checkLeftRightMotorPort(driveAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        checkLeftRightMotorPort(stopAction);
        addLeftAndRightMotorToUsedActors();
        return null;
    }

    private void checkLeftRightMotorPort(Phrase driveAction) {
        if ( hasTooManyMotors(driveAction) ) {
            return;
        }

        ConfigurationComponent leftMotor = this.robotConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.robotConfiguration.getFirstMotor(SC.RIGHT);
        checkLeftMotorPresenceAndRegulation(driveAction, leftMotor);
        checkRightMotorPresenceAndRegulation(driveAction, rightMotor);
        checkMotorRotationDirection(driveAction, leftMotor, rightMotor);
    }

    protected boolean hasTooManyMotors(Phrase driveAction) {
        if ( this.robotConfiguration.getMotors(SC.RIGHT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_RIGHT_MOTORS");
            return true;
        }
        if ( this.robotConfiguration.getMotors(SC.LEFT).size() > 1 ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MULTIPLE_LEFT_MOTORS");
            return true;
        }
        return false;
    }

    private void checkRightMotorPresenceAndRegulation(Phrase driveAction, ConfigurationComponent rightMotor) {
        if ( rightMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, rightMotor, "CONFIGURATION_ERROR_MOTOR_RIGHT_UNREGULATED");
        }
    }

    private void checkLeftMotorPresenceAndRegulation(Phrase driveAction, ConfigurationComponent leftMotor) {
        if ( leftMotor == null ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTOR_LEFT_MISSING");
        } else {
            checkIfMotorRegulated(driveAction, leftMotor, "CONFIGURATION_ERROR_MOTOR_LEFT_UNREGULATED");
        }
    }

    private void checkIfMotorRegulated(Phrase driveAction, ConfigurationComponent motor, String errorMsg) {
        if ( !motor.getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE) ) {
            addErrorToPhrase(driveAction, errorMsg);
        }
    }

    private void checkMotorRotationDirection(Phrase driveAction, ConfigurationComponent leftMotor, ConfigurationComponent rightMotor) {
        if ( (leftMotor == null) || (rightMotor == null) ) {
            return;
        }

        boolean rotationDirectionsEqual = leftMotor.getProperty(SC.MOTOR_REVERSE).equals(rightMotor.getProperty(SC.MOTOR_REVERSE));
        if ( !rotationDirectionsEqual ) {
            addErrorToPhrase(driveAction, "CONFIGURATION_ERROR_MOTORS_ROTATION_DIRECTION");
        }
    }

    private void checkForZeroSpeedInCurve(Expr speedLeft, Expr speedRight, Action action) {
        if ( speedLeft.getKind().hasName("NUM_CONST") && speedRight.getKind().hasName("NUM_CONST") ) {
            double speedLeftNumConst = Double.parseDouble(((NumConst) speedLeft).value);
            double speedRightNumConst = Double.parseDouble(((NumConst) speedRight).value);

            boolean bothMotorsHaveZeroSpeed = (Math.abs(speedLeftNumConst) < DOUBLE_EPS) && (Math.abs(speedRightNumConst) < DOUBLE_EPS);
            if ( bothMotorsHaveZeroSpeed ) {
                addWarningToPhrase(action, "MOTOR_SPEED_0");
            }
        }
    }

    private void addLeftAndRightMotorToUsedActors() {
        Optional<String> optionalLeftPort = Optional.ofNullable(robotConfiguration.getFirstMotor(SC.LEFT))
            .map(configurationComponent1 -> configurationComponent1.userDefinedPortName);

        Optional<String> optionalRightPort = Optional.ofNullable(robotConfiguration.getFirstMotor(SC.RIGHT))
            .map(configurationComponent -> configurationComponent.userDefinedPortName);

        if ( optionalLeftPort.isPresent() && optionalRightPort.isPresent() ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(optionalLeftPort.get(), SC.LARGE));
            usedHardwareBuilder.addUsedActor(new UsedActor(optionalRightPort.get(), SC.LARGE));
        }
    }

    protected void checkSensorPort(ExternalSensor sensor) {
        ConfigurationComponent usedSensor = this.robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        if ( usedSensor == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.componentType;
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
                case "COMPASS_CALIBRATE":
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

}

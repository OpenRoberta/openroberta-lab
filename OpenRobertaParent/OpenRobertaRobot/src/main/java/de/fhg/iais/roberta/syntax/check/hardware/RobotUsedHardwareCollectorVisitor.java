package de.fhg.iais.roberta.syntax.check.hardware;

import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
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
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction.Mode;
import de.fhg.iais.roberta.syntax.check.CheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public abstract class RobotUsedHardwareCollectorVisitor extends CheckVisitor implements AstSensorsVisitor<Void>, AstActorMotorVisitor<Void>,
    AstActorDisplayVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void>, AstActorCommunicationVisitor<Void> {
    protected final Set<UsedSensor> usedSensors = new LinkedHashSet<>();
    protected final Set<UsedActor> usedActors = new LinkedHashSet<>();

    protected final Configuration brickConfiguration;

    protected boolean isTimerSensorUsed;

    public RobotUsedHardwareCollectorVisitor(Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    /**
     * Returns set of used sensor in Blockly program.
     *
     * @return set of used sensors
     */
    public Set<UsedSensor> getUsedSensors() {
        return this.usedSensors;
    }

    /**
     * Returns set of used actors in Blockly program.
     *
     * @return set of used actors
     */
    public Set<UsedActor> getUsedActors() {
        return this.usedActors;
    }

    public boolean isTimerSensorUsed() {
        return this.isTimerSensorUsed;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.usedSensors.add(
            new UsedSensor(
                (ISensorPort) colorSensor.getPort(),
                SensorType.COLOR,
                ColorSensorMode.valueOf(((ColorSensorMode) colorSensor.getMode()).getModeValue())));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.usedSensors.add(
            new UsedSensor(
                (ISensorPort) lightSensor.getPort(),
                SensorType.LIGHT,
                LightSensorMode.valueOf(((LightSensorMode) lightSensor.getMode()).getModeValue())));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) soundSensor.getPort(), SensorType.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) temperatureSensor.getPort(), SensorType.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) voltageSensor.getPort(), SensorType.VOLTAGE, voltageSensor.getMode()));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(encoderSensor.getPort());
            if ( actor != null ) {
                this.usedActors.add(new UsedActor((IActorPort) encoderSensor.getPort(), actor.getName()));
            }
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) gyroSensor.getPort(), SensorType.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) infraredSensor.getPort(), SensorType.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) irSeekerSensor.getPort(), SensorType.IRSEEKER, irSeekerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) touchSensor.getPort(), SensorType.TOUCH, TouchSensorMode.TOUCH));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) ultrasonicSensor.getPort(), SensorType.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) humiditySensor.getPort(), SensorType.HUMIDITY, humiditySensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) compassSensor.getPort(), SensorType.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) motionSensor.getPort(), SensorType.MOTION, motionSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) dropSensor.getPort(), SensorType.DROP, dropSensor.getMode()));
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> mositureSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) mositureSensor.getPort(), SensorType.MOISTURE, mositureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometerSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) accelerometerSensor.getPort(), SensorType.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) pulseSensor.getPort(), SensorType.PULSE, pulseSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        this.usedSensors.add(new UsedSensor((ISensorPort) rfidSensor.getPort(), SensorType.RFID, rfidSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        this.isTimerSensorUsed = true;
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().visit(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            if ( (this.brickConfiguration.getLeftMotorPort() != null) && (this.brickConfiguration.getRightMotorPort() != null) ) {
                this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
                this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
            }
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().visit(this);
        curveAction.getParamRight().getSpeed().visit(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            if ( (this.brickConfiguration.getLeftMotorPort() != null) && (this.brickConfiguration.getRightMotorPort() != null) ) {
                this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
                this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
            }
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().visit(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            if ( (this.brickConfiguration.getLeftMotorPort() != null) && (this.brickConfiguration.getRightMotorPort() != null) ) {
                this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
                this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
            }
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorGetPowerAction.getPort());
            if ( actor != null ) {
                this.usedActors.add(new UsedActor(motorGetPowerAction.getPort(), actor.getName()));
            }
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorOnAction.getPort());
            if ( actor != null ) {
                this.usedActors.add(new UsedActor(motorOnAction.getPort(), actor.getName()));
            }
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        motorSetPowerAction.getPower().visit(this);
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorSetPowerAction.getPort());
            if ( actor != null ) {
                this.usedActors.add(new UsedActor(motorSetPowerAction.getPort(), actor.getName()));
            }
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( this.brickConfiguration != null ) {
            if ( this.brickConfiguration.getActors() != null ) {
                Actor actor = this.brickConfiguration.getActors().get(motorStopAction.getPort());
                if ( actor != null ) {
                    this.usedActors.add(new UsedActor(motorStopAction.getPort(), actor.getName()));
                }
            }
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        if ( this.brickConfiguration != null ) {
            if ( (this.brickConfiguration.getLeftMotorPort() != null) && (this.brickConfiguration.getRightMotorPort() != null) ) {
                this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
                this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
            }
        }
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
    public Void visitLightAction(LightAction<Void> lightAction) {
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
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
        if ( volumeAction.getMode() == Mode.SET ) {
            volumeAction.getVolume().visit(this);
        }
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
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
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

}

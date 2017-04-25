package de.fhg.iais.roberta.syntax.hardwarecheck.ev3;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.CheckVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareVisitor extends CheckVisitor {
    private final Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();
    private final Set<UsedActor> usedActors = new LinkedHashSet<UsedActor>();
    /* drive/turn/curve blocks don't declare the motors they are using */
    private final Configuration brickConfiguration;

    public UsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        this.brickConfiguration = null;
        check(phrasesSet);
    }

    public UsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
        check(phrasesSet);
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

    private void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().visit(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
            this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
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
            this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
            this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> driveAction) {
        if ( this.brickConfiguration != null ) {
            this.usedActors.add(new UsedActor(this.brickConfiguration.getLeftMotorPort(), ActorType.LARGE));
            this.usedActors.add(new UsedActor(this.brickConfiguration.getRightMotorPort(), ActorType.LARGE));
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorGetPowerAction.getPort());
            this.usedActors.add(new UsedActor(motorGetPowerAction.getPort(), actor.getName()));
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
            this.usedActors.add(new UsedActor(motorOnAction.getPort(), actor.getName()));
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        motorSetPowerAction.getPower().visit(this);
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorSetPowerAction.getPort());
            this.usedActors.add(new UsedActor(motorSetPowerAction.getPort(), actor.getName()));
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(motorStopAction.getPort());
            this.usedActors.add(new UsedActor(motorStopAction.getPort(), actor.getName()));
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.usedSensors.add(new UsedSensor(colorSensor.getPort(), SensorType.COLOR, colorSensor.getMode()));
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        if ( this.brickConfiguration != null ) {
            Actor actor = this.brickConfiguration.getActors().get(encoderSensor.getMotorPort());
            this.usedActors.add(new UsedActor(encoderSensor.getMotorPort(), actor.getName()));
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.usedSensors.add(new UsedSensor(gyroSensor.getPort(), SensorType.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedSensors.add(new UsedSensor(infraredSensor.getPort(), SensorType.INFRARED, infraredSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.usedSensors.add(new UsedSensor(touchSensor.getPort(), SensorType.TOUCH, touchSensor.getMode()));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.usedSensors.add(new UsedSensor(ultrasonicSensor.getPort(), SensorType.ULTRASONIC, ultrasonicSensor.getMode()));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.usedSensors.add(new UsedSensor(lightSensor.getPort(), SensorType.LIGHT, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.usedSensors.add(new UsedSensor(soundSensor.getPort(), SensorType.SOUND, soundSensor.getMode()));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        // TODO Auto-generated method stub
        return null;
    }

}

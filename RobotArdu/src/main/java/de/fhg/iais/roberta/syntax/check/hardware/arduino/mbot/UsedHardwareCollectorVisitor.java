package de.fhg.iais.roberta.syntax.check.hardware.arduino.mbot;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.arduino.MbotConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.action.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.sensor.arduino.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.Joystick;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.PIRMotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.arduino.MbotAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements MbotAstVisitor<Void> {
    private boolean isToneActionUsed = false;
    private boolean isTemperatureSensorUsed = false;

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, MbotConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    public boolean isToneActionUsed() {
        return this.isToneActionUsed;
    }

    public boolean isTemperatureSensorUsed() {
        return this.isTemperatureSensorUsed;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.usedSensors.add(new UsedSensor(temperatureSensor.getPort(), SensorType.TEMPERATURE, null));
        this.isTemperatureSensorUsed = true;
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        this.usedSensors.add(new UsedSensor(joystick.getPort(), SensorType.JOYSTICK, null));
        return null;
    }

    @Override
    public Void visitAmbientLightSensor(AmbientLightSensor<Void> lightSensor) {
        this.usedSensors.add(new UsedSensor(lightSensor.getPort(), SensorType.AMBIENT_LIGHT, null));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.usedSensors.add(new UsedSensor(lightSensor.getPort(), SensorType.LINE_FOLLOWER, lightSensor.getMode()));
        return null;
    }

    @Override
    public Void visitAccelerometer(Accelerometer<Void> accelerometer) {
        this.usedSensors.add(new UsedSensor(accelerometer.getPort(), SensorType.ACCELEROMETER, accelerometer.getCoordinate()));
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.usedSensors.add(new UsedSensor(gyroSensor.getPort(), SensorType.GYRO, gyroSensor.getMode()));
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        this.usedSensors.add(new UsedSensor(flameSensor.getPort(), SensorType.FLAMESENSOR, null));
        return null;
    }

    @Override
    public Void visitPIRMotionSensor(PIRMotionSensor<Void> motionSensor) {
        this.usedSensors.add(new UsedSensor(motionSensor.getPort(), SensorType.PIR_MOTION, null));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        super.visitToneAction(toneAction);
        this.isToneActionUsed = true;
        return null;
    }

    @Override
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        this.usedActors.add(new UsedActor(null, ActorType.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        this.usedActors.add(new UsedActor(null, ActorType.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
        // TODO Auto-generated method stub
        return null;
    }

}

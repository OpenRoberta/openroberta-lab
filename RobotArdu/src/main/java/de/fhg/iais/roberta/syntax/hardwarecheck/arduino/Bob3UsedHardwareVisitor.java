package de.fhg.iais.roberta.syntax.hardwarecheck.arduino;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.CheckVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class Bob3UsedHardwareVisitor extends CheckVisitor implements Bob3AstVisitor<Void> {
    private final Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();
    private final Set<UsedActor> usedActors = new LinkedHashSet<UsedActor>();

    private boolean isTimerSensorUsed;
    private boolean isTemperatureSensorUsed;

    private Configuration brickConfiguration;

    public Bob3UsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        check(phrasesSet);
    }

    public Bob3UsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
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

    public boolean isTimerSensorUsed() {
        return this.isTimerSensorUsed;
    }

    public boolean isTemperatureSensorUsed() {
        return this.isTemperatureSensorUsed;
    }

    @Override
    protected void check(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                phrase.visit(this);
            }
        }
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        this.isTimerSensorUsed = true;
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
        this.usedSensors.add(new UsedSensor(null, SensorType.GYRO, null));
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.usedSensors.add(new UsedSensor(infraredSensor.getPort(), SensorType.INFRARED, null));
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.usedSensors.add(new UsedSensor(touchSensor.getPort(), SensorType.TOUCH, null));
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.usedSensors.add(new UsedSensor(ultrasonicSensor.getPort(), SensorType.ULTRASONIC, null));
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.usedSensors.add(new UsedSensor(lightSensor.getPort(), SensorType.LIGHT, null));
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.usedSensors.add(new UsedSensor(soundSensor.getPort(), SensorType.SOUND, null));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.usedSensors.add(new UsedSensor(temperatureSensor.getPort(), SensorType.TEMPERATURE, null));
        this.isTemperatureSensorUsed = true;
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        this.usedSensors.add(new UsedSensor(null, SensorType.COMPASS, null));
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        return null;
    }

}

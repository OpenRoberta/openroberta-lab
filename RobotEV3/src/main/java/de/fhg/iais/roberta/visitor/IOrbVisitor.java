package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;

public interface IOrbVisitor<V> extends IActors4AutonomousDriveRobots<V> {

    V visitKeysSensor(KeysSensor keysSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitGyroReset(GyroReset gyroReset);

    Void visitEncoderReset(EncoderReset encoderReset);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }
}

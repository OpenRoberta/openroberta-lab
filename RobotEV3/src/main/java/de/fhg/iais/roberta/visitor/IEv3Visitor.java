package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.hardware.actor.IActors4AutonomousDriveRobots;
import de.fhg.iais.roberta.visitor.hardware.actor.IBluetoothVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ISpeechVisitor;

public interface IEv3Visitor<V> extends IActors4AutonomousDriveRobots<V>, IBluetoothVisitor<V>, ISpeechVisitor<V> {
    V visitShowPictureAction(ShowPictureAction showPictureAction);

    V visitKeysSensor(KeysSensor keysSensor);

    V visitColorSensor(ColorSensor colorSensor);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitEncoderSensor(EncoderSensor encoderSensor);

    V visitGyroSensor(GyroSensor gyroSensor);

    V visitInfraredSensor(InfraredSensor infraredSensor);

    V visitTimerSensor(TimerSensor timerSensor);

    V visitTouchSensor(TouchSensor touchSensor);

    V visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor);

    V visitCompassSensor(CompassSensor compassSensor);

    V visitIRSeekerSensor(IRSeekerSensor irSeekerSensor);

    V visitHTColorSensor(HTColorSensor htColorSensor);

    default V visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return sensorGetSample.sensor.accept(this);
    }
}

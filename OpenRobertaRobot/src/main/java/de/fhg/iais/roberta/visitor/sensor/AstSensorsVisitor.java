package de.fhg.iais.roberta.visitor.sensor;

import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.AstVisitor;

public interface AstSensorsVisitor<V> extends AstVisitor<V> {
    /**
     * visit a {@link BrickSensor}.
     *
     * @param brickSensor to be visited
     */
    V visitBrickSensor(BrickSensor<V> brickSensor);

    /**
     * visit a {@link ColorSensor}.
     *
     * @param colorSensor to be visited
     */
    V visitColorSensor(ColorSensor<V> colorSensor);

    /**
     * visit a {@link LightSensor}.
     *
     * @param colorSensor to be visited
     */
    V visitLightSensor(LightSensor<V> lightSensor);

    /**
     * visit a {@link SoundSensor}.
     *
     * @param colorSensor to be visited
     */
    V visitSoundSensor(SoundSensor<V> soundSensor);

    /**
     * visit a {@link EncoderSensor}.
     *
     * @param encoderSensor to be visited
     */
    V visitEncoderSensor(EncoderSensor<V> encoderSensor);

    /**
     * visit a {@link GyroSensor}.
     *
     * @param gyroSensor to be visited
     */
    V visitGyroSensor(GyroSensor<V> gyroSensor);

    /**
     * visit a {@link InfraredSensor}.
     *
     * @param infraredSensor to be visited
     */
    V visitInfraredSensor(InfraredSensor<V> infraredSensor);

    /**
     * visit a {@link TimerSensor}.
     *
     * @param timerSensor to be visited
     */
    V visitTimerSensor(TimerSensor<V> timerSensor);

    /**
     * visit a {@link TouchSensor}.
     *
     * @param touchSensor to be visited
     */
    V visitTouchSensor(TouchSensor<V> touchSensor);

    /**
     * visit a {@link UltrasonicSensor}.
     *
     * @param ultrasonicSensor to be visited
     */
    V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor);

    /**
     * visit a {@link CompassSensor}.
     *
     * @param compassSensor to be visited
     */
    V visitCompassSensor(CompassSensor<V> compassSensor);

    /**
     * visit a {@link GetSampleSensor}.
     *
     * @param sensorGetSample to be visited
     */
    default V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        sensorGetSample.getSensor().visit(this);
        return null;
    }

}

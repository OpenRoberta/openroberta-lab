package de.fhg.iais.roberta.visitor.arduino;

import de.fhg.iais.roberta.syntax.sensor.arduino.botnroll.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.Accelerometer;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.AmbientLightSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.Joystick;
import de.fhg.iais.roberta.syntax.sensor.arduino.mbot.PIRMotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

public interface MbotAstVisitor<V> extends ArduinoAstVisitor<V> {
    /**
     * visit a {@link VoltageSensor}.
     *
     * @param temperatureSensor to be visited
     */
    @Override
    V visitTemperatureSensor(TemperatureSensor<V> temperatureSensor);

    V visitAmbientLightSensor(AmbientLightSensor<V> lightSensor);

    V visitPIRMotionSensor(PIRMotionSensor<V> motionSensor);

    V visitJoystick(Joystick<V> joystick);

    V visitAccelerometer(Accelerometer<V> accelerometer);

    V visitFlameSensor(FlameSensor<V> flameSensor);

}

package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;

public interface INano33BleSensorVisitor<V> extends IArduinoVisitor<V> {
    V visitLsm9ds1AccSensor(Lsm9ds1AccSensor sensor);

    V visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor sensor);

    V visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor sensor);

    V visitApds9960DistanceSensor(Apds9960DistanceSensor sensor);

    V visitApds9960GestureSensor(Apds9960GestureSensor sensor);

    V visitApds9960ColorSensor(Apds9960ColorSensor sensor);

    V visitLps22hbPressureSensor(Lps22hbPressureSensor sensor);

    V visitHts221TemperatureSensor(Hts221TemperatureSensor sensor);

    V visitHts221HumiditySensor(Hts221HumiditySensor sensor);
}

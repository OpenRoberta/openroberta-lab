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
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface INano33BleSensorVisitor<V> {
    V visitLsm9ds1AccSensor(Lsm9ds1AccSensor<V> sensor);

    V visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<V> sensor);

    V visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<V> sensor);

    V visitApds9960DistanceSensor(Apds9960DistanceSensor<V> sensor);

    V visitApds9960GestureSensor(Apds9960GestureSensor<V> sensor);

    V visitApds9960ColorSensor(Apds9960ColorSensor<V> sensor);

    V visitLps22hbPressureSensor(Lps22hbPressureSensor<V> sensor);

    V visitHts221TemperatureSensor(Hts221TemperatureSensor<V> sensor);

    V visitHts221HumiditySensor(Hts221HumiditySensor<V> sensor);
}

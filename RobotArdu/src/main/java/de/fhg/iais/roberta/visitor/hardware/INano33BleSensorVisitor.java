package de.fhg.iais.roberta.visitor.hardware;

import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.*;
import de.fhg.iais.roberta.util.dbc.DbcException;

public interface INano33BleSensorVisitor<V> {
    default V visitLsm9ds1AccSensor(Lsm9ds1AccSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitApds9960DistanceSensor(Apds9960DistanceSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitApds9960GestureSensor(Apds9960GestureSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitApds9960ColorSensor(Apds9960ColorSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitLps22hbPressureSensor(Lps22hbPressureSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitHts221TemperatureSensor(Hts221TemperatureSensor<V> sensor) {
        throw new DbcException("Not supported!");
    }

    default V visitHts221HumiditySensor(Hts221HumiditySensor<V> sensor) {
        throw new DbcException("Not supported!");
    }
}

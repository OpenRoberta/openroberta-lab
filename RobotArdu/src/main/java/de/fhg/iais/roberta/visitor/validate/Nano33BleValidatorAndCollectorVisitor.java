package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.visitor.hardware.INano33BleSensorVisitor;

public class Nano33BleValidatorAndCollectorVisitor extends ArduinoValidatorAndCollectorVisitor implements INano33BleSensorVisitor<Void> {

    public Nano33BleValidatorAndCollectorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }


    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getR(), sensor.getG(), sensor.getB());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getDistance());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getGesture());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getHumidity());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getTemperature());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getPressure());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LPS22HB, SC.LPS22HB));
        return null;
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getX(), sensor.getY(), sensor.getZ());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.x, sensor.y, sensor.z);
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> sensor) {
        requiredComponentVisited(sensor, sensor.getX(), sensor.getY(), sensor.getZ());
        usedHardwareBuilder.addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }


}

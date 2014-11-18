package de.fhg.iais.roberta.hardwarecomponents.ev3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.hardwarecomponents.Category;
import de.fhg.iais.roberta.hardwarecomponents.HardwareComponentType;

/**
 * This class represents all possible EV3 sensors.
 */
public final class HardwareComponentEV3Sensor extends HardwareComponentType {
    public static final HardwareComponentEV3Sensor EV3_COLOR_SENSOR = new HardwareComponentEV3Sensor("robBrick_colour");
    public static final HardwareComponentEV3Sensor EV3_TOUCH_SENSOR = new HardwareComponentEV3Sensor("robBrick_touch");
    public static final HardwareComponentEV3Sensor EV3_ULTRASONIC_SENSOR = new HardwareComponentEV3Sensor("robBrick_ultrasonic");
    public static final HardwareComponentEV3Sensor EV3_IR_SENSOR = new HardwareComponentEV3Sensor("robBrick_infrared");
    public static final HardwareComponentEV3Sensor EV3_GYRO_SENSOR = new HardwareComponentEV3Sensor("robBrick_gyro");

    private HardwareComponentEV3Sensor(String name) {
        super(name, Category.SENSOR);
    }

    /**
     * Find EV3 sensor by name of blockly block
     *
     * @param blocklyName
     * @return EV3 sensor
     */
    public static HardwareComponentEV3Sensor find(String blockly_name) {
        Iterator<HardwareComponentEV3Sensor> iter = SENSORS.iterator();
        while ( iter.hasNext() ) {
            HardwareComponentEV3Sensor sensor = iter.next();
            if ( blockly_name.equals(sensor.getName()) ) {
                return sensor;
            }
        }
        throw new DbcException("There is no sensor with the following attribute: " + blockly_name);
    }

    private static final HardwareComponentEV3Sensor[] values = {
        EV3_COLOR_SENSOR, EV3_TOUCH_SENSOR, EV3_ULTRASONIC_SENSOR, EV3_IR_SENSOR, EV3_GYRO_SENSOR
    };

    public static final List<HardwareComponentEV3Sensor> SENSORS = Collections.unmodifiableList(Arrays.asList(values));

    @Override
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + getTypeName();
    }

    @Override
    public String getTypeName() {
        int counter = 0;
        for ( HardwareComponentEV3Sensor sensor : SENSORS ) {
            if ( sensor.getName().equals(this.getName()) ) {
                return this.getClass().getFields()[counter].getName();
            }
            counter++;
        }
        return null;
    }
}

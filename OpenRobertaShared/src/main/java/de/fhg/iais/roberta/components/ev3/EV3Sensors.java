package de.fhg.iais.roberta.components.ev3;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.HardwareComponentType;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents all possible EV3 sensors.
 */
public final class EV3Sensors extends HardwareComponentType {
    public static final EV3Sensors EV3_COLOR_SENSOR = new EV3Sensors("robBrick_colour", "color");
    public static final EV3Sensors EV3_TOUCH_SENSOR = new EV3Sensors("robBrick_touch", "touch");
    public static final EV3Sensors EV3_ULTRASONIC_SENSOR = new EV3Sensors("robBrick_ultrasonic", "ultrasonic");
    public static final EV3Sensors EV3_IR_SENSOR = new EV3Sensors("robBrick_infrared", "infrared");
    public static final EV3Sensors EV3_GYRO_SENSOR = new EV3Sensors("robBrick_gyro", "gyro");

    private EV3Sensors(String name, String shortName) {
        super(name, shortName, Category.SENSOR);
    }

    /**
     * Find EV3 sensor by name of blockly block
     *
     * @param blocklyName
     * @return EV3 sensor
     */
    public static EV3Sensors find(String blockly_name) {
        Iterator<EV3Sensors> iter = SENSORS.iterator();
        while ( iter.hasNext() ) {
            EV3Sensors sensor = iter.next();
            if ( blockly_name.equals(sensor.getName()) ) {
                return sensor;
            }
        }
        throw new DbcException("There is no sensor with the following attribute: " + blockly_name);
    }

    private static final EV3Sensors[] values = {
        EV3_COLOR_SENSOR, EV3_TOUCH_SENSOR, EV3_ULTRASONIC_SENSOR, EV3_IR_SENSOR, EV3_GYRO_SENSOR
    };

    public static final List<EV3Sensors> SENSORS = Collections.unmodifiableList(Arrays.asList(values));

    @Override
    public String getJavaCode() {
        return this.getClass().getSimpleName() + "." + getTypeName();
    }

    @Override
    public String getTypeName() {
        int counter = 0;
        for ( EV3Sensors sensor : SENSORS ) {
            if ( sensor.getName().equals(this.getName()) ) {
                return this.getClass().getFields()[counter].getName();
            }
            counter++;
        }
        return null;
    }

}

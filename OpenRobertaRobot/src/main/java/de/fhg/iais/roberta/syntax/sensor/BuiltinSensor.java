package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public abstract class BuiltinSensor<V> extends Sensor<V> {

    /**
     * This constructor expects no mode, port and slot. Used for internlal sensors, which don't need such things.
     */
    public BuiltinSensor(BlocklyProperties properties, ExternalSensorBean metaDataBean) {
        super(properties);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " []";
    }
}

package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public abstract class InternalSensor extends Sensor {

    /**
     * Under construction :-)
     */
    public InternalSensor(BlocklyProperties properties, ExternalSensorBean metaDataBean) {
        super(properties);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " []";
    }
}

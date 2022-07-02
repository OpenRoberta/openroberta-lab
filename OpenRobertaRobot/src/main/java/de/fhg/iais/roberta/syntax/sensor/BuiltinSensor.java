package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public abstract class BuiltinSensor<V> extends Sensor<V> {

    /**
     * This constructor expects no mode, port and slot. Used for internlal sensors, which don't need such things.
     */
    public BuiltinSensor(BlocklyBlockProperties properties, BlocklyComment comment, ExternalSensorBean metaDataBean) {
        super(properties, comment);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " []";
    }
}

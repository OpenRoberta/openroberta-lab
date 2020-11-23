package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;

public abstract class BuiltinSensor<V> extends Sensor<V> {

    /**
     * This constructor expects no mode, port and slot. Used for internlal sensors, which don't need such things.
     *
     * @param kind of the the sensor object used in AST,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public BuiltinSensor(SensorMetaDataBean metaDataBean, BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " []";
    }
}

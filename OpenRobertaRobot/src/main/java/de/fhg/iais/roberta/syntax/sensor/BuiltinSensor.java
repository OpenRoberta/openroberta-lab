package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

public abstract class BuiltinSensor<V> extends Sensor<V> {

    /**
     * This constructor expects no mode, port and slot. Used for internlal sensors, which don't need such things.
     *
     * @param kind of the the sensor object used in AST,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public BuiltinSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean metaDataBean) {
        super(properties, comment);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " []";
    }
}

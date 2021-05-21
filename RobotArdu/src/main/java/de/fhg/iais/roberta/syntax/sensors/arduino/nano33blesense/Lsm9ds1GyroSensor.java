package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;

@NepoPhrase(containerType = "LSM9DS1_GYRO")
public class Lsm9ds1GyroSensor<V> extends BuiltinSensor<V> {

    @NepoValue(name = BlocklyConstants.VARIABLE_X, type = BlocklyType.NUMBER_INT)
    public final Var<V> x;
    @NepoValue(name = BlocklyConstants.VARIABLE_Y, type = BlocklyType.NUMBER_INT)
    public final Var<V> y;
    @NepoValue(name = BlocklyConstants.VARIABLE_Z, type = BlocklyType.NUMBER_INT)
    public final Var<V> z;

    public Lsm9ds1GyroSensor(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Var<V> x, Var<V> y, Var<V> z) {
        super(null, kind, properties, comment);
        this.x = x;
        this.y = y;
        this.z = z;
        setReadOnly();
    }
}
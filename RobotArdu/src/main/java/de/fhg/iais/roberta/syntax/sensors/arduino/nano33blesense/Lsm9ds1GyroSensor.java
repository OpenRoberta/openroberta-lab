package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;

@NepoPhrase(containerType = "LSM9DS1_GYRO")
public class Lsm9ds1GyroSensor<V> extends BuiltinSensor<V> {

    @NepoValue(name = BlocklyConstants.VARIABLE_X, type = BlocklyType.NUMBER_INT)
    public final Expr<V> x;
    @NepoValue(name = BlocklyConstants.VARIABLE_Y, type = BlocklyType.NUMBER_INT)
    public final Expr<V> y;
    @NepoValue(name = BlocklyConstants.VARIABLE_Z, type = BlocklyType.NUMBER_INT)
    public final Expr<V> z;

    public Lsm9ds1GyroSensor(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> x, Expr<V> y, Expr<V> z) {
        super(null, kind, properties, comment);
        this.x = x;
        this.y = y;
        this.z = z;
        setReadOnly();
    }
}
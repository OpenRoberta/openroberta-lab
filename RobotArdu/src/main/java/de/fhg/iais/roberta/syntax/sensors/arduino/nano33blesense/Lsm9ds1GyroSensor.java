package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.*;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.*;
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
package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoExpr(name = "APDS9960_DISTANCE", category = "SENSOR", blocklyNames = {"robsensors_apds9960_distance_getDataAvailableSample"})
public final class Apds9960DistanceSensor<V> extends BuiltinSensor<V> {
    @NepoValue(name = "VARIABLE_VALUE", type = BlocklyType.NUMBER)
    public final Expr<V> distance;

    public Expr<V> getDistance() {
        return distance;
    }

    public Apds9960DistanceSensor(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> distance) {
        super(properties, comment, null);
        this.distance = distance;
        setReadOnly();
    }

    public static <V> Apds9960DistanceSensor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> distance) {
        return new Apds9960DistanceSensor<>(properties, comment, distance);
    }
}
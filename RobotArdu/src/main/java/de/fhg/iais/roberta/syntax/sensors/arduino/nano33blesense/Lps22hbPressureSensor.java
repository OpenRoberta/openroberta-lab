package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.BuiltinSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoExpr(name = "LPS22HB_PRESSURE", category = "SENSOR", blocklyNames = {"robsensors_lps22hb_pressure_getDataAvailableSample"})
public final class Lps22hbPressureSensor<V> extends BuiltinSensor<V> {
    @NepoValue(name = "VARIABLE_VALUE", type = BlocklyType.NUMBER)
    public final Expr<V> pressure;

    public Lps22hbPressureSensor(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> pressure) {
        super(properties, comment, null);
        this.pressure = pressure;
        setReadOnly();
    }

}
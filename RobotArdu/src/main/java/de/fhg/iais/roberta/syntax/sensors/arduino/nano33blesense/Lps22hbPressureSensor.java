package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.InternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "LPS22HB_PRESSURE", category = "SENSOR", blocklyNames = {"robsensors_lps22hb_pressure_getDataAvailableSample"}, blocklyType = BlocklyType.BOOLEAN)
public final class Lps22hbPressureSensor extends InternalSensor {

    @NepoValue(name = "VARIABLE_VALUE", type = BlocklyType.NUMBER)
    public final Expr pressure;

    public Lps22hbPressureSensor(BlocklyProperties properties, Expr pressure) {
        super(properties, null);
        this.pressure = pressure;
        setReadOnly();
    }
}
package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.InternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "APDS9960_COLOR", category = "SENSOR", blocklyNames = {"robsensors_apds9960_color_getDataAvailableSample"}, blocklyType = BlocklyType.BOOLEAN)
public final class Apds9960ColorSensor extends InternalSensor {

    @NepoValue(name = "VARIABLE_R", type = BlocklyType.NUMBER_INT)
    public final Expr r;

    @NepoValue(name = "VARIABLE_G", type = BlocklyType.NUMBER_INT)
    public final Expr g;

    @NepoValue(name = "VARIABLE_B", type = BlocklyType.NUMBER_INT)
    public final Expr b;

    public Apds9960ColorSensor(BlocklyProperties properties, Expr r, Expr g, Expr b) {
        super(properties, null);
        this.r = r;
        this.g = g;
        this.b = b;
        setReadOnly();
    }
}
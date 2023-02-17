package de.fhg.iais.roberta.syntax.sensor.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.InternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "APDS9960_GESTURE", category = "SENSOR", blocklyNames = {"robsensors_apds9960_gesture_getDataAvailableSample"}, blocklyType = BlocklyType.BOOLEAN)
public final class Apds9960GestureSensor extends InternalSensor {

    @NepoValue(name = "VARIABLE_VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr gesture;

    public Apds9960GestureSensor(BlocklyProperties properties, Expr gesture) {
        super(properties, null);
        this.gesture = gesture;
        setReadOnly();
    }
}
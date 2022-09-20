package de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.InternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "HTS221_HUMIDITY", category = "SENSOR", blocklyNames = {"robsensors_hts221_humidity_getDataAvailableSample"}, blocklyType = BlocklyType.BOOLEAN)
public final class Hts221HumiditySensor extends InternalSensor {

    @NepoValue(name = "VARIABLE_VALUE", type = BlocklyType.NUMBER_INT)
    public final Expr humidity;

    public Hts221HumiditySensor(BlocklyProperties properties, Expr humidity) {
        super(properties, null);
        this.humidity = humidity;
        setReadOnly();
    }
}
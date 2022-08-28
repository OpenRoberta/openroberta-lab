package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "TEMPERATURE_SENSING", category = "SENSOR", blocklyNames = {"mbedsensors_temperature_getsample", "robSensors_temperature_getSample"},
    sampleValues = {@F2M(field = "TEMPERATURE_PRESSURE", mode = "PRESSURE"), @F2M(field = "TEMPERATURE", mode = "TEMPERATURE"),
        @F2M(field = "TEMPERATURE_TEMPERATURE", mode = "TEMPERATURE"), @F2M(field = "TEMPERATURE_VALUE", mode = "VALUE")})
public final class TemperatureSensor extends ExternalSensor {

    public TemperatureSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

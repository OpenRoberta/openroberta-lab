package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "SCD40_SENSING", category = "SENSOR", blocklyNames = {"robSensors_scd40_getSample"},
    sampleValues = {@F2M(field = "SCD40_CO2", mode = "CO2"), @F2M(field = "SCD40_TEMPERATURE", mode = "TEMPERATURE"), @F2M(field = "SCD40_HUMIDITY", mode = "HUMIDITY")})
public final class Scd40Sensor extends ExternalSensor {

    public Scd40Sensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

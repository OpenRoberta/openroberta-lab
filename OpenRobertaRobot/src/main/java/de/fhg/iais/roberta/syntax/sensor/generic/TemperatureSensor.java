package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "TEMPERATURE_SENSING", category = "SENSOR", blocklyNames = {"mbedsensors_temperature_getsample", "robSensors_temperature_getSample"},
    sampleValues = {@F2M(field = "TEMPERATURE_PRESSURE", mode = "PRESSURE"), @F2M(field = "TEMPERATURE", mode = "TEMPERATURE"),
        @F2M(field = "TEMPERATURE_TEMPERATURE", mode = "TEMPERATURE"), @F2M(field = "TEMPERATURE_VALUE", mode = "TEMPERATURE")})
@NepoExternalSensor()
public final class TemperatureSensor<V> extends ExternalSensor<V> {

    public TemperatureSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

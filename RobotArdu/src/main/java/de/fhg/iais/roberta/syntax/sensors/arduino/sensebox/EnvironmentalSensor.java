package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "ENVIRONMENTAL", category = "SENSOR", blocklyNames = {"robSensors_environmental_getSample"},
    sampleValues = {@F2M(field = "ENVIRONMENTAL_TEMPERATURE", mode = "TEMPERATURE"), @F2M(field = "ENVIRONMENTAL_VOCEQUIVALENT", mode = "VOCEQUIVALENT"),
        @F2M(field = "ENVIRONMENTAL_CALIBRATION", mode = "CALIBRATION"), @F2M(field = "ENVIRONMENTAL_IAQ", mode = "IAQ"),
        @F2M(field = "ENVIRONMENTAL_PRESSURE", mode = "PRESSURE"), @F2M(field = "ENVIRONMENTAL_HUMIDITY", mode = "HUMIDITY"),
        @F2M(field = "ENVIRONMENTAL_CO2EQUIVALENT", mode = "CO2EQUIVALENT")})
@NepoExternalSensor
public final class EnvironmentalSensor<V> extends ExternalSensor<V> {

    public EnvironmentalSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

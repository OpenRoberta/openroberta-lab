package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "PARTICLE_PM25", sensor = "PARTICLE", mode = "PM25"), @NepoSampleValue(blocklyFieldName = "PARTICLE_PM10", sensor = "PARTICLE", mode = "PM10")}, containerType = "PARTICLE_SENSOR_GETSAMPLE", category = "SENSOR", blocklyNames = {"robSensors_particle_getSample"})
@NepoExternalSensor()
public final class ParticleSensor<V> extends ExternalSensor<V> {

    public ParticleSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

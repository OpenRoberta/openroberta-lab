package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "PARTICLE_SENSOR_GETSAMPLE", category = "SENSOR", blocklyNames = {"robSensors_particle_getSample"},
    sampleValues = {@F2M(field = "PARTICLE_PM25", mode = "PM25"), @F2M(field = "PARTICLE_PM10", mode = "PM10")})
public final class ParticleSensor extends ExternalSensor {

    public ParticleSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "GPS", category = "SENSOR", blocklyNames = {"robSensors_gps_getSample"},
    sampleValues = {@F2M(field = "GPS_LATITUDE", mode = "LATITUDE"), @F2M(field = "GPS_SPEED", mode = "SPEED"), @F2M(field = "GPS_DATE", mode = "DATE"),
        @F2M(field = "GPS_TIME", mode = "TIME"), @F2M(field = "GPS_ALTITUDE", mode = "ALTITUDE"), @F2M(field = "GPS_LONGITUDE", mode = "LONGITUDE")})
public final class GpsSensor extends ExternalSensor {

    public GpsSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}

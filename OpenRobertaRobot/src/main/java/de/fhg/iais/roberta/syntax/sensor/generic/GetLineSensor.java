package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(sampleValues = {@F2M(field = "LINE", mode = "LINE"), @F2M(field = "QUADRGB_LINE", mode = "LINE")}, name = "LINE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_line_getSample"})

public final class GetLineSensor extends ExternalSensor {

    public GetLineSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

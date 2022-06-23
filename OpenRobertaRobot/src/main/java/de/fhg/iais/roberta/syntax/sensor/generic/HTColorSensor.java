package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "HTCOLOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_htcolour_getSample"},
    sampleValues = {@F2M(field = "HTCOLOUR_AMBIENTLIGHT", mode = "AMBIENTLIGHT"), @F2M(field = "HTCOLOUR_COLOUR", mode = "COLOUR"), @F2M(field = "HTCOLOUR_LIGHT", mode = "LIGHT")})
@NepoExternalSensor()
public final class HTColorSensor<V> extends ExternalSensor<V> {

    public HTColorSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}

package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "HTCOLOUR_AMBIENTLIGHT", sensor = "COLOUR", mode = "AMBIENTLIGHT"), @NepoSampleValue(blocklyFieldName = "HTCOLOUR_COLOUR", sensor = "COLOUR", mode = "COLOUR"), @NepoSampleValue(blocklyFieldName = "HTCOLOUR_LIGHT", sensor = "COLOUR", mode = "LIGHT")}, containerType = "HTCOLOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_htcolour_getSample"})
@NepoExternalSensor()
public final class HTColorSensor<V> extends ExternalSensor<V> {

    public HTColorSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }
}

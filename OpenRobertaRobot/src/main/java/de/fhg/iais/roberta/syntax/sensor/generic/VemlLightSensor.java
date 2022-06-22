package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "LIGHTVEML_UVLIGHT", sensor = "LIGHTVEML_LIGHT", mode = "UVLIGHT"), @NepoSampleValue(blocklyFieldName = "LIGHTVEML_LIGHT", sensor = "LIGHTVEML_LIGHT", mode = "LIGHT")}, containerType = "VEMLLIGHT_SENSING", category = "SENSOR", blocklyNames = {"robSensors_lightveml_getSample"})
@NepoExternalSensor()
public final class VemlLightSensor<V> extends ExternalSensor<V> {

    public VemlLightSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        this.setReadOnly();
    }

}

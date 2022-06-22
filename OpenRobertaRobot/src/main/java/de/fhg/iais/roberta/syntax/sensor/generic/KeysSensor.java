package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "PLAYKEY_PRESSED", sensor = "PLAYKEY", mode = "PRESSED"), @NepoSampleValue(blocklyFieldName = "KEY_PRESSED", sensor = "KEY_PRESSED", mode = "PRESSED"), @NepoSampleValue(blocklyFieldName = "RECKEY_PRESSED", sensor = "RECKEY", mode = "PRESSED")}, containerType = "KEYS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_key_getSample"})
@NepoExternalSensor()
public final class KeysSensor<V> extends ExternalSensor<V> {

    public KeysSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "KEYS_SENSING", category = "SENSOR", blocklyNames = {"robSensors_key_getSample"},
    sampleValues = {@F2M(field = "PLAYKEY_PRESSED", mode = "PRESSED"), @F2M(field = "KEY_PRESSED", mode = "PRESSED"), @F2M(field = "RECKEY_PRESSED", mode = "PRESSED")})
@NepoExternalSensor()
public final class KeysSensor<V> extends ExternalSensor<V> {

    public KeysSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

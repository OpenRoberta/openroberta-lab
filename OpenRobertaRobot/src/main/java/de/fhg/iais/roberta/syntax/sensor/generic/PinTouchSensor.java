package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "PIN_TOUCHED", sensor = "PIN_TOUCHED", mode = "PIN_TOUCHED"), @NepoSampleValue(blocklyFieldName = "PINTOUCH_PRESSED", sensor = "PINTOUCH", mode = "PRESSED")}, containerType = "PIN_TOUCH_SENSING", category = "SENSOR", blocklyNames = {"robsensors_pintouch_getsample"})
@NepoExternalSensor()
public final class PinTouchSensor<V> extends ExternalSensor<V> {

    public PinTouchSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

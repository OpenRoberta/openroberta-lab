package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(sampleValues = {@NepoSampleValue(blocklyFieldName = "OUT_DIGITAL", sensor = "PIN_DIGITAL", mode = "DIGITAL"), @NepoSampleValue(blocklyFieldName = "PIN_PULSEHIGH", sensor = "PIN_PULSEHIGH", mode = "PULSEHIGH"), @NepoSampleValue(blocklyFieldName = "PIN_ANALOG", sensor = "PIN_ANALOG", mode = "ANALOG"), @NepoSampleValue(blocklyFieldName = "PIN_PULSELOW", sensor = "PIN_PULSELOW", mode = "PULSELOW"), @NepoSampleValue(blocklyFieldName = "PIN_DIGITAL", sensor = "PIN_DIGITAL", mode = "DIGITAL"), @NepoSampleValue(blocklyFieldName = "OUT_ANALOG", sensor = "PIN_ANALOG", mode = "ANALOG")}, containerType = "PIN_READ_VALUE", category = "SENSOR", blocklyNames = {"robsensors_pin_getsample", "mbedSensors_pin_getSample", "robsensors_out_getsample"})
@NepoExternalSensor()
public final class PinGetValueSensor<V> extends ExternalSensor<V> {

    public PinGetValueSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "PIN_READ_VALUE", category = "SENSOR", blocklyNames = {"robsensors_pin_getsample", "mbedSensors_pin_getSample", "robsensors_out_getsample"},
    sampleValues = {@F2M(field = "OUT_DIGITAL", mode = "DIGITAL"), @F2M(field = "PIN_PULSEHIGH", mode = "PULSEHIGH"),
        @F2M(field = "PIN_ANALOG", mode = "ANALOG"), @F2M(field = "PIN_PULSELOW", mode = "PULSELOW"), @F2M(field = "PIN_DIGITAL", mode = "DIGITAL"),
        @F2M(field = "OUT_ANALOG", mode = "ANALOG")})
@NepoExternalSensor()
public final class PinGetValueSensor<V> extends ExternalSensor<V> {

    public PinGetValueSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "TOUCH_SENSING", category = "SENSOR", blocklyNames = {"sim_touch_isPressed", "robSensors_touch_getSample"},
    sampleValues = {@F2M(field = "TOUCH_PRESSED", mode = "PRESSED"), @F2M(field = "TOUCH", mode = "TOUCH")})
@NepoExternalSensor()
public final class TouchSensor<V> extends ExternalSensor<V> {

    public TouchSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

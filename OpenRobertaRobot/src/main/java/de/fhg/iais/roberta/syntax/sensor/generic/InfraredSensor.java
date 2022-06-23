package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

@NepoExpr(name = "INFRARED_SENSING", category = "SENSOR", blocklyNames = {"robSensors_infrared_getSample"},
    sampleValues = {@F2M(field = "INFRARED_LINE", mode = "LINE"), @F2M(field = "INFRARED_REFLEXION", mode = "REFLEXION"),
        @F2M(field = "INFRARED_SEEK", mode = "SEEK"), @F2M(field = "INFRARED_DISTANCE", mode = "DISTANCE"), @F2M(field = "INFRARED_VALUE", mode = "VALUE"),
        @F2M(field = "INFRARED_OBSTACLE", mode = "OBSTACLE"), @F2M(field = "INFRARED_PRESENCE", mode = "PRESENCE")})
@NepoExternalSensor()
public final class InfraredSensor<V> extends ExternalSensor<V> {

    public InfraredSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

}

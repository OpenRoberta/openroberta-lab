package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "INFRARED_SENSING", category = "SENSOR", blocklyNames = {"robSensors_infrared_getSample", "robSensors_irhorizontal_getSample"},
    sampleValues = {@F2M(field = "INFRARED_LINE", mode = "LINE"), @F2M(field = "INFRARED_REFLEXION", mode = "REFLEXION"),
        @F2M(field = "INFRARED_AMBIENTLIGHT", mode = "AMBIENTLIGHT"),
        @F2M(field = "INFRARED_SEEK", mode = "SEEK"), @F2M(field = "INFRARED_DISTANCE", mode = "DISTANCE"), @F2M(field = "INFRARED_VALUE", mode = "VALUE"),
        @F2M(field = "INFRARED_OBSTACLE", mode = "OBSTACLE"), @F2M(field = "INFRARED_PRESENCE", mode = "PRESENCE"), @F2M(field = "IRHORIZONTAL_DISTANCE", mode = "DISTANCE"), @F2M(field = "INFRARED_LIGHT", mode = "LIGHT")})
public final class InfraredSensor extends ExternalSensor {

    public InfraredSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

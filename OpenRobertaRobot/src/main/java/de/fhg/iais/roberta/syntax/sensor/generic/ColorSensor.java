package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forClass.NepoExternalSensor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "COLOR_SENSING", category = "SENSOR", blocklyNames = {"robsensors_colourtcs3472_getsample", "sim_colour_getSample", "robSensors_colour_getSample"},
    sampleValues = {@F2M(field = "COLOUR_COLOUR", mode = "COLOUR"), @F2M(field = "COLOUR_AMBIENTLIGHT", mode = "AMBIENTLIGHT"), @F2M(field = "COLOUR_LIGHT", mode = "LIGHT")})
@NepoExternalSensor()
public final class ColorSensor<V> extends ExternalSensor<V> {

    public ColorSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}

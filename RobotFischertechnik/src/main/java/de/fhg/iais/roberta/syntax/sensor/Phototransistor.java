package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "PHOTOTRANSISTOR_SENSING", category = "SENSOR", blocklyNames = {"robSensors_phototransistor_getSample"},
    sampleValues = {@F2M(field = "PHOTOTRANSISTOR_OPENING", mode = "OPENING")})
public final class Phototransistor extends ExternalSensor {

    public Phototransistor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

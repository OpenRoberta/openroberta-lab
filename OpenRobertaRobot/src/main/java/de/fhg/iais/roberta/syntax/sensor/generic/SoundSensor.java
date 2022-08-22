package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.F2M;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoExpr(name = "SOUND_SENSING", category = "SENSOR", blocklyNames = {"robSensors_sound_getSample"},
    sampleValues = {@F2M(field = "SOUND_SOUND", mode = "SOUND"), @F2M(field = "SOUND", mode = "VALUE")})
public final class SoundSensor extends ExternalSensor {

    public SoundSensor(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }

}

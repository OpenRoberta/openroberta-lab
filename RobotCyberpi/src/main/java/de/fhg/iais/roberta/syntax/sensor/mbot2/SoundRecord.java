package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

@NepoPhrase(category = "SENSOR", blocklyNames = {"robSensors_sound_record"}, name = "SOUND_RECORD")
public final class SoundRecord extends ExternalSensor {

    public SoundRecord(BlocklyProperties properties, ExternalSensorBean externalSensorBean) {
        super(properties, externalSensorBean);
        setReadOnly();
    }
}

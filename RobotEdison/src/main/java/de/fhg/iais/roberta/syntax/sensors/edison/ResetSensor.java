package de.fhg.iais.roberta.syntax.sensors.edison;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "SENSOR", blocklyNames = {"edisonSensors_sensor_reset"}, name = "SENSOR_RESET")
public final class ResetSensor extends Sensor {

    @NepoField(name = BlocklyConstants.SENSOR)
    public final String sensor;

    public ResetSensor(BlocklyProperties props,  String sensor) {
        super(props);
        Assert.nonEmptyString(sensor);
        this.sensor = sensor;
        setReadOnly();
    }

}

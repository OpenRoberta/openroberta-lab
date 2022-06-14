package de.fhg.iais.roberta.syntax.sensors.edison;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "SENSOR", blocklyNames = {"edisonSensors_sensor_reset"}, containerType = "SENSOR_RESET")
public class ResetSensor<V> extends Sensor<V> {

    @NepoField(name = BlocklyConstants.SENSOR)
    public final String sensor;

    public ResetSensor(BlocklyBlockProperties props, BlocklyComment comment, String sensor) {
        super(props, comment);
        Assert.nonEmptyString(sensor);
        this.sensor = sensor;
        setReadOnly();
    }

    private static <V> ResetSensor<V> make(String sensor, BlocklyBlockProperties props, BlocklyComment comment) {
        return new ResetSensor<>(props, comment, sensor);
    }

    public String getSensor() {
        return this.sensor;
    }
}

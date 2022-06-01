package de.fhg.iais.roberta.syntax.sensors.edison;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "SENSOR_RESET")
public class ResetSensor<V> extends Sensor<V> {

    @NepoField(name = BlocklyConstants.SENSOR)
    public final String sensor;

    public ResetSensor(BlockType kind, BlocklyBlockProperties props, BlocklyComment comment, String sensor) {
        super(kind, props, comment);
        Assert.nonEmptyString(sensor);
        this.sensor = sensor;
        setReadOnly();
    }

    private static <V> ResetSensor<V> make(String sensor, BlocklyBlockProperties props, BlocklyComment comment) {
        return new ResetSensor<>(BlockTypeContainer.getByName("SENSOR_RESET"), props, comment, sensor);
    }

    public String getSensor() {
        return this.sensor;
    }
}

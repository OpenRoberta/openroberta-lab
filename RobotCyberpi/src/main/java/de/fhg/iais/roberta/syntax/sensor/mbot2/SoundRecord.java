package de.fhg.iais.roberta.syntax.sensor.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;

@NepoPhrase(containerType = "SOUND_RECORD")
public class SoundRecord<V> extends Sensor<V> implements WithUserDefinedPort<V> {
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT)
    public final String sensorPort;
    @NepoHide
    public final Hide hide;
    
    public SoundRecord(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String mode, String sensorPort, Hide hide) {
        super(kind, properties, comment);
        this.mode = mode;
        this.sensorPort = sensorPort;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.sensorPort;
    }


}

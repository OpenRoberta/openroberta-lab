package de.fhg.iais.roberta.syntax.action.mbot2;


import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(containerType = "QUADRGB_LIGHT_OFF_ACTION")
public class QuadRGBLightOffAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    public QuadRGBLightOffAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String port) {
        super(kind, properties, comment);
        Assert.nonEmptyString(port);
        this.port = port;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}

package de.fhg.iais.roberta.syntax.action.mbot2;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robActions_play_recording</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * stopping every movement of the robot.<br/>
 * <br/>
 */
@NepoPhrase(containerType = "PLAY_RECORDING_ACTION")
public class PlayRecordingAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public PlayRecordingAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.nonEmptyString(port);
        this.hide = hide;
        this.port = port;
        setReadOnly();
    }
    
    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}

package de.fhg.iais.roberta.syntax.action.sound;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_play_file", "actions_play_file", "actions_play_file_port", "actions_play_expression"}, name = "PLAY_FILE_ACTION")
public final class PlayFileAction extends Action {

    @NepoField(name = "ACTORPORT", value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    
    @NepoField(name = BlocklyConstants.FILE)
    public final String fileName;

    @NepoHide
    public final Hide hide;
    
    public PlayFileAction(BlocklyProperties properties,String port, String fileName, Hide hide) {
        super(properties);
        Assert.isTrue(!fileName.equals(""));
        this.fileName = fileName;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.actor.sound;

import de.fhg.iais.roberta.syntax.actor.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_play_file"}, name = "PLAY_FILE_ACTION")
public final class PlayFileAction extends Action {
    @NepoField(name = BlocklyConstants.FILE)
    public final String fileName;

    public PlayFileAction(BlocklyProperties properties, String fileName) {
        super(properties);
        Assert.isTrue(!fileName.equals(""));
        this.fileName = fileName;
        setReadOnly();
    }

}

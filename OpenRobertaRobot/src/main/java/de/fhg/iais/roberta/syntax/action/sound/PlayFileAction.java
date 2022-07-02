package de.fhg.iais.roberta.syntax.action.sound;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_play_file"}, name = "PLAY_FILE_ACTION")
public final class PlayFileAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.FILE)
    public final String fileName;

    public PlayFileAction(BlocklyBlockProperties properties, BlocklyComment comment, String fileName) {
        super(properties, comment);
        Assert.isTrue(!fileName.equals(""));
        this.fileName = fileName;
        setReadOnly();
    }

}

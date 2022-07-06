package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"text_comment"}, name = "TEXT_COMMENT")
public final class StmtTextComment extends Stmt {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String textComment;

    public StmtTextComment(BlocklyProperties properties, String textComment) {
        super(properties);
        Assert.isTrue(textComment != null);
        this.textComment = textComment;
        setReadOnly();
    }

}

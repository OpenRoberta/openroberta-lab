package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"text_comment"}, name = "TEXT_COMMENT")
public final class StmtTextComment<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String textComment;

    public StmtTextComment(BlocklyBlockProperties properties, BlocklyComment comment, String textComment) {
        super(properties, comment);
        Assert.isTrue(textComment != null);
        this.textComment = textComment;
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>text_comment</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for an
 * inline comment.
 */
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

    /**
     * Create read only object of {@link StmtTextComment}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StmtTextComment}
     */
    public static <V> StmtTextComment<V> make(String textComment, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StmtTextComment<V>(properties, comment, textComment);
    }

    /**
     * @return the text comment
     */
    public String getTextComment() {
        return this.textComment;
    }

}

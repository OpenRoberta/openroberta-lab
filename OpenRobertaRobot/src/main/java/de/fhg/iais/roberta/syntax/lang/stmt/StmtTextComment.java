package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>text_comment</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for an
 * inline comment.
 */
@NepoPhrase(containerType = "TEXT_COMMENT")
public class StmtTextComment<V> extends Stmt<V> {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String textComment;

    public StmtTextComment(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String textComment) {
        super(kind, properties, comment);
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
        return new StmtTextComment<V>(BlockTypeContainer.getByName("TEXT_COMMENT"), properties, comment, textComment);
    }

    /**
     * @return the text comment
     */
    public String getTextComment() {
        return this.textComment;
    }

}

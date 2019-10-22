package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>text_comment</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for an
 * inline comment.
 */
public class StmtTextComment<V> extends Stmt<V> {
    private final String textComment;

    private StmtTextComment(String textComment, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TEXT_COMMENT"), properties, comment);
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
        return new StmtTextComment<V>(textComment, properties, comment);
    }

    /**
     * @return the text comment
     */
    public String getTextComment() {
        return this.textComment;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, "StmtTextComment [" + this.textComment + "]");
        return sb.toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitStmtTextComment(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String comment = helper.extractField(fields, BlocklyConstants.TEXT);
        return StmtTextComment.make(comment, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TEXT, this.textComment);
        return jaxbDestination;
    }
}

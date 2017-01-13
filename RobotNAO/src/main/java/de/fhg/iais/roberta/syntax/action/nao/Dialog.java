package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.NaoAstVisitor;

/**
 * This class represents the <b>naoActions_sayText</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making the robot say the text.<br>
 * <br>
 * <br>
 */
public class Dialog<V> extends Action<V> {
    private final Expr<V> phrase;
    private final Expr<V> answer;

    private Dialog(Expr<V> phrase, Expr<V> answer, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DIALOG"), properties, comment);
        Assert.isTrue(phrase != null);
        Assert.isTrue(answer != null);
        this.phrase = phrase;
        this.answer = answer;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayTextAction}. This instance is read only and can not be modified.
     *
     * @param msg {@link msg} that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayTextAction}
     */
    private static <V> Dialog<V> make(Expr<V> phrase, Expr<V> answer, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Dialog<>(phrase, answer, properties, comment);
    }

    /**
     * @return the phrase.
     */
    public Expr<V> getPhrase() {
        return this.phrase;
    }

    /**
     * @return the answer.
     */
    public Expr<V> getAnswer() {
        return this.answer;
    }

    @Override
    public String toString() {
        return "Dialog [" + this.phrase + ", " + this.answer + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((NaoAstVisitor<V>) visitor).visitDialog(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> phrase = helper.extractValue(values, new ExprParam(BlocklyConstants.PHRASE, String.class));
        Phrase<V> answer = helper.extractValue(values, new ExprParam(BlocklyConstants.ANSWER, String.class));
        return Dialog
            .make(helper.convertPhraseToExpr(phrase), helper.convertPhraseToExpr(answer), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.PHRASE, this.phrase);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.ANSWER, this.answer);

        return jaxbDestination;
    }

}

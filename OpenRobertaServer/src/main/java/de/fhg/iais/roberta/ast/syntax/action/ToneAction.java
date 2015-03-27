package de.fhg.iais.roberta.ast.syntax.action;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_play_tone</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for making sound.<br/>
 * <br/>
 * The client must provide the frequency and the duration of the sound.
 */
public class ToneAction<V> extends Action<V> {
    private final Expr<V> frequency;
    private final Expr<V> duration;

    private ToneAction(Expr<V> frequency, Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.TONE_ACTION, properties, comment);
        Assert.isTrue(frequency.isReadOnly() && duration.isReadOnly() && frequency != null && duration != null);
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ToneAction}. This instance is read only and can not be modified.
     *
     * @param frequency of the sound; must be <b>not</b> null and <b>read only</b>,
     * @param duration of the sound; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ToneAction}.
     */
    public static <V> ToneAction<V> make(Expr<V> frequency, Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ToneAction<V>(frequency, duration, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 2);
        Phrase<V> left = helper.extractValue(values, new ExprParam(BlocklyConstants.FREQUENCE, Integer.class));
        Phrase<V> right = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, Integer.class));
        return ToneAction.make(
            helper.convertPhraseToExpr(left),
            helper.convertPhraseToExpr(right),
            helper.extractBlockProperties(block),
            helper.extractComment(block));
    }

    /**
     * @return frequency of the sound
     */
    public Expr<V> getFrequency() {
        return this.frequency;
    }

    /**
     * @return duration of the sound.
     */
    public Expr<V> getDuration() {
        return this.duration;
    }

    @Override
    public String toString() {
        return "ToneAction [" + this.frequency + ", " + this.duration + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitToneAction(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.FREQUENCE, getFrequency());
        AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.DURATION, getDuration());

        return jaxbDestination;
    }
}

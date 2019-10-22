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
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoActions_randomEyes</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * coloring the eyes randomly.<br/>
 * <br/>
 * The client must provide the {@link duration} (time the eyes will be colored).
 */
public final class RandomEyesDuration<V> extends Action<V> {

    @Override
    public String toString() {
        return "RandomEyesDuration [" + this.duration + "]";
    }

    private final Expr<V> duration;

    private RandomEyesDuration(Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RANDOM_EYES_DURATION"), properties, comment);
        this.duration = duration;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RandomEyesDuration}. This instance is read only and can not be modified.
     *
     * @param duration {@link duration} the eyes will be colored for,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RandomEyesDuration}
     */
    private static <V> RandomEyesDuration<V> make(Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RandomEyesDuration<V>(duration, properties, comment);
    }

    public Expr<V> getDuration() {
        return this.duration;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitRandomEyesDuration(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);

        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));

        return RandomEyesDuration.make(helper.convertPhraseToExpr(duration), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);

        return jaxbDestination;
    }
}

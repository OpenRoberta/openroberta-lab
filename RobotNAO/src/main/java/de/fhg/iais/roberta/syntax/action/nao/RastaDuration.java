package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * This class represents the <b>robActions_motor_on_for</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for coloring the eyes in red, yellow and green.<br/>
 * <br/>
 * The client must provide the {@link duration} (time the eyes will be colored).
 */
public final class RastaDuration<V> extends Action<V> {

    @Override
    public String toString() {
        return "RastaDuration [" + this.duration + "]";
    }

    private final Expr<V> duration;

    private RastaDuration(Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RASTA_DURATION"), properties, comment);
        this.duration = duration;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RastaDuration}. This instance is read only and can not be modified.
     *
     * @param duration {@link duration} the eyes will be colored for,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RastaDuration}
     */
    private static <V> RastaDuration<V> make(Expr<V> duration, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RastaDuration<V>(duration, properties, comment);
    }

    public Expr<V> getDuration() {
        return this.duration;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase<V> duration = helper.extractValue(values, new ExprParam(BlocklyConstants.DURATION, BlocklyType.NUMBER_INT));

        return RastaDuration.make(Jaxb2Ast.convertPhraseToExpr(duration), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.DURATION, this.duration);

        return jaxbDestination;
    }
}

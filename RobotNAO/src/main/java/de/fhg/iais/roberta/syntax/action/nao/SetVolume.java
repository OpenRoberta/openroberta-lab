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
 * This class represents the <b>naoActions_setVolume</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the of the robot.<br/>
 * <br/>
 * The client must provide the {@link Volume} (fraction the speech engine is set to).
 */
public final class SetVolume<V> extends Action<V> {

    private final Expr<V> volume;

    private SetVolume(Expr<V> volume, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SET_VOLUME"), properties, comment);
        this.volume = volume;
        setReadOnly();
    }

    /**
     * Creates instance of {@link SetVolume}. This instance is read only and can not be modified.
     *
     * @param volume {@link volume} the robot is set to,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SetVolume}
     */
    private static <V> SetVolume<V> make(Expr<V> volume, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SetVolume<V>(volume, properties, comment);
    }

    public Expr<V> getVolume() {
        return this.volume;
    }

    @Override
    public String toString() {
        return "SetVolume [" + this.volume + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitSetVolume(this);
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

        Phrase<V> volume = helper.extractValue(values, new ExprParam(BlocklyConstants.VOLUME, BlocklyType.NUMBER_INT));

        return SetVolume.make(helper.convertPhraseToExpr(volume), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VOLUME, this.volume);

        return jaxbDestination;
    }
}

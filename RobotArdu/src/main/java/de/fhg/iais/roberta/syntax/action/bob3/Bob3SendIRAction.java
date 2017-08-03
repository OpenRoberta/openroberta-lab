package de.fhg.iais.roberta.syntax.action.bob3;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;

/**
 * This class represents the <b>bob3communication_sendblock</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class Bob3SendIRAction<V> extends Action<V> {
    private final Expr<V> code;

    private Bob3SendIRAction(Expr<V> code, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("BOB3_BODYLED"), properties, comment);
        this.code = code;
        setReadOnly();
    }

    /**
     * Creates instance of {@link Bob3SendIRAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link Bob3SendIRAction}
     */
    private static <V> Bob3SendIRAction<V> make(Expr<V> code, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new Bob3SendIRAction<>(code, properties, comment);
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.code + " ]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((Bob3AstVisitor<V>) visitor).visitSendIRAction(this);
    }

    public Expr<V> getCode() {
        return this.code;
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> code = helper.extractValue(values, new ExprParam(BlocklyConstants.MESSAGE, BlocklyType.NUMBER));
        return Bob3SendIRAction.make(helper.convertPhraseToExpr(code), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.MESSAGE, this.code);
        return jaxbDestination;

    }
}

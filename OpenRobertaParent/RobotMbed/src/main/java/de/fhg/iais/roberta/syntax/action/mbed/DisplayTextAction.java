package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.mbed.DisplayTextMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.mbed.MbedAstVisitor;

/**
 * This class represents the <b>mbedActions_display_text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 */
public class DisplayTextAction<V> extends Action<V> {
    private final DisplayTextMode mode;
    private final Expr<V> msg;

    private DisplayTextAction(DisplayTextMode mode, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DISPLAY_TEXT_ACTION"), properties, comment);
        Assert.isTrue(msg != null && mode != null);
        this.msg = msg;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DisplayTextAction}. This instance is read only and can not be modified.
     *
     * @param msg that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DisplayTextAction}
     */
    private static <V> DisplayTextAction<V> make(DisplayTextMode mode, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DisplayTextAction<>(mode, msg, properties, comment);
    }

    public DisplayTextMode getMode() {
        return this.mode;
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "DisplayTextAction [" + this.mode + ", " + this.msg + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbedAstVisitor<V>) visitor).visitDisplayTextAction(this);

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
        List<Field> fields = helper.extractFields(block, (short) 1);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.STRING));
        String displaMode = "";
        try {
            displaMode = helper.extractField(fields, BlocklyConstants.TYPE);
        } catch ( DbcException e ) {
            displaMode = "TEXT";
        }
        return DisplayTextAction
            .make(DisplayTextMode.get(displaMode), helper.convertPhraseToExpr(msg), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, this.mode.toString());
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.OUT, this.msg);

        return jaxbDestination;
    }

}

package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents the <b>mbedActions_display_text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 */
public class LEDMatrixTextAction<V> extends Action<V> {
    private final String port;
    private final Expr<V> msg;
    private final String displayMode;

    private LEDMatrixTextAction(String port, String displayMode, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_MATRIX_TEXT_ACTION"), properties, comment);
        Assert.isTrue(msg != null && port != null);
        this.port = port;
        this.msg = msg;
        this.displayMode = displayMode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LEDMatrixTextAction}. This instance is read only and can not be modified.
     *
     * @param msg that will be printed on the display of the brick; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LEDMatrixTextAction}
     */
    private static <V> LEDMatrixTextAction<V> make(String port, String displayMode, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LEDMatrixTextAction<>(port, displayMode, msg, properties, comment);
    }

    public String getPort() {
        return this.port;
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return "DisplayTextAction [" + this.msg + "]";
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
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 2);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.STRING));
        String displayMode = "";
        try {
            displayMode = Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE);
        } catch ( DbcException e ) {
            displayMode = "TEXT";
        }
        return LEDMatrixTextAction.make(port, displayMode, Jaxb2Ast.convertPhraseToExpr(msg), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, this.displayMode);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.OUT, this.msg);

        return jaxbDestination;
    }

}

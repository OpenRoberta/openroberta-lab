package de.fhg.iais.roberta.syntax.action.display;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.IDisplayVisitor;

/**
 * This class represents the <b>robActions_display_text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, Expr, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 * The client must provide the message and x and y coordinates.
 */
public class ShowTextAction<V> extends Action<V> {
    private final Expr<V> msg;
    private final Expr<V> x;
    private final Expr<V> y;
    private String port;

    private ShowTextAction(Expr<V> msg, Expr<V> column, Expr<V> row, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SHOW_TEXT_ACTION"), properties, comment);
        Assert.isTrue((msg != null) && (column != null) && (row != null));
        this.msg = msg;
        this.x = column;
        this.y = row;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link ShowTextAction}. This instance is read only and can not be modified.
     *
     * @param msg that will be printed on the display of the brick; must be <b>not</b> null,
     * @param x position where the message will start; must be <b>not</b> null,
     * @param y position where the message will start; must be <b>not</b> null,
     * @param port
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ShowTextAction}
     */
    public static <V> ShowTextAction<V> make(Expr<V> msg, Expr<V> x, Expr<V> y, String port, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ShowTextAction<>(msg, x, y, port, properties, comment);
    }

    /**
     * @return the message.
     */
    public Expr<V> getMsg() {
        return this.msg;
    }

    /**
     * @return position x of the picture on the display.
     */
    public Expr<V> getX() {
        return this.x;
    }

    /**
     * @return position y of the picture on the display.
     */
    public Expr<V> getY() {
        return this.y;
    }

    /**
     * @return port of the display.
     */
    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "ShowTextAction [" + this.msg + ", " + this.x + ", " + this.y + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IDisplayVisitor<V>) visitor).visitShowTextAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Value> values = helper.extractValues(block, (short) 3);
        List<Field> fields = helper.extractFields(block, (short) 1);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.OUT, BlocklyType.STRING));
        Phrase<V> col = helper.extractValue(values, new ExprParam(BlocklyConstants.COL, BlocklyType.NUMBER_INT));
        Phrase<V> row = helper.extractValue(values, new ExprParam(BlocklyConstants.ROW, BlocklyType.NUMBER_INT));
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        return ShowTextAction
            .make(
                helper.convertPhraseToExpr(msg),
                helper.convertPhraseToExpr(col),
                helper.convertPhraseToExpr(row),
                factory.sanitizePort(port),
                helper.extractBlockProperties(block),
                helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.OUT, getMsg());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.COL, getX());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ROW, getY());
        return jaxbDestination;
    }
}

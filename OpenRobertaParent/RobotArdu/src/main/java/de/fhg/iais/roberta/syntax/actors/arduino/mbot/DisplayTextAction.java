package de.fhg.iais.roberta.syntax.actors.arduino.mbot;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
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
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitors.arduino.MbotAstVisitor;

/**
 * This class represents the <b>mbedActions_display_text</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * showing a text message on the screen of the brick.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 * <br>
 */
public class DisplayTextAction<V> extends Action<V> {
    private final Expr<V> msg;
    private final IActorPort displayPort;

    private DisplayTextAction(IActorPort displayPort, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MAKEBLOCK_DISPLAY_TEXT"), properties, comment);
        Assert.isTrue(msg != null);
        this.msg = msg;
        this.displayPort = displayPort;
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
    private static <V> DisplayTextAction<V> make(IActorPort displayPort, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DisplayTextAction<>(displayPort, msg, properties, comment);
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

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((MbotAstVisitor<V>) visitor).visitDisplayTextAction(this);

    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> msg = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.STRING));
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.SENSORPORT);
        return DisplayTextAction
            .make(factory.getActorPort(port), helper.convertPhraseToExpr(msg), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        String fieldValue = getPort().getValues()[0];
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, this.msg);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);

        return jaxbDestination;
    }

    public IActorPort getPort() {
        return this.displayPort;
    }

}

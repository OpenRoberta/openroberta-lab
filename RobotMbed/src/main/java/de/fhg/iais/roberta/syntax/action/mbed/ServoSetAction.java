package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_set_servo</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 */
public final class ServoSetAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    private final String port;
    private final Expr<V> value;

    private ServoSetAction(String port, Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SERVO_SET_ACTION"), properties, comment);
        Assert.notNull(port);
        Assert.notNull(value);
        this.port = port;
        this.value = value;
        setReadOnly();
    }

    /**
     * Create object of the class {@link PinWriteValueAction}.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link PinWriteValueAction}
     */
    public static <V> ServoSetAction<V> make(String port, Expr<V> value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ServoSetAction<>(port, value, properties, comment);
    }

    public String getUserDefinedPort() {
        return this.port;
    }

    public Expr<V> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "ServoSetAction [" + this.port + ", " + this.value + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.PIN_PORT);
        Phrase<V> value = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));
        return ServoSetAction
            .make(Jaxb2Ast.sanitizePort(port), Jaxb2Ast.convertPhraseToExpr(value), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PIN_PORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.value);
        return jaxbDestination;
    }
}

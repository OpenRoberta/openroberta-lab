package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.*;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(String, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class LedOnAction<V> extends Action<V> implements WithUserDefinedPort<V> {
    private final Expr<V> ledColor;
    private final String port;

    private LedOnAction(String port, Expr<V> ledColor, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_ON_ACTION"), properties, comment);
        Assert.nonEmptyString(port);
        Assert.notNull(ledColor);
        this.port = port;
        this.ledColor = ledColor;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedOnAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedOnAction}
     */
    public static <V> LedOnAction<V> make(String port, Expr<V> ledColor, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedOnAction<>(port, ledColor, properties, comment);
    }

    public String getUserDefinedPort() {
        return this.port;
    }

    /**
     * @return {@link ColorConst} color of the led.
     */
    public Expr<V> getLedColor() {
        return this.ledColor;
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.port + ", " + this.ledColor + " ]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, "0");
        Phrase<V> ledColor = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));

        return LedOnAction.make(port, Jaxb2Ast.convertPhraseToExpr(ledColor), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.COLOR, this.ledColor);
        if ( !this.port.toString().equals("0") ) {
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        }

        return jaxbDestination;

    }
}

package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_leds_on</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * turning on the Led.<br/>
 * <br>
 * The client must provide the {@link ColorConst} color of the led. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(ColorConst, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class StepMotorAction<V> extends Action<V> {
    private final Expr<V> stepMotorPos;
    
    private StepMotorAction(Expr<V> stepMotorPos, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("FESTOBIONIC_STEPMOTOR"), properties, comment);
        Assert.notNull(stepMotorPos);
        this.stepMotorPos = stepMotorPos;
        setReadOnly();
    }

    /**
     * Creates instance of {@link StepMotorAction}. This instance is read only and can not be modified.
     *
     * @param ledColor {@link ColorConst} color of the led; must <b>not</b> be null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link StepMotorAction}
     */
    private static <V> StepMotorAction<V> make(Expr<V> stepMotorPos, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StepMotorAction<>(stepMotorPos, properties, comment);
    }

    /**
     * @return {@link ColorConst} color of the led.
     */
    public Expr<V> getStepMotorPos() {
        return this.stepMotorPos;
    }

    @Override
    public String toString() {
        return "StepMotorAction [ " + this.stepMotorPos + " ]";
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
        //List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        //String side = Jaxb2Ast.extractField(fields, BlocklyConstants.VALUE + BlocklyConstants.SIDE);

        Phrase<V> stepMotorPos = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.NUMBER_INT));

        return StepMotorAction.make(Jaxb2Ast.convertPhraseToExpr(stepMotorPos), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
		//Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VALUE + BlocklyConstants.SIDE, this.side);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.VALUE, this.stepMotorPos);
        return jaxbDestination;

    }
}

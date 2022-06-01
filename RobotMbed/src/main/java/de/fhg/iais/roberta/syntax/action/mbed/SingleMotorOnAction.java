package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_single_motor_on</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class SingleMotorOnAction<V> extends Action<V> {
    private final Expr<V> speed;

    private SingleMotorOnAction(Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SINGLE_MOTOR_ON_ACTION"), properties, comment);
        Assert.isTrue((speed != null) && speed.isReadOnly());
        this.speed = speed;

        setReadOnly();
    }

    /**
     * Creates instance of {@link SingleMotorOnAction}. This instance is read only and can not be modified.
     *
     * @param speed {@link Expr} speed of the robot,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SingleMotorOnAction}
     */
    public static <V> SingleMotorOnAction<V> make(Expr<V> speed, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SingleMotorOnAction<>(speed, properties, comment);
    }

    public Expr<V> getSpeed() {
        return this.speed;
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
        Phrase<V> speed = helper.extractValue(values, new ExprParam(BlocklyConstants.POWER, BlocklyType.NUMBER_INT));

        return SingleMotorOnAction.make(Jaxb2Ast.convertPhraseToExpr(speed), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public String toString() {
        return "SingleMotorOnAction [" + this.speed + "]";
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.POWER, this.speed);

        return jaxbDestination;
    }
}

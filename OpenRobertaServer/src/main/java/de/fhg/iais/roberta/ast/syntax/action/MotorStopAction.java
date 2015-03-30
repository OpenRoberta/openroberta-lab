package de.fhg.iais.roberta.ast.syntax.action;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_stop</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning off the motor.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotorStopMode} (is the motor breaking or not).
 */
public class MotorStopAction<V> extends Action<V> {
    private final ActorPort port;
    private final MotorStopMode mode;

    private MotorStopAction(ActorPort port, MotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.MOTOR_STOP_ACTION, properties, comment);
        Assert.isTrue(port != null && mode != null);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorStopAction}. This instance is read only and can not be modified.
     *
     * @param port {@link ActorPort} on which the motor is connected; must be <b>not</b> null,
     * @param mode of stopping {@link MotorStopMode}; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MotorStopAction}
     */
    private static <V> MotorStopAction<V> make(ActorPort port, MotorStopMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MotorStopAction<V>(port, mode, properties, comment);
    }

    /**
     * @return port on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

    /**
     * @return stopping mode in which the motor is set.
     */
    public MotorStopMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "MotorStop [port=" + this.port + ", mode=" + this.mode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorStopAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 2);
        String portName = helper.extractField(fields, BlocklyConstants.MOTORPORT);
        String modeName = helper.extractField(fields, BlocklyConstants.MODE_);
        return MotorStopAction.make(ActorPort.get(portName), MotorStopMode.get(modeName), helper.extractBlockProperties(block), helper.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MOTORPORT, getPort().name());
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());

        return jaxbDestination;
    }
}

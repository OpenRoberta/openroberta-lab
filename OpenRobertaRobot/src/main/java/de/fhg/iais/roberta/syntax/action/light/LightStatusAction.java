package de.fhg.iais.roberta.syntax.action.light;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.actor.ILightVisitor;

/**
 * This class represents the <b>robActions_brickLight_off</b> and <b>robActions_brickLight_reset</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for turning the light off or reset them.<br/>
 * <br/>
 * The client must provide the {@link Status}.
 */
public class LightStatusAction<V> extends Action<V> {
    private final Status status;
    private final String port;

    private LightStatusAction(String port, Status status, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LIGHT_STATUS_ACTION"), properties, comment);
        Assert.isTrue(status != null);
        this.status = status;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LightStatusAction}. This instance is read only and can not be modified.
     *
     * @param status in which we want to set the lights (off or reset); must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LightStatusAction}
     */
    public static <V> LightStatusAction<V> make(String port, Status status, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LightStatusAction<>(port, status, properties, comment);
    }

    /**
     * @return status of the lights user wants to set.
     */
    public Status getStatus() {
        return this.status;
    }

    @Override
    public String toString() {
        return "LightStatusAction [" + this.port + ", " + this.status + "]";
    }

    /**
     * @return port.
     */
    public String getPort() {
        return this.port;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILightVisitor<V>) visitor).visitLightStatusAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        Status status = LightStatusAction.Status.RESET;
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, "0");
        if ( block.getType().equals(BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_OFF)
            || block.getType().equals("mbedActions_leds_off")
            || block.getType().equals("robActions_leds_off") ) {
            status = LightStatusAction.Status.OFF;
        }
        return LightStatusAction.make(factory.sanitizePort(port), status, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        if ( !this.port.toString().equals("0") ) {
            Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getPort().toString());
        }
        return jaxbDestination;
    }

    /**
     * Status in which user can set the lights.
     */
    public static enum Status {
        OFF, RESET;
    }
}

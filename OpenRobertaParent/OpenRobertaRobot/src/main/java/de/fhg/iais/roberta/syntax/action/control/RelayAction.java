package de.fhg.iais.roberta.syntax.action.control;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IRelayMode;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorControlVisitor;

public class RelayAction<V> extends Action<V> {
    private final IActorPort port;
    private final IRelayMode mode;

    private RelayAction(IActorPort port, IRelayMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("RELAY_ACTION"), properties, comment);
        Assert.isTrue(mode != null);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link RelayAction}. This instance is read only and can not be modified.
     *
     * @param color of the lights on the brick. All possible colors are defined in {@link BrickLedColor}; must be <b>not</b> null,
     * @param blinkMode type of the blinking; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link RelayAction}
     */
    private static <V> RelayAction<V> make(IActorPort port, IRelayMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RelayAction<>(port, mode, properties, comment);
    }

    /**
     * @return mode on/off.
     */
    public IRelayMode getMode() {
        return this.mode;
    }

    /**
     * @return port.
     */
    public IActorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "RelayAction [" + this.port + ", " + this.mode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstActorControlVisitor<V>) visitor).visitRelayAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        String mode = helper.extractField(fields, BlocklyConstants.RELAYSTATE, BlocklyConstants.DEFAULT);
        return RelayAction.make(factory.getActorPort(port), factory.getRelayMode(mode), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getPort().toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.RELAYSTATE, getMode().toString());
        return jaxbDestination;

    }
}
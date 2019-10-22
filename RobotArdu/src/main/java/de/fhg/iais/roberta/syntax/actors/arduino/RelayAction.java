package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.action.IRelayMode;
import de.fhg.iais.roberta.mode.action.BrickLedColor;
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
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class RelayAction<V> extends Action<V> {
    private final String port;
    private final IRelayMode mode;

    private RelayAction(String port, IRelayMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
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
    private static <V> RelayAction<V> make(String port, IRelayMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
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
    public String getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "RelayAction [" + this.port + ", " + this.mode + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitRelayAction(this);
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
        List<Field> fields = helper.extractFields(block, (short) 2);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        String mode = helper.extractField(fields, BlocklyConstants.RELAYSTATE, BlocklyConstants.DEFAULT);
        return RelayAction.make(factory.sanitizePort(port), factory.getRelayMode(mode), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getPort().toString());
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.RELAYSTATE, getMode().toString());
        return jaxbDestination;

    }
}
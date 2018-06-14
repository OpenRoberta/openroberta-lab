package de.fhg.iais.roberta.syntax.action.light;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;

/**
 * This class represents the <b>robactions_set_led</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code
 * for setting the LED status.<br/>
 * <br/>
 * The client must provide the {@link IActorPort} and {@link IMode}
 */
public class LedAction<V> extends Action<V> {
    private final IActorPort port;
    private final IMode mode;

    private LedAction(IActorPort port, IMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("LED_ACTION"), properties, comment);
        this.port = port;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Creates instance of {@link LedAction}. This instance is read only and can not be modified.
     *
     * @param port,
     * @param mode,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link LedAction}
     */
    private static <V> LedAction<V> make(IActorPort port, IMode mode, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new LedAction<>(port, mode, properties, comment);
    }

    /**
     * @return port.
     */
    public IActorPort getPort() {
        return this.port;
    }

    /**
     * @return mode.
     */
    public IMode getMode() {
        return this.mode;
    }

    @Override
    public String toString() {
        return "LightAction [" + this.port + ", " + this.mode + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstActorLightVisitor<V>) visitor).visitLedAction(this);
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
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT);
        String mode = helper.extractField(fields, BlocklyConstants.LEDSTATE);
        return LedAction.make(factory.getActorPort(port), factory.getLedMode(mode), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getPort().toString());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.LEDSTATE, getMode().toString());
        return jaxbDestination;

    }
}

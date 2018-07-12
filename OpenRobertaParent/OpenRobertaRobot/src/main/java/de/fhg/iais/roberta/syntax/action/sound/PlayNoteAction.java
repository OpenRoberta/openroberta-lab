package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
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
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;

/**
 * This class represents the <b>mbedActions_play_note</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * playing a specific note.<br/>
 * <br/>
 * The client must provide the note value and note of the sound. <br>
 */
public class PlayNoteAction<V> extends Action<V> {
    private final String duration;
    private final String frequency;
    private final IActorPort port;

    private PlayNoteAction(IActorPort port, String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLAY_NOTE_ACTION"), properties, comment);
        Assert.isTrue(NumberUtils.isNumber(duration) && NumberUtils.isNumber(frequency));
        this.duration = duration;
        this.frequency = frequency;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link PlayNoteAction}. This instance is read only and can not be modified.
     *
     * @param duration of the sound, the note value,
     * @param frequency of the sound, the note,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link PlayNoteAction}
     */
    private static <V> PlayNoteAction<V> make(IActorPort port, String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlayNoteAction<>(port, duration, frequency, properties, comment);
    }

    /**
     * @return port.
     */
    public IActorPort getPort() {
        return this.port;
    }

    /**
     * @return the duration of the sound.
     */
    public String getDuration() {
        return this.duration;
    }

    /**
     * @return the frequency of this action.
     */
    public String getFrequency() {
        return this.frequency;
    }

    @Override
    public String toString() {
        return "PlayNoteAction [ duration=" + this.duration + ", frequency=" + this.frequency + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return ((AstActorSoundVisitor<V>) visitor).visitPlayNoteAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 3);
        IRobotFactory factory = helper.getModeFactory();
        String duration = helper.extractField(fields, BlocklyConstants.DURATION);
        String port = helper.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        String frequency = helper.extractField(fields, BlocklyConstants.FREQUENCE);
        return PlayNoteAction.make(factory.getActorPort(port), duration, frequency, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        //        if ( !this.port.toString().equals(BlocklyConstants.NO_PORT) ) {
        //            JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.ACTORPORT, getPort().toString());
        //        }
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.FREQUENCE, this.frequency);

        return jaxbDestination;
    }
}

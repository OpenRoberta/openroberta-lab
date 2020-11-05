package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

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
import de.fhg.iais.roberta.visitor.hardware.actor.ISoundVisitor;

/**
 * This class represents the <b>mbedActions_play_note</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * playing a specific note.<br/>
 * <br/>
 * The client must provide the note value and note of the sound. <br>
 */
public class PlayNoteAction<V> extends Action<V> {
    private final String duration;
    private final String frequency;
    private final String port;

    private PlayNoteAction(String port, String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("PLAY_NOTE_ACTION"), properties, comment);
        Assert.isTrue(NumberUtils.isCreatable(duration) && NumberUtils.isCreatable(frequency));
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
    public static <V> PlayNoteAction<V> make(String port, String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new PlayNoteAction<>(port, duration, frequency, properties, comment);
    }

    /**
     * @return port.
     */
    public String getPort() {
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISoundVisitor<V>) visitor).visitPlayNoteAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = AbstractJaxb2Ast.extractFields(block, (short) 3);
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        String port = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT, BlocklyConstants.NO_PORT);
        String duration = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.DURATION, "2000");
        String frequency = AbstractJaxb2Ast.extractField(fields, BlocklyConstants.FREQUENCE, "261.626");
        return PlayNoteAction
            .make(factory.sanitizePort(port), duration, frequency, AbstractJaxb2Ast.extractBlockProperties(block), AbstractJaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.DURATION, this.duration);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.FREQUENCE, this.frequency);

        return jaxbDestination;
    }
}

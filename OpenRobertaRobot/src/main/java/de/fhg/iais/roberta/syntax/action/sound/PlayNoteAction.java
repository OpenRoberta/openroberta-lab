package de.fhg.iais.roberta.syntax.action.sound;

import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoHide;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>mbedActions_play_note</b> block<br/>
 * The client must provide the note value and note of the sound. <br>
 */
@NepoPhrase(containerType = "PLAY_NOTE_ACTION")
public class PlayNoteAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.DURATION, value = "2000")
    public final String duration;
    @NepoField(name = BlocklyConstants.FREQUENCE, value = "261.626")
    public final String frequency;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final  String port;
    @NepoHide
    public final Hide hide;

    public PlayNoteAction(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String duration, String frequency, String port, Hide hide) {
        super(kind, properties, comment);
        Assert.isTrue(NumberUtils.isCreatable(duration) && NumberUtils.isCreatable(frequency));
        this.duration = duration;
        this.frequency = frequency;
        this.port = port;
        this.hide = hide;
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
    public static <V> PlayNoteAction<V> make(String port, String duration, String frequency, BlocklyBlockProperties properties, BlocklyComment comment, Hide hide) {
        return new PlayNoteAction<>(BlockTypeContainer.getByName("PLAY_NOTE_ACTION"), properties, comment, duration, frequency, port, hide);
    }

    public String getPort() {
        return this.port;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getFrequency() {
        return this.frequency;
    }

    public Hide getHide() {
        return this.hide;
    }
}

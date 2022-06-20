package de.fhg.iais.roberta.syntax.action.sound;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>mbedActions_play_note</b> block<br/>
 * The client must provide the note value and note of the sound. <br>
 */
@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_play_note"}, containerType = "PLAY_NOTE_ACTION")
public final class PlayNoteAction<V> extends Action<V> {
    @NepoField(name = BlocklyConstants.DURATION, value = "2000")
    public final String duration;
    @NepoField(name = BlocklyConstants.FREQUENCE, value = "261.626")
    public final String frequency;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public PlayNoteAction(BlocklyBlockProperties properties, BlocklyComment comment, String duration, String frequency, String port, Hide hide) {
        super(properties, comment);
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
        return new PlayNoteAction<>(properties, comment, duration, frequency, port, hide);
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

    public String getPort() {
        return this.port;
    }
}

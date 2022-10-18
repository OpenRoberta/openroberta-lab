package de.fhg.iais.roberta.syntax.action.spike;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_play_note"}, name = "PLAY_NOTE_ACTION")
public final class PlayNoteAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DURATION, value = "2000")
    public final String duration;
    @NepoField(name = BlocklyConstants.FREQUENCY, value = "261.626")
    public final String frequency;

    public PlayNoteAction(BlocklyProperties properties, String duration, String frequency, Hide hide) {
        super(properties, hide);
        Assert.isTrue(NumberUtils.isCreatable(duration) && NumberUtils.isCreatable(frequency));
        this.duration = duration;
        this.frequency = frequency;
        setReadOnly();
    }

}

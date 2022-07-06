package de.fhg.iais.roberta.syntax.action.sound;

import org.apache.commons.lang3.math.NumberUtils;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_play_note"}, name = "PLAY_NOTE_ACTION")
public final class PlayNoteAction extends Action {
    @NepoField(name = BlocklyConstants.DURATION, value = "2000")
    public final String duration;
    @NepoField(name = BlocklyConstants.FREQUENCE, value = "261.626")
    public final String frequency;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public PlayNoteAction(BlocklyProperties properties, String duration, String frequency, String port, Hide hide) {
        super(properties);
        Assert.isTrue(NumberUtils.isCreatable(duration) && NumberUtils.isCreatable(frequency));
        this.duration = duration;
        this.frequency = frequency;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

}

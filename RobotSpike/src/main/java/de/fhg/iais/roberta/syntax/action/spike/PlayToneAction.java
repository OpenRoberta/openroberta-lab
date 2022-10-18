package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_play_tone"}, name = "TONE_ACTION")
public final class PlayToneAction extends ActionWithoutUserChosenName {
    @NepoValue(name = BlocklyConstants.FREQUENCY, type = BlocklyType.NUMBER_INT)
    public final Expr frequency;
    @NepoValue(name = BlocklyConstants.DURATION, type = BlocklyType.NUMBER_INT)
    public final Expr duration;

    public PlayToneAction(BlocklyProperties properties, Expr frequency, Expr duration, Hide hide) {
        super(properties, hide);
        Assert.isTrue(frequency.isReadOnly() && duration.isReadOnly() && (frequency != null) && (duration != null));
        this.frequency = frequency;
        this.duration = duration;
        setReadOnly();
    }
    
}

package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_brickled_on"}, name = "LED_ON_ACTION")
public final class LedOnAction extends ActionWithoutUserChosenName {
    @NepoValue(name = BlocklyConstants.COLOUR)
    public final Expr colour;

    public LedOnAction(BlocklyProperties properties, Expr colour, Hide hide) {
        super(properties, hide);
        this.colour = colour;
        setReadOnly();
    }

}

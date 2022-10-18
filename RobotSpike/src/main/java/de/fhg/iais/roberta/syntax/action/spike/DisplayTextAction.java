package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_text"}, name = "DISPLAY_TEXT_ACTION")
public final class DisplayTextAction extends ActionWithoutUserChosenName {
    @NepoField(name = "TYPE")
    public final String displayTextMode;
    @NepoValue(name = "TEXT", type = BlocklyType.STRING)
    public final Expr textToDisplay;

    public DisplayTextAction(BlocklyProperties properties, String displayTextMode, Expr textToDisplay, Hide hide) {
        super(properties, hide);
        //Assert.isTrue(displayTextMode != null && textToDisplay != null);
        this.displayTextMode = displayTextMode;
        this.textToDisplay = textToDisplay;
        setReadOnly();
    }
}
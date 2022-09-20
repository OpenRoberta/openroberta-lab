package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "DISPLAY_TEXT_ACTION", category = "ACTOR", blocklyNames = {"mbedActions_display_text"})
public final class DisplayTextAction extends Action {

    @NepoField(name = "TYPE")
    public final String mode;

    @NepoValue(name = "OUT", type = BlocklyType.STRING)
    public final Expr msg;

    public DisplayTextAction(BlocklyProperties properties, String mode, Expr msg) {
        super(properties);
        Assert.isTrue(msg != null && mode != null);
        this.msg = msg;
        this.mode = mode;
        setReadOnly();
    }
}

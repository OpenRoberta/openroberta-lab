package de.fhg.iais.roberta.syntax.actors.arduino;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "LED_MATRIX_TEXT_ACTION", category = "ACTOR", blocklyNames = {"mBotActions_display_text"})
public final class LEDMatrixTextAction extends Action {

    @NepoField(name = "TYPE")
    public final String displayMode;

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "OUT", type = BlocklyType.STRING)
    public final Expr msg;

    public LEDMatrixTextAction(BlocklyProperties properties, String displayMode, String port, Expr msg) {
        super(properties);
        Assert.isTrue(msg != null && port != null);
        this.displayMode = displayMode;
        this.port = port;
        this.msg = msg;
        setReadOnly();
    }
}

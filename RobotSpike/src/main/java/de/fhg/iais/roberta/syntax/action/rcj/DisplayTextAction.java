package de.fhg.iais.roberta.syntax.action.rcj;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_display_text_rcj"}, name = "DISPLAY_TEXT_ACTION")
public final class DisplayTextAction extends Action implements WithUserDefinedPort {
    @NepoValue(name = BlocklyConstants.TEXT, type = BlocklyType.ANY)
    public final Expr text;
    @NepoValue(name = BlocklyConstants.ROW, type = BlocklyType.NUMBER_INT)
    public final Expr row;
    @NepoField(name = BlocklyConstants.ACTORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoHide
    public final Hide hide;

    public DisplayTextAction(
        BlocklyProperties properties, Expr text, Expr row, String port,
        Hide hide) {
        super(properties);
        this.text = text;
        this.row = row;
        this.port = port;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.hide.getValue();
    }
}
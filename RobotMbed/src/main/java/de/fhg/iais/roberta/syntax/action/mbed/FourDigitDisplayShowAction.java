package de.fhg.iais.roberta.syntax.action.mbed;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"mbedActions_fourDigitDisplay_show"}, name = "FOURDIGITDISPLAY_SHOW_ACTION")
public final class FourDigitDisplayShowAction extends Action {
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER_INT)
    public final Expr value;
    @NepoValue(name = BlocklyConstants.POSITION, type = BlocklyType.NUMBER_INT)
    public final Expr position;
    @NepoValue(name = BlocklyConstants.COLON, type = BlocklyType.BOOLEAN)
    public final Expr colon;

    public FourDigitDisplayShowAction(BlocklyProperties properties, Expr value, Expr position, Expr colon) {
        super(properties);
        Assert.isTrue(value != null && position != null && colon != null);
        this.value = value;
        this.position = position;
        this.colon = colon;
        setReadOnly();
    }

}

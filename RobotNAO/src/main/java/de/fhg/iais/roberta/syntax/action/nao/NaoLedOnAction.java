package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SET_INTENSITY", category = "ACTOR", blocklyNames = {"actions_led_on_nao"})
public final class NaoLedOnAction extends Action {

    @NepoField(name = "ACTORPORT")
    public final String port;

    @NepoValue(name = "INTENSITY", type = BlocklyType.NUMBER_INT)
    public final Expr intensity;

    public NaoLedOnAction(BlocklyProperties properties, String port, Expr Intensity) {
        super(properties);
        this.port = port;
        Assert.notNull(Intensity);
        this.intensity = Intensity;
        setReadOnly();
    }
}

package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "SET_INTENSITY", category = "ACTOR", blocklyNames = {"naoActions_setIntensity"})
public final class SetIntensity extends Action {

    @NepoField(name = "LED")
    public final String led;

    @NepoValue(name = "INTENSITY", type = BlocklyType.NUMBER_INT)
    public final Expr Intensity;

    public SetIntensity(BlocklyProperties properties, String led, Expr Intensity) {
        super(properties);
        this.led = led;
        Assert.notNull(Intensity);
        this.Intensity = Intensity;
        setReadOnly();
    }
}

package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "RGB_LED", category = "ACTOR", blocklyNames = {"naoActions_rgbLeds"})
public final class SetLeds extends Action {

    @NepoField(name = "LED")
    public final String led;

    @NepoValue(name = "COLOR", type = BlocklyType.COLOR)
    public final Expr Color;

    public SetLeds(BlocklyProperties properties, String led, Expr Color) {
        super(properties);
        this.led = led;
        Assert.notNull(Color);
        this.Color = Color;
        setReadOnly();
    }
}

package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "SET_VOLUME", category = "ACTOR", blocklyNames = {"naoActions_setVolume"})
public final class SetVolume extends Action {
    @NepoValue(name = "VOLUME", type = BlocklyType.NUMBER_INT)
    public final Expr volume;

    public SetVolume(BlocklyProperties properties, Expr volume) {
        super(properties);
        this.volume = volume;
        setReadOnly();
    }

}

package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(name = "SET_MODE", category = "ACTOR", blocklyNames = {"naoActions_mode"})
public final class SetMode extends Action {

    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String modus;

    public SetMode(BlocklyProperties properties, String modus) {
        super(properties);
        Assert.notNull(modus, "Missing modus in Mode block!");
        this.modus = modus;
        setReadOnly();
    }
}

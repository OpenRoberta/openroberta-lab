package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;

@NepoPhrase(name = "HAND", category = "ACTOR", blocklyNames = {"naoActions_hand"})
public final class Hand extends Action {

    @NepoField(name = "SIDE")
    public final TurnDirection turnDirection;

    @NepoField(name = "MODE")
    public final String modus;

    public Hand(BlocklyProperties properties, TurnDirection turnDirection, String modus) {
        super(properties);
        Assert.notNull(turnDirection, "Missing turn direction in Hand block!");
        Assert.notNull(modus, "Missing modus in Hand block!");
        this.turnDirection = turnDirection;
        this.modus = modus;
        setReadOnly();
    }
}
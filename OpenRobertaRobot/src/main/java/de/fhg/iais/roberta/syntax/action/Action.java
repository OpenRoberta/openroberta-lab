package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

public abstract class Action extends Phrase {

    public Action(BlocklyProperties properties) {
        super(properties);
        this.setBlocklyType(BlocklyType.VOID);
    }

}

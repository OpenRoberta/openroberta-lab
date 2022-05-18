package de.fhg.iais.roberta.syntax.action.thymio;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robSensors_record_begin"}, name = "RECORD_BEGIN_ACTION")
public final class RecordStartAction extends Action {
    @NepoValue(name = "FILENAME", type = BlocklyType.NUMBER)
    public final Expr filename;

    public RecordStartAction(BlocklyProperties properties, Expr filename) {
        super(properties);
        this.filename = filename;
        setReadOnly();
    }
}

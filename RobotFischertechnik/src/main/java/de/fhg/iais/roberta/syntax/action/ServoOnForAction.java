package de.fhg.iais.roberta.syntax.action;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"robActions_servo_on_for_txt4"}, name = "SERVO_ON_FOR_ACTION")
public final class ServoOnForAction extends ActionWithUserChosenName {
    @NepoValue(name = BlocklyConstants.VALUE, type = BlocklyType.NUMBER)
    public final Expr value;

    public ServoOnForAction(
        BlocklyProperties properties,
        Expr value,
        String port) {
        super(properties, port);
        this.value = value;
        setReadOnly();
    }

}
package de.fhg.iais.roberta.syntax.action.spike;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "ACTOR", blocklyNames = {"actions_motordiff_on"}, name = "MOTORDIFF_ON_ACTION")
public final class MotorDiffOnAction extends ActionWithoutUserChosenName {
    @NepoField(name = BlocklyConstants.DIRECTION)
    public final String direction;
    @NepoValue(name = BlocklyConstants.POWER, type = BlocklyType.NUMBER)
    public final Expr power;
    @NepoField(name = "REGULATION")
    public final Boolean regulation;

    public MotorDiffOnAction(
        BlocklyProperties properties,
        String direction,
        Expr power,
        Boolean regulation,
        Hide hide) {
        super(properties, hide);
        this.direction = direction;
        this.power = power;
        this.regulation = regulation;
        setReadOnly();
    }

}
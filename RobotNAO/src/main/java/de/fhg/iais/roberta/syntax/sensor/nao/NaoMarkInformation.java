package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "NAO_MARK_INFORMATION", category = "SENSOR", blocklyNames = {"naoSensors_getMarkInformation"}, blocklyType = BlocklyType.ARRAY_NUMBER)
public final class NaoMarkInformation extends Sensor {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER)
    public final Expr naoMarkId;

    public NaoMarkInformation(BlocklyProperties properties, String mode, Expr naoMarkId) {
        super(properties);
        this.mode = mode;
        this.naoMarkId = naoMarkId;
        setReadOnly();
    }
}
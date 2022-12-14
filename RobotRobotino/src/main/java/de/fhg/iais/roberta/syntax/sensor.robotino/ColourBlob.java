package de.fhg.iais.roberta.syntax.sensor.robotino;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoExpr(name = "COLOUR_BLOB", category = "SENSOR", blocklyNames = {"robSensors_get_colourBlob"}, blocklyType = BlocklyType.ARRAY_NUMBER)
public final class ColourBlob extends Sensor implements WithUserDefinedPort {

    @NepoField(name = "MODE")
    public final String mode;
    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoValue(name = "MIN_HUE", type = BlocklyType.NUMBER)
    public final Expr minHue;
    @NepoValue(name = "MAX_HUE", type = BlocklyType.NUMBER)
    public final Expr maxHue;
    @NepoValue(name = "MIN_SAT", type = BlocklyType.NUMBER)
    public final Expr minSat;
    @NepoValue(name = "MAX_SAT", type = BlocklyType.NUMBER)
    public final Expr maxSat;
    @NepoValue(name = "MIN_VAL", type = BlocklyType.NUMBER)
    public final Expr minVal;
    @NepoValue(name = "MAX_VAL", type = BlocklyType.NUMBER)
    public final Expr maxVal;
    @NepoHide
    public final Hide hide;

    public ColourBlob(
        BlocklyProperties properties,
        String mode,
        String port,
        Expr minHue,
        Expr maxHue,
        Expr minSat,
        Expr maxSat,
        Expr minVal,
        Expr maxVal,
        Hide hide) {
        super(properties);
        this.mode = mode;
        this.port = port;
        this.minHue = minHue;
        this.maxHue = maxHue;
        this.minSat = minSat;
        this.maxSat = maxSat;
        this.minVal = minVal;
        this.maxVal = maxVal;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
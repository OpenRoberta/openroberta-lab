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

@NepoExpr(name = "MARKER_INFORMATION", category = "SENSOR", blocklyNames = {"robSensors_get_markerinf"}, blocklyType = BlocklyType.ARRAY_NUMBER)
public final class MarkerInformation extends Sensor implements WithUserDefinedPort {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoValue(name = "VALUE", type = BlocklyType.NUMBER)
    public final Expr markerId;
    
    @NepoHide
    public final Hide hide;


    public MarkerInformation(BlocklyProperties properties, String mode, String port, Expr markerId, Hide hide) {
        super(properties);
        this.mode = mode;
        this.port = port;
        this.markerId = markerId;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
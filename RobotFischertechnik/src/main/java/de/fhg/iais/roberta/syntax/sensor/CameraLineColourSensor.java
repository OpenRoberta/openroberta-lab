package de.fhg.iais.roberta.syntax.sensor;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoExpr(name = "CAMERA_LINE_COLOUR", category = "SENSOR", blocklyNames = {"robSensors_get_line_colour_camera_txt4"})
public final class CameraLineColourSensor extends Sensor implements WithUserDefinedPort {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;

    @NepoValue(name = "INDEX", type = BlocklyType.NUMBER)
    public final Expr lineId;

    public CameraLineColourSensor(BlocklyProperties properties, String mode, String port, Expr lineId) {
        super(properties);
        this.mode = mode;
        this.port = port;
        this.lineId = lineId;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * This class represents the <b>naoSensors_getFaceInformation</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for detecting a NaoMark.<br/>
 * <br/>
 */
@NepoExpr(name = "NAO_FACE_INFORMATION", category = "SENSOR", blocklyNames = {"naoSensors_getFaceInformation"}, blocklyType = BlocklyType.ARRAY_NUMBER)
public final class DetectedFaceInformation extends Sensor {

    @NepoField(name = "MODE")
    public final String mode;

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr faceName;

    public DetectedFaceInformation(BlocklyProperties properties, String mode, Expr faceName) {
        super(properties);
        this.mode = mode;
        this.faceName = faceName;
        setReadOnly();
    }
}

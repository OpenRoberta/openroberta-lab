package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "GET_VOLUME", category = "SENSOR", blocklyNames = {"naoActions_getVolume"}, blocklyType = BlocklyType.NUMBER)
public final class GetVolume extends Sensor {

    public GetVolume(BlocklyProperties properties) {
        super(properties);
        setReadOnly();
    }

}

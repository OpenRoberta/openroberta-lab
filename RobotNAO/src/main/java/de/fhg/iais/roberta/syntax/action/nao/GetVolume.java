package de.fhg.iais.roberta.syntax.action.nao;

import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoExpr(name = "GET_VOLUME", category = "SENSOR", blocklyNames = {"naoActions_getVolume"}, blocklyType = BlocklyType.NUMBER)
public final class GetVolume<V> extends Sensor<V> {

    public GetVolume(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        setReadOnly();
    }

    public static <V> GetVolume<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GetVolume<V>(properties, comment);
    }
}

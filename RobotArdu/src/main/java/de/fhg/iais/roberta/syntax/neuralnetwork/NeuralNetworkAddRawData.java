package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_ADD_RAWDATA", category = "STMT", blocklyNames = {"robActions_aifes_addrawdata"})
public final class NeuralNetworkAddRawData<V> extends Stmt<V> {
    @NepoValue(name = "NN_RAW_DATA", type = BlocklyType.CAPTURED_TYPE)
    public Expr<V> rawData;

    public NeuralNetworkAddRawData(BlocklyProperties properties, Expr<V> rawData) {
        super(properties);
        this.rawData = rawData;
        setReadOnly();
    }

}

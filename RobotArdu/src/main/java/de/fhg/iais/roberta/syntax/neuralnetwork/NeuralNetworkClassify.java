package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;

@NepoPhrase(containerType = "NEURAL_NETWORK_CLASSIFY")
public class NeuralNetworkClassify<V> extends Stmt<V> {
    @NepoValue(name = "NN_CLASS_PROBABILITIES", type = BlocklyType.ARRAY_NUMBER)
    public final Expr<V> probabilities;

    public NeuralNetworkClassify(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> probabilities) {
        super(kind, properties, comment);
        this.probabilities = probabilities;
        setReadOnly();
    }
}

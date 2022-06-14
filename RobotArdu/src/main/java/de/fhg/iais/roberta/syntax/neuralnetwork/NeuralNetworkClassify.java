package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_aifes_classify"}, containerType = "NEURAL_NETWORK_CLASSIFY")
public class NeuralNetworkClassify<V> extends Stmt<V> {
    @NepoValue(name = "NN_CLASS_PROBABILITIES", type = BlocklyType.ARRAY_NUMBER)
    public final Expr<V> probabilities;

    public NeuralNetworkClassify(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> probabilities) {
        super(properties, comment);
        this.probabilities = probabilities;
        setReadOnly();
    }
}

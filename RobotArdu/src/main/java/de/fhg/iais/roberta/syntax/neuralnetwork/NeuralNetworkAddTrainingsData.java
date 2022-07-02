package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;

@NepoPhrase(name = "NEURAL_NETWORK_ADD_TRAININGSDATA", category = "STMT", blocklyNames = {"robActions_aifes_addtrainingsdata"})
public final class NeuralNetworkAddTrainingsData<V> extends Stmt<V> {
    @NepoValue(name = "NN_CLASS_NUMBER", type = BlocklyType.CAPTURED_TYPE)
    public Expr<V> classNumber;

    public NeuralNetworkAddTrainingsData(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> classNumber) {
        super(properties, comment);
        this.classNumber = classNumber;
        setReadOnly();
    }

}

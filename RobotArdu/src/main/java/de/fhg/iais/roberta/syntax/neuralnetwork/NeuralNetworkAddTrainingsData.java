package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_ADD_TRAININGSDATA", category = "STMT", blocklyNames = {"robActions_aifes_addtrainingsdata"})
public final class NeuralNetworkAddTrainingsData extends Stmt {
    @NepoValue(name = "NN_CLASS_NUMBER", type = BlocklyType.CAPTURED_TYPE)
    public Expr classNumber;

    public NeuralNetworkAddTrainingsData(BlocklyProperties properties, Expr classNumber) {
        super(properties);
        this.classNumber = classNumber;
        setReadOnly();
    }

}

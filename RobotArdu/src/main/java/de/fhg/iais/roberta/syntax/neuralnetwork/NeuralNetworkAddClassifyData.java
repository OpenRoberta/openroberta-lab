package de.fhg.iais.roberta.syntax.neuralnetwork;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(name = "NEURAL_NETWORK_ADD_CLASSFYDATA", category = "STMT", blocklyNames = {"robActions_aifes_addclassifydata"})
public final class NeuralNetworkAddClassifyData extends Stmt {
    @NepoValue(name = "NN_CLASSIFY_DATA", type = BlocklyType.CAPTURED_TYPE)
    public Expr classNumber;

    public NeuralNetworkAddClassifyData(BlocklyProperties properties, Expr classNumber) {
        super(properties);
        this.classNumber = classNumber;
        setReadOnly();
    }

    public String getValueNNClassify(Expr rawData) {
        String rawValue = rawData.toString();
        if ( rawValue.contains("NumConst") ) {
            rawValue = rawValue.replace("NumConst", "");
            rawValue = rawValue.replace("[", "");
            rawValue = rawValue.replace("]", "");
            rawValue = rawValue.replace("value: ", "");
        }
        if ( rawValue.contains("Var") ) {
            rawValue = rawValue.replace("Var", "");
            rawValue = rawValue.replace("[", "");
            rawValue = rawValue.replace("]", "");
            rawValue = rawValue.replace(" ", "___");
        }
        return rawValue;
    }

}

package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "SENSOR", blocklyNames = {"naoSensors_recognizeWord"}, name = "RECOGNIZE_WORD", blocklyType = BlocklyType.STRING)
public final class RecognizeWord extends Sensor {
    @NepoValue(name = BlocklyConstants.WORD, type = BlocklyType.STRING)
    public final Expr vocabulary;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;

    public RecognizeWord(BlocklyProperties properties, Expr vocabulary, String mode) {
        super(properties);
        Assert.isTrue(vocabulary != null);
        this.vocabulary = vocabulary;
        this.mode = mode;
        setReadOnly();
    }
}

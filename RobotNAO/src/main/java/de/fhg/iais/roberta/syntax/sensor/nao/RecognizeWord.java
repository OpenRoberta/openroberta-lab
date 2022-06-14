package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>naoActions_recognizeWord</b> block
 */
@NepoPhrase(category = "SENSOR", blocklyNames = {"naoSensors_recognizeWord"}, containerType = "RECOGNIZE_WORD")
public class RecognizeWord<V> extends Sensor<V> {
    @NepoValue(name = BlocklyConstants.WORD, type = BlocklyType.STRING)
    public final Expr<V> vocabulary;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;

    public RecognizeWord(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> vocabulary, String mode) {
        super(properties, comment);
        Assert.isTrue(vocabulary != null);
        this.vocabulary = vocabulary;
        this.mode = mode;
        setReadOnly();
    }
}

package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoPhrase;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>naoActions_recognizeWord</b> block
 */
@NepoPhrase(containerType = "RECOGNIZE_WORD")
public class RecognizeWord<V> extends Sensor<V> {
    @NepoValue(name = BlocklyConstants.WORD, type = BlocklyType.STRING)
    public final Expr<V> vocabulary;
    @NepoField(name = BlocklyConstants.MODE)
    public final String mode;

    public RecognizeWord(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> vocabulary, String mode) {
        super(kind, properties, comment);
        Assert.isTrue(vocabulary != null);
        this.vocabulary = vocabulary;
        this.mode = mode;
        setReadOnly();
    }
}

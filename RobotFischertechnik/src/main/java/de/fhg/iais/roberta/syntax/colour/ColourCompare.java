package de.fhg.iais.roberta.syntax.colour;

import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "ACTOR", blocklyNames = {"colour_compare"}, name = "COMPARE_COLOUR")
public final class ColourCompare extends Action {
    @NepoField(name = "OP")
    public final String op;
    @NepoValue(name = "COLOUR1", type = BlocklyType.COLOR)
    public final Expr colour1;
    @NepoValue(name = "COLOUR2", type = BlocklyType.COLOR)
    public final Expr colour2;
    @NepoValue(name = "TOLERANCE", type = BlocklyType.NUMBER)
    public final Expr tolerance;


    public ColourCompare(BlocklyProperties properties, String op, Expr colour1, Expr colour2, Expr tolerance) {
        super(properties);
        this.op = op;
        this.colour1 = colour1;
        this.colour2 = colour2;
        this.tolerance = tolerance;
        setReadOnly();
    }
}

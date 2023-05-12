package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoExpr(name = "TEXT_INDEX_OF_FUNCT", category = "FUNCTION", blocklyNames = {"lists_indexOf", "robLists_indexOf"}, blocklyType = BlocklyType.CAPTURED_TYPE)
public final class IndexOfFunct extends Function {

    @NepoField(name = "END")
    public final IndexLocation location;

    @NepoValue(name = "VALUE", type = BlocklyType.STRING)
    public final Expr value;

    @NepoValue(name = "FIND", type = BlocklyType.STRING)
    public final Expr find;

    public IndexOfFunct(BlocklyProperties properties, IndexLocation name, Expr value, Expr find) {
        super(properties);
        this.location = name;
        this.value = value;
        this.find = find;
        setReadOnly();
    }
}

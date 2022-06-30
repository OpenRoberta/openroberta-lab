package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"text"}, name = "STRING_CONST", blocklyType = BlocklyType.STRING)
public final class StringConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.TEXT)
    public final String value;

    public StringConst(BlocklyBlockProperties properties, BlocklyComment comment, String value) {
        super(properties, comment);
        this.value = value;
        setReadOnly();
    }

    public static <V> StringConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new StringConst<V>(properties, comment, value);
    }

    public String getValue() {
        return this.value;
    }
}

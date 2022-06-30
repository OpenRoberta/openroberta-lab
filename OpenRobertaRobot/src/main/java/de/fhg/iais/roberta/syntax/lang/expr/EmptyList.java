package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"lists_create_empty"}, name = "EMPTY_LIST", blocklyType = BlocklyType.ARRAY)
public final class EmptyList<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.LIST_TYPE)
    public final BlocklyType typeVar;

    public EmptyList(BlocklyBlockProperties properties, BlocklyComment comment, BlocklyType typeVar) {
        super(properties, comment);
        Assert.isTrue(typeVar != null);
        this.typeVar = typeVar;
        setReadOnly();
    }

    public static <V> EmptyList<V> make(BlocklyType typeVar, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EmptyList<V>(properties, comment, typeVar);
    }

    public BlocklyType getTypeVar() {
        return this.typeVar;
    }
}

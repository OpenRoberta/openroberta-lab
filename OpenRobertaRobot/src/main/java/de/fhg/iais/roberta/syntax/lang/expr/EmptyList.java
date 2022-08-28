package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"lists_create_empty"}, name = "EMPTY_LIST", blocklyType = BlocklyType.ARRAY)
public final class EmptyList extends Expr {
    @NepoField(name = BlocklyConstants.LIST_TYPE)
    public final BlocklyType typeVar;

    public EmptyList(BlocklyProperties properties, BlocklyType typeVar) {
        super(properties);
        Assert.isTrue(typeVar != null);
        this.typeVar = typeVar;
        setReadOnly();
    }

}

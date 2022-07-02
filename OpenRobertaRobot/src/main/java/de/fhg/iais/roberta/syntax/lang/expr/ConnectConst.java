package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoData;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

@NepoExpr(name = "CONNECTION_CONST", category = "EXPR", blocklyNames = {"robCommunication_connection"})
public final class ConnectConst<V> extends Expr<V> {
    @NepoField(name = "CONNECTION")
    public final String value;

    @NepoData
    public final String dataValue;

    public ConnectConst(BlocklyBlockProperties properties, BlocklyComment comment, String value, String dataValue) {
        super(properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        this.dataValue = dataValue;
        setReadOnly();
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.CONNECTION;
    }
}

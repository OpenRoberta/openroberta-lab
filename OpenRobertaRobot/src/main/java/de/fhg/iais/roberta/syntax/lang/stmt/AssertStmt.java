package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoPhrase(category = "STMT", blocklyNames = {"robActions_assert"}, name = "ASSERT_STMT")
public final class AssertStmt extends Stmt {
    @NepoValue(name = BlocklyConstants.OUT, type = BlocklyType.BOOLEAN)
    public final Expr asserts;
    @NepoField(name = BlocklyConstants.TEXT)
    public final String msg;

    public AssertStmt(BlocklyProperties properties, Expr asserts, String msg) {
        super(properties);
        Assert.isTrue(asserts != null && asserts.isReadOnly());
        this.asserts = asserts;
        this.msg = msg;
        setReadOnly();
    }

}

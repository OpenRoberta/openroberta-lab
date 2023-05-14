package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoMutation;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

@NepoExpr(name = "MATH_ON_LIST_FUNCT", category = "FUNCTION", blocklyNames = {"math_on_list"}, blocklyType = BlocklyType.NUMBER)
public final class MathOnListFunct extends Function {

    @NepoMutation(fieldName = "op")
    public final Mutation mutation;

    @NepoField(name = "OP")
    public final FunctionNames functName;

    @NepoValue(name = "LIST", type = BlocklyType.ARRAY)
    public final Expr list;

    public MathOnListFunct(BlocklyProperties properties, Mutation mutation, FunctionNames name, Expr list) {
        super(properties);
        this.mutation = mutation;
        this.functName = name;
        this.list = list;
        setReadOnly();
    }
}

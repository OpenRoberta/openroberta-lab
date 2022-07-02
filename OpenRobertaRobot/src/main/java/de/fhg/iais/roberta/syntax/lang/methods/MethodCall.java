package de.fhg.iais.roberta.syntax.lang.methods;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "METHOD_CALL", category = "METHOD", blocklyNames = {"robProcedures_callreturn", "robProcedures_callnoreturn"})
public final class MethodCall<V> extends Method<V> {
    public final String oraMethodName;
    public final ExprList<V> oraParameters;
    public final ExprList<V> oraParametersValues;
    public final BlocklyType oraReturnType;

    public MethodCall(
        String oraMethodName,
        ExprList<V> oraParameters,
        ExprList<V> oraParametersValues,
        BlocklyType oraReturnType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(properties, comment);
        Assert.isTrue(!oraMethodName.equals("") && oraParameters.isReadOnly() && oraParametersValues.isReadOnly());
        this.oraMethodName = oraMethodName;
        this.oraParameters = oraParameters;
        this.oraParametersValues = oraParametersValues;
        this.oraReturnType = oraReturnType;
        setReadOnly();
    }

    @Override
    public String getMethodName() {
        return this.oraMethodName;
    }

    @Override
    public ExprList<V> getParameters() {
        return this.oraParameters;
    }

    public ExprList<V> getParametersValues() {
        return this.oraParametersValues;
    }

    @Override
    public BlocklyType getReturnType() {
        return this.oraReturnType;
    }

    @Override
    public String toString() {
        return "MethodCall [" + this.oraMethodName + ", " + this.oraParameters + ", " + this.oraParametersValues + ", " + this.oraReturnType + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyType outputType = block.getMutation().getOutputType() == null ? BlocklyType.VOID : BlocklyType.get(block.getMutation().getOutputType());
        String methodName = block.getMutation().getName();
        List<Arg> arguments = block.getMutation().getArg();
        ExprList<V> parameters = helper.argumentsToExprList(arguments);
        BlocklyType[] types = Jaxb2Ast.argumentsToParametersType(arguments);
        int numberOfArguments = arguments.size();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) numberOfArguments);

        ExprList<V> parametersValues = helper.valuesToExprList(values, types, numberOfArguments, BlocklyConstants.ARG);

        return new MethodCall<>(methodName, parameters, parametersValues, outputType, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setName(getMethodName());
        if ( this.oraReturnType != BlocklyType.VOID ) {
            mutation.setOutputType(this.oraReturnType.getBlocklyName());
        }
        if ( !this.oraParameters.get().isEmpty() ) {
            for ( Expr<V> parameter : this.oraParameters.get() ) {
                Arg arg = new Arg();
                arg.setName(((Var<V>) parameter).name);
                arg.setType(((Var<V>) parameter).getVarType().getBlocklyName());
                mutation.getArg().add(arg);
            }
        }
        jaxbDestination.setMutation(mutation);
        int counter = 0;
        for ( Expr<V> parameterValue : this.oraParametersValues.get() ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ARG + counter, parameterValue);
            counter++;
        }

        return jaxbDestination;
    }
}

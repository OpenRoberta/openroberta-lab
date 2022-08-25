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
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "METHOD_CALL", category = "METHOD", blocklyNames = {"robProcedures_callreturn", "robProcedures_callnoreturn"})
public final class MethodCall extends Method {
    public final String oraMethodName;
    public final ExprList oraParameters;
    public final ExprList oraParametersValues;
    public final BlocklyType oraReturnType;

    public MethodCall(
        String oraMethodName,
        ExprList oraParameters,
        ExprList oraParametersValues,
        BlocklyType oraReturnType,
        BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(!oraMethodName.equals("") && oraParameters.isReadOnly() && oraParametersValues.isReadOnly());
        this.oraMethodName = Util.sanitizeProgramProperty(oraMethodName);
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
    public String getCodeSafeMethodName() {
        return CODE_SAFE_PREFIX + this.oraMethodName;
    }

    @Override
    public ExprList getParameters() {
        return this.oraParameters;
    }

    public ExprList getParametersValues() {
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

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        BlocklyType outputType = block.getMutation().getOutputType() == null ? BlocklyType.VOID : BlocklyType.get(block.getMutation().getOutputType());
        String methodName = block.getMutation().getName();
        List<Arg> arguments = block.getMutation().getArg();
        ExprList parameters = helper.argumentsToExprList(arguments);
        BlocklyType[] types = Jaxb2Ast.argumentsToParametersType(arguments);
        int numberOfArguments = arguments.size();
        List<Value> values = Jaxb2Ast.extractValues(block, (short) numberOfArguments);

        ExprList parametersValues = helper.valuesToExprList(values, types, numberOfArguments, BlocklyConstants.ARG);

        return new MethodCall(methodName, parameters, parametersValues, outputType, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setName(getMethodName());
        if ( this.oraReturnType != BlocklyType.VOID ) {
            mutation.setOutputType(this.oraReturnType.getBlocklyName());
        }
        if ( !this.oraParameters.get().isEmpty() ) {
            for ( Expr parameter : this.oraParameters.get() ) {
                Arg arg = new Arg();
                arg.setName(((Var) parameter).name);
                arg.setType(((Var) parameter).getVarType().getBlocklyName());
                mutation.getArg().add(arg);
            }
        }
        jaxbDestination.setMutation(mutation);
        int counter = 0;
        for ( Expr parameterValue : this.oraParametersValues.get() ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ARG + counter, parameterValue);
            counter++;
        }

        return jaxbDestination;
    }
}

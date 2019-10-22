package de.fhg.iais.roberta.syntax.lang.methods;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>robProcedures_defreturn</b> block from Blockly into the AST (abstract syntax tree). Object from this class is used to create a
 * method with return value<br/>
 */
public class MethodCall<V> extends Method<V> {
    private final String oraMethodName;
    private final ExprList<V> oraParameters;
    private final ExprList<V> oraParametersValues;
    private final BlocklyType oraReturnType;

    private MethodCall(
        String oraMethodName,
        ExprList<V> oraParameters,
        ExprList<V> oraParametersValues,
        BlocklyType oraReturnType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        super(BlockTypeContainer.getByName("METHOD_CALL"), properties, comment);
        Assert.isTrue(!oraMethodName.equals("") && oraParameters.isReadOnly() && oraParametersValues.isReadOnly());
        this.oraMethodName = oraMethodName;
        this.oraParameters = oraParameters;
        this.oraParametersValues = oraParametersValues;
        this.oraReturnType = oraReturnType;
        setReadOnly();
    }

    /**
     * creates instance of {@link MethodCall}. This instance is read only and cannot be modified.
     *
     * @param methodName
     * @param parameters
     * @param body of the method
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @param return_ type of the method
     * @return read only object of class {@link MethodCall}
     */
    public static <V> MethodCall<V> make(
        String methodName,
        ExprList<V> parameters,
        ExprList<V> parametersValues,
        BlocklyType returnType,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        return new MethodCall<>(methodName, parameters, parametersValues, returnType, properties, comment);
    }

    /**
     * @return the methodName
     */
    @Override
    public String getMethodName() {
        return this.oraMethodName;
    }

    /**
     * @return the parameters
     */
    @Override
    public ExprList<V> getParameters() {
        return this.oraParameters;
    }

    /**
     * @return the parametersValues
     */
    public ExprList<V> getParametersValues() {
        return this.oraParametersValues;
    }

    /**
     * @return the return_
     */
    @Override
    public BlocklyType getReturnType() {
        return this.oraReturnType;
    }

    @Override
    public String toString() {
        return "MethodCall [" + this.oraMethodName + ", " + this.oraParameters + ", " + this.oraParametersValues + ", " + this.oraReturnType + "]";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMethodCall(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        BlocklyType outputType = block.getMutation().getOutputType() == null ? BlocklyType.VOID : BlocklyType.get(block.getMutation().getOutputType());
        String methodName = block.getMutation().getName();
        List<Arg> arguments = block.getMutation().getArg();
        ExprList<V> parameters = helper.argumentsToExprList(arguments);
        BlocklyType[] types = helper.argumentsToParametersType(arguments);
        int numberOfArguments = arguments.size();
        List<Value> values = helper.extractValues(block, (short) numberOfArguments);

        ExprList<V> parametersValues = helper.valuesToExprList(values, types, numberOfArguments, BlocklyConstants.ARG);

        return MethodCall.make(methodName, parameters, parametersValues, outputType, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setName(getMethodName());
        if ( this.oraReturnType != BlocklyType.VOID ) {
            mutation.setOutputType(this.oraReturnType.getBlocklyName());
        }
        if ( !this.oraParameters.get().isEmpty() ) {
            for ( Expr<V> parameter : this.oraParameters.get() ) {
                Arg arg = new Arg();
                arg.setName(((Var<V>) parameter).getValue());
                arg.setType(((Var<V>) parameter).getVarType().getBlocklyName());
                mutation.getArg().add(arg);
            }
        }
        jaxbDestination.setMutation(mutation);
        int counter = 0;
        for ( Expr<V> parameterValue : this.oraParametersValues.get() ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ARG + counter, parameterValue);
            counter++;
        }

        return jaxbDestination;
    }
}

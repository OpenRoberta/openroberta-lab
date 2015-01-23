package de.fhg.iais.roberta.ast.syntax.methods;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robProcedures_defnoreturn</b> block from Blockly
 * into the AST (abstract syntax tree). Object from this class is used to create a method with no return<br/>
 */
public class MethodVoid<V> extends Method<V> {
    private final String methodName;
    private final ExprList<V> parameters;
    private final StmtList<V> body;

    private MethodVoid(String methodName, ExprList<V> parameters, StmtList<V> body, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.METHOD_VOID, properties, comment);
        Assert.isTrue(!methodName.equals("") && parameters.isReadOnly() && body.isReadOnly());
        this.methodName = methodName;
        this.parameters = parameters;
        this.body = body;
        setReadOnly();
    }

    /**
     * creates instance of {@link MethodVoid}. This instance is read only and cannot be modified.
     *
     * @param methodName
     * @param parameters
     * @param body of the method
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MethodVoid}
     */
    public static <V> MethodVoid<V> make(String methodName, ExprList<V> parameters, StmtList<V> body, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MethodVoid<V>(methodName, parameters, body, properties, comment);
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * @return the variables
     */
    public ExprList<V> getVParameters() {
        return this.parameters;
    }

    /**
     * @return the body
     */
    public StmtList<V> getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        return "MethodVoid [" + this.methodName + ", " + this.parameters + ", " + this.body + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMethodVoid(this);
    }

    @Override
    public Block astToBlock() {
        boolean declare = this.parameters.get().size() == 0 ? false : true;
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);
        Mutation mutation = new Mutation();
        mutation.setDeclare(declare);
        jaxbDestination.setMutation(mutation);
        AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.NAME, this.methodName);
        AstJaxbTransformerHelper.addStatement(jaxbDestination, BlocklyConstants.ST, this.parameters);
        AstJaxbTransformerHelper.addStatement(jaxbDestination, BlocklyConstants.STACK, this.body);
        return jaxbDestination;
    }

}

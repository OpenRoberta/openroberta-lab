package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>variables_set</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * assignment a value to a variable.<br/>
 * <br>
 * The client must provide the name of the variable and value.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Var, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class AssignStmt<V> extends Stmt<V> {
    private final Var<V> name;
    private final Expr<V> expr;

    private AssignStmt(Var<V> name, Expr<V> expr, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ASSIGN_STMT"), properties, comment);
        Assert.isTrue(name != null && expr != null && name.isReadOnly() && expr.isReadOnly());
        this.name = name;
        this.expr = expr;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AssignStmt}.
     *
     * @param name of the variable; must be <b>not</b> null and <b>read only</b>,,
     * @param expr that we want to assign to the {@link #name}; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return instance of {@link AssignStmt}
     */
    public static <V> AssignStmt<V> make(Var<V> name, Expr<V> expr, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new AssignStmt<V>(name, expr, properties, comment);
    }

    /**
     * @return name of the variable.
     */
    public final Var<V> getName() {
        return this.name;
    }

    /**
     * @return expression that will be assigned to the variable.
     */
    public final Expr<V> getExpr() {
        return this.expr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        sb.append(this.name).append(" := ").append(this.expr).append("\n");
        return sb.toString();
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitAssignStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Value> values = helper.extractValues(block, (short) 1);
        Phrase<V> p = helper.extractValue(values, new ExprParam(BlocklyConstants.VALUE, BlocklyType.CAPTURED_TYPE));
        Expr<V> exprr = helper.convertPhraseToExpr(p);
        return AssignStmt
            .make((Var<V>) helper.extractVar(block), helper.convertPhraseToExpr(exprr), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        String varType = getName().getVarType().getBlocklyName();

        Mutation mutation = new Mutation();
        mutation.setDatatype(varType);
        jaxbDestination.setMutation(mutation);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.VAR, getName().getValue());
        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.VALUE, getExpr());

        return jaxbDestination;
    }

}

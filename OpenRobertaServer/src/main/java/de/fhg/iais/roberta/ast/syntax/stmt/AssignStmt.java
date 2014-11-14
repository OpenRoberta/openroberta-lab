package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>variables_set</b> block from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for assignment a value to a variable.<br/>
 * <br>
 * The client must provide the name of the variable and value.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Var, Expr, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class AssignStmt<V> extends Stmt<V> {
    private final Var<V> name;
    private final Expr<V> expr;

    private AssignStmt(Var<V> name, Expr<V> expr, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.ASSIGN_STMT, properties, comment);
        Assert.isTrue(name.isReadOnly() && expr.isReadOnly());
        this.name = name;
        this.expr = expr;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AssignStmt}.
     *
     * @param name of the variable,
     * @param expr that we want to assign to the {@link #name},
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
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitAssignStmt(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        AstJaxbTransformerHelper.addField(jaxbDestination, "VAR", getName().getValue());
        AstJaxbTransformerHelper.addValue(jaxbDestination, "VALUE", getExpr());

        return jaxbDestination;
    }

}

package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>variables_set</b> block from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for assignment a value to a variable.<br/>
 * <br>
 * The client must provide the name of the variable and value.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(Var, Expr)}.<br>
 */
public class AssignStmt extends Stmt {
    private final Var name;
    private final Expr expr;

    private AssignStmt(Var name, Expr expr) {
        super(Phrase.Kind.ASSIGN_STMT);
        Assert.isTrue(name.isReadOnly() && expr.isReadOnly());
        this.name = name;
        this.expr = expr;
        setReadOnly();
    }

    /**
     * Create object of the class {@link AssignStmt}.
     * 
     * @param name of the variable
     * @param expr that we want to assign to the {@link #name}
     * @return instance of {@link AssignStmt}
     */
    public static AssignStmt make(Var name, Expr expr) {
        return new AssignStmt(name, expr);
    }

    /**
     * @return name of the variable.
     */
    public final Var getName() {
        return this.name;
    }

    /**
     * @return expression that will be assigned to the variable.
     */
    public final Expr getExpr() {
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
    public void accept(Visitor visitor) {
        visitor.visitAssignStmt(this);
    }

}

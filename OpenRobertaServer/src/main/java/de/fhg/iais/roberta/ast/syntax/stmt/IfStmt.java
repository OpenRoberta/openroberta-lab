package de.fhg.iais.roberta.ast.syntax.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>if-else-elseif</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate if statement.<br/>
 */
public class IfStmt<V> extends Stmt<V> {
    private final List<Expr<V>> expr;
    private final List<StmtList<V>> thenList;
    private final StmtList<V> elseList;
    private final boolean ternary;
    private final int _else;
    private final int _elseIf;

    private IfStmt(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        boolean ternary,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        super(Phrase.Kind.IF_STMT, properties, comment);
        Assert.isTrue(expr.size() == thenList.size() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        this.ternary = ternary;
        this._else = _else;
        this._elseIf = _elseIf;
        setReadOnly();
    }

    /**
     * create <b>if-then-else</b> statement where we have one or more the one <b>if</b> and <b>then</b>.
     *
     * @param expr list of all expressions that should be evaluated in the <b>if</b> parts,
     * @param thenList all statements that are in the <b>then</b> parts,
     * @param elseList all statements that are in the <b>else</b> parts,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @param _else statement,
     * @param _elseIf number of if statements
     * @return read only object of class {@link IfStmt}
     */
    public static <V> IfStmt<V> make(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        return new IfStmt<V>(expr, thenList, elseList, false, properties, comment, _else, _elseIf);
    }

    /**
     * create ternary operator
     *
     * @param expr expression that should be evaluated in the <b>if</b> part,
     * @param thenList statement that is in the <b>then</b> part,
     * @param elseList all statement that is in the <b>else</b> part,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @param _else statement,
     * @param _elseIf number of if statements
     * @return read only object of class {@link IfStmt}
     */
    public static <V> IfStmt<V> make(
        Expr<V> expr,
        StmtList<V> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        List<Expr<V>> exprsList = new ArrayList<Expr<V>>();
        List<StmtList<V>> thensList = new ArrayList<StmtList<V>>();
        exprsList.add(expr);
        thensList.add(thenList);
        return new IfStmt<V>(exprsList, thensList, elseList, true, properties, comment, _else, _elseIf);
    }

    /**
     * create <b>if-then</b> statement where we have one or more the one <b>if</b> and <b>then</b>.
     *
     * @param expr list of all expressions that should be evaluated in the <b>if</b> parts,
     * @param thenList all statements that are in the <b>then</b> parts,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @param _else statement,
     * @param _elseIf number of if statements
     * @return read only object of class {@link IfStmt}
     */
    public static <V> IfStmt<V> make(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        StmtList<V> elseList = StmtList.make();
        elseList.setReadOnly();
        return new IfStmt<V>(expr, thenList, elseList, false, properties, comment, _else, _elseIf);
    }

    /**
     * @return <b>true</b> if the statement is ternary.
     */
    public boolean isTernary() {
        return this.ternary;
    }

    /**
     * @return list with all expressions that should be evaluated in the <b>if</b> part.
     */
    public final List<Expr<V>> getExpr() {
        return this.expr;
    }

    /**
     * @return list with all statements that are in <b>then</b> part.
     */
    public final List<StmtList<V>> getThenList() {
        return this.thenList;
    }

    /**
     * @return list with all statements that are in <b>else</b> part.
     */
    public final StmtList<V> getElseList() {
        return this.elseList;
    }

    /**
     * @return 1 if there is else statement
     */
    public int get_else() {
        return this._else;
    }

    /**
     * @return number of if statements
     */
    public int get_elseIf() {
        return this._elseIf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        for ( int i = 0; i < this.expr.size(); i++ ) {
            sb.append("if ").append(this.expr.get(i));
            appendNewLine(sb, 0, ",then");
            sb.append(this.thenList.get(i).toString());
            if ( i + 1 < this.expr.size() ) {
                appendNewLine(sb, 0, ",else ");
            }
        }
        if ( this.elseList.get().size() != 0 ) {
            appendNewLine(sb, 0, ",else");
            sb.append(this.elseList.toString());
        }
        appendNewLine(sb, 0, "");
        return sb.toString();
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitIfStmt(this);
    }

    @Override
    public Block astToBlock() {
        Mutation mutation;
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals("logic_ternary") ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, "IF", getExpr().get(0));
            AstJaxbTransformerHelper.addValue(jaxbDestination, "THEN", getThenList().get(0).get().get(0));
            AstJaxbTransformerHelper.addValue(jaxbDestination, "ELSE", getElseList().get().get(0));
            return jaxbDestination;
        }
        int _else = get_else();
        int _elseIf = get_elseIf();

        StmtList<?> elseList = getElseList();
        int expr = 0;
        expr = getExpr().size();

        if ( _else != 0 || _elseIf != 0 ) {
            mutation = new Mutation();
            if ( _else != 0 ) {
                mutation.setElse(BigInteger.ONE);
            }
            if ( _elseIf > 0 ) {
                mutation.setElseif(BigInteger.valueOf(_elseIf));
            }
            jaxbDestination.setMutation(mutation);
            Repetitions repetitions = new Repetitions();
            for ( int i = 0; i < expr; i++ ) {
                AstJaxbTransformerHelper.addValue(repetitions, "IF" + i, getExpr().get(i));
                AstJaxbTransformerHelper.addStatement(repetitions, "DO" + i, getThenList().get(i));
            }
            if ( elseList.get().size() != 0 ) {
                AstJaxbTransformerHelper.addStatement(repetitions, "ELSE", getElseList());
            }
            jaxbDestination.setRepetitions(repetitions);
            return jaxbDestination;
        }

        AstJaxbTransformerHelper.addValue(jaxbDestination, "IF0", getExpr().get(0));
        AstJaxbTransformerHelper.addStatement(jaxbDestination, "DO0", getThenList().get(0));
        if ( elseList.get().size() != 0 ) {
            AstJaxbTransformerHelper.addStatement(jaxbDestination, "ELSE", getElseList());
        }

        return jaxbDestination;
    }

}

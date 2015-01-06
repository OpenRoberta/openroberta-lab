package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>repeat statement</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate repeat statement statement.<br/>
 * <br>
 * See {@link #getMode()} for the kind of the repeat statements.
 */
public class RepeatStmt<V> extends Stmt<V> {
    private final Mode mode;
    private final Expr<V> expr;
    private final StmtList<V> list;

    private RepeatStmt(Mode mode, Expr<V> expr, StmtList<V> list, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.REPEAT_STMT, properties, comment);
        Assert.isTrue(mode != null && expr != null && list != null && expr.isReadOnly() && list.isReadOnly());
        this.expr = expr;
        this.list = list;
        this.mode = mode;
        setReadOnly();
    }

    /**
     * Create read only object of {@link RepeatStmt}.
     *
     * @param mode of the repeat statement; must be <b>not</b> null; see enum {@link Mode} for all possible modes,
     * @param expr that should be evaluated; must be <b>not</b> null and <b>read only</b>,
     * @param list of statements; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link RepeatStmt}
     */
    public static <V> RepeatStmt<V> make(Mode mode, Expr<V> expr, StmtList<V> list, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RepeatStmt<V>(mode, expr, list, properties, comment);
    }

    /**
     * @return mode of the repeat statement. See enum {@link Mode} for all possible modes
     */
    public Mode getMode() {
        return this.mode;
    }

    /**
     * @return expression that should be evaluated
     */
    public final Expr<V> getExpr() {
        return this.expr;
    }

    /**
     * @return list of statements
     */
    public final StmtList<V> getList() {
        return this.list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        sb.append("(repeat [" + this.mode + ", ").append(this.expr).append("]");
        sb.append(this.list.toString());
        appendNewLine(sb, 0, ")");
        return sb.toString();
    }

    /**
     * Modes in which the repeat statement can be set.
     *
     * @author kcvejoski
     */
    public static enum Mode {
        WHILE(), UNTIL(), TIMES(), FOR(), FOR_EACH(), WAIT(), FOREVER();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        /**
         * get mode from {@link Mode} from string parameter. It is possible for one mode to have multiple string mappings.
         * Throws exception if the mode does not exists.
         *
         * @param name of the mode
         * @return mode from the enum {@link Mode}
         */
        public static Mode get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid mode symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Mode mo : Mode.values() ) {
                if ( mo.toString().equals(sUpper) ) {
                    return mo;
                }
                for ( String value : mo.values ) {
                    if ( sUpper.equals(value) ) {
                        return mo;
                    }
                }
            }
            throw new DbcException("Invalid mode symbol: " + s);
        }
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitRepeatStmt(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        switch ( getMode() ) {
            case TIMES:
                AstJaxbTransformerHelper.addValue(jaxbDestination, "TIMES", ((Binary<?>) ((ExprList<?>) getExpr()).get().get(1)).getRight());
                break;

            case WAIT:
            case UNTIL:
                AstJaxbTransformerHelper.addField(jaxbDestination, "MODE", getMode().name());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "BOOL", ((Unary<?>) getExpr()).getExpr());
                break;

            case WHILE:
                AstJaxbTransformerHelper.addField(jaxbDestination, "MODE", getMode().name());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "BOOL", getExpr());
                break;

            case FOR:
                ExprList<?> exprList = (ExprList<?>) getExpr();
                AstJaxbTransformerHelper.addField(jaxbDestination, "VAR", ((VarDeclaration<?>) exprList.get().get(0)).getName());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "FROM", ((VarDeclaration<?>) exprList.get().get(0)).getValue());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "TO", ((Binary<?>) exprList.get().get(1)).getRight());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "BY", ((Binary<?>) exprList.get().get(2)).getRight());
                break;

            case FOR_EACH:
                Binary<?> exprBinary = (Binary<?>) getExpr();
                AstJaxbTransformerHelper.addField(jaxbDestination, "VAR", ((Var<?>) exprBinary.getLeft()).getValue());
                AstJaxbTransformerHelper.addValue(jaxbDestination, "LIST", exprBinary.getRight());
                break;

            default:
                break;
        }
        AstJaxbTransformerHelper.addStatement(jaxbDestination, "DO", getList());

        return jaxbDestination;
    }

}

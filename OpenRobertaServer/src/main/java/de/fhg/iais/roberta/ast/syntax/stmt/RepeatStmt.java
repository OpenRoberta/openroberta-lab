package de.fhg.iais.roberta.ast.syntax.stmt;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Unary.Op;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
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

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitRepeatStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        Phrase<V> exprr;
        List<Value> values;
        List<Field> fields;
        Phrase<V> from;
        Phrase<V> to;
        Phrase<V> by;
        ExprList<V> exprList;
        Expr<V> exprCondition;
        Phrase<V> var;
        Expr<V> var1;
        Expr<V> increment;

        switch ( block.getType() ) {
            case BlocklyConstants.CONTROLS_REPEAT_EXT:
                values = helper.extractValues(block, (short) 1);
                exprList = ExprList.make();
                from = NumConst.make("0", helper.extractBlockProperties(block), helper.extractComment(block));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TIMES, Integer.class));
                by = NumConst.make("1", helper.extractBlockProperties(block), helper.extractComment(block));
                var1 =
                    VarDeclaration.make(
                        BlocklyType.NUMERIC_INT,
                        "i" + helper.getVariableCounter(),
                        helper.convertPhraseToExpr(from),
                        false,
                        false,
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                var = Var.make(BlocklyType.NUMERIC_INT, "i" + helper.getVariableCounter(), helper.extractBlockProperties(block), helper.extractComment(block));
                exprCondition =
                    Binary.make(
                        Binary.Op.LT,
                        helper.convertPhraseToExpr(var),
                        helper.convertPhraseToExpr(to),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                increment =
                    Unary
                        .make(Unary.Op.POSTFIX_INCREMENTS, helper.convertPhraseToExpr(var), helper.extractBlockProperties(block), helper.extractComment(block));
                exprList.addExpr(var1);
                exprList.addExpr(exprCondition);
                exprList.addExpr(increment);
                exprList.setReadOnly();

                helper.setVariableCounter(helper.getVariableCounter() + 1);
                return helper.extractRepeatStatement(block, exprList, BlocklyConstants.TIMES);

            case BlocklyConstants.CONTROLS_REPEAT:
                fields = helper.extractFields(block, (short) 1);
                exprList = ExprList.make();
                from = NumConst.make("0", helper.extractBlockProperties(block), helper.extractComment(block));
                to = NumConst.make(helper.extractField(fields, BlocklyConstants.TIMES), helper.extractBlockProperties(block), helper.extractComment(block));
                by = NumConst.make("1", helper.extractBlockProperties(block), helper.extractComment(block));
                var1 =
                    VarDeclaration.make(
                        BlocklyType.NUMERIC_INT,
                        "i" + helper.getVariableCounter(),
                        helper.convertPhraseToExpr(from),
                        false,
                        false,
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                var = Var.make(BlocklyType.NUMERIC_INT, "i" + helper.getVariableCounter(), helper.extractBlockProperties(block), helper.extractComment(block));
                exprCondition =
                    Binary.make(
                        Binary.Op.LT,
                        helper.convertPhraseToExpr(var),
                        helper.convertPhraseToExpr(to),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                increment =
                    Unary
                        .make(Unary.Op.POSTFIX_INCREMENTS, helper.convertPhraseToExpr(var), helper.extractBlockProperties(block), helper.extractComment(block));
                exprList.addExpr(var1);
                exprList.addExpr(exprCondition);
                exprList.addExpr(increment);
                exprList.setReadOnly();

                helper.setVariableCounter(helper.getVariableCounter() + 1);
                return helper.extractRepeatStatement(block, exprList, BlocklyConstants.TIMES);

            case BlocklyConstants.CONTROLS_FOR_EACH:
                fields = helper.extractFields(block, (short) 2);
                EmptyExpr<V> empty = EmptyExpr.make(Integer.class);
                var =
                    VarDeclaration.make(
                        BlocklyType.get(helper.extractField(fields, BlocklyConstants.TYPE)),
                        helper.extractField(fields, BlocklyConstants.VAR),
                        empty,
                        false,
                        false,
                        BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true),
                        null);

                values = helper.extractValues(block, (short) 1);
                exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.LIST_, ArrayList.class));

                Binary<V> exprBinary =
                    Binary.make(
                        Binary.Op.IN,
                        helper.convertPhraseToExpr(var),
                        helper.convertPhraseToExpr(exprr),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                return helper.extractRepeatStatement(block, exprBinary, BlocklyConstants.FOR_EACH);

            case BlocklyConstants.CONTROLS_FOR:
                var = helper.extractVar(block);
                values = helper.extractValues(block, (short) 3);
                exprList = ExprList.make();

                from = helper.extractValue(values, new ExprParam(BlocklyConstants.FROM_, Integer.class));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TO_, Integer.class));
                by = helper.extractValue(values, new ExprParam(BlocklyConstants.BY_, Integer.class));
                var1 =
                    VarDeclaration.make(
                        BlocklyType.NUMERIC_INT,
                        ((Var<V>) var).getValue(),
                        helper.convertPhraseToExpr(from),
                        false,
                        false,
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));

                exprCondition =
                    Binary.make(
                        Binary.Op.LTE,
                        helper.convertPhraseToExpr(var),
                        helper.convertPhraseToExpr(to),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                Binary<V> exprBy =
                    Binary.make(
                        Binary.Op.ADD_ASSIGNMENT,
                        helper.convertPhraseToExpr(var),
                        helper.convertPhraseToExpr(by),
                        "",
                        helper.extractBlockProperties(block),
                        helper.extractComment(block));
                exprList.addExpr(var1);
                exprList.addExpr(exprCondition);
                exprList.addExpr(exprBy);
                exprList.setReadOnly();
                return helper.extractRepeatStatement(block, exprList, BlocklyConstants.FOR);

            case BlocklyConstants.CONTROLS_WHILE_UNTIL:
                fields = helper.extractFields(block, (short) 1);
                String modee = helper.extractField(fields, BlocklyConstants.MODE_);
                values = helper.extractValues(block, (short) 1);
                if ( RepeatStmt.Mode.UNTIL == RepeatStmt.Mode.get(modee) ) {
                    exprr =
                        Unary.make(
                            Op.NOT,
                            helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, Boolean.class))),
                            helper.extractBlockProperties(block),
                            helper.extractComment(block));
                } else {
                    exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, Boolean.class));
                }
                return helper.extractRepeatStatement(block, exprr, modee);
            default:
                exprr = BoolConst.make(true, helper.extractBlockProperties(block), helper.extractComment(block));
                return helper.extractRepeatStatement(block, exprr, RepeatStmt.Mode.FOREVER.toString());
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        switch ( getMode() ) {
            case TIMES:
                if ( getProperty().getBlockType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    AstJaxbTransformerHelper.addField(
                        jaxbDestination,
                        BlocklyConstants.TIMES,
                        ((NumConst<?>) ((Binary<?>) ((ExprList<?>) getExpr()).get().get(1)).getRight()).getValue());
                } else {
                    AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.TIMES, ((Binary<?>) ((ExprList<?>) getExpr()).get().get(1)).getRight());
                }
                break;

            case WAIT:
            case UNTIL:
                AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, ((Unary<?>) getExpr()).getExpr());
                break;

            case WHILE:
                AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.MODE_, getMode().name());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, getExpr());
                break;

            case FOR:
                ExprList<?> exprList = (ExprList<?>) getExpr();
                AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VAR, ((VarDeclaration<?>) exprList.get().get(0)).getName());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.FROM_, ((VarDeclaration<?>) exprList.get().get(0)).getValue());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.TO_, ((Binary<?>) exprList.get().get(1)).getRight());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.BY_, ((Binary<?>) exprList.get().get(2)).getRight());
                break;

            case FOR_EACH:
                Binary<?> exprBinary = (Binary<?>) getExpr();
                AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.TYPE, ((VarDeclaration<?>) exprBinary.getLeft())
                    .getTypeVar()
                    .getBlocklyName());
                AstJaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.VAR, ((VarDeclaration<?>) exprBinary.getLeft()).getName());
                AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.LIST_, exprBinary.getRight());
                break;

            default:
                break;
        }
        AstJaxbTransformerHelper.addStatement(jaxbDestination, BlocklyConstants.DO, getList());

        return jaxbDestination;
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
}

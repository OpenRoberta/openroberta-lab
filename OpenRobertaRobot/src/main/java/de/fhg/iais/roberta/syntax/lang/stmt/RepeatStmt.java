package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Unary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>repeat statement</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate repeat
 * statement statement.<br/>
 * <br>
 * See {@link #getMode()} for the kind of the repeat statements.
 */
public class RepeatStmt<V> extends Stmt<V> {
    private final Mode mode;
    private final Expr<V> expr;
    private final StmtList<V> list;

    private RepeatStmt(Mode mode, Expr<V> expr, StmtList<V> list, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("REPEAT_STMT"), properties, comment);
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
        return new RepeatStmt<>(mode, expr, list, properties, comment);
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitRepeatStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        Phrase<V> exprr;
        List<Value> values;
        List<Field> fields;
        Phrase<V> from;
        Phrase<V> to;
        Phrase<V> by;
        ExprList<V> exprList;
        Phrase<V> var;

        switch ( block.getType() ) {
            case BlocklyConstants.CONTROLS_REPEAT_EXT:
            case BlocklyConstants.CONTROLS_REPEAT:
                values = helper.extractValues(block, (short) 1);
                exprList = ExprList.make();

                //TODO: replace var, from, to by expressions
                var = Var.make(BlocklyType.NUMBER_INT, "k" + helper.getVariableCounter(), helper.extractBlockProperties(block), helper.extractComment(block));
                from = NumConst.make("0", helper.extractBlockProperties(block), helper.extractComment(block));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TIMES, BlocklyType.NUMBER_INT));
                if ( block.getType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    fields = helper.extractFields(block, (short) 1);
                    to = NumConst.make(helper.extractField(fields, BlocklyConstants.TIMES), helper.extractBlockProperties(block), helper.extractComment(block));
                }
                by = NumConst.make("1", helper.extractBlockProperties(block), helper.extractComment(block));

                exprList.addExpr(helper.convertPhraseToExpr(var));
                exprList.addExpr(helper.convertPhraseToExpr(from));
                exprList.addExpr(helper.convertPhraseToExpr(to));
                exprList.addExpr(helper.convertPhraseToExpr(by));
                exprList.setReadOnly();

                helper.setVariableCounter(helper.getVariableCounter() + 1);
                return helper.extractRepeatStatement(block, exprList, BlocklyConstants.TIMES);

            case BlocklyConstants.CONTROLS_FOR:
            case BlocklyConstants.ROB_CONTROLS_FOR:
                values = helper.extractValues(block, (short) 3);
                exprList = ExprList.make();

                var = helper.extractVar(block);
                from = helper.extractValue(values, new ExprParam(BlocklyConstants.FROM, BlocklyType.NUMBER_INT));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
                by = helper.extractValue(values, new ExprParam(BlocklyConstants.BY, BlocklyType.NUMBER_INT));

                exprList.addExpr(helper.convertPhraseToExpr(var));
                exprList.addExpr(helper.convertPhraseToExpr(from));
                exprList.addExpr(helper.convertPhraseToExpr(to));
                exprList.addExpr(helper.convertPhraseToExpr(by));
                exprList.setReadOnly();
                return helper.extractRepeatStatement(block, exprList, BlocklyConstants.FOR);
            case BlocklyConstants.ROB_CONTROLS_FOR_EACH:
            case BlocklyConstants.CONTROLS_FOR_EACH:
                fields = helper.extractFields(block, (short) 2);
                String type = fields.get(0).getValue();
                EmptyExpr<V> empty = EmptyExpr.make(BlocklyType.get(type));
                var =
                    VarDeclaration
                        .make(
                            BlocklyType.get(helper.extractField(fields, BlocklyConstants.TYPE)),
                            helper.extractField(fields, BlocklyConstants.VAR),
                            empty,
                            false,
                            false,
                            BlocklyBlockProperties.make("1", "1"),
                            null);

                values = helper.extractValues(block, (short) 1);
                exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.LIST, BlocklyType.ARRAY));

                Binary<V> exprBinary =
                    Binary
                        .make(
                            Binary.Op.IN,
                            helper.convertPhraseToExpr(var),
                            helper.convertPhraseToExpr(exprr),
                            "",
                            helper.extractBlockProperties(block),
                            helper.extractComment(block));
                return helper.extractRepeatStatement(block, exprBinary, BlocklyConstants.FOR_EACH);

            case BlocklyConstants.CONTROLS_WHILE_UNTIL:
                fields = helper.extractFields(block, (short) 1);
                String modee = helper.extractField(fields, BlocklyConstants.MODE);
                values = helper.extractValues(block, (short) 1);
                if ( RepeatStmt.Mode.UNTIL == RepeatStmt.Mode.get(modee) ) {
                    exprr =
                        Unary
                            .make(
                                Op.NOT,
                                helper.convertPhraseToExpr(helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN))),
                                helper.extractBlockProperties(block),
                                helper.extractComment(block));
                } else {
                    exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN));
                }
                return helper.extractRepeatStatement(block, exprr, modee);
            case BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER_ARDU:
                exprr = BoolConst.make(true, helper.extractBlockProperties(block), helper.extractComment(block));
                return helper.extractRepeatStatement(block, exprr, RepeatStmt.Mode.FOREVER_ARDU.toString());
            default:
                exprr = BoolConst.make(true, helper.extractBlockProperties(block), helper.extractComment(block));
                return helper.extractRepeatStatement(block, exprr, RepeatStmt.Mode.FOREVER.toString());
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        switch ( getMode() ) {
            case TIMES:
                if ( getProperty().getBlockType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TIMES, ((NumConst<?>) (((ExprList<?>) getExpr()).get().get(2))).getValue());
                } else {
                    Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.TIMES, (((ExprList<?>) getExpr()).get().get(2)));
                }
                break;

            case WAIT:
            case UNTIL:
                Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().name());
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, ((Unary<?>) getExpr()).getExpr());
                break;

            case WHILE:
                Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.MODE, getMode().name());
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.BOOL, getExpr());
                break;

            case FOR:
                ExprList<?> exprList = (ExprList<?>) getExpr();
                Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.VAR, ((Var<?>) exprList.get().get(0)).getValue());
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.FROM, (exprList.get().get(1)));
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.TO, (exprList.get().get(2)));
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.BY, (exprList.get().get(3)));
                break;

            case FOR_EACH:
                Binary<?> exprBinary = (Binary<?>) getExpr();
                Mutation mutation = new Mutation();
                mutation.setListType(((VarDeclaration<?>) exprBinary.getLeft()).getTypeVar().getBlocklyName());
                jaxbDestination.setMutation(mutation);
                Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.TYPE, ((VarDeclaration<?>) exprBinary.getLeft()).getTypeVar().getBlocklyName());
                Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.VAR, ((VarDeclaration<?>) exprBinary.getLeft()).getName());
                Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.LIST, exprBinary.getRight());
                break;
            case FOREVER:
            case FOREVER_ARDU:
                break;

            default:
                break;
        }
        Ast2JaxbHelper.addStatement(jaxbDestination, BlocklyConstants.DO, getList());

        return jaxbDestination;
    }

    /**
     * Modes in which the repeat statement can be set.
     *
     * @author kcvejoski
     */
    public static enum Mode {
        WHILE(), UNTIL(), TIMES(), FOR(), FOR_EACH(), WAIT(), FOREVER(), FOREVER_ARDU();

        private final String[] values;

        private Mode(String... values) {
            this.values = values;
        }

        /**
         * get mode from {@link Mode} from string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode does not
         * exists.
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

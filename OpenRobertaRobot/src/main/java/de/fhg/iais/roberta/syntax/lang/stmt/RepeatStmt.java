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
import de.fhg.iais.roberta.syntax.lang.expr.*;
import de.fhg.iais.roberta.syntax.lang.expr.Unary.Op;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

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

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
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
                values = Jaxb2Ast.extractValues(block, (short) 1);
                exprList = ExprList.make();

                //TODO: replace var, from, to by expressions
                var =
                    Var.make(BlocklyType.NUMBER_INT, "k" + helper.getVariableCounter(), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
                from = NumConst.make("0", Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TIMES, BlocklyType.NUMBER_INT));
                if ( block.getType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    fields = Jaxb2Ast.extractFields(block, (short) 1);
                    to =
                        NumConst
                            .make(
                                Jaxb2Ast.extractField(fields, BlocklyConstants.TIMES),
                                Jaxb2Ast.extractBlockProperties(block),
                                Jaxb2Ast.extractComment(block));
                }
                by = NumConst.make("1", Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(var));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(from));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(to));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(by));
                exprList.setReadOnly();

                helper.setVariableCounter(helper.getVariableCounter() + 1);
                return helper.extractRepeatStatement(block, (Phrase<V>) exprList, BlocklyConstants.TIMES, BlocklyConstants.DO, 1);

            case BlocklyConstants.CONTROLS_FOR:
            case BlocklyConstants.ROB_CONTROLS_FOR:
                values = Jaxb2Ast.extractValues(block, (short) 3);
                exprList = ExprList.make();

                var = Jaxb2Ast.extractVar(block);
                from = helper.extractValue(values, new ExprParam(BlocklyConstants.FROM, BlocklyType.NUMBER_INT));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
                by = helper.extractValue(values, new ExprParam(BlocklyConstants.BY, BlocklyType.NUMBER_INT));

                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(var));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(from));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(to));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(by));
                exprList.setReadOnly();
                return helper.extractRepeatStatement(block, (Phrase<V>) exprList, BlocklyConstants.FOR, BlocklyConstants.DO, 1);
            case BlocklyConstants.ROB_CONTROLS_FOR_EACH:
            case BlocklyConstants.CONTROLS_FOR_EACH:
                fields = Jaxb2Ast.extractFields(block, (short) 2);
                String type = fields.get(0).getValue();
                EmptyExpr<V> empty = EmptyExpr.make(BlocklyType.get(type));
                var =
                    VarDeclaration
                        .make(
                            BlocklyType.get(Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE)),
                            Jaxb2Ast.extractField(fields, BlocklyConstants.VAR),
                            empty,
                            false,
                            false,
                            Jaxb2Ast.extractBlockProperties(block),
                            null);
                values = Jaxb2Ast.extractValues(block, (short) 1);
                exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.LIST, BlocklyType.ARRAY));

                Binary<V> exprBinary =
                    Binary
                        .make(
                            Binary.Op.IN,
                            Jaxb2Ast.convertPhraseToExpr(var),
                            Jaxb2Ast.convertPhraseToExpr(exprr),
                            "",
                            Jaxb2Ast.extractBlockProperties(block),
                            Jaxb2Ast.extractComment(block));
                return helper.extractRepeatStatement(block, (Phrase<V>) exprBinary, BlocklyConstants.FOR_EACH, BlocklyConstants.DO, 1);

            case BlocklyConstants.CONTROLS_WHILE_UNTIL:
                fields = Jaxb2Ast.extractFields(block, (short) 1);
                String modee = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
                values = Jaxb2Ast.extractValues(block, (short) 1);
                if ( RepeatStmt.Mode.UNTIL == RepeatStmt.Mode.get(modee) ) {
                    exprr =
                        Unary
                            .make(
                                Op.NOT,
                                Jaxb2Ast.convertPhraseToExpr(helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN))),
                                Jaxb2Ast.extractBlockProperties(block),
                                Jaxb2Ast.extractComment(block));
                } else {
                    exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN));
                }
                return helper.extractRepeatStatement(block, exprr, modee, BlocklyConstants.DO, 1);
            case BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER_ARDU:
                exprr = BoolConst.make(true, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
                return helper.extractRepeatStatement(block, exprr, Mode.FOREVER_ARDU.toString(), BlocklyConstants.DO, 1);
            default:
                exprr = BoolConst.make(true, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
                return helper.extractRepeatStatement(block, exprr, Mode.FOREVER.toString(), BlocklyConstants.DO, 1);
        }
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        switch ( getMode() ) {
            case TIMES:
                if ( getProperty().getBlockType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TIMES, ((NumConst<?>) (((ExprList<?>) getExpr()).get().get(2))).getValue());
                } else {
                    Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TIMES, (((ExprList<?>) getExpr()).get().get(2)));
                }
                break;

            case WAIT:
            case UNTIL:
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getMode().name());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BOOL, ((Unary<?>) getExpr()).getExpr());
                break;

            case WHILE:
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, getMode().name());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BOOL, getExpr());
                break;

            case FOR:
                ExprList<?> exprList = (ExprList<?>) getExpr();
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, ((Var<?>) exprList.get().get(0)).getValue());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FROM, (exprList.get().get(1)));
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, (exprList.get().get(2)));
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BY, (exprList.get().get(3)));
                break;

            case FOR_EACH:
                Binary<?> exprBinary = (Binary<?>) getExpr();
                Mutation mutation = new Mutation();
                mutation.setListType(((VarDeclaration<?>) exprBinary.getLeft()).getTypeVar().getBlocklyName());
                jaxbDestination.setMutation(mutation);
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, ((VarDeclaration<?>) exprBinary.getLeft()).getTypeVar().getBlocklyName());
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, ((VarDeclaration<?>) exprBinary.getLeft()).getName());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, exprBinary.getRight());
                break;
            case FOREVER:
            case FOREVER_ARDU:
                break;

            default:
                break;
        }
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO, getList());

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

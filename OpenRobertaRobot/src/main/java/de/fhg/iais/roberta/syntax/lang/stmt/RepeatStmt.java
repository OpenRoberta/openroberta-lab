package de.fhg.iais.roberta.syntax.lang.stmt;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
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
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "REPEAT_STMT", category = "STMT", blocklyNames = {"controls_whileUntil", "robControls_for", "robControls_loopForever", "controls_for", "controls_forEach", "controls_repeat", "robControls_forEach", "controls_repeat_ext", "robControls_loopForever_ardu"})
public final class RepeatStmt extends Stmt {
    public final Mode mode;
    public final Expr expr;
    public final StmtList list;

    public RepeatStmt(Mode mode, Expr expr, StmtList list, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(mode != null && expr != null && list != null && expr.isReadOnly() && list.isReadOnly());
        this.expr = expr;
        this.list = list;
        this.mode = mode;
        setReadOnly();
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


    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        Phrase exprr;
        List<Value> values;
        List<Field> fields;
        Phrase from;
        Phrase to;
        Phrase by;
        ExprList exprList;
        Phrase var;

        switch ( block.getType() ) {
            case BlocklyConstants.CONTROLS_REPEAT_EXT:
            case BlocklyConstants.CONTROLS_REPEAT:
                values = Jaxb2Ast.extractValues(block, (short) 1);
                exprList = new ExprList();

                //TODO: replace var, from, to by expressions
                var =
                    new Var(BlocklyType.NUMBER_INT, "k" + helper.getVariableCounter(), Jaxb2Ast.extractBlocklyProperties(block));
                from = new NumConst(Jaxb2Ast.extractBlocklyProperties(block), "0");
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TIMES, BlocklyType.NUMBER_INT));
                if ( block.getType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    fields = Jaxb2Ast.extractFields(block, (short) 1);
                    to =
                        new NumConst(Jaxb2Ast.extractBlocklyProperties(block), Jaxb2Ast.extractField(fields, BlocklyConstants.TIMES));
                }
                by = new NumConst(Jaxb2Ast.extractBlocklyProperties(block), "1");

                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(var));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(from));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(to));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(by));
                exprList.setReadOnly();

                helper.setVariableCounter(helper.getVariableCounter() + 1);
                return helper.extractRepeatStatement(block, (Phrase) exprList, BlocklyConstants.TIMES, BlocklyConstants.DO, 1);

            case BlocklyConstants.CONTROLS_FOR:
            case BlocklyConstants.ROB_CONTROLS_FOR:
                values = Jaxb2Ast.extractValues(block, (short) 3);
                exprList = new ExprList();

                var = Jaxb2Ast.extractVar(block);
                from = helper.extractValue(values, new ExprParam(BlocklyConstants.FROM, BlocklyType.NUMBER_INT));
                to = helper.extractValue(values, new ExprParam(BlocklyConstants.TO, BlocklyType.NUMBER_INT));
                by = helper.extractValue(values, new ExprParam(BlocklyConstants.BY, BlocklyType.NUMBER_INT));

                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(var));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(from));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(to));
                exprList.addExpr(Jaxb2Ast.convertPhraseToExpr(by));
                exprList.setReadOnly();
                return helper.extractRepeatStatement(block, (Phrase) exprList, BlocklyConstants.FOR, BlocklyConstants.DO, 1);
            case BlocklyConstants.ROB_CONTROLS_FOR_EACH:
            case BlocklyConstants.CONTROLS_FOR_EACH:
                fields = Jaxb2Ast.extractFields(block, (short) 2);
                String type = fields.get(0).getValue();
                EmptyExpr empty = new EmptyExpr(BlocklyType.get(type));
                var =
                    new VarDeclaration(BlocklyType.get(Jaxb2Ast.extractField(fields, BlocklyConstants.TYPE)), Jaxb2Ast.extractField(fields, BlocklyConstants.VAR), empty, false, false, Jaxb2Ast.extractBlocklyProperties(block));
                values = Jaxb2Ast.extractValues(block, (short) 1);
                exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.LIST, BlocklyType.ARRAY));

                Binary exprBinary =
                    new Binary(Binary.Op.IN, Jaxb2Ast.convertPhraseToExpr(var), Jaxb2Ast.convertPhraseToExpr(exprr), "", Jaxb2Ast.extractBlocklyProperties(block));
                return helper.extractRepeatStatement(block, (Phrase) exprBinary, BlocklyConstants.FOR_EACH, BlocklyConstants.DO, 1);

            case BlocklyConstants.CONTROLS_WHILE_UNTIL:
                fields = Jaxb2Ast.extractFields(block, (short) 1);
                String modee = Jaxb2Ast.extractField(fields, BlocklyConstants.MODE);
                values = Jaxb2Ast.extractValues(block, (short) 1);
                if ( RepeatStmt.Mode.UNTIL == RepeatStmt.Mode.get(modee) ) {
                    exprr =
                        new Unary(Op.NOT, Jaxb2Ast.convertPhraseToExpr(helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN))), Jaxb2Ast.extractBlocklyProperties(block));
                } else {
                    exprr = helper.extractValue(values, new ExprParam(BlocklyConstants.BOOL, BlocklyType.BOOLEAN));
                }
                return helper.extractRepeatStatement(block, exprr, modee, BlocklyConstants.DO, 1);
            case BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER_ARDU:
                exprr = new BoolConst(Jaxb2Ast.extractBlocklyProperties(block), true);
                return helper.extractRepeatStatement(block, exprr, Mode.FOREVER_ARDU.toString(), BlocklyConstants.DO, 1);
            default:
                exprr = new BoolConst(Jaxb2Ast.extractBlocklyProperties(block), true);
                return helper.extractRepeatStatement(block, exprr, Mode.FOREVER.toString(), BlocklyConstants.DO, 1);
        }
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        NepoInfos infos = this.expr.getInfos();
        if ( infos.getErrorCount() > 0 ) {
            Ast2Jaxb.addError(this.expr, jaxbDestination);
        }
        switch ( this.mode ) {
            case TIMES:
                if ( getProperty().getBlockType().equals(BlocklyConstants.CONTROLS_REPEAT) ) {
                    Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TIMES, ((NumConst) (((ExprList) this.expr).get().get(2))).value);
                } else {
                    Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TIMES, (((ExprList) this.expr).get().get(2)));
                }
                break;

            case WAIT:
            case UNTIL:
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.mode.name());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BOOL, ((Unary) this.expr).expr);
                break;

            case WHILE:
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.MODE, this.mode.name());
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BOOL, this.expr);
                break;

            case FOR:
                ExprList exprList = (ExprList) this.expr;
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, ((Var) exprList.get().get(0)).name);
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.FROM, (exprList.get().get(1)));
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.TO, (exprList.get().get(2)));
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BY, (exprList.get().get(3)));
                break;

            case FOR_EACH:
                Binary exprBinary = (Binary) this.expr;
                Mutation mutation = new Mutation();
                mutation.setListType(((VarDeclaration) exprBinary.left).typeVar.getBlocklyName());
                jaxbDestination.setMutation(mutation);
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.TYPE, ((VarDeclaration) exprBinary.left).typeVar.getBlocklyName());
                Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.VAR, ((VarDeclaration) exprBinary.left).name);
                Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.LIST, exprBinary.getRight());
                break;
            case FOREVER:
            case FOREVER_ARDU:
                break;

            default:
                break;
        }
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO, this.list);

        return jaxbDestination;
    }

    /**
     * Modes in which the repeat statement can be set.
     */
    public static enum Mode {
        WHILE(), UNTIL(), TIMES(), FOR(), FOR_EACH(), WAIT(), FOREVER(), FOREVER_ARDU();

        public final String[] values;

        Mode(String... values) {
            this.values = values;
        }

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

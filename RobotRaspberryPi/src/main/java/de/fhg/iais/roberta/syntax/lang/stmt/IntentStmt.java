package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IRaspberryPiVisitor;

public class IntentStmt<V> extends Stmt<V> {
    private final List<Expr<V>> expr;
    private final List<StmtList<V>> thenList;
    private final StmtList<V> elseList;
    private final int _else;
    private final int _elseIf;
    private final String intent;

    private IntentStmt(
        String intent,
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        super(BlockTypeContainer.getByName("INTENT_STMT"), properties, comment);
        Assert.isTrue(elseList.isReadOnly());
        this.intent = intent;
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
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
    public static <V> IntentStmt<V> make(
        String intent,
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        return new IntentStmt<V>(intent, expr, thenList, elseList, properties, comment, _else, _elseIf);
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
    public static <V> IntentStmt<V> make(
        String intent,
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        StmtList<V> elseList = StmtList.make();
        elseList.setReadOnly();
        return new IntentStmt<V>(intent, expr, thenList, elseList, properties, comment, _else, _elseIf);
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
        int exprListSize = this.expr.size();
        sb.append("intent ").append(this.intent);
        for ( int i = 0; i < exprListSize; i++ ) {
            sb.append("slot" + i).append(this.expr.get(i));
            appendNewLine(sb, 0, ",then");
            sb.append(this.thenList.get(i).toString());
            if ( i + 1 < exprListSize ) {
                appendNewLine(sb, 0, ",else ");
            }
        }
        if ( !this.elseList.get().isEmpty() ) {
            appendNewLine(sb, 0, ",else");
            sb.append(this.elseList.toString());
        }
        appendNewLine(sb, 0, "");
        return sb.toString();
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IRaspberryPiVisitor<V>) visitor).visitIntentStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        Mutation mutation = block.getMutation();
        int _elseIf = helper.getElseIf(mutation);
        int _else = helper.getElse(mutation);

        return (Phrase<V>) blocksToIntentStmt(block, _else, _elseIf, (AbstractJaxb2Ast<Void>) helper);
    }

    @Override
    public Block astToBlock() {
        Mutation mutation;
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.NAME, this.intent);

        int _elseIf = get_elseIf();

        StmtList<?> elseList = getElseList();
        int expr = 0;
        expr = getExpr().size();
        mutation = new Mutation();
        mutation.setElseif(BigInteger.valueOf(_elseIf));
        mutation.setElse(BigInteger.valueOf(_else));
        jaxbDestination.setMutation(mutation);
        Repetitions repetitions = new Repetitions();
        if ( _elseIf != 0 ) {
            for ( int i = 0; i < expr; i++ ) {
                Ast2JaxbHelper.addValue(repetitions, "SLOT_NR" + (i + 1), getExpr().get(i));
                Ast2JaxbHelper.addStatement(repetitions, BlocklyConstants.DO + (i + 1), getThenList().get(i));
            }
            jaxbDestination.setRepetitions(repetitions);
        } else {
            Ast2JaxbHelper.addStatement(repetitions, BlocklyConstants.DO + "0", getThenList().get(0));
            jaxbDestination.setRepetitions(repetitions);
        }

        if ( !elseList.get().isEmpty() ) {
            Ast2JaxbHelper.addStatement(jaxbDestination, BlocklyConstants.ELSE, getElseList());
        }

        return jaxbDestination;
    }

    public static Phrase<Void> blocksToIntentStmt(Block block, int _else, int _elseIf, AbstractJaxb2Ast<Void> helper) {
        List<Expr<Void>> exprsList = new ArrayList<>();
        List<StmtList<Void>> thenList = new ArrayList<>();
        StmtList<Void> elseList = null;

        List<Value> values = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        List<Field> fields = helper.extractFields(block, (short) 1);
        String intent = helper.extractField(fields, BlocklyConstants.NAME);

        if ( _else != 0 && _elseIf != 0 ) {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            Assert.isTrue(valAndStmt.size() <= ((2 * _elseIf) + 2 + _else));
            helper.convertStmtValList(values, statements, valAndStmt);
        } else {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            //  Assert.isTrue(valAndStmt.size() <= ((2 * _elseIf) + 2 + _else));
            helper.convertStmtValList(values, statements, valAndStmt);
        }
        if ( _elseIf == 0 && _else == 0 ) {
            thenList.add(helper.extractStatement(statements, BlocklyConstants.DO + 0));
        } else if ( (_else != 0) ) {
            elseList = helper.extractStatement(statements, BlocklyConstants.ELSE);
        }
        for ( int i = 1; i <= (_elseIf + _else + 1); i++ ) {
            Phrase<Void> p = helper.extractValue(values, new ExprParam("SLOT_NR" + i, BlocklyType.STRING));
            exprsList.add((Expr<Void>) p);
            thenList.add(helper.extractStatement(statements, BlocklyConstants.DO + i));
        }
        if ( _else == 0 ) {
            return IntentStmt.make(intent, exprsList, thenList, helper.extractBlockProperties(block), helper.extractComment(block), _else, _elseIf);
        } else {
            return IntentStmt.make(intent, exprsList, thenList, elseList, helper.extractBlockProperties(block), helper.extractComment(block), _else, _elseIf);
        }
    }

    public String getIntent() {
        return this.intent;
    }
}

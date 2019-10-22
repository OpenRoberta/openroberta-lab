package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>if-else-elseif</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate if
 * statement.<br/>
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
        super(BlockTypeContainer.getByName("IF_STMT"), properties, comment);
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
        int exprListSize = this.expr.size();
        for ( int i = 0; i < exprListSize; i++ ) {
            sb.append("if ").append(this.expr.get(i));
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
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitIfStmt(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        if ( block.getType().equals(BlocklyConstants.LOGIC_TERNARY) ) {
            List<Value> values = block.getValue();
            Assert.isTrue(values.size() <= 3, "Number of values is not less or equal to 3!");
            Phrase<V> ifExpr = helper.extractValue(values, new ExprParam(BlocklyConstants.IF, BlocklyType.BOOLEAN));
            Phrase<V> thenStmt = helper.extractValue(values, new ExprParam(BlocklyConstants.THEN, BlocklyType.ANY));
            Phrase<V> elseStmt = helper.extractValue(values, new ExprParam(BlocklyConstants.ELSE, BlocklyType.ANY));
            StmtList<V> thenList = StmtList.make();
            thenList.addStmt(ExprStmt.make(helper.convertPhraseToExpr(thenStmt)));
            thenList.setReadOnly();
            StmtList<V> elseList = StmtList.make();
            elseList.addStmt(ExprStmt.make(helper.convertPhraseToExpr(elseStmt)));
            elseList.setReadOnly();
            return IfStmt
                .make(helper.convertPhraseToExpr(ifExpr), thenList, elseList, helper.extractBlockProperties(block), helper.extractComment(block), 0, 0);
        }
        Mutation mutation = block.getMutation();
        int _else = helper.getElse(mutation);
        int _elseIf = helper.getElseIf(mutation);

        return helper.blocksToIfStmt(block, _else, _elseIf);
    }

    @Override
    public Block astToBlock() {
        Mutation mutation;
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.LOGIC_TERNARY) ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.IF, getExpr().get(0));
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.THEN, getThenList().get(0).get().get(0));
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.ELSE, getElseList().get().get(0));
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
                Ast2JaxbHelper.addValue(repetitions, BlocklyConstants.IF + i, getExpr().get(i));
                Ast2JaxbHelper.addStatement(repetitions, BlocklyConstants.DO + i, getThenList().get(i));
            }
            if ( !elseList.get().isEmpty() ) {
                Ast2JaxbHelper.addStatement(repetitions, BlocklyConstants.ELSE, getElseList());
            }
            jaxbDestination.setRepetitions(repetitions);
            return jaxbDestination;
        }

        Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.IF + "0", getExpr().get(0));
        Ast2JaxbHelper.addStatement(jaxbDestination, BlocklyConstants.DO + "0", getThenList().get(0));
        if ( !elseList.get().isEmpty() ) {
            Ast2JaxbHelper.addStatement(jaxbDestination, BlocklyConstants.ELSE, getElseList());
        }

        return jaxbDestination;
    }

}

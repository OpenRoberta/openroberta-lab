package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.blockly.generated.Warning;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfo.Severity;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class is a helper class containing helper methods for AST => JAXB transformation.
 *
 * @author kcvejoski
 */
public final class Ast2JaxbHelper {

    /**
     * Sets the basic properties(<i>comments and visual state</i>) of a Blockly block. <br>
     * <br>
     * <b>astSource</b> is representation of the block in the AST,<br>
     * <b>jaxbDestination</b> is representation of the block with JAXB classes
     *
     * @param astSource block from which properties are extracted; must be <b>not</b> null,
     * @param jaxbDestination to which properties are applied; must be <b>not</b> null,
     */
    public static void setBasicProperties(Phrase<?> astSource, Block jaxbDestination) {
        Assert.notNull(astSource);
        Assert.notNull(jaxbDestination);
        if ( astSource.getProperty() == null ) {
            return;
        }
        String blockType;
        blockType = astSource.getProperty().getBlockType();
        setProperties(astSource, jaxbDestination, blockType);
        addComment(astSource, jaxbDestination);
        addError(astSource, jaxbDestination);
        addWarning(astSource, jaxbDestination);
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param block to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *        {@link BlockType#STMT_LIST}
     */
    public static void addStatement(Block block, String name, Phrase<?> value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(value);
        Assert.isTrue(value.getKind().hasName("STMT_LIST"), "Phrase is not STMT_LIST");
        if ( !((StmtList<?>) value).get().isEmpty() ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            block.getStatement().add(statement);
        }
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param block to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *        {@link BlockType#EXPR_LIST}
     */
    public static void addStatement(Block block, String name, ExprList<?> exprList) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(exprList);
        Assert.isTrue(exprList.getKind().hasName("EXPR_LIST"), "Phrase is not EXPR_LIST");
        if ( !exprList.get().isEmpty() ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractExprList(exprList));
            block.getStatement().add(statement);
        }
    }

    /**
     * Add's a statement {@link Statement} object to JAXB {@link Repetitions} object.
     *
     * @param repetitions object to which the statement will be added; must be <b>not</b> null
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *        {@link BlockType#STMT_LIST}
     */
    public static void addStatement(Repetitions repetitions, String name, Phrase<?> value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(repetitions);
        Assert.notNull(value);
        Assert.isTrue(value.getKind().hasName("STMT_LIST"), "Phrase is not STMT_LIST");
        if ( !((StmtList<?>) value).get().isEmpty() ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(value));
            repetitions.getValueAndStatement().add(statement);
        }
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param repetitions object to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *        {@link BlockType#EXPR_LIST}
     */
    public static void addStatement(Repetitions repetitions, String name, ExprList<?> exprList) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(repetitions);
        Assert.notNull(exprList);
        Assert.isTrue(exprList.getKind().hasName("EXPR_LIST"), "Phrase is not EXPR_LIST");
        if ( !exprList.get().isEmpty() ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractExprList(exprList));
            repetitions.getValueAndStatement().add(statement);
        }
    }

    /**
     * Add's a value {@link Value} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the value object into {@link Repetitions} object.
     *
     * @param block to which the value will be added; must be <b>not</b> null,
     * @param name of the value; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored; must be <b>not</b> null
     */
    public static void addValue(Block block, String name, Phrase<?> value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(value);
        if ( !value.getKind().hasName("EMPTY_EXPR") ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            if ( value.getKind().hasName("SHADOW_EXPR") ) {
                ShadowExpr<?> shadowExpr = (ShadowExpr<?>) value;
                blockValue.setShadow(block2shadow(shadowExpr.getShadow().astToBlock()));
                if ( shadowExpr.getBlock() != null ) {
                    blockValue.setBlock(shadowExpr.getBlock().astToBlock());
                }
            } else {
                blockValue.setBlock(value.astToBlock());
            }

            block.getValue().add(blockValue);
        }
    }

    /**
     * Add's a value {@link Value} object to JAXB block representation {@link Block}.
     *
     * @param repetitions to which the value will be added; must be <b>not</b> null,
     * @param name of the value; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored; must be <b>not</b> null
     */
    public static void addValue(Repetitions repetitions, String name, Phrase<?> value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(repetitions);
        Assert.notNull(value);
        if ( !value.getKind().hasName("EMPTY_EXPR") ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(value.astToBlock());
            repetitions.getValueAndStatement().add(blockValue);
        }
    }

    /**
     * Add's a value {@link Field} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the {@link Field} object into {@link Repetitions} object.
     *
     * @param block to which the field will be added; must be <b>not</b> null,
     * @param name of the field; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored
     */
    public static void addField(Block block, String name, String value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Field field = new Field();
        field.setName(name);
        field.setValue(value);
        block.getField().add(field);
    }

    private static Shadow block2shadow(Block block) {
        Shadow shadow = new Shadow();
        shadow.setId(block.getId());
        shadow.setType(block.getType());
        shadow.setIntask(block.isIntask());
        shadow.setField(block.getField().get(0));
        return shadow;
    }

    private static List<Block> extractStmtList(Phrase<?> phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind().hasName("STMT_LIST"), "Phrase is not StmtList!");
        StmtList<?> stmtList = (StmtList<?>) phrase;
        for ( Stmt<?> stmt : stmtList.get() ) {
            result.add(stmt.astToBlock());
        }
        return result;
    }

    private static List<Block> extractExprList(Phrase<?> phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind().hasName("EXPR_LIST"), "Phrase is not ExprList!");
        ExprList<?> exprList = (ExprList<?>) phrase;
        for ( Expr<?> expr : exprList.get() ) {
            result.add(expr.astToBlock());
        }
        return result;
    }

    private static void setProperties(Phrase<?> astSource, Block block, String type) {
        block.setType(type);
        block.setId(astSource.getProperty().getBlocklyId());
        setDisabled(astSource, block);
        setCollapsed(astSource, block);
        setInline(astSource, block);
        setDeletable(astSource, block);
        setMovable(astSource, block);
        setInTask(astSource, block);
    }

    private static void setInline(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isInline() != null ) {
            block.setInline(astObject.getProperty().isInline());
        }
    }

    private static void setCollapsed(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isCollapsed() ) {
            block.setCollapsed(astObject.getProperty().isCollapsed());
        }
    }

    private static void setDisabled(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isDisabled() ) {
            block.setDisabled(astObject.getProperty().isDisabled());
        }
    }

    private static void setInTask(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isInTask() != null ) {
            block.setIntask(astObject.getProperty().isInTask());
        }
    }

    private static void setDeletable(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isDeletable() != null ) {
            block.setDeletable(astObject.getProperty().isDeletable());
        }
    }

    private static void setMovable(Phrase<?> astObject, Block block) {
        if ( astObject.getProperty().isMovable() != null ) {
            block.setMovable(astObject.getProperty().isMovable());
        }
    }

    private static void addComment(Phrase<?> astSource, Block block) {
        if ( astSource.getComment() != null ) {
            Comment comment = new Comment();
            comment.setValue(astSource.getComment().getComment());
            comment.setPinned(astSource.getComment().isPinned());
            comment.setH(astSource.getComment().getHeight());
            comment.setW(astSource.getComment().getWidth());
            block.setComment(comment);
        }
    }

    private static void addWarning(Phrase<?> astSource, Block block) {
        Warning warning = new Warning();
        for ( NepoInfo info : astSource.getInfos().getInfos() ) {
            if ( info.getSeverity() == Severity.WARNING ) {
                warning.setValue(info.getMessage());
                warning.setPinned(true);
                warning.setH("undefined");
                warning.setW("undefined");
                block.setWarning(warning);
            }
        }
    }

    private static void addError(Phrase<?> astSource, Block block) {
        de.fhg.iais.roberta.blockly.generated.Error error = new de.fhg.iais.roberta.blockly.generated.Error();
        for ( NepoInfo info : astSource.getInfos().getInfos() ) {
            if ( info.getSeverity() == Severity.ERROR ) {
                error.setValue(info.getMessage());
                error.setPinned(true);
                error.setH("undefined");
                error.setW("undefined");
                block.setError(error);
            }
        }
    }
}

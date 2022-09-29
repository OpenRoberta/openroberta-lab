package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Data;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.blockly.generated.Warning;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.typecheck.NepoInfo.Severity;
import de.fhg.iais.roberta.util.ast.BlockDescriptor;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class is a helper class containing helper methods for AST => JAXB transformation.
 *
 * @author kcvejoski
 */
public final class Ast2Jaxb {

    /**
     * Sets the basic properties(<i>comments and visual state</i>) of a Blockly block. <br>
     * <br>
     * <b>phrase</b> is representation of the block in the AST,<br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param phrase from which properties are extracted; must be <b>not</b> null,
     * @param block to which properties are applied; must be <b>not</b> null,
     */
    public static void setBasicProperties(Phrase phrase, Block block) {
        Assert.notNull(phrase);
        Assert.notNull(block);
        BlocklyProperties property = phrase.getProperty();
        if ( property == null ) {
            return;
        }
        setProperties(phrase, block, property.getBlockType());
        addError(phrase, block);
        addWarning(phrase, block);
        block.setComment(property.getComment());
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param block to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param phrase is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *     {@link BlockDescriptor#STMT_LIST}
     */
    public static void addStatement(Block block, String name, Phrase phrase) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(phrase);
        Assert.isTrue(phrase.getKind().hasName("STMT_LIST"), "Phrase is not STMT_LIST");
        if ( !((StmtList) phrase).get().isEmpty() ) {
            Statement statement = new Statement();
            statement.setName(name);
            statement.getBlock().addAll(extractStmtList(phrase));
            block.getStatement().add(statement);
        }
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation of a configuration block {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param block to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the List of ConfigurationComponents of the statement
     */
    public static void addConfigurationComponents(Block block, String name, List<ConfigurationComponent> values) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(values);
        Statement statement = new Statement();
        statement.setName(name);
        for ( ConfigurationComponent component : values ) {
            statement.getBlock().add(component.ast2xml());
        }
        block.getStatement().add(statement);
    }

    /**
     * Add's a statement {@link Statement} object to JAXB block representation {@link Block}.
     * <p>
     * This method does <b>not</b> add the statement object into {@link Repetitions} object.
     *
     * @param block to which the statement will be added; must be <b>not</b> null,
     * @param name of the statement; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the statement is stored; must be <b>not</b> null and {@link Phrase#getKind()} must be
     *     {@link BlockDescriptor#EXPR_LIST}
     */
    public static void addStatement(Block block, String name, ExprList exprList) {
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
     *     {@link BlockDescriptor#STMT_LIST}
     */
    public static void addStatement(Repetitions repetitions, String name, Phrase value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(repetitions);
        Assert.notNull(value);
        Assert.isTrue(value.getKind().hasName("STMT_LIST"), "Phrase is not STMT_LIST");
        if ( !((StmtList) value).get().isEmpty() ) {
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
     *     {@link BlockDescriptor#EXPR_LIST}
     */
    public static void addStatement(Repetitions repetitions, String name, ExprList exprList) {
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
    public static void addValue(Block block, String name, Phrase value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        Assert.notNull(value);
        if ( !value.getKind().hasName("EMPTY_EXPR") ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            if ( value.getKind().hasName("SHADOW_EXPR") ) {
                ShadowExpr shadowExpr = (ShadowExpr) value;
                blockValue.setShadow(block2shadow(shadowExpr.shadow.ast2xml()));
                if ( shadowExpr.block != null ) {
                    blockValue.setBlock(shadowExpr.block.ast2xml());
                }
            } else {
                blockValue.setBlock(value.ast2xml());
            }

            block.getValue().add(blockValue);
        }
    }

    /**
     * Add's a {@link Value} object to JAXB block representation {@link Block}.<br>
     * This method skips a field which value BlocklyConstants.EMPTY_PORT (this is a bad hack!)
     *
     * @param repetitions to which the value will be added; must be <b>not</b> null,
     * @param name of the value; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored; must be <b>not</b> null
     */
    public static void addValue(Repetitions repetitions, String name, Phrase value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(repetitions);
        Assert.notNull(value);
        if ( value.equals(BlocklyConstants.EMPTY_PORT) ) {
            return;
        }
        if ( !value.getKind().hasName("EMPTY_EXPR") ) {
            Value blockValue = new Value();
            blockValue.setName(name);
            blockValue.setBlock(value.ast2xml());
            repetitions.getValueAndStatement().add(blockValue);
        }
    }

    /**
     * Add's a {@link Field} object to JAXB block representation {@link Block}.
     * <p>
     * - This method does <b>not</b> add the {@link Field} object into {@link Repetitions} object.<br>
     * - This method skips a field which value BlocklyConstants.EMPTY_PORT (this is a bad hack!)<br>
     * - This method handles a field which value BlocklyConstants.EMPTY_SLOT very special (this is a bad hack!)
     *
     * @param block to which the field will be added; must be <b>not</b> null,
     * @param name of the field; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored
     */
    public static void addField(Block block, String name, String value) {
        Assert.isTrue(!name.equals(""));
        Assert.notNull(block);
        if ( value.equals(BlocklyConstants.EMPTY_PORT) ) {
            // ignore
        } else if ( value.equals(BlocklyConstants.EMPTY_SLOT) ) {
            Field field = new Field();
            field.setName(name);
            field.setValue("");
            block.getField().add(field);
        } else {
            Field field = new Field();
            field.setName(name);
            field.setValue(value);
            block.getField().add(field);
        }
    }

    /**
     * Add's a {@link Data} object to JAXB block representation
     *
     * @param block to which the field will be added; must be <b>not</b> null,
     * @param value is the AST representation of the Blockly block where the value is stored
     */
    public static void addData(Block block, String value) {
        Assert.notNull(block);
        Data data = new Data();
        data.setValue(value);
        block.setData(data);
    }

    /**
     * Add's a {@link Mutation} object to JAXB block representation {@link Block}.
     *
     * @param block to which the mutation will be added; must be <b>not</b> null,
     * @param name of the field; must be <b>non-empty</b> string
     * @param value is the AST representation of the Blockly block where the value is stored
     */
    public static void addMutation(Block block, Mutation value) {
        Assert.notNull(block);
        block.setMutation(value);
    }

    private static Shadow block2shadow(Block block) {
        Shadow shadow = new Shadow();
        shadow.setId(block.getId());
        shadow.setType(block.getType());
        shadow.setIntask(block.isIntask());
        shadow.setField(block.getField().get(0));
        return shadow;
    }

    private static List<Block> extractStmtList(Phrase phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind().hasName("STMT_LIST"), "Phrase is not StmtList!");
        StmtList stmtList = (StmtList) phrase;
        for ( Stmt stmt : stmtList.get() ) {
            result.add(stmt.ast2xml());
        }
        return result;
    }

    private static List<Block> extractExprList(Phrase phrase) {
        List<Block> result = new ArrayList<Block>();
        Assert.isTrue(phrase.getKind().hasName("EXPR_LIST"), "Phrase is not ExprList!");
        ExprList exprList = (ExprList) phrase;
        for ( Expr expr : exprList.get() ) {
            result.add(expr.ast2xml());
        }
        return result;
    }

    private static void setProperties(Phrase astSource, Block block, String type) {
        block.setType(type);
        block.setId(astSource.getProperty().getBlocklyId());
        setDisabled(astSource, block);
        setCollapsed(astSource, block);
        setInline(astSource, block);
        setDeletable(astSource, block);
        setMovable(astSource, block);
        setInTask(astSource, block);
    }

    private static void setInline(Phrase astObject, Block block) {
        if ( astObject.getProperty().isInline() != null ) {
            block.setInline(astObject.getProperty().isInline());
        }
    }

    private static void setCollapsed(Phrase astObject, Block block) {
        if ( astObject.getProperty().isCollapsed() ) {
            block.setCollapsed(astObject.getProperty().isCollapsed());
        }
    }

    private static void setDisabled(Phrase astObject, Block block) {
        if ( astObject.getProperty().isDisabled() ) {
            block.setDisabled(astObject.getProperty().isDisabled());
        }
    }

    private static void setInTask(Phrase astObject, Block block) {
        if ( astObject.getProperty().isInTask() != null ) {
            block.setIntask(astObject.getProperty().isInTask());
        }
    }

    private static void setDeletable(Phrase astObject, Block block) {
        if ( astObject.getProperty().isDeletable() != null ) {
            block.setDeletable(astObject.getProperty().isDeletable());
        }
    }

    private static void setMovable(Phrase astObject, Block block) {
        if ( astObject.getProperty().isMovable() != null ) {
            block.setMovable(astObject.getProperty().isMovable());
        }
    }

    private static void addWarning(Phrase astSource, Block block) {
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

    public static void addError(Phrase astSource, Block block) {
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

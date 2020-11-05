package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.syntax.lang.methods.Method;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

abstract public class AbstractJaxb2Ast<V> {
    private final IRobotFactory robotFactory;
    private int variableCounter = 0;

    protected AbstractJaxb2Ast(IRobotFactory factory) {
        this.robotFactory = factory;
    }

    public BlocklyDropdownFactory getDropdownFactory() {
        return this.robotFactory.getBlocklyDropdownFactory();
    }

    public IRobotFactory getRobotFactory() {
        return this.robotFactory;
    }

    /**
     * @return the variableCounter
     */
    public int getVariableCounter() {
        return this.variableCounter;
    }

    /**
     * @param variableCounter the variableCounter to set
     */
    public void setVariableCounter(int variableCounter) {
        this.variableCounter = variableCounter;
    }

    /**
     * Transforms valid JAXB object to AST object
     *
     * @param block to be transformed
     * @return corresponding AST object
     */
    abstract protected Phrase<V> blockToAST(Block block);

    /**
     * Transforms JAXB object to AST unary object expression.<br>
     * <br>
     *
     * @param block to be transformed
     * @param exprParam of the unary operation
     * @param operationType performed on the exprParam
     * @return AST unary expression object
     */
    public Phrase<V> blockToUnaryExpr(Block block, ExprParam exprParam, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 1);
        Phrase<V> expr = extractValue(values, exprParam);
        return Unary.make(Unary.Op.get(op), convertPhraseToExpr(expr), extractBlockProperties(block), extractComment(block));
    }

    /**
     * Transforms JAXB object to AST binary object expression.<br>
     * <br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block to be transformed
     * @param leftExpr parameter of the expression
     * @param rightExpr parameter of the expression
     * @param operationType of the expression
     * @return AST binary expression object
     */
    public Binary<V> blockToBinaryExpr(Block block, ExprParam leftExpr, ExprParam rightExpr, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 2);
        Phrase<V> left = extractValue(values, leftExpr);
        Phrase<V> right = extractValue(values, rightExpr);
        String operationRange = "";
        if ( block.getMutation() != null && block.getMutation().getOperatorRange() != null ) {
            operationRange = block.getMutation().getOperatorRange();
        }
        return Binary
            .make(
                Binary.Op.get(op),
                convertPhraseToExpr(left),
                convertPhraseToExpr(right),
                operationRange,
                extractBlockProperties(block),
                extractComment(block));
    }

    /**
     * Extracts expression parameters from JAXB {@link Block} class.<br>
     * <br>
     * Client must provide the JAXB source block and list of {@link ExprParam} with the correct names of the parameters.<br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block as source class
     * @param exprParams that are extracted from the block
     * @return list of parameters represented with the {@link Expr} class.
     */
    public List<Expr<V>> extractExprParameters(Block block, List<ExprParam> exprParams) {
        List<Expr<V>> params = new ArrayList<>();
        List<Value> values = extractValues(block, (short) exprParams.size());
        for ( ExprParam exprParam : exprParams ) {
            params.add(convertPhraseToExpr(extractValue(values, exprParam)));
        }
        return params;
    }

    /**
     * Transforms JAXB object to AST if statement object.<br>
     * <br>
     * <b>block</b> is representation of the block with JAXB classes
     *
     * @param block that is transformed
     * @param _else number else's
     * @param _elseIf number of else if's
     * @return if statement object from the AST representation
     */
    public Phrase<V> blocksToIfStmt(Block block, int _else, int _elseIf) {
        List<Expr<V>> exprsList = new ArrayList<>();
        List<StmtList<V>> thenList = new ArrayList<>();
        StmtList<V> elseList = null;

        List<Value> values = new ArrayList<>();
        List<Statement> statements = new ArrayList<>();

        if ( _else + _elseIf != 0 ) {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            Assert.isTrue(valAndStmt.size() <= 2 * _elseIf + 2 + _else);
            convertStmtValList(values, statements, valAndStmt);
        } else {
            values = extractValues(block, (short) 1);
            statements = extractStatements(block, (short) 1);
        }

        for ( int i = 0; i < _elseIf + _else + 1; i++ ) {
            if ( _else != 0 && i == _elseIf + _else ) {
                elseList = extractStatement(statements, BlocklyConstants.ELSE);
            } else {
                Phrase<V> p = extractValue(values, new ExprParam(BlocklyConstants.IF + i, BlocklyType.BOOLEAN));
                exprsList.add(convertPhraseToExpr(p));
                thenList.add(extractStatement(statements, BlocklyConstants.DO + i));
            }
        }

        if ( _else != 0 ) {
            return IfStmt.make(exprsList, thenList, elseList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
        }
        return IfStmt.make(exprsList, thenList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
    }

    /**
     * Converts mixed list of {@link Value} and {@link Statement} into to separated lists
     *
     * @param values list for saving values
     * @param statements list for saving statements
     * @param valAndStmt list to be separated
     */
    public static void convertStmtValList(List<Value> values, List<Statement> statements, List<Object> valAndStmt) {
        for ( Object ob : valAndStmt ) {
            if ( ob.getClass() == Value.class ) {
                values.add((Value) ob);
            } else {
                statements.add((Statement) ob);
            }
        }
    }

    /**
     * Transforms JAXB {@link Block} to list of expressions.
     *
     * @param block to be transformed
     * @param defVal if the expression is missing
     * @return list of expressions
     */
    public ExprList<V> blockToExprList(Block block, BlocklyType[] defVal) {
        int items = 0;
        if ( block.getMutation().getItems() != null ) {
            items = block.getMutation().getItems().intValue();
        }
        Assert.isTrue(defVal.length == items);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= items, "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, defVal, items, BlocklyConstants.ADD);
    }

    /**
     * Transforms JAXB {@link Block} to list of expressions.
     *
     * @param block to be transformed
     * @param defVal if the expression is missing
     * @return list of expressions
     */
    public ExprList<V> blockToExprList(Block block, BlocklyType defVal) {
        int items = 0;
        if ( block.getMutation().getItems() != null ) {
            items = block.getMutation().getItems().intValue();
        }
        BlocklyType[] types = new BlocklyType[items];
        Arrays.fill(types, defVal);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= items, "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, types, items, BlocklyConstants.ADD);
    }

    /**
     * Transforms JAXB list of {@link Arg} objects to list of AST expressions.
     *
     * @param arguments to be transformed
     * @return list of AST expressions
     */
    public ExprList<V> argumentsToExprList(List<Arg> arguments) {
        ExprList<V> parameters = ExprList.make();
        for ( Arg arg : arguments ) {
            Var<V> parametar = Var.make(BlocklyType.get(arg.getType()), arg.getName(), BlocklyBlockProperties.make("1", "1"), null);
            parameters.addExpr(parametar);
        }
        parameters.setReadOnly();
        return parameters;
    }

    /**
     * Transforms JAXB list of {@link Arg} objects to list of AST parameters type.
     *
     * @param arguments to be transformed
     * @return array of AST expressions
     */
    public static BlocklyType[] argumentsToParametersType(List<Arg> arguments) {
        BlocklyType[] types = new BlocklyType[arguments.size()];
        int i = 0;
        for ( Arg arg : arguments ) {
            types[i] = BlocklyType.get(arg.getType());
            i++;
        }

        return types;
    }

    /**
     * Converts {@link Phrase} to {@link Expr}.
     *
     * @param p to be converted to expression
     */
    public Expr<V> convertPhraseToExpr(Phrase<V> p) {
        Expr<V> expr;
        if ( p.getKind().getCategory() == Category.SENSOR ) {
            expr = SensorExpr.make((Sensor<V>) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            expr = ActionExpr.make((Action<V>) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            expr = FunctionExpr.make((Function<V>) p);
        } else if ( p.getKind().getCategory() == Category.METHOD ) {
            expr = MethodExpr.make((Method<V>) p);
        } else if ( p.getKind().hasName("IF_STMT") && ((IfStmt<V>) p).isTernary() ) {
            expr = StmtExpr.make((Stmt<V>) p);
        } else {
            expr = (Expr<V>) p;
        }
        return expr;
    }

    /**
     * Convert list of values ({@link Value}) to list of expressions ({@link ExprList}).
     *
     * @param values to be converted
     * @param nItems that should be converted
     * @param name of the values
     */
    public ExprList<V> valuesToExprList(List<Value> values, BlocklyType[] parametersTypes, int nItems, String name) {
        ExprList<V> exprList = ExprList.make();
        for ( int i = 0; i < nItems; i++ ) {
            exprList.addExpr(convertPhraseToExpr(extractValue(values, new ExprParam(name + i, parametersTypes[i]))));
        }
        exprList.setReadOnly();
        return exprList;
    }

    /**
     * Extract the operation from block.
     *
     * @param operationType name of the xml element where the operation is stored
     * @return the name of the operation
     */
    public static String getOperation(Block block, String operationType) {
        String op = operationType;
        if ( !block.getField().isEmpty() ) {
            op = extractOperation(block, operationType);
        }
        return op;
    }

    /**
     * Extract repeat statement from {@link Block}.
     */
    public Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode) {
        return extractRepeatStatement(block, expr, mode, BlocklyConstants.DO, 1);
    }

    /**
     * Extracts variable from a {@link Block}.
     *
     * @param block from which variable is extracted
     * @return AST object representing variable
     */
    public Phrase<V> extractVar(Block block) {
        String typeVar = block.getMutation() != null ? block.getMutation().getDatatype() : BlocklyConstants.NUMBER;
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, BlocklyConstants.VAR);
        return Var.make(BlocklyType.get(typeVar), field, extractBlockProperties(block), extractComment(block));
    }

    /**
     * Extract values from a {@link Block}. <br>
     * <br>
     * Throws {@link DbcException} if the number of values is not less or equal to the numOfValues
     *
     * @param block from which the values are extracted
     * @param numOfValues to be extracted
     * @return list of {@link Value}
     */
    public static List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    /**
     * Extract specific value from the list of values.
     *
     * @param values as a source
     * @param param with name of the value and default value if the value is missing (see. {@link ExprParam})
     * @return AST object or {@link EmptyExpr} if the value is missing
     */
    public Phrase<V> extractValue(List<Value> values, ExprParam param) {
        for ( Value value : values ) {
            if ( value.getName().equals(param.getName()) ) {
                return extractBlock(value);
            }
        }
        return EmptyExpr.make(param.getDefaultValue());
    }

    private Phrase<V> extractBlock(Value value) {
        Shadow shadow = value.getShadow();
        Block block = value.getBlock();
        if ( shadow != null ) {
            Block shadowBlock = shadow2block(shadow);
            if ( block != null ) {
                return ShadowExpr.make(convertPhraseToExpr(blockToAST(shadowBlock)), convertPhraseToExpr(blockToAST(block)));
            }
            return ShadowExpr.make(convertPhraseToExpr(blockToAST(shadowBlock)));
        } else {
            return blockToAST(block);
        }

    }

    private static Block shadow2block(Shadow shadow) {
        Block block = new Block();
        block.setId(shadow.getId());
        block.setType(shadow.getType());
        block.setIntask(shadow.isIntask());
        block.getField().add(shadow.getField());
        block.setShadow(true);
        return block;
    }

    /**
     * Extract {@link Statement} from the list of statements. <br>
     * <br>
     * Throws {@link DbcException} if the number of statements is not less or equal to the numOfStatements
     *
     * @param block as source
     * @param numOfStatements to be extracted
     * @return list of statements
     */
    public static List<Statement> extractStatements(Block block, short numOfStatements) {
        List<Statement> statements;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= numOfStatements, "Statements size is not less or equal to " + numOfStatements + "!");
        return statements;
    }

    /**
     * Extract {@link Statement} from list of statements.
     *
     * @param statements as source
     * @param stmtName to be extracted
     */
    public StmtList<V> extractStatement(List<Statement> statements, String stmtName) {
        StmtList<V> stmtList = StmtList.make();
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToStmtList(statement.getBlock());
            }
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    /**
     * Convert list of {@link Statement} to {@link ExprList}.
     *
     * @param statements as source
     * @param stmtName of statement to be extracted
     */
    public ExprList<V> statementsToExprs(List<Statement> statements, String stmtName) {
        ExprList<V> exprList = ExprList.make();
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToExprList(statement.getBlock());
            }
        }
        exprList.setReadOnly();
        return exprList;
    }

    /**
     * Extract {@link Field} from a {@link Block}. <br>
     * <br>
     * Throws {@link DbcException} if the number of fields is not less or equal to the numOfFields
     *
     * @param block as source
     * @param numOfFields to be extracted
     * @return list of fields
     */
    public static List<Field> extractFields(Block block, short numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() <= numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    /**
     * Extract field from list of {@link Field}. If the field with the given name is not found it returns the default {@link Value}.<br>
     * <br>
     * Throws {@link DbcException} if the field is not found and the defaultValue is set to <b>null</b>.
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @param defaultValue if the field is not existent
     * @return value containing the field
     */
    public static String extractField(List<Field> fields, String name, String defaultValue) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        if ( defaultValue == null ) {
            throw new DbcException("There is no field with name " + name);
        } else {
            return defaultValue;
        }
    }

    /**
     * Extract field from list of {@link Field}. If the field with the given name is not found or it is empty, it returns the default {@link Value}.<br>
     * <br>
     * Throws {@link DbcException} if the field is not found and the defaultValue is set to <b>null</b>.
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @param defaultValue if the field is not existent
     * @return value containing the field
     */
    public static String extractNonEmptyField(List<Field> fields, String name, String defaultValue) {
        Assert.notNull(defaultValue);
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                String value = field.getValue();
                if ( value.trim().equals("") ) {
                    return defaultValue;
                } else {
                    return value;
                }
            }
        }
        return defaultValue;
    }

    /**
     * Extract field from list of {@link Field}. <br>
     * <br>
     * Throws {@link DbcException} if the field is not found
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @return value containing the field
     */
    public static String extractField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        throw new DbcException("There is no field with name " + name);
    }

    /**
     * Extract an optional field from a list of {@link Field}. <br>
     * <br>
     *
     * @param fields as a source
     * @param name of the field to be extracted
     * @return value containing the field; if the key was not found, return null
     */
    public static String optField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        return null;
    }

    public static String extractOperation(Block block, String name) {
        List<Field> fields = extractFields(block, (short) 1);
        return extractField(fields, name);
    }

    /**
     * Extracts the comment from {@link Block}
     *
     * @param block as source
     */
    public static BlocklyComment extractComment(Block block) {
        if ( block.getComment() != null ) {
            Comment comment = block.getComment();
            return BlocklyComment.make(comment.getValue(), comment.isPinned(), comment.getH(), comment.getW());
        }
        return null;
    }

    /**
     * Extracts the visual state of the {@link Block}.
     *
     * @param block as a source
     */
    public static BlocklyBlockProperties extractBlockProperties(Block block) {
        return BlocklyBlockProperties
            .make(
                block.getType(),
                block.getId(),
                isDisabled(block),
                isCollapsed(block),
                isInline(block),
                isDeletable(block),
                isMovable(block),
                isInTask(block),
                isShadow(block),
                isErrorAttribute(block));
    }

    public static int getElseIf(Mutation mutation) {
        if ( mutation != null && mutation.getElseif() != null ) {
            return mutation.getElseif().intValue();
        }
        return 0;
    }

    public static int getElse(Mutation mutation) {
        if ( mutation != null && mutation.getElse() != null ) {
            return mutation.getElse().intValue();
        }
        return 0;
    }

    private StmtList<V> blocksToStmtList(List<Block> statementBolcks) {
        StmtList<V> stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            convertPhraseToStmt(stmtList, sb);
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    private ExprList<V> blocksToExprList(List<Block> exprBolcks) {
        ExprList<V> exprList = ExprList.make();
        for ( Block exb : exprBolcks ) {
            Phrase<V> p;

            p = blockToAST(exb);

            exprList.addExpr(convertPhraseToExpr(p));
        }
        exprList.setReadOnly();
        return exprList;
    }

    private void convertPhraseToStmt(StmtList<V> stmtList, Block sb) {
        Phrase<V> p;

        p = blockToAST(sb);

        Stmt<V> stmt;
        if ( p.getKind().getCategory() == Category.EXPR ) {
            stmt = ExprStmt.make((Expr<V>) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            stmt = ActionStmt.make((Action<V>) p);
        } else if ( p.getKind().getCategory() == Category.SENSOR ) {
            stmt = SensorStmt.make((Sensor<V>) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            stmt = FunctionStmt.make((Function<V>) p);
        } else if ( p.getKind().getCategory() == Category.METHOD ) {
            stmt = MethodStmt.make((Method<V>) p);
        } else {
            stmt = (Stmt<V>) p;
        }
        stmtList.addStmt(stmt);
    }

    private Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode, String location, int mutation) {
        List<Statement> statements = extractStatements(block, (short) mutation);
        StmtList<V> stmtList = extractStatement(statements, location);
        return RepeatStmt.make(RepeatStmt.Mode.get(mode), convertPhraseToExpr(expr), stmtList, extractBlockProperties(block), extractComment(block));
    }

    private static boolean isDisabled(Block block) {
        return block.isDisabled() != null;
    }

    private static boolean isCollapsed(Block block) {
        return block.isCollapsed() != null;
    }

    private static Boolean isInline(Block block) {
        if ( block.isInline() == null ) {
            return null;
        }
        return block.isInline();
    }

    private static Boolean isDeletable(Block block) {
        if ( block.isDeletable() == null ) {
            return null;
        }
        return block.isDeletable();
    }

    private static Boolean isMovable(Block block) {
        if ( block.isMovable() == null ) {
            return null;
        }
        return block.isMovable();
    }

    private static Boolean isInTask(Block block) {
        if ( block.isIntask() == null ) {
            return null;
        }
        return block.isIntask();
    }

    private static Boolean isShadow(Block block) {
        if ( block.isShadow() == null ) {
            return null;
        }
        return block.isShadow();
    }

    private static Boolean isErrorAttribute(Block block) {
        if ( block.isErrorAttribute() == null ) {
            return null;
        }
        return block.isErrorAttribute();
    }
}

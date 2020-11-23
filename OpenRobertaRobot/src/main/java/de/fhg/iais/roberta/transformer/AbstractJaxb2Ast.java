package de.fhg.iais.roberta.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Shadow;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
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
        String op = Jaxb2Ast.getOperation(block, operationType);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        Phrase<V> expr = extractValue(values, exprParam);
        return Unary.make(Unary.Op.get(op), convertPhraseToExpr(expr), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
        String op = Jaxb2Ast.getOperation(block, operationType);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
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
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block));
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
        List<Value> values = Jaxb2Ast.extractValues(block, (short) exprParams.size());
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
            Jaxb2Ast.convertStmtValList(values, statements, valAndStmt);
        } else {
            values = Jaxb2Ast.extractValues(block, (short) 1);
            statements = Jaxb2Ast.extractStatements(block, (short) 1);
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
            return IfStmt.make(exprsList, thenList, elseList, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), _else, _elseIf);
        }
        return IfStmt.make(exprsList, thenList, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), _else, _elseIf);
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
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String field = Jaxb2Ast.extractField(fields, BlocklyConstants.VAR);
        return Var.make(BlocklyType.get(typeVar), field, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
     * get from a value list (a XML substructure) the phrase matching<br>
     * - a variable - a given name
     *
     * @param values
     * @param name
     * @param blocklyType TODO
     * @return the Var<V> phrase; throw exception, if not found
     */
    public Var<V> getVar(List<Value> values, String name) {
        Phrase<V> p = extractValue(values, new ExprParam(name, BlocklyType.NUMBER));
        if ( p instanceof Var ) {
            return (Var<V>) p;
        } else {
            throw new DbcException("only variables allowed for field " + name);
        }
    }

    private Phrase<V> extractBlock(Value value) {
        Shadow shadow = value.getShadow();
        Block block = value.getBlock();
        if ( shadow != null ) {
            Block shadowBlock = AbstractJaxb2Ast.shadow2block(shadow);
            if ( block != null ) {
                return ShadowExpr.make(convertPhraseToExpr(blockToAST(shadowBlock)), convertPhraseToExpr(blockToAST(block)));
            }
            return ShadowExpr.make(convertPhraseToExpr(blockToAST(shadowBlock)));
        } else {
            return blockToAST(block);
        }
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
        List<Statement> statements = Jaxb2Ast.extractStatements(block, (short) mutation);
        StmtList<V> stmtList = extractStatement(statements, location);
        return RepeatStmt
            .make(RepeatStmt.Mode.get(mode), convertPhraseToExpr(expr), stmtList, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
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
}

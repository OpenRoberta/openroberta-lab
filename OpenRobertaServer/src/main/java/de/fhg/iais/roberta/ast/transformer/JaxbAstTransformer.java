package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.functions.Function;
import de.fhg.iais.roberta.ast.syntax.methods.Method;
import de.fhg.iais.roberta.ast.syntax.sensor.Sensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.typecheck.BlocklyType;
import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.hardwarecomponents.Category;

abstract public class JaxbAstTransformer<V> {
    protected ArrayList<ArrayList<Phrase<V>>> tree = new ArrayList<ArrayList<Phrase<V>>>();
    private int variableCounter = 0;

    /**
     * @return abstract syntax tree generated from JAXB objects.
     */
    public ArrayList<ArrayList<Phrase<V>>> getTree() {
        return this.tree;
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

    @Override
    public String toString() {
        return "BlockAST [project=" + this.tree + "]";
    }

    abstract protected Phrase<V> blockToAST(Block block);

    public Phrase<V> blockToUnaryExpr(Block block, ExprParam exprParam, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 1);
        Phrase<V> expr = extractValue(values, exprParam);
        return Unary.make(Unary.Op.get(op), convertPhraseToExpr(expr), extractBlockProperties(block), extractComment(block));
    }

    public Binary<V> blockToBinaryExpr(Block block, ExprParam leftExpr, ExprParam rightExpr, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 2);
        Phrase<V> left = extractValue(values, leftExpr);
        Phrase<V> right = extractValue(values, rightExpr);
        String operationRange = "";
        if ( block.getMutation() != null && block.getMutation().getOperatorRange() != null ) {
            operationRange = block.getMutation().getOperatorRange();
        }
        return Binary.make(
            Binary.Op.get(op),
            convertPhraseToExpr(left),
            convertPhraseToExpr(right),
            operationRange,
            extractBlockProperties(block),
            extractComment(block));
    }

    public List<Expr<V>> extractExprParameters(Block block, List<ExprParam> exprParams) {
        List<Expr<V>> params = new ArrayList<Expr<V>>();
        List<Value> values = extractValues(block, (short) exprParams.size());
        for ( ExprParam exprParam : exprParams ) {
            params.add(convertPhraseToExpr(extractValue(values, exprParam)));
        }
        return params;
    }

    public Phrase<V> blocksToIfStmt(Block block, int _else, int _elseIf) {
        List<Expr<V>> exprsList = new ArrayList<Expr<V>>();
        List<StmtList<V>> thenList = new ArrayList<StmtList<V>>();
        StmtList<V> elseList = null;

        List<Value> values = new ArrayList<Value>();
        List<Statement> statements = new ArrayList<Statement>();

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
                Phrase<V> p = extractValue(values, new ExprParam(BlocklyConstants.IF + i, Boolean.class));
                exprsList.add(convertPhraseToExpr(p));
                thenList.add(extractStatement(statements, BlocklyConstants.DO + i));
            }
        }

        if ( _else != 0 ) {
            return IfStmt.make(exprsList, thenList, elseList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
        }
        return IfStmt.make(exprsList, thenList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
    }

    public void convertStmtValList(List<Value> values, List<Statement> statements, List<Object> valAndStmt) {
        for ( int i = 0; i < valAndStmt.size(); i++ ) {
            Object ob = valAndStmt.get(i);
            if ( ob.getClass() == Value.class ) {
                values.add((Value) ob);
            } else {
                statements.add((Statement) ob);
            }
        }
    }

    public ExprList<V> blockToExprList(Block block, Class<?> defVal) {
        int items = 0;
        if ( block.getMutation().getItems() != null ) {
            items = block.getMutation().getItems().intValue();
        }
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= items, "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, defVal, items, BlocklyConstants.ADD);
    }

    public ExprList<V> argumentsToExprList(List<Arg> arguments) {
        ExprList<V> parameters = ExprList.make();
        for ( Arg arg : arguments ) {
            Var<V> parametar = Var.make(BlocklyType.get(arg.getType()), arg.getName(), null, null);
            parameters.addExpr(parametar);
        }
        parameters.setReadOnly();
        return parameters;
    }

    protected StmtList<V> blocksToStmtList(List<Block> statementBolcks) {
        StmtList<V> stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            convertPhraseToStmt(stmtList, sb);
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    protected ExprList<V> blocksToExprList(List<Block> exprBolcks) {
        ExprList<V> exprList = ExprList.make();
        for ( Block exb : exprBolcks ) {
            Phrase<V> p;

            p = blockToAST(exb);

            exprList.addExpr(convertPhraseToExpr(p));
        }
        exprList.setReadOnly();
        return exprList;
    }

    protected void convertPhraseToStmt(StmtList<V> stmtList, Block sb) {
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
        } else {
            expr = (Expr<V>) p;
        }
        return expr;
    }

    public ExprList<V> valuesToExprList(List<Value> values, Class<?> defVal, int nItems, String name) {
        ExprList<V> exprList = ExprList.make();
        for ( int i = 0; i < nItems; i++ ) {
            exprList.addExpr(convertPhraseToExpr(extractValue(values, new ExprParam(name + i, defVal))));
        }
        exprList.setReadOnly();
        return exprList;
    }

    public String getOperation(Block block, String operationType) {
        String op = operationType;
        if ( block.getField().size() != 0 ) {
            op = extractOperation(block, operationType);
        }
        return op;
    }

    public Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode) {
        return extractRepeatStatement(block, expr, mode, BlocklyConstants.DO, 1);
    }

    protected Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode, String location, int mutation) {
        List<Statement> statements = extractStatements(block, (short) mutation);
        StmtList<V> stmtList = extractStatement(statements, location);
        return RepeatStmt.make(RepeatStmt.Mode.get(mode), convertPhraseToExpr(expr), stmtList, extractBlockProperties(block), extractComment(block));
    }

    public Phrase<V> extractVar(Block block) {
        String typeVar = block.getMutation() != null ? block.getMutation().getDatatype() : "NUMERIC";
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, BlocklyConstants.VAR);
        return Var.make(BlocklyType.get(typeVar), field, extractBlockProperties(block), extractComment(block));
    }

    public List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    public Phrase<V> extractValue(List<Value> values, ExprParam param) {
        for ( Value value : values ) {
            if ( value.getName().equals(param.getName()) ) {
                return blockToAST(value.getBlock());
            }
        }
        return EmptyExpr.make(param.getDefaultValue());
    }

    public List<Statement> extractStatements(Block block, short numOfStatements) {
        List<Statement> statements;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= numOfStatements, "Statements size is not less or equal to " + numOfStatements + "!");
        return statements;
    }

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

    public List<Field> extractFields(Block block, short numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() == numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    public String extractField(List<Field> fields, String name) {
        for ( Field field : fields ) {
            if ( field.getName().equals(name) ) {
                return field.getValue();
            }
        }
        throw new DbcException("There is no field with name " + name);
    }

    public String extractOperation(Block block, String name) {
        List<Field> fields = extractFields(block, (short) 1);
        String operation = extractField(fields, name);
        return operation;
    }

    public BlocklyComment extractComment(Block block) {
        if ( block.getComment() != null ) {
            Comment comment = block.getComment();
            return BlocklyComment.make(comment.getValue(), comment.isPinned(), comment.getH(), comment.getW());
        }
        return null;
    }

    public BlocklyBlockProperties extractBlockProperties(Block block) {
        return BlocklyBlockProperties.make(
            block.getType(),
            block.getId(),
            isDisabled(block),
            isCollapsed(block),
            isInline(block),
            isDeletable(block),
            isMovable(block));
    }

    protected boolean isDisabled(Block block) {
        return block.isDisabled() == null ? false : true;
    }

    protected boolean isCollapsed(Block block) {
        return block.isCollapsed() == null ? false : true;
    }

    protected Boolean isInline(Block block) {
        if ( block.isInline() == null ) {
            return null;
        }
        return block.isInline();
    }

    protected Boolean isDeletable(Block block) {
        if ( block.isDeletable() == null ) {
            return null;
        }
        return block.isDeletable();
    }

    protected Boolean isMovable(Block block) {
        if ( block.isMovable() == null ) {
            return null;
        }
        return block.isMovable();
    }

    public int getElseIf(Mutation mutation) {
        if ( mutation != null && mutation.getElseif() != null ) {
            return mutation.getElseif().intValue();
        }
        return 0;
    }

    public int getElse(Mutation mutation) {
        if ( mutation != null && mutation.getElse() != null ) {
            return mutation.getElse().intValue();
        }
        return 0;
    }
}

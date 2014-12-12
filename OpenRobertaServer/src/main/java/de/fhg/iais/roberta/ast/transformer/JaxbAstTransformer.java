package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.action.Action;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.Var.TypeVar;
import de.fhg.iais.roberta.ast.syntax.functions.Function;
import de.fhg.iais.roberta.ast.syntax.sensor.Sensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Comment;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.hardwarecomponents.Category;

abstract public class JaxbAstTransformer<V> {
    protected ArrayList<Phrase<V>> tree = new ArrayList<Phrase<V>>();

    /**
     * @return abstract syntax tree generated from JAXB objects.
     */
    public ArrayList<Phrase<V>> getTree() {
        return this.tree;
    }

    @Override
    public String toString() {
        return "BlockAST [project=[" + this.tree + "]]";
    }

    abstract protected Phrase<V> blockToAST(Block block);

    protected Phrase<V> blockToUnaryExpr(Block block, ExprParam exprParam, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 1);
        Phrase<V> expr = extractValue(values, exprParam);
        return Unary.make(Unary.Op.get(op), convertPhraseToExpr(expr), extractBlockProperties(block), extractComment(block));
    }

    protected Binary<V> blockToBinaryExpr(Block block, ExprParam leftExpr, ExprParam rightExpr, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 2);
        Phrase<V> left = extractValue(values, leftExpr);
        Phrase<V> right = extractValue(values, rightExpr);
        return Binary.make(Binary.Op.get(op), convertPhraseToExpr(left), convertPhraseToExpr(right), extractBlockProperties(block), extractComment(block));
    }

    //    protected Func<V> blockToFunction(Block block, List<ExprParam> exprParams, String operationType) {
    //        String op = getOperation(block, operationType);
    //        List<Expr<V>> params = extractExprParameters(block, exprParams);
    //        return Func.make(Functions.get(op), params, extractBlockProperties(block), extractComment(block));
    //    }
    //
    //    protected Func<V> blockToFunction(Block block, List<String> strParams, List<ExprParam> exprParams, String operationType) {
    //        List<Expr<V>> params = extractExprParameters(block, exprParams);
    //        return Func.make(Functions.get(operationType), strParams, params, extractBlockProperties(block), extractComment(block));
    //    }

    protected List<Expr<V>> extractExprParameters(Block block, List<ExprParam> exprParams) {
        List<Expr<V>> params = new ArrayList<Expr<V>>();
        List<Value> values = extractValues(block, (short) exprParams.size());
        for ( ExprParam exprParam : exprParams ) {
            params.add(convertPhraseToExpr(extractValue(values, exprParam)));
        }
        return params;
    }

    protected Phrase<V> blocksToIfStmt(Block block, int _else, int _elseIf) {
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
                elseList = extractStatement(statements, "ELSE");
            } else {
                Phrase<V> p = extractValue(values, new ExprParam("IF" + i, Boolean.class));
                exprsList.add(convertPhraseToExpr(p));
                thenList.add(extractStatement(statements, "DO" + i));
            }
        }

        if ( _else != 0 ) {
            return IfStmt.make(exprsList, thenList, elseList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
        } else {
            return IfStmt.make(exprsList, thenList, extractBlockProperties(block), extractComment(block), _else, _elseIf);
        }
    }

    protected void convertStmtValList(List<Value> values, List<Statement> statements, List<Object> valAndStmt) {
        for ( int i = 0; i < valAndStmt.size(); i++ ) {
            Object ob = valAndStmt.get(i);
            if ( ob.getClass() == Value.class ) {
                values.add((Value) ob);
            } else {
                statements.add((Statement) ob);
            }
        }
    }

    protected ExprList<V> blockToExprList(Block block, Class<?> defVal) {
        int items = 0;
        if ( block.getMutation().getItems() != null ) {
            items = block.getMutation().getItems().intValue();
        }
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= items, "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, defVal, items);
    }

    protected Phrase<V> blockToConst(Block block, String type) {
        //what about template class?
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, type, (short) 0);
        switch ( type ) {
            case "BOOL":
                return BoolConst.make(Boolean.parseBoolean(field.toLowerCase()), extractBlockProperties(block), extractComment(block));
            case "NUM":
                return NumConst.make(field, extractBlockProperties(block), extractComment(block));
            case "TEXT":
                return StringConst.make(field, extractBlockProperties(block), extractComment(block));
            case "CONSTANT":
                return MathConst.make(MathConst.Const.get(field), extractBlockProperties(block), extractComment(block));
            case "COLOUR":
                return ColorConst.make(field, extractBlockProperties(block), extractComment(block));
            default:
                throw new DbcException("Invalid type constant!");
        }
    }

    protected StmtList<V> blocksToStmtList(List<Block> statementBolcks) {
        StmtList<V> stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            convertPhraseToStmt(stmtList, sb);
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    protected void convertPhraseToStmt(StmtList<V> stmtList, Block sb) {
        Phrase<V> p = blockToAST(sb);
        Stmt<V> stmt;
        if ( p.getKind().getCategory() == Category.EXPR ) {
            stmt = ExprStmt.make((Expr<V>) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            stmt = ActionStmt.make((Action<V>) p);
        } else if ( p.getKind().getCategory() == Category.SENSOR ) {
            stmt = SensorStmt.make((Sensor<V>) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            stmt = FunctionStmt.make((Function<V>) p);
        } else {
            stmt = (Stmt<V>) p;
        }
        stmtList.addStmt(stmt);
    }

    protected Expr<V> convertPhraseToExpr(Phrase<V> p) {
        Expr<V> expr;
        if ( p.getKind().getCategory() == Category.SENSOR ) {
            expr = SensorExpr.make((Sensor<V>) p);
        } else if ( p.getKind().getCategory() == Category.ACTOR ) {
            expr = ActionExpr.make((Action<V>) p);
        } else if ( p.getKind().getCategory() == Category.FUNCTION ) {
            expr = FunctionExpr.make((Function<V>) p);
        } else {
            expr = (Expr<V>) p;
        }
        return expr;
    }

    protected ExprList<V> valuesToExprList(List<Value> values, Class<?> defVal, int nItems) {
        ExprList<V> exprList = ExprList.make();
        for ( int i = 0; i < nItems; i++ ) {
            exprList.addExpr(convertPhraseToExpr(extractValue(values, new ExprParam("ADD" + i, defVal))));
        }
        exprList.setReadOnly();
        return exprList;
    }

    protected String getOperation(Block block, String operationType) {
        String op = operationType;
        if ( block.getField().size() != 0 ) {
            op = extractOperation(block, operationType);
        }
        return op;
    }

    protected Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode) {
        return extractRepeatStatement(block, expr, mode, "DO", 1);
    }

    protected Phrase<V> extractRepeatStatement(Block block, Phrase<V> expr, String mode, String location, int mutation) {
        List<Statement> statements = extractStatements(block, (short) mutation);
        StmtList<V> stmtList = extractStatement(statements, location);
        return RepeatStmt.make(RepeatStmt.Mode.get(mode), convertPhraseToExpr(expr), stmtList, extractBlockProperties(block), extractComment(block));
    }

    protected Phrase<V> extractVar(Block block) {
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, "VAR", (short) 0);
        return Var.make(field, TypeVar.NONE, extractBlockProperties(block), extractComment(block));
    }

    protected List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    protected Phrase<V> extractValue(List<Value> values, ExprParam param) {
        for ( Value value : values ) {
            if ( value.getName().equals(param.getName()) ) {
                return blockToAST(value.getBlock());
            }
        }
        return EmptyExpr.make(param.getDefaultValue());
    }

    protected List<Statement> extractStatements(Block block, short numOfStatements) {
        List<Statement> statements;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= numOfStatements, "Statements size is not less or equal to " + numOfStatements + "!");
        return statements;
    }

    protected StmtList<V> extractStatement(List<Statement> statements, String stmtName) {
        StmtList<V> stmtList = StmtList.make();
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToStmtList(statement.getBlock());
            }
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    protected List<Field> extractFields(Block block, short numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() == numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    protected String extractField(List<Field> fields, String name, short fieldLocation) {
        Field field = fields.get(fieldLocation);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name + "!");
        return field.getValue();
    }

    protected String extractOperation(Block block, String name) {
        List<Field> fields = extractFields(block, (short) 1);
        String operation = extractField(fields, name, (short) 0);
        return operation;
    }

    protected BlocklyComment extractComment(Block block) {
        if ( block.getComment() != null ) {
            Comment comment = block.getComment();
            return BlocklyComment.make(comment.getValue(), comment.isPinned(), comment.getH(), comment.getW());
        }
        return null;
    }

    protected BlocklyBlockProperties extractBlockProperties(Block block) {
        return BlocklyBlockProperties.of(block.getType(), block.getId(), isDisabled(block), isCollapsed(block), isInline(block), isDeletable(block));
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
}

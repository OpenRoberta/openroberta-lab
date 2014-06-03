package de.fhg.iais.roberta.ast.transformer;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.IntConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.transformer.BlockASTType.Types;
import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

public class BlockAST {

    private BlockASTType type;

    public BlockAST(String t) {
        setType(Types.valueOf(t));
    }

    private void setType(Types t) {
        this.type = t.make();
    }

    public Phrase bToA(Block block) {

        List<Value> values;
        Value value;
        Field field;
        List<Field> fields;

        Phrase left;
        Phrase right;

        switch ( block.getType() ) {
        //Logik
            case "logic_compare":
                return blockToBinaryExpr(block, "A", "B", "OP");

            case "logic_operation":
                return blockToBinaryExpr(block, "A", "B", "OP");

            case "logic_negate":
                return blockToUnaryExpr(block, "BOOL", "NOT");

            case "logic_boolean":
                return blockToConst(block, "BOOL");

            case "logic_null":
                return NullConst.make();

                //Mathematik
            case "math_number":
                return blockToConst(block, "NUM");

            case "math_arithmetic":
                return blockToBinaryExpr(block, "A", "B", "OP");

            case "math_single":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_trig":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_number_property":
                Mutation mutation = block.getMutation();
                List<Arg> args = mutation.getArg();
                Assert.isTrue(args.size() == 0);
                Arg divisorInput = args.get(0);
                String op = getOperation(block, "PROPERTY");
                if ( op.equals("DIVISIBLE_BY") ) {
                    Assert.isTrue(divisorInput.getName().equals("true"), "Divisor input is not equal to true!");
                    return blockToBinaryExpr(block, "NUMBER_TO_CHECK", "DIVISOR", op);
                } else {
                    Assert.isTrue(divisorInput.getName().equals("false"), "Divisor input is not equal to false!");
                    return blockToUnaryExpr(block, "NUMBER_TO_CHECK", op);
                }

            case "math_change":
                left = getVar(block);
                right = getExp(block.getValue(), "DELTA", (short) 0);
                return Binary.make(Binary.Op.MATH_CHANGE, (Expr) left, (Expr) right);

            case "math_round":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_on_list":
                return blockToUnaryExpr(block, "LIST", "OP");

            case "math_modulo":
                return blockToBinaryExpr(block, "DIVIDEND", "DIVISOR", "MOD");

            case "math_constrain":
                values = block.getValue();
                Assert.isTrue(values.size() == 3, "Values size is not equal to 3");
                Phrase valueExpr = getExp(values, "VALUE", (short) 0);
                Phrase lowExpr = getExp(values, "LOW", (short) 1);
                Phrase maxExpr = Binary.make(Binary.Op.MAX, (Expr) lowExpr, (Expr) valueExpr);
                Phrase highExpr = getExp(values, "HIGH", (short) 2);
                return Binary.make(Binary.Op.MIN, (Expr) maxExpr, (Expr) highExpr);

            case "math_random_integer":
                return blockToBinaryExpr(block, "FROM", "TO", "RANDOM_INTEGER");

                //TEXT
            case "text":
                return blockToConst(block, "TEXT");

            case "text_join":
                return Unary.make(Unary.Op.TEXT_JOIN, valuesToExprList(block));

            case "text_append":
                left = getVar(block);
                right = getExp(block.getValue(), "TEXT", (short) 0);
                return Binary.make(Binary.Op.TEXT_APPEND, (Expr) left, (Expr) right);

            case "text_length":
                return blockToUnaryExpr(block, "VALUE", "TEXT_LENGTH");

            case "text_isEmpty":
                return blockToUnaryExpr(block, "VALZE", "IS_EMPTY");

            case "text_indexOf":
                return blockToBinaryExpr(block, "VALUE", "FIND", "END");

            case "text_charAt":
                args = block.getMutation().getArg();
                Assert.isTrue(args.size() == 1, "Number of arguments in mutation is not equal to 1!");
                Arg atArg = args.get(0);
                if ( atArg.getName().equals("true") ) {
                    return blockToBinaryExpr(block, "VALUE", "AT", "WHERE");
                } else {
                    return blockToUnaryExpr(block, "VALUE", "WHERE");
                }

            case "text_getSubstring":
                //TODO Not finished yet
                args = block.getMutation().getArg();
                Assert.isTrue(args.size() == 2, "Number of arguments in mutation is not equal to 2!");
                String atArg1 = args.get(0).getName();
                String atArg2 = args.get(1).getName();
                values = block.getValue();
                if ( atArg1.equals("true") && atArg2.equals("true") ) {
                    Assert.isTrue(values.size() == 3);
                    getExp(values, "STRING", (short) 0);
                    getExp(values, "AT1", (short) 1);
                    getExp(values, "AT2", (short) 2);
                }

            case "text_change":
                return blockToUnaryExpr(block, "TEXT", "CASE");

            case "text_trim":
                return blockToUnaryExpr(block, "TEXT", "MODE");

            case "text_prompt":
                fields = block.getField();
                Assert.isTrue(fields.size() == 2, "Number of fields is not equal to 2");
                Field type = fields.get(0);
                Field text = fields.get(1);
                Assert.isTrue(type.getName().equals("TYPE"));
                Assert.isTrue(text.getName().equals("TEXT"));
                StringConst txtExpr = StringConst.make(text.getValue());
                return Unary.make(Unary.Op.valueOf(type.getValue()), txtExpr);

                //LISTEN
            case "lists_create_empty":
                return ExprList.make();

            case "lists_create_with":
                return valuesToExprList(block);

            case "lists_repeat":
                return blockToBinaryExpr(block, "ITEM", "NUM", "LISTS_REPEAT");

            case "lists_length":
                return blockToUnaryExpr(block, "VALUE", "LISTS_LENGTH");

            case "lists_isEmpty":
                return blockToUnaryExpr(block, "VALUE", "IS_EMPTY");

            case "lists_indexOf":
                return blockToBinaryExpr(block, "VALUE", "FIND", "END");

            case "lists_getIndex":
                //TODO not implemented

            case "lists_setIndex":
                //TODO not implemented

            case "lists_getSublist":
                //TODO not implemented

                //VARIABLEN
            case "variables_set":
                return Binary.make(Binary.Op.ASSIGNMENT, (Expr) getVar(block), (Expr) getExp(block.getValue(), "VALUE", (short) 0));

            case "variables_get":
                return getVar(block);

                //KONTROLLE
            case "controls_if":

            default:
                throw new RuntimeException("Invalid Block: " + block.getType());
        }
    }

    private Phrase getVar(Block block) {
        List<Field> fields = block.getField();
        Assert.isTrue(fields.size() == 1, "Number of fields is not equal to 1");
        Field field = fields.get(0);
        Assert.isTrue(field.getName().equals("VAR"), "Field name is not equal to VAR");
        return Var.make(field.getValue());
    }

    private ExprList valuesToExprList(Block block) {
        List<Arg> args = block.getMutation().getArg();
        Assert.isTrue(args.size() == 1, "Number of arguments in mutation is not equal 1!");
        Arg items = args.get(0);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() == Integer.parseInt(items.getName()), "Number of values is not equal to number of items in mutation!");
        ExprList exprList = ExprList.make();
        for ( int i = 0; i < values.size(); i++ ) {
            exprList.addExpr((Expr) getExp(values, "ADD" + i, (short) i));
        }
        return exprList;
    }

    private Phrase blockToUnaryExpr(Block block, String valueType, String operationType) {
        String op = checkOperationType(block, operationType);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() == 1, "Values size is not equal to 1!");
        Phrase expr = getExp(values, valueType, (short) 0);
        return Unary.make(Unary.Op.valueOf(op), (Expr) expr);
    }

    private Phrase blockToConst(Block block, String type) {
        List<Field> fields = block.getField();
        Assert.isTrue(fields.size() == 1, "Field size is not equal to 1!");
        Field field = fields.get(0);
        switch ( type ) {
            case "BOOL":
                Assert.isTrue(field.getName().equals("BOOL"), "Field name is not equal to BOOL!");
                return BoolConst.make(Boolean.parseBoolean(field.getValue().toLowerCase()));
            case "NUM":
                Assert.isTrue(field.getName().equals("NUM"), "Field name is not equal to NUM!");
                return IntConst.make(Integer.parseInt(field.getValue()));
            case "TEXT":
                Assert.isTrue(field.getName().equals("TEXT"), "Field name is not equal to STRING!");
                return StringConst.make(field.getValue());
            default:
                throw new RuntimeException("Invalid type constant!");
        }

    }

    private Binary blockToBinaryExpr(Block block, String leftExpName, String rightExprName, String operationType) {
        String op = checkOperationType(block, operationType);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() == 2, "Values size is not equal to 2");
        Phrase left = getExp(values, leftExpName, (short) 0);
        Phrase right = getExp(values, rightExprName, (short) 1);
        return Binary.make(Binary.Op.valueOf(op), (Expr) left, (Expr) right);
    }

    private String checkOperationType(Block block, String operationType) {
        String op = operationType;
        if ( block.getField().size() != 0 ) {
            op = getOperation(block, operationType);
        }
        return op;
    }

    private Phrase getExp(List<Value> values, String name, short exprNum) {
        Value value = values.get(exprNum);
        Assert.isTrue(value.getName().equals(name), "Value name is not equal to " + name + "!");
        return bToA(value.getBlock());
    }

    private String getOperation(Block block, String name) {
        List<Field> fields = block.getField();
        Assert.isTrue(fields.size() == 1, "Number of fields is not equal to 1");
        Field field = fields.get(0);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name);
        return field.getValue();
    }

    private StmtList blocksToStmtList(List<Block> statementBolcks) {
        StmtList stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            stmtList.addStmt((Stmt) bToA(sb));
        }
        return stmtList;
    }

    //            case "controls_repeat_ext":
    //                values = block.getValue();
    //                Assert.isTrue(values.size() == 1);
    //                value = values.get(0);
    //                Assert.isTrue(value.getName().equals("TIMES"));
    //                Block expBlock = value.getBlock();
    //                Phrase expr = bToA(expBlock);
    //                List<Statement> statements = block.getStatement();
    //                Assert.isTrue(statements.size() <= 1);
    //                if ( statements.size() == 1 ) {
    //                    Statement statement = statements.get(0);
    //                    Assert.isTrue(statement.getName().equals("DO"));
    //                    List<Block> statementBolcks = statement.getBlock();
    //                    StmtList stmtList = blocksToStmtList(statementBolcks);
    //                    RepeatStmt repeatStmt = RepeatStmt.make((Expr) expr, stmtList);
    //                    return repeatStmt;
    //                } else {
    //                    StmtList stmtList = StmtList.make();
    //                    RepeatStmt repeatStmt = RepeatStmt.make((Expr) expr, stmtList);
    //                    return repeatStmt;
    //                }

    public void blockToAST() {
        //        return this.type.blockToAST(blockAST, block)ToAST(this);
    }

}

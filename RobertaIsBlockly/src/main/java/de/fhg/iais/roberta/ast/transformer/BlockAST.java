package de.fhg.iais.roberta.ast.transformer;

import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.IntConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.sensoren.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.DrehSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.SteinSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.UltraSSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

public class BlockAST {

    public Phrase bToA(Block block) {
        List<Arg> args;
        List<Value> values;
        List<Field> fields;
        List<Statement> statements;

        Value value;
        Field field;
        StmtList stmtList;

        Phrase left;
        Phrase right;
        Phrase expr;
        Phrase var;

        String mode;
        String port;
        String name;

        switch ( block.getType() ) {
        //Sensoren
            case "robSensors_touch_isPressed":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return TouchSensor.make(Integer.valueOf(port));

            case "robSensors_ultrasonic_setMode":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, "SENSORPORT", (short) 0);
                mode = extractField(fields, "MODE", (short) 1);
                return UltraSSensor.make(UltraSSensor.Mode.valueOf(mode), Integer.valueOf(port));

            case "robSensors_ultrasonic_getMode":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return UltraSSensor.make(UltraSSensor.Mode.GET_MODE, Integer.valueOf(port));

            case "robSensors_ultrasonic_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return UltraSSensor.make(UltraSSensor.Mode.GET_SAMPLE, Integer.valueOf(port));

            case "robSensors_colour_setMode":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, "SENSORPORT", (short) 0);
                mode = extractField(fields, "MODE", (short) 1);
                return ColorSensor.make(ColorSensor.Mode.valueOf(mode), Integer.valueOf(port));

            case "robSensors_colour_getMode":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return ColorSensor.make(ColorSensor.Mode.GET_MODE, Integer.valueOf(port));

            case "robSensors_colour_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return ColorSensor.make(ColorSensor.Mode.GET_SAMPLE, Integer.valueOf(port));

            case "robSensors_infrared_setMode":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, "SENSORPORT", (short) 0);
                mode = extractField(fields, "MODE", (short) 1);
                return InfraredSensor.make(InfraredSensor.Mode.valueOf(mode), Integer.valueOf(port));

            case "robSensors_infrared_getMode":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return InfraredSensor.make(InfraredSensor.Mode.GET_MODE, Integer.valueOf(port));

            case "robSensors_infrared_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return InfraredSensor.make(InfraredSensor.Mode.GET_SAMPLE, Integer.valueOf(port));

            case "robSensors_encoder_setMode":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, "MOTORPORT", (short) 0);
                mode = extractField(fields, "MODE", (short) 1);
                return DrehSensor.make(DrehSensor.Mode.valueOf(mode), port);

            case "robSensors_encoder_getMode":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "MOTORPORT", (short) 0);
                return DrehSensor.make(DrehSensor.Mode.GET_MODE, port);

            case "robSensors_encoder_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "MOTORPORT", (short) 0);
                return DrehSensor.make(DrehSensor.Mode.GET_SAMPLE, port);

            case "robSensors_encoder_reset":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "MOTORPORT", (short) 0);
                return DrehSensor.make(DrehSensor.Mode.RESET, port);

            case "robSensors_key_isPressed":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "KEY", (short) 0);
                return SteinSensor.make(SteinSensor.Mode.IS_PRESSED, port);

            case "robSensors_key_waitForPress":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "KEY", (short) 0);
                return SteinSensor.make(SteinSensor.Mode.WAIT_FOR_PRESS, port);

            case "robSensors_key_waitForPressAndRelease":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "KEY", (short) 0);
                return SteinSensor.make(SteinSensor.Mode.WAIT_FOR_PRESS_AND_RELEASE, port);

            case "robSensors_gyro_setMode":
                fields = extractFields(block, (short) 2);
                port = extractField(fields, "SENSORPORT", (short) 0);
                mode = extractField(fields, "MODE", (short) 1);
                return GyroSensor.make(GyroSensor.Mode.valueOf(mode), Integer.valueOf(port));

            case "robSensors_gyro_getMode":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return GyroSensor.make(GyroSensor.Mode.GET_MODE, Integer.valueOf(port));

            case "robSensors_gyro_getSample":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "SENSORPORT", (short) 0);
                return GyroSensor.make(GyroSensor.Mode.GET_SAMPLE, Integer.valueOf(port));

            case "robSensors_gyro_reset":
                fields = extractFields(block, (short) 1);
                port = extractField(fields, "MOTORPORT", (short) 0);
                return GyroSensor.make(GyroSensor.Mode.RESET, Integer.valueOf(port));

                //Logik
            case "logic_compare":
                return blockToBinaryExpr(block, "A", "B", "OP", Integer.class);

            case "logic_operation":
                return blockToBinaryExpr(block, "A", "B", "OP", Boolean.class);

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
                return blockToBinaryExpr(block, "A", "B", "OP", Integer.class);

            case "math_single":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_trig":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_number_property":
                args = extractArguments(block.getMutation(), (short) 1);
                Arg divisorInput = args.get(0);
                String op = getOperation(block, "PROPERTY");
                if ( op.equals("DIVISIBLE_BY") ) {
                    Assert.isTrue(divisorInput.getName().equals("true"), "Divisor input is not equal to true!");
                    return blockToBinaryExpr(block, "NUMBER_TO_CHECK", "DIVISOR", op, Integer.class);
                } else {
                    Assert.isTrue(divisorInput.getName().equals("false"), "Divisor input is not equal to false!");
                    return blockToUnaryExpr(block, "NUMBER_TO_CHECK", op);
                }

            case "math_change":
                left = extractVar(block);
                right = extractValue(block.getValue(), "DELTA", (short) 0);
                return Binary.make(Binary.Op.MATH_CHANGE, (Expr) left, (Expr) right);

            case "math_round":
                return blockToUnaryExpr(block, "NUM", "OP");

            case "math_on_list":
                return blockToUnaryExpr(block, "LIST", "OP");

            case "math_modulo":
                return blockToBinaryExpr(block, "DIVIDEND", "DIVISOR", "MOD", Integer.class);

            case "math_constrain":
                values = extractValues(block, (short) 3);
                Phrase valueExpr = extractValue(values, "VALUE", (short) 0);
                Phrase lowExpr = extractValue(values, "LOW", (short) 1);
                Phrase maxExpr = Binary.make(Binary.Op.MAX, (Expr) lowExpr, (Expr) valueExpr);
                Phrase highExpr = extractValue(values, "HIGH", (short) 2);
                return Binary.make(Binary.Op.MIN, (Expr) maxExpr, (Expr) highExpr);

            case "math_random_integer":
                return blockToBinaryExpr(block, "FROM", "TO", "RANDOM_INTEGER", Integer.class);

                //TEXT
            case "text":
                return blockToConst(block, "TEXT");

            case "text_join":
                return Unary.make(Unary.Op.TEXT_JOIN, blockToExprList(block));

            case "text_append":
                left = extractVar(block);
                right = extractValue(block.getValue(), "TEXT", (short) 0);
                return Binary.make(Binary.Op.TEXT_APPEND, (Expr) left, (Expr) right);

            case "text_length":
                return blockToUnaryExpr(block, "VALUE", "TEXT_LENGTH");

            case "text_isEmpty":
                return blockToUnaryExpr(block, "VALUE", "IS_EMPTY");

            case "text_indexOf":
                return blockToBinaryExpr(block, "VALUE", "FIND", "END", String.class);

            case "text_charAt":
                args = extractArguments(block.getMutation(), (short) 1);
                Arg atArg = args.get(0);
                if ( atArg.getName().equals("true") ) {
                    return blockToBinaryExpr(block, "VALUE", "AT", "WHERE", Integer.class);
                } else {
                    return blockToUnaryExpr(block, "VALUE", "WHERE");
                }

            case "text_getSubstring":
                //TODO Not finished yet
                args = extractArguments(block.getMutation(), (short) 2);
                String atArg1 = args.get(0).getName();
                String atArg2 = args.get(1).getName();
                values = block.getValue();
                if ( atArg1.equals("true") && atArg2.equals("true") ) {
                    Assert.isTrue(values.size() == 3);
                    extractValue(values, "STRING", (short) 0);
                    extractValue(values, "AT1", (short) 1);
                    extractValue(values, "AT2", (short) 2);
                }

            case "text_change":
                return blockToUnaryExpr(block, "TEXT", "CASE");

            case "text_trim":
                return blockToUnaryExpr(block, "TEXT", "MODE");

            case "text_prompt":
                fields = extractFields(block, (short) 2);
                String type = extractField(fields, "TYPE", (short) 0);
                String text = extractField(fields, "TEXT", (short) 1);
                StringConst txtExpr = StringConst.make(text);
                return Unary.make(Unary.Op.valueOf(type), txtExpr);

                //LISTEN
            case "lists_create_empty":
                return ExprList.make();

            case "lists_create_with":
                return blockToExprList(block);

            case "lists_repeat":
                return blockToBinaryExpr(block, "ITEM", "NUM", "LISTS_REPEAT", Integer.class);

            case "lists_length":
                return blockToUnaryExpr(block, "VALUE", "LISTS_LENGTH");

            case "lists_isEmpty":
                return blockToUnaryExpr(block, "VALUE", "IS_EMPTY");

            case "lists_indexOf":
                return blockToBinaryExpr(block, "VALUE", "FIND", "END", String.class);

            case "lists_getIndex":
                //TODO not implemented

            case "lists_setIndex":
                //TODO not implemented

            case "lists_getSublist":
                //TODO not implemented

                //VARIABLEN
            case "variables_set":
                values = block.getValue();
                Assert.isTrue(values.size() <= 1, "Number of fields is not less or equal to 1!");

                if ( values.size() == 1 ) {
                    expr = extractValue(block.getValue(), "VALUE", (short) 0);
                } else {
                    expr = IntConst.make(0);
                }
                return AssignStmt.make((Var) extractVar(block), (Expr) expr);

            case "variables_get":
                return extractVar(block);

                //KONTROLLE
            case "controls_if":
                //TODO not complete
                values = block.getValue();
                statements = block.getStatement();
                if ( block.getMutation() == null ) {
                    Assert.isTrue(values.size() <= 1, "Number of fields is not less or equal to 1!");
                    if ( values.size() == 1 ) {
                        expr = extractValue(block.getValue(), "IF0", (short) 0);
                    } else {
                        expr = BoolConst.make(false);
                    }
                    Assert.isTrue(statements.size() <= 1, "Number of statements is not equal or less to 1!");

                    if ( statements.size() == 1 ) {
                        stmtList = stmtsToStmtList(statements);
                        IfStmt ifStmt = IfStmt.make((Expr) expr, stmtList);
                        return ifStmt;
                    } else {
                        stmtList = StmtList.make();
                        IfStmt ifStmt = IfStmt.make((Expr) expr, stmtList);
                        return ifStmt;
                    }
                } else {

                }

            case "controls_whileUntil":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, "MODE", (short) 0);

                values = block.getValue();

                Assert.isTrue(values.size() <= 1, "Number of fields is not less or equal to 1!");
                if ( values.size() == 1 ) {
                    expr = extractValue(block.getValue(), "BOOL", (short) 0);
                } else {
                    expr = BoolConst.make(false);
                }
                return extractRepeatStatement(block, expr, mode);

            case "controls_for":
                var = extractVar(block);

                values = block.getValue();
                Assert.isTrue(values.size() == 3, "Number of values is not equal to 3!");

                ExprList exprList = ExprList.make();

                Phrase from = extractValue(values, "FROM", (short) 0);
                Phrase to = extractValue(values, "TO", (short) 1);
                Phrase by = extractValue(values, "BY", (short) 2);

                Binary exprAssig = Binary.make(Binary.Op.ASSIGNMENT, (Expr) var, (Expr) from);
                Binary exprCondition = Binary.make(Binary.Op.LTE, (Expr) var, (Expr) to);
                exprList.addExpr(exprAssig);

                exprList.addExpr(exprCondition);
                exprList.addExpr((Expr) by);

                return extractRepeatStatement(block, exprList, "FOR");

            case "controls_forEach":
                var = extractVar(block);

                values = block.getValue();

                Assert.isTrue(values.size() <= 1, "Number of fields is not less or equal to 1!");
                if ( values.size() == 1 ) {
                    expr = extractValue(values, "LIST", (short) 0);
                } else {
                    expr = ExprList.make();
                }

                Binary exprBinary = Binary.make(Binary.Op.IN, (Expr) var, (Expr) expr);

                return extractRepeatStatement(block, exprBinary, "FOR_EACH");

            case "controls_flow_statements":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, "FLOW", (short) 0);
                return StmtFlowCon.make(Flow.valueOf(mode));

            case "controls_repeat_ext":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, "TIMES", (short) 0);
                return extractRepeatStatement(block, expr, "TIMES");

            default:
                throw new RuntimeException("Invalid Block: " + block.getType());
        }
    }

    private Phrase blockToUnaryExpr(Block block, String valueType, String operationType) {
        String op = checkOperationType(block, operationType);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= 1, "Values size is not less or equal to 1!");
        Phrase expr;
        if ( values.size() == 1 ) {
            expr = extractValue(values, valueType, (short) 0);
        } else {
            expr = ExprList.make();
        }
        return Unary.make(Unary.Op.valueOf(op), (Expr) expr);
    }

    private Binary blockToBinaryExpr(Block block, String leftExpName, String rightExprName, String operationType, Class<?> defVal) {
        String op = checkOperationType(block, operationType);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= 2, "Values size is not less or equal to 2!");

        Phrase left = EmptyExpr.make(defVal);
        Phrase right = EmptyExpr.make(defVal);
        if ( values.size() == 1 ) {
            if ( values.get(0).getName().equals(leftExpName) ) {
                left = extractValue(values, leftExpName, (short) 0);
            } else {
                right = extractValue(values, rightExprName, (short) 1);
            }
        } else if ( values.size() == 2 ) {
            left = extractValue(values, leftExpName, (short) 0);
            right = extractValue(values, rightExprName, (short) 1);
        }
        return Binary.make(Binary.Op.valueOf(op), (Expr) left, (Expr) right);
    }

    private ExprList blockToExprList(Block block) {
        List<Arg> args = block.getMutation().getArg();
        Assert.isTrue(args.size() == 1, "Number of arguments in mutation is not equal 1!");
        Arg items = args.get(0);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() == Integer.parseInt(items.getName()), "Number of values is not equal to number of items in mutation!");
        return valuesToExprList(values);
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

    private StmtList blocksToStmtList(List<Block> statementBolcks) {
        StmtList stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            stmtList.addStmt((Stmt) bToA(sb));
        }
        return stmtList;
    }

    private ExprList valuesToExprList(List<Value> values) {
        ExprList exprList = ExprList.make();
        for ( int i = 0; i < values.size(); i++ ) {
            exprList.addExpr((Expr) extractValue(values, "ADD" + i, (short) i));
        }
        return exprList;
    }

    private StmtList stmtsToStmtList(List<Statement> statements) {
        Statement statement;
        statement = statements.get(0);
        Assert.isTrue(statement.getName().equals("DO"));
        List<Block> statementBolcks = statement.getBlock();
        StmtList stmtList = blocksToStmtList(statementBolcks);
        return stmtList;
    }

    private String checkOperationType(Block block, String operationType) {
        String op = operationType;
        if ( block.getField().size() != 0 ) {
            op = getOperation(block, operationType);
        }
        return op;
    }

    private Phrase extractRepeatStatement(Block block, Phrase expr, String mode) {
        List<Statement> statements;
        StmtList stmtList;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= 1, "Number of statements is not equal or less to 1!");

        if ( statements.size() == 1 ) {
            stmtList = stmtsToStmtList(statements);
            RepeatStmt repeatStmt = RepeatStmt.make(RepeatStmt.Mode.valueOf(mode), (Expr) expr, stmtList);
            return repeatStmt;
        } else {
            stmtList = StmtList.make();
            RepeatStmt repeatStmt = RepeatStmt.make(RepeatStmt.Mode.valueOf(mode), (Expr) expr, stmtList);
            return repeatStmt;
        }
    }

    private Phrase extractVar(Block block) {
        List<Field> fields = block.getField();
        Assert.isTrue(fields.size() == 1, "Number of fields is not equal to 1");
        Field field = fields.get(0);
        Assert.isTrue(field.getName().equals("VAR"), "Field name is not equal to VAR");
        return Var.make(field.getValue());
    }

    private List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() == numOfValues, "Values size is not equal to " + numOfValues + "!");
        return values;
    }

    private Phrase extractValue(List<Value> values, String name, short exprNum) {
        Value value = values.get(exprNum);
        Assert.isTrue(value.getName().equals(name), "Value name is not equal to " + name + "!");
        return bToA(value.getBlock());
    }

    private List<Field> extractFields(Block block, short numOfFields) {
        List<Field> fields;
        fields = block.getField();
        Assert.isTrue(fields.size() == numOfFields, "Number of fields is not equal to " + numOfFields + "!");
        return fields;
    }

    private String extractField(List<Field> fields, String name, short fieldLocation) {
        Field field = fields.get(fieldLocation);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name + "!");
        return field.getName();
    }

    private List<Arg> extractArguments(Mutation mutation, short numOfArgs) {
        List<Arg> args = mutation.getArg();
        Assert.isTrue(args.size() == numOfArgs, "Number of fields is not equal to " + numOfArgs + "!");
        return args;
    }

    private String getOperation(Block block, String name) {
        List<Field> fields = block.getField();
        Assert.isTrue(fields.size() == 1, "Number of fields is not equal to 1");
        Field field = fields.get(0);
        Assert.isTrue(field.getName().equals(name), "Field name is not equal to " + name);
        return field.getValue();
    }

    public void blockToAST() {
        //        return this.type.blockToAST(blockAST, block)ToAST(this);
    }

}

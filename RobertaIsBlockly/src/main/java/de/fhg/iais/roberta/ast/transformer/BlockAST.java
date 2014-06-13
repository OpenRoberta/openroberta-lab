package de.fhg.iais.roberta.ast.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils.Null;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Category;
import de.fhg.iais.roberta.ast.syntax.aktion.Aktion;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.sensoren.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.DrehSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.Sensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.SteinSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensoren.UltraSSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.AktionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon.Flow;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.blockly.generated.Arg;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Project;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.dbc.Assert;

public class BlockAST {
    private final ArrayList<ArrayList<Phrase>> project = new ArrayList<ArrayList<Phrase>>();

    public void projectToAST(Project pr) {
        List<Instance> instances = pr.getInstance();
        for ( Instance instance : instances ) {
            this.project.add(instanceToAST(instance));
        }
    }

    private ArrayList<Phrase> instanceToAST(Instance instance) {
        List<Block> blocks = instance.getBlock();
        ArrayList<Phrase> phrases = new ArrayList<Phrase>();
        for ( Block block : blocks ) {
            phrases.add(bToA(block));
        }
        return phrases;
    }

    private Phrase bToA(Block block) {
        List<Arg> args;
        List<Value> values;
        List<Field> fields;
        List<Statement> statements;

        StmtList stmtList;

        Phrase left;
        Phrase right;
        Phrase expr;
        Phrase var;

        String mode;
        String port;

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
                return blockToBinaryExpr(block, new ExprParam("A", Integer.class), new ExprParam("B", Integer.class), "OP");

            case "logic_operation":
                return blockToBinaryExpr(block, new ExprParam("A", Boolean.class), new ExprParam("B", Boolean.class), "OP");

            case "logic_negate":
                return blockToUnaryExpr(block, new ExprParam("BOOL", Boolean.class), "NOT");

            case "logic_boolean":
                return blockToConst(block, "BOOL");

            case "logic_null":
                return NullConst.make();

            case "logic_ternary":
                //TODO need to be modified
                //                values = block.getValue();
                //                Assert.isTrue(values.size() <= 3, "Number of values is not less or equal to 3!");
                //                Phrase ifExpr = extractValue(values, new ExprParam("IF", Boolean.class));
                //                Phrase thenExpr = extractValue(values, new ExprParam("THEN", Stmt.class));
                //                Phrase elseExpr = extractValue(values, new ExprParam("ELSE", Stmt.class));
                //                return IfStmt.make((Expr) ifExpr, (StmtList) thenExpr, (StmtList) elseExpr);

                //Mathematik
            case "math_number":
                return blockToConst(block, "NUM");

            case "math_arithmetic":
                return blockToBinaryExpr(block, new ExprParam("A", Integer.class), new ExprParam("B", Integer.class), "OP");

            case "math_single":
                return blockToUnaryExpr(block, new ExprParam("NUM", Integer.class), "OP");

            case "math_trig":
                return blockToUnaryExpr(block, new ExprParam("NUM", Integer.class), "OP");

            case "math_constant":
                return blockToConst(block, "CONSTANT");

            case "math_number_property":
                args = extractArguments(block.getMutation(), (short) 1);
                Arg divisorInput = args.get(0);
                String op = extractOperation(block, "PROPERTY");
                if ( op.equals("DIVISIBLE_BY") ) {
                    Assert.isTrue(divisorInput.getName().equals("true"), "Divisor input is not equal to true!");
                    return blockToBinaryExpr(block, new ExprParam("NUMBER_TO_CHECK", Integer.class), new ExprParam("DIVISOR", Integer.class), op);
                } else {
                    Assert.isTrue(divisorInput.getName().equals("false"), "Divisor input is not equal to false!");
                    return blockToUnaryExpr(block, new ExprParam("NUMBER_TO_CHECK", Integer.class), op);
                }

            case "math_change":
                values = extractValues(block, (short) 1);
                left = extractVar(block);
                right = extractValue(values, new ExprParam("DELTA", Integer.class));
                return Binary.make(Binary.Op.MATH_CHANGE, (Expr) left, (Expr) right);

            case "math_round":
                return blockToUnaryExpr(block, new ExprParam("NUM", Integer.class), "OP");

            case "math_on_list":
                return blockToUnaryExpr(block, new ExprParam("LIST", ArrayList.class), "OP");

            case "math_modulo":
                return blockToBinaryExpr(block, new ExprParam("DIVIDEND", Integer.class), new ExprParam("DIVISOR", Integer.class), "MOD");

            case "math_constrain":
                values = extractValues(block, (short) 3);
                Phrase valueExpr = extractValue(values, new ExprParam("VALUE", Integer.class));
                Phrase lowExpr = extractValue(values, new ExprParam("LOW", Integer.class));
                Phrase maxExpr = Binary.make(Binary.Op.MAX, (Expr) lowExpr, (Expr) valueExpr);
                Phrase highExpr = extractValue(values, new ExprParam("HIGH", Integer.class));
                return Binary.make(Binary.Op.MIN, (Expr) maxExpr, (Expr) highExpr);

            case "math_random_integer":
                return blockToBinaryExpr(block, new ExprParam("FROM", Integer.class), new ExprParam("TO", Integer.class), "RANDOM_INTEGER");

            case "math_random_float":
                //TODO math_random_float

                //TEXT
            case "text":
                return blockToConst(block, "TEXT");

            case "text_join":
                return Unary.make(Unary.Op.TEXT_JOIN, blockToExprList(block, String.class));

            case "text_append":
                values = extractValues(block, (short) 1);
                left = extractVar(block);
                right = extractValue(values, new ExprParam("TEXT", String.class));
                return Binary.make(Binary.Op.TEXT_APPEND, (Expr) left, (Expr) right);

            case "text_length":
                return blockToUnaryExpr(block, new ExprParam("VALUE", String.class), "TEXT_LENGTH");

            case "text_isEmpty":
                return blockToUnaryExpr(block, new ExprParam("VALUE", String.class), "IS_EMPTY");

            case "text_indexOf":
                return blockToBinaryExpr(block, new ExprParam("VALUE", String.class), new ExprParam("FIND", String.class), "END");

            case "text_charAt":
                args = extractArguments(block.getMutation(), (short) 1);
                Arg atArg = args.get(0);
                if ( atArg.getName().equals("true") ) {
                    return blockToBinaryExpr(block, new ExprParam("VALUE", String.class), new ExprParam("AT", Integer.class), "WHERE");
                } else {
                    return blockToUnaryExpr(block, new ExprParam("VALUE", String.class), "WHERE");
                }

            case "text_getSubstring":
                //TODO Not finished yet
                args = extractArguments(block.getMutation(), (short) 2);
                String atArg1 = args.get(0).getName();
                String atArg2 = args.get(1).getName();
                values = block.getValue();
                if ( atArg1.equals("true") && atArg2.equals("true") ) {
                    Assert.isTrue(values.size() == 3);
                    //                    extractValue(values, "STRING", (short) 0);
                    //                    extractValue(values, "AT1", (short) 1);
                    //                    extractValue(values, "AT2", (short) 2);
                }

            case "text_change":
                return blockToUnaryExpr(block, new ExprParam("TEXT", String.class), "CASE");

            case "text_trim":
                return blockToUnaryExpr(block, new ExprParam("TEXT", String.class), "MODE");

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
                return blockToExprList(block, Null.class);

            case "lists_repeat":
                return blockToBinaryExpr(block, new ExprParam("ITEM", Null.class), new ExprParam("NUM", Integer.class), "LISTS_REPEAT");

            case "lists_length":
                return blockToUnaryExpr(block, new ExprParam("VALUE", ArrayList.class), "LISTS_LENGTH");

            case "lists_isEmpty":
                return blockToUnaryExpr(block, new ExprParam("VALUE", ArrayList.class), "IS_EMPTY");

            case "lists_indexOf":
                return blockToBinaryExpr(block, new ExprParam("VALUE", ArrayList.class), new ExprParam("FIND", Null.class), "END");

            case "lists_getIndex":
                //TODO not implemented

            case "lists_setIndex":
                //TODO not implemented

            case "lists_getSublist":
                //TODO not implemented

                //VARIABLEN
            case "variables_set":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam("VALUE", Null.class));
                return AssignStmt.make((Var) extractVar(block), (Expr) expr);

            case "variables_get":
                return extractVar(block);

                //KONTROLLE
            case "controls_if":
                List<Expr> exprsList = new ArrayList<Expr>();
                List<StmtList> stmtLists = new ArrayList<StmtList>();

                //TODO not finished 
                if ( block.getMutation() == null ) {
                    values = extractValues(block, (short) 1);
                    exprsList.add((Expr) extractValue(values, new ExprParam("IF0", Boolean.class)));
                    statements = extractStatements(block, (short) 1);
                    stmtLists.add(extractStatement(statements, "DO0"));
                    return IfStmt.make(exprsList, stmtLists);
                } else {
                    Mutation mutation = block.getMutation();
                    if ( mutation.getElseif() != null ) {
                        int _elseIf = mutation.getElseif().intValue();
                        List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
                        Assert.isTrue(valAndStmt.size() <= 2 * _elseIf + 2);
                        values = new ArrayList<Value>();
                        statements = new ArrayList<Statement>();
                        convertStmtValList(values, statements, valAndStmt);

                        for ( int i = 0; i < values.size(); i++ ) {
                            exprsList.add((Expr) extractValue(values, new ExprParam("IF" + i, Boolean.class)));
                            stmtLists.add(extractStatement(statements, "DO" + i));
                        }

                        return IfStmt.make(exprsList, stmtLists);
                    }
                }

            case "controls_whileUntil":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, "MODE", (short) 0);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam("BOOL", Boolean.class));
                return extractRepeatStatement(block, expr, mode);

            case "controls_for":
                var = extractVar(block);
                values = extractValues(block, (short) 3);
                ExprList exprList = ExprList.make();

                Phrase from = extractValue(values, new ExprParam("FROM", Integer.class));
                Phrase to = extractValue(values, new ExprParam("TO", Integer.class));
                Phrase by = extractValue(values, new ExprParam("BY", Integer.class));
                Binary exprAssig = Binary.make(Binary.Op.ASSIGNMENT, (Expr) var, (Expr) from);
                Binary exprCondition = Binary.make(Binary.Op.LTE, (Expr) var, (Expr) to);
                exprList.addExpr(exprAssig);
                exprList.addExpr(exprCondition);
                exprList.addExpr((Expr) by);
                return extractRepeatStatement(block, exprList, "FOR");

            case "controls_forEach":
                var = extractVar(block);
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam("LIST", ArrayList.class));
                Binary exprBinary = Binary.make(Binary.Op.IN, (Expr) var, (Expr) expr);
                return extractRepeatStatement(block, exprBinary, "FOR_EACH");

            case "controls_flow_statements":
                fields = extractFields(block, (short) 1);
                mode = extractField(fields, "FLOW", (short) 0);
                return StmtFlowCon.make(Flow.valueOf(mode));

            case "controls_repeat_ext":
                values = extractValues(block, (short) 1);
                expr = extractValue(values, new ExprParam("TIMES", Integer.class));
                return extractRepeatStatement(block, expr, "TIMES");

            default:
                throw new RuntimeException("Invalid Block: " + block.getType());
        }
    }

    private void convertStmtValList(List<Value> values, List<Statement> statements, List<Object> valAndStmt) {
        for ( int i = 0; i < valAndStmt.size(); i++ ) {
            Object ob = valAndStmt.get(i);
            if ( ob.getClass() == Value.class ) {
                values.add((Value) ob);
            } else {
                statements.add((Statement) ob);
            }
        }
    }

    private Phrase blockToUnaryExpr(Block block, ExprParam exprParam, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 1);
        Phrase expr = extractValue(values, exprParam);
        return Unary.make(Unary.Op.valueOf(op), (Expr) expr);
    }

    private Binary blockToBinaryExpr(Block block, ExprParam leftExpr, ExprParam rightExpr, String operationType) {
        String op = getOperation(block, operationType);
        List<Value> values = extractValues(block, (short) 2);
        Phrase left = extractValue(values, leftExpr);
        Phrase right = extractValue(values, leftExpr);
        return Binary.make(Binary.Op.get(op), (Expr) left, (Expr) right);
    }

    private ExprList blockToExprList(Block block, Class<?> defVal) {
        List<Arg> args = block.getMutation().getArg();
        Assert.isTrue(args.size() == 1, "Number of arguments in mutation is not equal 1!");
        Arg items = args.get(0);
        List<Value> values = block.getValue();
        Assert.isTrue(values.size() <= Integer.parseInt(items.getName()), "Number of values is not less or equal to number of items in mutation!");
        return valuesToExprList(values, defVal);
    }

    private Phrase blockToConst(Block block, String type) {
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, type, (short) 0);
        switch ( type ) {
            case "BOOL":
                return BoolConst.make(Boolean.parseBoolean(field.toLowerCase()));
            case "NUM":
                return NumConst.make(field);
            case "TEXT":
                return StringConst.make(field);
            case "CONSTANT":
                return MathConst.make(MathConst.Const.valueOf(field));
            default:
                throw new RuntimeException("Invalid type constant!");
        }
    }

    private StmtList blocksToStmtList(List<Block> statementBolcks) {
        StmtList stmtList = StmtList.make();
        for ( Block sb : statementBolcks ) {
            convertPhraseToStmt(stmtList, sb);
        }
        stmtList.setReadOnly();
        return stmtList;
    }

    private void convertPhraseToStmt(StmtList stmtList, Block sb) {
        Phrase p = bToA(sb);
        Stmt stmt;
        if ( p.getKind().getCategory() == Category.EXPR ) {
            stmt = ExprStmt.make((Expr) p);
        } else if ( p.getKind().getCategory() == Category.AKTOR ) {
            stmt = AktionStmt.make((Aktion) p);
        } else if ( p.getKind().getCategory() == Category.SENSOR ) {
            stmt = SensorStmt.make((Sensor) p);
        } else {
            stmt = (Stmt) p;
        }
        stmtList.addStmt(stmt);
    }

    private ExprList valuesToExprList(List<Value> values, Class<?> defVal) {
        ExprList exprList = ExprList.make();
        for ( int i = 0; i < values.size(); i++ ) {
            exprList.addExpr((Expr) extractValue(values, new ExprParam("ADD" + i, defVal)));
        }
        exprList.setReadOnly();
        return exprList;
    }

    private String getOperation(Block block, String operationType) {
        String op = operationType;
        if ( block.getField().size() != 0 ) {
            op = extractOperation(block, operationType);
        }
        return op;
    }

    private Phrase extractRepeatStatement(Block block, Phrase expr, String mode) {
        List<Statement> statements = extractStatements(block, (short) 1);
        StmtList stmtList = extractStatement(statements, "DO");
        return RepeatStmt.make(RepeatStmt.Mode.valueOf(mode), (Expr) expr, stmtList);
    }

    private Phrase extractVar(Block block) {
        List<Field> fields = extractFields(block, (short) 1);
        String field = extractField(fields, "VAR", (short) 0);
        return Var.make(field);
    }

    private List<Value> extractValues(Block block, short numOfValues) {
        List<Value> values;
        values = block.getValue();
        Assert.isTrue(values.size() <= numOfValues, "Values size is not less or equal to " + numOfValues + "!");
        return values;
    }

    private Phrase extractValue(List<Value> values, ExprParam param) {
        for ( Value value : values ) {
            if ( value.getName().equals(param.getName()) ) {
                return bToA(value.getBlock());
            }
        }
        return EmptyExpr.make(param.getDefaultValue());
    }

    private List<Statement> extractStatements(Block block, short numOfStatements) {
        List<Statement> statements;
        statements = block.getStatement();
        Assert.isTrue(statements.size() <= numOfStatements, "Statements size is not less or equal to " + numOfStatements + "!");
        return statements;
    }

    private StmtList extractStatement(List<Statement> statements, String stmtName) {
        StmtList stmtList = StmtList.make();
        for ( Statement statement : statements ) {
            if ( statement.getName().equals(stmtName) ) {
                return blocksToStmtList(statement.getBlock());
            }
        }
        stmtList.setReadOnly();
        return stmtList;
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
        return field.getValue();
    }

    private List<Arg> extractArguments(Mutation mutation, short numOfArgs) {
        List<Arg> args = mutation.getArg();
        Assert.isTrue(args.size() == numOfArgs, "Number of fields is not equal to " + numOfArgs + "!");
        return args;
    }

    private String extractOperation(Block block, String name) {
        List<Field> fields = extractFields(block, (short) 1);
        String operation = extractField(fields, name, (short) 0);
        return operation;
    }

    @Override
    public String toString() {
        return "BlockAST [project=" + this.project + "]";
    }
}

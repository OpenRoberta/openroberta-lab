package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class Ast2Ev3SimVisitorTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void test() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"x\", createConstant(CONST.NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(CONST.NUMBER, \"y\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(CONST.NUM_CONST, 80), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 39));\n"
                + "var stmt3 = createRepeatStmt('loop_1', CONST.WHILE, createBinaryExpr(CONST.LT, createVarReference(CONST.NUMBER, \"y\"), createConstant(CONST.NUM_CONST, 5)), "
                + "[createAssignStmt(\"x\", createBinaryExpr(CONST.MULTIPLY, createVarReference(CONST.NUMBER, \"x\"), createVarReference(CONST.NUMBER, \"x\"))), "
                + "createAssignStmt(\"y\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"y\"), createConstant(CONST.NUM_CONST, 1)))]);\n"
                + "var stmt4 = createAssignStmt(\"x\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 10)));\n"
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var stmt6 = createDriveAction(createConstant(CONST.NUM_CONST, -50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.ULTRASONIC, CONST.DISTANCE), createConstant(CONST.NUM_CONST, 30))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"x\", createConstant(CONST.NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(CONST.NUMBER, \"y\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(CONST.NUM_CONST, 80), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 39));\n"
                + "var stmt3 = createAssignStmt(\"x\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 10)));\n"
                + "var stmt4 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var stmt5 = createDriveAction(createConstant(CONST.NUM_CONST, -50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt6 = createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.RIGHT, createConstant(CONST.NUM_CONST, 30));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.ULTRASONIC, CONST.DISTANCE), createConstant(CONST.NUM_CONST, 30))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator1.xml");
    }

    @Test
    public void test2() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"x\", createConstant(CONST.NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(CONST.NUMBER, \"y\", createBinaryExpr(CONST.ADD, createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt3 = createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.RIGHT);\n"
                + "var stmt4 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt5 = createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.LEFT);\n"
                + "var stmt6 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createTurnLight(CONST.GREEN, CONST.ON);\n"
                + "var stmt3 = createStopDrive();\n"
                + "var stmt4 = createStatusLight(CONST.RESET);\n"
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(CONST.NEQ, createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 6)), createBinaryExpr(CONST.GTE, createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 6))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD)], [createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD)]], []);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(CONST.LTE, createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD)]], [createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.RIGHT, createConstant(CONST.NUM_CONST, 80))]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createRepeatStmt('loop_1', CONST.TIMES, [createVarReference(CONST.NUMBER_INT, \"k0\"), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 10), createConstant(CONST.NUM_CONST, 1)], [createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20)), createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD, createConstant(CONST.NUM_CONST, 30))]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createBinaryExpr(CONST.OR, createConstant(CONST.BOOL_CONST, false), createConstant(CONST.BOOL_CONST, true)))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0))], [[createIfStmt([createBinaryExpr(CONST.NEQ, createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 1)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 1))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD)], [createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD), createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.RIGHT)]], [])]], [createStopDrive()]);\n"
                + "var stmt3 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.ULTRASONIC, CONST.DISTANCE), createConstant(CONST.NUM_CONST, 30))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD)]]), createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.ULTRASONIC, CONST.DISTANCE), createConstant(CONST.NUM_CONST, 30))], [[createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])])]])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator8.xml");
    }

    @Test
    public void test9() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"
                + "var stmt1 = createDriveAction(createConstant(CONST.NUM_CONST, 80), CONST.BACKWARD);\n"
                + "var stmt2 = createTurnAction(createConstant(CONST.NUM_CONST, 50), CONST.RIGHT);\n"
                + "var stmt3 = createTurnAction(createConstant(CONST.NUM_CONST, 90), CONST.LEFT);\n"
                + "var stmt4 = createStopDrive();\n"
                + "var stmt5 = createTurnAction(createConstant(CONST.NUM_CONST, 80), CONST.RIGHT, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt6 = createTurnAction(createConstant(CONST.NUM_CONST, 80), CONST.LEFT, createConstant(CONST.NUM_CONST, 15));\n"
                + "var stmt7 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 26));\n"
                + "var stmt8 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD, createConstant(CONST.NUM_CONST, 33));\n"
                + "var stmt9 = createTurnLight(CONST.GREEN, CONST.ON);\n"
                + "var stmt10 = createStatusLight(CONST.OFF);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator9.xml");

    }

    @Test
    public void test10() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.COLOR, \"variablenName\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.NONE));\n"
                + "var stmt1 = createVarDeclaration(CONST.COLOR, \"variablenName2\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.BLACK));\n"
                + "var stmt2 = createVarDeclaration(CONST.COLOR, \"variablenName3\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.BLUE));\n"
                + "var stmt3 = createVarDeclaration(CONST.COLOR, \"variablenName4\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.GREEN));\n"
                + "var stmt4 = createVarDeclaration(CONST.COLOR, \"variablenName5\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.YELLOW));\n"
                + "var stmt5 = createVarDeclaration(CONST.COLOR, \"variablenName6\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.RED));\n"
                + "var stmt6 = createVarDeclaration(CONST.COLOR, \"variablenName7\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.WHITE));\n"
                + "var stmt7 = createVarDeclaration(CONST.COLOR, \"variablenName8\", createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.BROWN));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator10.xml");

    }

    @Test
    public void test11() throws Exception {
        String a =
            "var stmt0 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.COLOR, CONST.RED), createConstant(CONST.NUM_CONST, 30))], [])]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.COLOR, CONST.COLOUR), createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.RED))], [])]);\n"
                + "var stmt2 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.COLOR, CONST.COLOUR), createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.GREEN))], [])]);\n"
                + "var stmt3 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.LT, createGetSample(CONST.COLOR, CONST.RED), createConstant(CONST.NUM_CONST, 45))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator11.xml");
    }

    @Test
    public void test12() throws Exception {

        String a =
            "var stmt0 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, false))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD)]]), createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD, createConstant(CONST.NUM_CONST, 20))]])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator12.xml");

    }

    @Test
    public void test13() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true)), createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, false))], [[createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD)], [createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD, createConstant(CONST.NUM_CONST, 20))]], []);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator13.xml");

    }

    @Test
    public void test14() throws Exception {

        String a =
            "var stmt0 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_RIGHT);\n"
                + "var stmt1 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_LEFT);\n"
                + "var stmt2 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_RIGHT, createDuration(CONST.ROTATIONS, createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt3 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_LEFT, createDuration(CONST.DEGREE, createConstant(CONST.NUM_CONST, 1)));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator14.xml");

    }

    @Test
    public void test15() throws Exception {

        String a =
            "var stmt0 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_RIGHT, createDuration(CONST.ROTATIONS, createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt1 = createStopMotorAction(CONST.MOTOR_RIGHT);\n"
                + "var stmt2 = createStopMotorAction(CONST.MOTOR_LEFT);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator15.xml");

    }

    @Test
    public void test16() throws Exception {
        String a =
            "var stmt0 = createMotorOnAction(createUnaryExpr(CONST.NEG, createConstant(CONST.NUM_CONST, 30)), CONST.MOTOR_RIGHT, createDuration(CONST.ROTATIONS, createConstant(CONST.NUM_CONST, 1)));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator16.xml");

    }

    @Test
    public void test17() throws Exception {
        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ROOT', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt1 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ABS', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt2 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('LN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt3 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('LOG10', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt4 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('EXP', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt5 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('POW10', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt6 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('SIN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt7 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('COS', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt8 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('TAN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt9 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ASIN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt10 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ACOS', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt11 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ATAN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt12 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ROUND', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt13 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ROUNDUP', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var stmt14 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createSingleFunction('ROUNDDOWN', createConstant(CONST.NUM_CONST, 20)));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10,stmt11,stmt12,stmt13,stmt14]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator17.xml");

    }

    @Test
    public void test18() throws Exception {
        String a =
            "var stmt0 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('PI'));\n"
                + "var stmt1 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('E'));\n"
                + "var stmt2 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('GOLDEN_RATIO'));\n"
                + "var stmt3 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('SQRT2'));\n"
                + "var stmt4 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('SQRT1_2'));\n"
                + "var stmt5 = createDriveAction(createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createMathConstant('INFINITY'));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator18.xml");

    }

    @Test
    public void visitCurveAction_ByDefaultWithoutDistance_CreateCurveAction() throws Exception {
        String a =
            "var stmt0 = createCurveAction(createConstant(CONST.NUM_CONST, 20), createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD);\n"
                + "var stmt1 = createCurveAction(createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD);\n"

                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator30.xml");

    }

    @Test
    public void visitSoundSensor_ByDefault_CreateGetSample() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetSample(CONST.SOUND), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator32.xml");

    }

    @Test
    public void visitCurveAction_ByDefaultWhithDistance_CreateCurveAction() throws Exception {
        String a =
            "var stmt0 = createCurveAction(createConstant(CONST.NUM_CONST, 20), createConstant(CONST.NUM_CONST, 50), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20));\n"
                + "var stmt1 = createCurveAction(createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 50), CONST.BACKWARD, createConstant(CONST.NUM_CONST, 20));\n"

                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator31.xml");

    }

    @Test
    public void test19() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createConstant(CONST.STRING_CONST, 'Hallo'), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator19.xml");

    }

    @Test
    public void test20() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createMathPropFunct('EVEN', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createMathPropFunct('ODD', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createMathPropFunct('PRIME', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt3 = createShowTextAction(createMathPropFunct('WHOLE', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt4 = createShowTextAction(createMathPropFunct('POSITIVE', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt5 = createShowTextAction(createMathPropFunct('NEGATIVE', createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt6 = createShowTextAction(createMathPropFunct('DIVISIBLE_BY', createConstant(CONST.NUM_CONST, 3), createConstant(CONST.NUM_CONST, 3)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator20.xml");

    }

    @Test
    public void test21() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createMathConstrainFunct(createConstant(CONST.NUM_CONST, 200), createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 100)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createMathConstrainFunct(createConstant(CONST.NUM_CONST, -200), createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 100)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createMathConstrainFunct(createConstant(CONST.NUM_CONST, 20), createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 100)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator21.xml");

    }

    @Test
    public void test22() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createRandInt(createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 100)), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createRandDouble(), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator22.xml");

    }

    @Test
    public void test23() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"item\", createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createVarDeclaration(CONST.STRING, \"item2\", createConstant(CONST.STRING_CONST, 'cc'));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator23.xml");

    }

    @Test
    public void test24() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetMotorPower(CONST.MOTOR_RIGHT), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator24.xml");

    }

    @Test
    public void test25() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetSampleEncoderSensor(CONST.MOTOR_RIGHT, CONST.ROTATION), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createGetSampleEncoderSensor(CONST.MOTOR_RIGHT, CONST.DEGREE), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createGetSampleEncoderSensor(CONST.MOTOR_RIGHT, CONST.DISTANCE), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt3 = createResetEncoderSensor(CONST.MOTOR_LEFT);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator25.xml");

    }

    @Test
    public void test26() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetGyroSensorSample(CONST.GYRO, CONST.ANGLE), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createGetGyroSensorSample(CONST.GYRO, CONST.RATE), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt2 = createResetGyroSensor();\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator26.xml");

    }

    @Test
    public void createGetTimerSensorSample() throws Exception {
        String a = "createGetSample(CONST.TIMER, 'timer1')" + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/sensors/sensor_getSampleTimer.xml");
    }

    @Test
    public void createResetTimerSensor() throws Exception {
        String a = "var stmt0 = createResetTimer('timer1');\n" + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/sensors/sensor_resetTimer.xml");
    }

    @Test
    public void test27() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createCreateListWith(CONST.ARRAY_STRING, [createConstant(CONST.STRING_CONST, 'a'), createConstant(CONST.STRING_CONST, 'v'), createConstant(CONST.STRING_CONST, 'b\')]), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator27.xml");

    }

    @Test
    public void test28() throws Exception {
        String a =
            "var stmt0 = createDebugAction();\n"
                + "var stmt1 = createShowPictureAction('OLDGLASSES', createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 11));\n"
                + "var stmt2 = createShowPictureAction('EYESOPEN', createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 11));\n"
                + "var stmt3 = createClearDisplayAction();\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3]};";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator28.xml");

    }

    @Test
    public void test29() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"variablenName\", createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createMathChange(createVarReference(CONST.NUMBER, \"variablenName\"), createConstant(CONST.NUM_CONST, 1));"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCodeIsOk(a, "/syntax/math/math_change.xml");
    }

    @Test
    public void createListWithItem() throws Exception {
        String a =
            "createCreateListWithItem(createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 5))" + "var blocklyProgram = {'programStmts': []};";

        assertCodeIsOk(a, "/syntax/lists/lists_create_with_item.xml");
    }

    @Test
    public void createListLength() throws Exception {
        String a =
            "createListLength(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0.1), createConstant(CONST.NUM_CONST, 0.0), createConstant(CONST.NUM_CONST, 0)]))"
                + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }

    @Test
    public void createListIsEmpty() throws Exception {
        String a =
            "createListIsEmpty(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]))"
                + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml");
    }

    @Test
    public void createListFindElementFirst() throws Exception {
        String a =
            "var stmt0 = createMotorOnAction(createListFindItem(CONST.FIRST, createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), createConstant(CONST.NUM_CONST, 30)), CONST.MOTOR_RIGHT);\n"
                + "var stmt1 = createMotorOnAction(createListFindItem(CONST.LAST, createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), createConstant(CONST.NUM_CONST, 30)), CONST.MOTOR_RIGHT);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";
        assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

    @Test
    public void createListsIndexGetFromStart() throws Exception {
        String a =
            "var stmt0 = createDriveAction(createListsGetIndex(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), CONST.GET, CONST.FROM_START, createConstant(CONST.NUM_CONST, 0)), CONST.FOREWARD);\n"
                + "var stmt1 = createDriveAction(createListsGetIndex(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), CONST.GET_REMOVE, CONST.FIRST), CONST.FOREWARD);\n"
                + "var stmt2 = createListsGetIndexStmt(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), CONST.REMOVE, CONST.LAST);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2]};";
        assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }

    @Test
    public void createListsSetFromStart() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 55), createConstant(CONST.NUM_CONST, 66), createConstant(CONST.NUM_CONST, 11)]), CONST.SET, createConstant(CONST.NUM_CONST, 99), CONST.FROM_START, createConstant(CONST.NUM_CONST, 1));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index.xml");
    }

    @Test
    public void createListsInsertFromStart() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 55), createConstant(CONST.NUM_CONST, 66), createConstant(CONST.NUM_CONST, 11)]), CONST.INSERT, createConstant(CONST.NUM_CONST, 99), CONST.FROM_START, createConstant(CONST.NUM_CONST, 1));\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index1.xml");
    }

    @Test
    public void createListsInsertRandom() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 55), createConstant(CONST.NUM_CONST, 66), createConstant(CONST.NUM_CONST, 11)]), CONST.INSERT, createConstant(CONST.NUM_CONST, 99), CONST.RANDOM);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index2.xml");
    }

    @Test
    public void createGetSubListFromStartAndFromEnd() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.ARRAY_NUMBER, \"Element\", createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]));\n"
                + "var stmt1 = createAssignStmt(\"Element\", createGetSubList({list: createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), where1: CONST.FROM_START, at1: createConstant(CONST.NUM_CONST, 0), where2: CONST.FROM_START, at2: createConstant(CONST.NUM_CONST, 0)}));\n"
                + "var stmt2 = createAssignStmt(\"Element\", createGetSubList({list: createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), where1: CONST.FROM_END, at1: createConstant(CONST.NUM_CONST, 0), where2: CONST.FROM_END, at2: createConstant(CONST.NUM_CONST, 0)}));\n"
                + "var stmt3 = createAssignStmt(\"Element\", createGetSubList({list: createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), where1: CONST.FROM_START, at1: createConstant(CONST.NUM_CONST, 0), where2: CONST.LAST}));\n"
                + "var stmt4 = createAssignStmt(\"Element\", createGetSubList({list: createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)]), where1: CONST.FIRST, where2: CONST.FROM_START, at2: createConstant(CONST.NUM_CONST, 0)}));\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4]};";
        assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

    @Test
    public void createMathOnList() throws Exception {
        String a =
            "createMathOnList(CONST.SUM, )"
                + "createMathOnList(CONST.MIN, )"
                + "createMathOnList(CONST.MAX, )"
                + "createMathOnList(CONST.AVERAGE, )"
                + "createMathOnList(CONST.MEDIAN, )"
                + "createMathOnList(CONST.MODE, )"
                + "createMathOnList(CONST.STD_DEV, )"
                + "createMathOnList(CONST.RANDOM, )"
                + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/math/math_on_list.xml");
    }

    @Test
    public void createTextJoin() throws Exception {
        String a =
            "createTextJoin(["
                + "createConstant(CONST.NUM_CONST, 0), "
                + "createConstant(CONST.NUM_CONST, 0), "
                + "createConstant(CONST.STRING_CONST, 'a'), "
                + "createConstant(CONST.STRING_CONST, 'b'), "
                + "createConstant(CONST.BOOL_CONST, true), "
                + "createGetSample(CONST.TOUCH)"
                + "])"
                + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/text/text_join.xml");
    }

    @Test
    public void createTernary() throws Exception {
        String a =
            "createTernaryExpr("
                + "createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)), "
                + "createConstant(CONST.STRING_CONST, '2'), "
                + "createConstant(CONST.STRING_CONST, '3')"
                + ")"
                + "var blocklyProgram = {'programStmts': []};";
        assertCodeIsOk(a, "/syntax/logic/logic_ternary.xml");
    }

    @Test
    public void createForEachStmt() throws Exception {
        String a =
            "var stmt0 = createRepeatStmt('loop_1', "
                + "CONST.FOR_EACH, "
                + "createBinaryExpr(CONST.IN, createVarDeclaration(CONST.NUMBER, \"item\", createConstant(CONST.NUM_CONST, 0)), "
                + "createCreateListWith(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0)])), "
                + "[createShowTextAction(createVarReference(CONST.NUMBER, \"item\"), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0))]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/control/repeat_stmt_for_each3.xml");
    }

    @Test
    public void createForStmt() throws Exception {
        String a =
            "var stmt0 = createRepeatStmt('loop_1', "
                + "CONST.FOR, ["
                + "createVarReference(CONST.NUMBER, \"i\"), createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 10), createConstant(CONST.NUM_CONST, 1)], "
                + "[createTextAppend(createVarReference(CONST.STRING, \"item\"), createConstant(CONST.STRING_CONST, 'kllk'))]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/control/repeat_stmt_for.xml");
    }

    @Test
    public void createMethodVoidTwoParameters() throws Exception {
        String a =
            "var stmt0 = createMotorOnAction(createConstant(CONST.NUM_CONST, 30), CONST.MOTOR_RIGHT, createDuration(CONST.ROTATIONS, createConstant(CONST.NUM_CONST, 1)));\n"
                + "var stmt1 = createMethodCallVoid('macheEtwas', [createAssignMethodParameter(\"x\", createConstant(CONST.NUM_CONST, 10)), createAssignMethodParameter(\"x2\", createConstant(CONST.NUM_CONST, 10))]);\n"
                + "var method0 = createMethodVoid('macheEtwas', [createVarDeclaration(CONST.NUMBER, \"x\", createConstant(CONST.NUM_CONST, 0)), createVarDeclaration(CONST.NUMBER, \"x2\", createConstant(CONST.NUM_CONST, 0))], [createShowPictureAction('OLDGLASSES', createVarReference(CONST.NUMBER, \"x\"), createVarReference(CONST.NUMBER, \"x2\"))]);\n"
                + "var blocklyProgram = {'programMethods': [method0], 'programStmts': [stmt0,stmt1]};";
        assertCodeIsOk(a, "/syntax/methods/method_void_1.xml");
    }

    @Test
    public void createMethodVoidNoParameters() throws Exception {
        String a =
            "var stmt0 = createMethodCallVoid('test', []);\n"
                + "var method0 = createMethodVoid('test', [], [createTurnLight(CONST.GREEN, CONST.ON)]);\n"
                + "var blocklyProgram = {'programMethods': [method0], 'programStmts': [stmt0]};";
        assertCodeIsOk(a, "/syntax/methods/method_void_2.xml");
    }

    @Test
    public void createMethodVoidMultipleMethods() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.NUMBER, \"variablenName\", createConstant(CONST.NUM_CONST, 0));\n"
                + "var stmt1 = createVarDeclaration(CONST.BOOLEAN, \"variablenName2\", createConstant(CONST.BOOL_CONST, true));\n"
                + "var stmt2 = createMethodCallVoid('test1', [createAssignMethodParameter(\"x\", createConstant(CONST.NUM_CONST, 0)), createAssignMethodParameter(\"x2\", createConstant(CONST.NUM_CONST, 0))]);\n"
                + "var stmt3 = createMethodCallVoid('test2', []);\n"
                + "var method0 = createMethodVoid('test1', [createVarDeclaration(CONST.NUMBER, \"x\", createConstant(CONST.NUM_CONST, 0)), createVarDeclaration(CONST.NUMBER, \"x2\", createConstant(CONST.NUM_CONST, 0))], [createShowTextAction(createConstant(CONST.STRING_CONST, 'Hallo'), createVarReference(CONST.NUMBER, \"x\"), createVarReference(CONST.NUMBER, \"x2\"))]);\n"
                + "var method1 = createMethodVoid('test2', [], [createIfReturn(createVarReference(CONST.BOOLEAN, \"variablenName2\"), CONST.VOID, createConstant(CONST.NULL_CONST, null)), createTurnLight(CONST.GREEN, CONST.ON)]);\n"
                + "var blocklyProgram = {'programMethods': [method0,method1], 'programStmts': [stmt0,stmt1,stmt2,stmt3]};";
        assertCodeIsOk(a, "/syntax/methods/method_void_3.xml");
    }

    @Test
    public void createMethodReturnNumber() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.ARRAY_STRING, \"variablenName\", createCreateListWith(CONST.ARRAY_STRING, [createConstant(CONST.STRING_CONST, 'a'), createConstant(CONST.STRING_CONST, 'b'), createConstant(CONST.STRING_CONST, 'c')]));\n"
                + "var stmt1 = createShowTextAction(createMethodCallReturn('test', [createAssignMethodParameter(\"x\", createConstant(CONST.NUM_CONST, 0)), createAssignMethodParameter(\"x2\", createVarReference(CONST.ARRAY_STRING, \"variablenName\"))]), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var method0 = createMethodReturn('test', [createShowTextAction(createVarReference(CONST.ARRAY_STRING, \"x2\"), createVarReference(CONST.NUMBER, \"x\"), createConstant(CONST.NUM_CONST, 0))], CONST.NUMBER, createVarReference(CONST.NUMBER, \"x\"));\n"

                + "var blocklyProgram = {'programMethods': [method0], 'programStmts': [stmt0,stmt1]};";
        assertCodeIsOk(a, "/syntax/methods/method_return_1.xml");
    }

    @Test
    public void createMethodReturnColor() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(CONST.ARRAY_STRING, \"variablenName\", createCreateListWith(CONST.ARRAY_STRING, [createConstant(CONST.STRING_CONST, 'a'), createConstant(CONST.STRING_CONST, 'b'), createConstant(CONST.STRING_CONST, 'c')]));\n"
                + "var stmt1 = createShowTextAction(createMethodCallReturn('test', []), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0));\n"
                + "var method0 = createMethodReturn('test', [createShowTextAction(createVarReference(CONST.ARRAY_STRING, \"variablenName\"), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 0))], CONST.COLOR, createConstant(CONST.COLOR_CONST, CONST.COLOR_ENUM.NONE));\n"

                + "var blocklyProgram = {'programMethods': [method0], 'programStmts': [stmt0,stmt1]};";
        assertCodeIsOk(a, "/syntax/methods/method_return_2.xml");
    }

    @Test
    public void createTextAppend() throws Exception {
        String a =
            "var stmt0 = var stmt1 = createTextAppend(createVarReference(CONST.STRING, \"item\"), createGetSample(CONST.TOUCH));\n"
                + ";var stmt2 = var stmt3 = createTextAppend(createVarReference(CONST.STRING, \"item\"), createConstant(CONST.NUM_CONST, 0));\n"
                + ";var stmt4 = var stmt5 = createTextAppend(createVarReference(CONST.STRING, \"item\"), createConstant(CONST.STRING_CONST, 'aaa'));\n"
                + ";var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5]};";
        assertCodeIsOk(a, "/syntax/text/text_append.xml");
    }

    @Test
    public void loopWithBreakAndContinue() throws Exception {
        String a =
            ""
                + "var stmt0 = createDebugAction();\n"
                + "var stmt1 = createRepeatStmt('loop_1', CONST.FOREVER, createConstant(CONST.BOOL_CONST, true), [createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 12))], [[createStmtFlowControl('loop_1', CONST.BREAK)], [createStmtFlowControl('loop_1', CONST.CONTINUE)]], [])]);\n"
                + "var stmt2 = createRepeatStmt('loop_2', CONST.FOR, [createVarReference(CONST.NUMBER, \"i\"), createConstant(CONST.NUM_CONST, 1), createConstant(CONST.NUM_CONST, 10), createConstant(CONST.NUM_CONST, 1)], [createDriveAction(createConstant(CONST.NUM_CONST, 30), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20)), createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 12))], [[createStmtFlowControl('loop_2', CONST.CONTINUE)], [createStmtFlowControl('loop_2', CONST.BREAK)]], [])]);\n"
                + "var stmt3 = createRepeatStmt('loop_3', CONST.FOR_EACH, createBinaryExpr(CONST.IN, createVarDeclaration(CONST.NUMBER, \"item\", createConstant(CONST.NUM_CONST, 0)), createVarReference(CONST.ARRAY_NUMBER, \"item2\")), [createDriveAction(createConstant(CONST.NUM_CONST, 30), CONST.FOREWARD, createConstant(CONST.NUM_CONST, 20)), createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20))], [[createStmtFlowControl('loop_3', CONST.CONTINUE)], [createStmtFlowControl('loop_3', CONST.BREAK)]], [])]);\n"
                + "var stmt4 = createRepeatStmt('loop_4', CONST.WHILE, createConstant(CONST.BOOL_CONST, true), [createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20))], [[createStmtFlowControl('loop_4', CONST.CONTINUE)], [createStmtFlowControl('loop_4', CONST.BREAK)]], [])]);\n"
                + "var stmt5 = createRepeatStmt('loop_5', CONST.TIMES, [createVarReference(CONST.NUMBER_INT, \"k0\"), createConstant(CONST.NUM_CONST, 0), createConstant(CONST.NUM_CONST, 10), createConstant(CONST.NUM_CONST, 1)], [createIfStmt([createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20)), createBinaryExpr(CONST.EQ, createConstant(CONST.NUM_CONST, 30), createConstant(CONST.NUM_CONST, 20))], [[createStmtFlowControl('loop_5', CONST.BREAK)], [createStmtFlowControl('loop_5', CONST.CONTINUE)]], [])]);\n"
                + "var stmt6 = createWaitStmt([createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], []), createIfStmt([createBinaryExpr(CONST.EQ, createGetSample(CONST.TOUCH), createConstant(CONST.BOOL_CONST, true))], [])]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6]};";

        assertCodeIsOk(a, "/syntax/code_generator/java/loops_with_break_and_continue.xml");
    }

    @Test
    public void loopInLoop() throws Exception {
        String a =
            ""
                + "var stmt0 = createRepeatStmt('loop_1', CONST.FOREVER, createConstant(CONST.BOOL_CONST, true),"
                + "                             [createRepeatStmt('loop_2', CONST.FOREVER, createConstant(CONST.BOOL_CONST, true),"
                + "                                               [createStmtFlowControl('loop_2', CONST.BREAK)]),"
                + "                              createStmtFlowControl('loop_1', CONST.BREAK)"
                + "                             ]);\n"
                + "var stmt1 = createRepeatStmt('loop_3', CONST.FOREVER, createConstant(CONST.BOOL_CONST, true),"
                + "                             [createStmtFlowControl('loop_3', CONST.BREAK)]);\n"
                + "var blocklyProgram = {'programStmts': [stmt0,stmt1]};";

        assertCondensedCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_loop_in_loop.xml");
    }

    private void assertCondensedCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateJavaScript(fileName).replaceAll("\\s+", ""));
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a, this.h.generateJavaScript(fileName));
    }
}

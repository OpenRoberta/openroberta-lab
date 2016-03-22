package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class AstToLejosJavaScriptVisitorTest {

    @BeforeClass
    public static void setupConfigurationForAllTests() {

    }

    @Test
    public void test() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"x\", createConstant(NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(NUMERIC, \"y\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 80), FOREWARD, createConstant(NUM_CONST, 39));\n"
                + "var stmt3 = createRepeatStmt(WHILE, createBinaryExpr(LT, createVarReference(NUMERIC, \"y\"), createConstant(NUM_CONST, 5)), "
                + "[createAssignStmt(\"x\", createBinaryExpr(MULTIPLY, createVarReference(NUMERIC, \"x\"), createVarReference(NUMERIC, \"x\"))), "
                + "createAssignStmt(\"y\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"y\"), createConstant(NUM_CONST, 1)))]);\n"
                + "var stmt4 = createAssignStmt(\"x\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 10)));\n"
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var stmt6 = createDriveAction(createConstant(NUM_CONST, -50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC, DISTANCE), createConstant(NUM_CONST, 30))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"x\", createConstant(NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(NUMERIC, \"y\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 80), FOREWARD, createConstant(NUM_CONST, 39));\n"
                + "var stmt3 = createAssignStmt(\"x\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 10)));\n"
                + "var stmt4 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var stmt5 = createDriveAction(createConstant(NUM_CONST, -50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt6 = createTurnAction(createConstant(NUM_CONST, 50), RIGHT, createConstant(NUM_CONST, 30));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC, DISTANCE), createConstant(NUM_CONST, 30))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator1.xml");
    }

    @Test
    public void test2() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"x\", createConstant(NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(NUMERIC, \"y\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt3 = createTurnAction(createConstant(NUM_CONST, 50), RIGHT);\n"
                + "var stmt4 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt5 = createTurnAction(createConstant(NUM_CONST, 50), LEFT);\n"
                + "var stmt6 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createTurnLight(GREEN, ON);\n"
                + "var stmt3 = createStopDrive();\n"
                + "var stmt4 = createStatusLight(RESET);\n"
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(NEQ, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 6)), createBinaryExpr(GTE, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 6))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(LTE, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)]], [createTurnAction(createConstant(NUM_CONST, 50), RIGHT, createConstant(NUM_CONST, 80))]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createRepeatStmt(TIMES, createConstant(NUM_CONST, 10), [createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 20)), createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 30))]);\n"
                + "var pp = [stmt0,stmt1,stmt2];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createBinaryExpr(OR, createConstant(BOOL_CONST, false), createConstant(BOOL_CONST, true)))], [])]);\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])]);\n"
                + "var stmt2 = createIfStmt([createBinaryExpr(EQ, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0))], [[createIfStmt([createBinaryExpr(NEQ, createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 1)), createBinaryExpr(EQ, createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 1))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD), createTurnAction(createConstant(NUM_CONST, 50), RIGHT)]])]], [createStopDrive()]);\n"
                + "var stmt3 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC, DISTANCE), createConstant(NUM_CONST, 30))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]), createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC, DISTANCE), createConstant(NUM_CONST, 30))], [[createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [])])]])]);\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator8.xml");
    }

    @Test
    public void test9() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createDriveAction(createConstant(NUM_CONST, 80), BACKWARD);\n"
                + "var stmt2 = createTurnAction(createConstant(NUM_CONST, 50), RIGHT);\n"
                + "var stmt3 = createTurnAction(createConstant(NUM_CONST, 90), LEFT);\n"
                + "var stmt4 = createStopDrive();\n"
                + "var stmt5 = createTurnAction(createConstant(NUM_CONST, 80), RIGHT, createConstant(NUM_CONST, 20));\n"
                + "var stmt6 = createTurnAction(createConstant(NUM_CONST, 80), LEFT, createConstant(NUM_CONST, 15));\n"
                + "var stmt7 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 26));\n"
                + "var stmt8 = createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 33));\n"
                + "var stmt9 = createTurnLight(GREEN, ON);\n"
                + "var stmt10 = createStatusLight(OFF);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator9.xml");

    }

    @Test
    public void test10() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(COLOR, \"variablenName\", createConstant(COLOR_CONST, COLOR_ENUM.NONE));\n"
                + "var stmt1 = createVarDeclaration(COLOR, \"variablenName2\", createConstant(COLOR_CONST, COLOR_ENUM.BLACK));\n"
                + "var stmt2 = createVarDeclaration(COLOR, \"variablenName3\", createConstant(COLOR_CONST, COLOR_ENUM.BLUE));\n"
                + "var stmt3 = createVarDeclaration(COLOR, \"variablenName4\", createConstant(COLOR_CONST, COLOR_ENUM.GREEN));\n"
                + "var stmt4 = createVarDeclaration(COLOR, \"variablenName5\", createConstant(COLOR_CONST, COLOR_ENUM.YELLOW));\n"
                + "var stmt5 = createVarDeclaration(COLOR, \"variablenName6\", createConstant(COLOR_CONST, COLOR_ENUM.RED));\n"
                + "var stmt6 = createVarDeclaration(COLOR, \"variablenName7\", createConstant(COLOR_CONST, COLOR_ENUM.WHITE));\n"
                + "var stmt7 = createVarDeclaration(COLOR, \"variablenName8\", createConstant(COLOR_CONST, COLOR_ENUM.BROWN));\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator10.xml");

    }

    @Test
    public void test11() throws Exception {
        String a =
            "var stmt0 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(COLOR, RED), createConstant(NUM_CONST, 30))], [])]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(COLOR, COLOUR), createConstant(COLOR_CONST, COLOR_ENUM.RED))], [])]);\n"
                + "var stmt2 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(COLOR, COLOUR), createConstant(COLOR_CONST, COLOR_ENUM.GREEN))], [])]);\n"
                + "var stmt3 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(COLOR, RED), createConstant(NUM_CONST, 45))], [])]);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator11.xml");
    }

    @Test
    public void test12() throws Exception {

        String a =
            "var stmt0 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, false))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)]]), createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 20))]])]);\n"
                + "var pp = [stmt0];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator12.xml");

    }

    @Test
    public void test13() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true)), createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, false))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 20))]]);\n"
                + "var pp = [stmt0];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator13.xml");

    }

    @Test
    public void test14() throws Exception {

        String a =
            "var stmt0 = createMotorOnAction(createConstant(NUM_CONST, 30), MOTOR_RIGHT);\n"
                + "var stmt1 = createMotorOnAction(createConstant(NUM_CONST, 30), MOTOR_LEFT);\n"
                + "var stmt2 = createMotorOnAction(createConstant(NUM_CONST, 30), MOTOR_RIGHT, createDuration(ROTATIONS, createConstant(NUM_CONST, 1)));\n"
                + "var stmt3 = createMotorOnAction(createConstant(NUM_CONST, 30), MOTOR_LEFT, createDuration(DEGREE, createConstant(NUM_CONST, 1)));\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator14.xml");

    }

    @Test
    public void test15() throws Exception {

        String a =
            "var stmt0 = createMotorOnAction(createConstant(NUM_CONST, 30), MOTOR_RIGHT, createDuration(ROTATIONS, createConstant(NUM_CONST, 1)));\n"
                + "var stmt1 = createStopMotorAction(MOTOR_RIGHT);\n"
                + "var stmt2 = createStopMotorAction(MOTOR_LEFT);\n"
                + "var pp = [stmt0,stmt1,stmt2];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator15.xml");

    }

    @Test
    public void test16() throws Exception {
        String a =
            "var stmt0 = createMotorOnAction(createUnaryExpr(NEG, createConstant(NUM_CONST, 30)), MOTOR_RIGHT, createDuration(ROTATIONS, createConstant(NUM_CONST, 1)));\n"
                + "var pp = [stmt0];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator16.xml");

    }

    @Test
    public void test17() throws Exception {
        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ROOT', createConstant(NUM_CONST, 20)));\n"
                + "var stmt1 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ABS', createConstant(NUM_CONST, 20)));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('LN', createConstant(NUM_CONST, 20)));\n"
                + "var stmt3 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('LOG10', createConstant(NUM_CONST, 20)));\n"
                + "var stmt4 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('EXP', createConstant(NUM_CONST, 20)));\n"
                + "var stmt5 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('POW10', createConstant(NUM_CONST, 20)));\n"
                + "var stmt6 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('SIN', createConstant(NUM_CONST, 20)));\n"
                + "var stmt7 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('COS', createConstant(NUM_CONST, 20)));\n"
                + "var stmt8 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('TAN', createConstant(NUM_CONST, 20)));\n"
                + "var stmt9 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ASIN', createConstant(NUM_CONST, 20)));\n"
                + "var stmt10 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ACOS', createConstant(NUM_CONST, 20)));\n"
                + "var stmt11 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ATAN', createConstant(NUM_CONST, 20)));\n"
                + "var stmt12 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ROUND', createConstant(NUM_CONST, 20)));\n"
                + "var stmt13 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ROUNDUP', createConstant(NUM_CONST, 20)));\n"
                + "var stmt14 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createSingleFunction('ROUNDDOWN', createConstant(NUM_CONST, 20)));\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6,stmt7,stmt8,stmt9,stmt10,stmt11,stmt12,stmt13,stmt14];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator17.xml");

    }

    @Test
    public void test18() throws Exception {
        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('PI'));\n"
                + "var stmt1 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('E'));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('GOLDEN_RATIO'));\n"
                + "var stmt3 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('SQRT2'));\n"
                + "var stmt4 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('SQRT1_2'));\n"
                + "var stmt5 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createMathConstant('INFINITY'));\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator18.xml");

    }

    @Test
    public void test19() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createConstant(STRING_CONST, 'Hallo'), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator19.xml");

    }

    @Test
    public void test20() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createMathPropFunct('EVEN', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createMathPropFunct('ODD', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createMathPropFunct('PRIME', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt3 = createShowTextAction(createMathPropFunct('WHOLE', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt4 = createShowTextAction(createMathPropFunct('POSITIVE', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt5 = createShowTextAction(createMathPropFunct('NEGATIVE', createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt6 = createShowTextAction(createMathPropFunct('DIVISIBLE_BY', createConstant(NUM_CONST, 3), createConstant(NUM_CONST, 3)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3,stmt4,stmt5,stmt6];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator20.xml");

    }

    @Test
    public void test21() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createMathConstrainFunct(createConstant(NUM_CONST, 200), createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 100)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createMathConstrainFunct(createConstant(NUM_CONST, -200), createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 100)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createMathConstrainFunct(createConstant(NUM_CONST, 20), createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 100)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0,stmt1,stmt2];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator21.xml");

    }

    @Test
    public void test22() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createRandInt(createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 100)), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createRandDouble(), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator22.xml");

    }

    @Test
    public void test23() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"item\", createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createVarDeclaration(STRING, \"item2\", createConstant(STRING_CONST, 'cc'));\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator23.xml");

    }

    @Test
    public void test24() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetMotorPower(MOTOR_RIGHT), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator24.xml");

    }

    @Test
    public void test25() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetSampleEncoderSensor(MOTOR_RIGHT, ROTATION), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createGetSampleEncoderSensor(MOTOR_RIGHT, DEGREE), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt2 = createShowTextAction(createGetSampleEncoderSensor(MOTOR_RIGHT, DISTANCE), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt3 = createResetEncoderSensor(MOTOR_LEFT);\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator25.xml");

    }

    @Test
    public void test26() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createGetSample(GYRO, ANGLE), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createGetSample(GYRO, RATE), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt2 = createResetGyroSensor();\n"
                + "var pp = [stmt0,stmt1,stmt2];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator26.xml");

    }

    @Test
    public void test27() throws Exception {
        String a =
            "var stmt0 = createShowTextAction(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0)]), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createShowTextAction(createCreateListWith(STRING_ARRAY, [createConstant(STRING_CONST, 'a'), createConstant(STRING_CONST, 'v'), createConstant(STRING_CONST, 'b\')]), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0));\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator27.xml");

    }

    @Test
    public void test28() throws Exception {
        String a =
            "var stmt0 = createDebugAction();\n"
                + "var stmt1 = createShowPictureAction('OLDGLASSES', createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 11));\n"
                + "var stmt2 = createShowPictureAction('EYESOPEN', createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 11));\n"
                + "var stmt3 = createClearDisplayAction();\n"
                + "var pp = [stmt0,stmt1,stmt2,stmt3];";

        assertCodeIsOk(a, "/syntax/code_generator/java_script/java_script_code_generator28.xml");

    }

    @Test
    public void test29() throws Exception {
        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"variablenName\", createConstant(NUM_CONST, 0));\n"
                + "var stmt1 = createMathChange(createVarReference(NUMERIC, \"variablenName\"), createConstant(NUM_CONST, 1));\n"
                + "var pp = [stmt0,stmt1];";

        assertCodeIsOk(a, "/ast/math/math_change.xml");
    }

    @Test
    public void createListWithItem() throws Exception {
        String a = "createCreateListWithItem(createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 5))" + "var pp = [";

        assertCodeIsOk(a, "/syntax/lists/lists_create_with_item.xml");
    }

    @Test
    public void createListLength() throws Exception {
        String a =
            "createListLength(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 0.1), createConstant(NUM_CONST, 0.0), createConstant(NUM_CONST, 0)]))"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_length.xml");
    }

    @Test
    public void createListIsEmpty() throws Exception {
        String a =
            "createListIsEmpty(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0)]))"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_is_empty.xml");
    }

    @Test
    public void createListFindElementFirst() throws Exception {
        String a =
            "createListFindItem(FIRST, createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 5), createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 2)]), createConstant(NUM_CONST, 2))"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

    @Test
    public void createListFindElementLast() throws Exception {
        String a =
            "createListFindItem(LAST, createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 5), createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 2)]), createConstant(NUM_CONST, 2))"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_occurrence1.xml");
    }

    @Test
    public void createListsIndexGetFromStart() throws Exception {
        String a =
            "createListsGetIndex(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), GET, FROM_START, createConstant(NUM_CONST, 1))"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_get_index.xml");
    }

    @Test
    public void createListsIndexRemoveFromStart() throws Exception {
        String a =
            "var stmt0 = createListsGetIndexStmt(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), REMOVE, FROM_START, createConstant(NUM_CONST, 1));\n"
                + "var pp = [stmt0];";
        assertCodeIsOk(a, "/syntax/lists/lists_get_index1.xml");
    }

    @Test
    public void createListsIndexGetRemoveLast() throws Exception {
        String a =
            "createListsGetIndex(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), GET_REMOVE, LAST)"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_get_index2.xml");
    }

    @Test
    public void createListsSetFromStart() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), SET, createConstant(NUM_CONST, 99), FROM_START, createConstant(NUM_CONST, 1));\n"
                + "var pp = [stmt0];";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index.xml");
    }

    @Test
    public void createListsInsertFromStart() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), INSERT, createConstant(NUM_CONST, 99), FROM_START, createConstant(NUM_CONST, 1));\n"
                + "var pp = [stmt0];";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index1.xml");
    }

    @Test
    public void createListsInsertRandom() throws Exception {
        String a =
            "var stmt0 = createListsSetIndex(createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 55), createConstant(NUM_CONST, 66), createConstant(NUM_CONST, 11)]), INSERT, createConstant(NUM_CONST, 99), RANDOM);\n"
                + "var pp = [stmt0];";
        assertCodeIsOk(a, "/syntax/lists/lists_set_index2.xml");
    }

    @Test
    public void createGetSubListFromStartAndFromEnd() throws Exception {
        String a =
            "createGetSubList({list: createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 11), createConstant(NUM_CONST, 22), createConstant(NUM_CONST, 33), createConstant(NUM_CONST, 44)]), where1: FROM_START, at1: createConstant(NUM_CONST, 1), where2: FROM_END, at2: createConstant(NUM_CONST, 1)})"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

    @Test
    public void createGetSubListFirstAndFromEnd() throws Exception {
        String a =
            "createGetSubList({list: createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 11), createConstant(NUM_CONST, 22), createConstant(NUM_CONST, 33), createConstant(NUM_CONST, 44)]), where1: FIRST, where2: FROM_END, at2: createConstant(NUM_CONST, 1)})"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_sub_list1.xml");
    }

    @Test
    public void createGetSubListFromStartAndLast() throws Exception {
        String a =
            "createGetSubList({list: createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 11), createConstant(NUM_CONST, 22), createConstant(NUM_CONST, 33), createConstant(NUM_CONST, 44)]), where1: FROM_START, at1: createConstant(NUM_CONST, 1), where2: LAST})"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_sub_list2.xml");
    }

    @Test
    public void createGetSubListFirstAndLast() throws Exception {
        String a =
            "createGetSubList({list: createCreateListWith(NUMERIC_ARRAY, [createConstant(NUM_CONST, 11), createConstant(NUM_CONST, 22), createConstant(NUM_CONST, 33), createConstant(NUM_CONST, 44)]), where1: FIRST, where2: LAST})"
                + "var pp = [";
        assertCodeIsOk(a, "/syntax/lists/lists_sub_list3.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        Assert.assertEquals(a, Helper.generateJavaScript(fileName));
    }
}

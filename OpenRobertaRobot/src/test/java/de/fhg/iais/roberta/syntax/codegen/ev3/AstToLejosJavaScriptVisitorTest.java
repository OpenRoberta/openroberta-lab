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
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n"
                + "var stmt6 = createDriveAction(createConstant(NUM_CONST, -50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, 30))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a =
            "var stmt0 = createVarDeclaration(NUMERIC, \"x\", createConstant(NUM_CONST, 1));\n"
                + "var stmt1 = createVarDeclaration(NUMERIC, \"y\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 1)));\n"
                + "var stmt2 = createDriveAction(createConstant(NUM_CONST, 80), FOREWARD, createConstant(NUM_CONST, 39));\n"
                + "var stmt3 = createAssignStmt(\"x\", createBinaryExpr(ADD, createVarReference(NUMERIC, \"x\"), createConstant(NUM_CONST, 10)));\n"
                + "var stmt4 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n"
                + "var stmt5 = createDriveAction(createConstant(NUM_CONST, -50), FOREWARD, createConstant(NUM_CONST, 20));\n"
                + "var stmt6 = createTurnAction(createConstant(NUM_CONST, 50), RIGHT, createConstant(NUM_CONST, 30));\n"
                + "var stmt7 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, 30))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator1.xml");
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
                + "var stmt6 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n"
                + "var stmt2 = createTurnLight(GREEN, ON);\n"
                + "var stmt3 = createStopDrive();\n"
                + "var stmt4 = createResetLight();\n"
                + "var stmt5 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(NEQ, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 6)), createBinaryExpr(GTE, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 6))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator4.xml");
    }

    @Test
    public void test5() throws Exception {

        String a =
            "var stmt0 = createIfStmt([createBinaryExpr(LTE, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0))], [[createDriveAction(createConstant(NUM_CONST, 50), FOREWARD)]], [createTurnAction(createConstant(NUM_CONST, 50), RIGHT, createConstant(NUM_CONST, 80))]);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n"
                + "var stmt2 = createRepeatStmt(TIMES, createConstant(NUM_CONST, 10), [createDriveAction(createConstant(NUM_CONST, 50), FOREWARD, createConstant(NUM_CONST, 20)), createDriveAction(createConstant(NUM_CONST, 50), BACKWARD, createConstant(NUM_CONST, 30))]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createBinaryExpr(OR, createConstant(BOOL_CONST, false), createConstant(BOOL_CONST, true)))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator6.xml");
    }

    @Test
    public void test7() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])]);\n"
                + "var stmt2 = createIfStmt([createBinaryExpr(EQ, createConstant(NUM_CONST, 0), createConstant(NUM_CONST, 0))], [[createIfStmt([createBinaryExpr(NEQ, createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 1)), createBinaryExpr(EQ, createConstant(NUM_CONST, 1), createConstant(NUM_CONST, 1))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)], [createDriveAction(createConstant(NUM_CONST, 50), BACKWARD), createTurnAction(createConstant(NUM_CONST, 50), RIGHT)]])]], [createStopDrive()]);\n"
                + "var stmt3 = createWaitStmt([createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, 30))], [[]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator7.xml");
    }

    @Test
    public void test8() throws Exception {

        String a =
            "var stmt0 = createDriveAction(createConstant(NUM_CONST, 50), FOREWARD);\n"
                + "var stmt1 = createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[createDriveAction(createConstant(NUM_CONST, 50), BACKWARD)]]), createIfStmt([createBinaryExpr(LT, createGetSample(ULTRASONIC), createConstant(NUM_CONST, 30))], [[createWaitStmt([createIfStmt([createBinaryExpr(EQ, createGetSample(TOUCH), createConstant(BOOL_CONST, true))], [[]])])]])]);\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_script_code_generator8.xml");
    }

    private void assertCodeIsOk(String a, String fileName) throws Exception {
        // Assert.assertEquals(a, Helper.generateString(fileName, brickConfiguration));
        Assert.assertEquals(a, Helper.generateJavaScript(fileName));
        //        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateJavaScript(fileName).replaceAll("\\s+", ""));
    }
}

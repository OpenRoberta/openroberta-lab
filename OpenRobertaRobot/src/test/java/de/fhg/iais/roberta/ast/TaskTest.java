package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TaskTest extends AstTest {

    @Test
    public void mainTask() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=7], MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, item, NumConst [0], true, true]\n"
                + "exprStmt VarDeclaration [STRING, item2, StringConst [ss], true, true]\n"
                + "exprStmt VarDeclaration [BOOLEAN, item3, BoolConst [true], true, true]\n"
                + "exprStmt VarDeclaration [ARRAY_NUMBER, item4, ListCreate [NUMBER, NumConst [1], NumConst [2], NumConst [3]], true, true]\n"
                + "exprStmt VarDeclaration [ARRAY_STRING, item5, ListCreate [STRING, StringConst [a], EmptyExpr [defVal=ARRAY], StringConst [b]], true, true]\n"
                + "exprStmt VarDeclaration [ARRAY_BOOLEAN, item6, ListCreate [BOOLEAN, BoolConst [true], BoolConst [false], EmptyExpr [defVal=ARRAY]], true, true]\n"
                + "exprStmt VarDeclaration [ARRAY_COLOUR, item7, ListCreate [COLOR, ColorConst [#B30006], ColorConst [#000000], ColorConst [#585858]], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, item8, ColorConst [#585858], false, true]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/task/task_mainTask.xml");
    }

    @Test
    public void mainTask1() throws Exception {
        String a = "BlockAST [project=[[Location [x=10, y=83], MainTask []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/task/task_mainTask1.xml");
    }

    @Test
    public void activityTask() throws Exception {
        String a = "BlockAST [project=[[Location [x=66, y=175], ActivityTask [activityName=Var [zwei]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/task/task_activityTask.xml");
    }

    @Test
    public void startActivityTask() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=66], StartActivityTask [activityName=Var [zwei]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/task/task_startActivityTask.xml");
    }

    @Test
    public void reverseTransformatinMainTask() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/task/task_mainTask.xml");
    }

    @Test
    public void reverseTransformatinMainTask1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/task/task_mainTask1.xml");
    }

    @Test
    public void reverseTransformatinActivityTask() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/task/task_activityTask.xml");
    }

    @Test
    public void reverseTransformatinStartActivityTask() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/task/task_startActivityTask.xml");
    }
}

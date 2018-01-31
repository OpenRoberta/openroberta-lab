package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class TaskTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

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
                + "exprStmt VarDeclaration [ARRAY_COLOUR, item7, ListCreate [COLOR, ColorConst [RED], ColorConst [BLACK], ColorConst [NONE]], true, true]\n"
                + "exprStmt VarDeclaration [COLOR, item8, ColorConst [NONE], false, true]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/task/task_mainTask.xml"));
    }

    @Test
    public void mainTaskShadow() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=38, y=88], MainTask [\n"
                + "exprStmt VarDeclaration [NUMBER, item, ShadowExpr [NumConst [0], null], true, true]\n"
                + "exprStmt VarDeclaration [NUMBER, item2, ShadowExpr [NumConst [0], NumConst [15]], false, true]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/task/task_mainTaskShadow.xml"));
    }

    @Test
    public void mainTask1() throws Exception {
        String a = "BlockAST [project=[[Location [x=10, y=83], MainTask []]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/task/task_mainTask1.xml"));
    }

    @Test
    public void activityTask() throws Exception {
        String a = "BlockAST [project=[[Location [x=66, y=175], ActivityTask [activityName=Var [zwei]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/task/task_activityTask.xml"));
    }

    @Test
    public void startActivityTask() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=66], StartActivityTask [activityName=Var [zwei]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/task/task_startActivityTask.xml"));
    }

    @Test
    public void reverseTransformatinMainTask() throws Exception {
        this.h.assertTransformationIsOk("/ast/task/task_mainTask.xml");
    }

    @Test
    public void reverseTransformatinMainTask1() throws Exception {
        this.h.assertTransformationIsOk("/ast/task/task_mainTask1.xml");
    }

    @Test
    public void reverseTransformatinActivityTask() throws Exception {
        this.h.assertTransformationIsOk("/ast/task/task_activityTask.xml");
    }

    @Test
    public void reverseTransformatinStartActivityTask() throws Exception {
        this.h.assertTransformationIsOk("/ast/task/task_startActivityTask.xml");
    }

    @Test
    public void reverseTransformatinMainTaskShadow() throws Exception {
        this.h.assertTransformationIsOk("/ast/task/task_mainTaskShadow.xml");
    }

}

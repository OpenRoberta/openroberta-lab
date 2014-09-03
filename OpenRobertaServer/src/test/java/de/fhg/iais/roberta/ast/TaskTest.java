package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TaskTest {

    @Test
    public void mainTask() throws Exception {
        String a = "BlockAST [project=[[MainTask [x=11, y=35]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/task/task_mainTask.xml"));
    }

    @Test
    public void activityTask() throws Exception {
        String a = "BlockAST [project=[[ActivityTask [x=66, y=175, activityName=Var [zwei]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/task/task_activityTask.xml"));
    }

    @Test
    public void startActivityTask() throws Exception {
        String a = "BlockAST [project=[[StartActivityTask [activityName=Var [zwei]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/task/task_startActivityTask.xml"));
    }
}

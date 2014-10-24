package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ControlTest {

    @Test
    public void robWait() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [statements=\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [sensor=TimerSensor [mode=GET_SAMPLE, timer=1]]], NumConst [500]]]"
                + "\n)]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/wait_stmt.xml"));
    }

    @Ignore
    public void robWaitFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [statements=\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [sensor=TouchSensor [port=S1]]], BoolConst [true]]]\n"
                + ")]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/wait_stmt1.xml"));
    }

    @Ignore
    public void robWaitFor1() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=100,y=50],MainTask[],WaitStmt[statements=(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[sensor=BrickSensor[key=ENTER,mode=IS_PRESSED]]],BoolConst[true]]])]]]]";
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateTransformerString("/ast/control/wait_stmt2.xml").replaceAll("\\s+", ""));
    }

    @Ignore
    public void reverseTransformation() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/wait_stmt.xml");
    }

    @Ignore
    public void reverseTransformationFor() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/wait_stmt1.xml");
    }

    @Ignore
    public void reverseTransformationFor1() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/wait_stmt2.xml");
    }

    @Ignore
    public void reverseTransformationFor2() throws Exception {
        Helper.assertTransformationIsOk("/ast/control/wait_stmt3.xml");
    }
}

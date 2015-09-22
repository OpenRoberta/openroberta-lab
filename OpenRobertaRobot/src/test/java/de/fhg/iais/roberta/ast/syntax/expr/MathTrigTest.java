package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathTrigTest {
    @Test
    public void Test() throws Exception {
        String a = "((float)Math.sin(0))((float)Math.cos(0))((float)Math.tan(0))((float)Math.asin(0))((float)Math.acos(0))((float)Math.atan(0))";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==((float)Math.sin(0))){hal.regulatedDrive(ActorPort.A,ActorPort.B,false,DriveDirection.FOREWARD,((float)Math.acos(0)));}";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig1.xml");
    }

}

package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.codegen.lejos.Helper;

public class MathTrigTest {
    @Test
    public void Test() throws Exception {
        String a = "Math.sin(0)Math.cos(0)Math.tan(0)Math.asin(0)Math.acos(0)Math.atan(0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==Math.sin(0)){hal.regulatedDrive(ActorPort.A,ActorPort.B,false,DriveDirection.FOREWARD,Math.acos(0));}";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig1.xml");
    }

}

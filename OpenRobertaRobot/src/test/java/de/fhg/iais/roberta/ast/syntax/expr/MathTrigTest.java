package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class MathTrigTest {
    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.sin(0)BlocklyMethods.cos(0)BlocklyMethods.tan(0)BlocklyMethods.asin(0)BlocklyMethods.acos(0)BlocklyMethods.atan(0)";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==BlocklyMethods.sin(0)){hal.regulatedDrive(ActorPort.A,ActorPort.B,false,DriveDirection.FOREWARD,BlocklyMethods.acos(0));}";

        Helper.assertCodeIsOk(a, "/syntax/math/math_trig1.xml");
    }

}

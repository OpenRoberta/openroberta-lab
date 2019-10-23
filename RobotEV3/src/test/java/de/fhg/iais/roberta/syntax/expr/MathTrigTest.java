package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MathTrigTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a = "(float)Math.sin(0)(float)Math.cos(0)(float)Math.tan(0)(float)Math.asin(0)" + "(float)Math.acos(0)(float)Math.atan(0)}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig.xml", false);
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==(float)Math.sin(0)){hal.regulatedDrive(DriveDirection.FOREWARD,(float)Math.acos(0));}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/math/math_trig1.xml", makeStandard(), false);
    }

}

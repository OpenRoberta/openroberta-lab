package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class MathTrigTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a = "BlocklyMethods.sin(0)BlocklyMethods.cos(0)BlocklyMethods.tan(0)BlocklyMethods.asin(0)BlocklyMethods.acos(0)BlocklyMethods.atan(0)}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig.xml");
    }

    @Test
    public void Test1() throws Exception {
        String a = "if(0==BlocklyMethods.sin(0)){hal.regulatedDrive(DriveDirection.FOREWARD,BlocklyMethods.acos(0));}}";

        this.h.assertCodeIsOk(a, "/syntax/math/math_trig1.xml");
    }

}

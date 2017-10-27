package de.fhg.iais.roberta.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class WaitStmtTest {
    Helper h = new Helper();

    @Test
    public void test1() throws Exception {
        String a = "publicvoidrun()throwsException{while(true){if(hal.isPressed(BrickKey.ENTER)==true){break;}hal.waitFor(15);}}";

        this.h.assertCodeIsOk(a, "/syntax/control/wait_stmt2.xml");
    }

    @Test
    public void javaCode() throws Exception {
        String a = "\nhal.waitFor(500);}";

        this.h.assertCodeIsOk(a, "/syntax/control/wait_time_stmt.xml");
    }
}
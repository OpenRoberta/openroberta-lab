package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

@Ignore
public class WaitStmtTest extends NxtAstTest {

    @Test
    public void test1() throws Exception {
        String a = "publicvoidrun(){if(TRUE){while(true){if(hal.isPressed(BrickKey.ENTER)==true){break;}hal.waitFor(15);}}}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/control/wait_stmt2.xml", false);
    }
}
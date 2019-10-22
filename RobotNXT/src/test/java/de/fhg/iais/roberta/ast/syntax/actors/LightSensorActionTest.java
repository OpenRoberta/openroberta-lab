package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class LightSensorActionTest extends NxtAstTest {

    @Ignore
    public void redOn() throws Exception {
        String a = "";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_LightSensorAction.xml", false);
    }
}
package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class VolumeActionTest extends NxtAstTest {

    @Test
    public void setVolume() throws Exception {
        String a = "\nvolume=(50)*4/100.0;";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_SetVolume.xml", false);
    }

    @Test
    public void getVolume() throws Exception {
        String a = "\nvolume*100/4";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_GetVolume.xml", false);
    }
}

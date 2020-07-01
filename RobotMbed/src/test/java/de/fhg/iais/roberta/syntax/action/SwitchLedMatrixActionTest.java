package de.fhg.iais.roberta.syntax.action;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SwitchLedMatrixActionTest extends CalliopeAstTest {

    @Test
    public void calliopeGenerateSource_GivenXml_ShouldGenerateSameSource() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/action/switch_led_matrix.cpp", "/action/switch_led_matrix.xml", configuration);
    }
}

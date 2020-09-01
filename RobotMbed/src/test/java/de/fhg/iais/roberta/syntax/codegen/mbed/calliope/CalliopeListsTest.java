package de.fhg.iais.roberta.syntax.codegen.mbed.calliope;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.fhg.iais.roberta.syntax.CalliopeAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class CalliopeListsTest extends CalliopeAstTest {

    @Test
    public void calliopeGetSetTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/lists/calliope_lists_get_set_test.cpp",
                "/lists/calliope_lists_get_set_test.xml",
                configuration);
    }

    @Test
    public void calliopeFindFirstLastTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/lists/calliope_lists_find_last_first_test.cpp",
                "/lists/calliope_lists_find_last_first_test.xml",
                configuration);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void showSource_failsWithError_whenListOpsAreUsedWithListCreate() {
        this.exceptionRule.expect(AssertionError.class);
        this.exceptionRule.expectMessage("ValidatorWorker failed with 8 errors");
        UnitTestHelper.checkWorkflow(testFactory, "showsource", "/lists/calliope_lists_ops_used_with_create.xml");
    }
}

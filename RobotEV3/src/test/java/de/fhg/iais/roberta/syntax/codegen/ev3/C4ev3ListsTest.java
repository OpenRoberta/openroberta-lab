package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import de.fhg.iais.roberta.Ev3C4ev3AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class C4ev3ListsTest extends Ev3C4ev3AstTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void showSource_failsWithError_whenListOpsAreUsedWithListCreate() {
        this.exceptionRule.expect(AssertionError.class);
        this.exceptionRule.expectMessage("ValidatorWorker failed with 7 errors");
        UnitTestHelper.checkWorkflow(testFactory, "showsource", "/syntax/lists/lists_ops_used_with_create.xml");
    }
}

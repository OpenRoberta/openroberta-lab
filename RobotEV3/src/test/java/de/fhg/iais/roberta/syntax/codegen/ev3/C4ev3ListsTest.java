package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.Ev3C4ev3AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class C4ev3ListsTest extends Ev3C4ev3AstTest {
    @Test
    public void showSource_failsWithError_whenListOpsAreUsedWithListCreate() {
        Assert
            .assertThrows(
                "ValidatorWorker failed with 7 errors",
                AssertionError.class,
                () -> UnitTestHelper.checkWorkflow(testFactory, "showsource", "/syntax/lists/lists_ops_used_with_create.xml"));
    }
}

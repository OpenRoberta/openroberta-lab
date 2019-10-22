package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3DevAstTest;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class Ev3DevListsTest extends Ev3DevAstTest {

    private final ConfigurationAst configuration = makeStandard();

    @Test
    public void ev3DevGetListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/ev3dev_lists_get_test.py",
                "/ast/lists/ev3dev_lists_get_test.xml",
                this.configuration);
    }

    @Test
    public void ev3DevSetListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/ev3dev_lists_set_test.py",
                "/ast/lists/ev3dev_lists_set_test.xml",
                this.configuration);
    }

    @Test
    public void ev3DevMathOnListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/ev3dev_lists_math_on_list_test.py",
                "/ast/lists/ev3dev_lists_math_on_list_test.xml",
                this.configuration);
    }

    @Test
    public void ev3DevRepeatFindSublistListsTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/lists/ev3dev_lists_repeat_find_sublist_test.py",
                "/ast/lists/ev3dev_lists_repeat_find_sublist_test.xml",
                this.configuration);
    }

}

package de.fhg.iais.roberta.syntax.codegen.ev3;

import org.junit.Test;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class Ev3DevListsTest {
    private final HelperEv3ForXmlTest ev3DevHelper = new HelperEv3ForXmlTest();
    private final Configuration configuration = HelperEv3ForXmlTest.makeConfiguration();

    @Test
    public void ev3DevGetListsTest() throws Exception {
        this.ev3DevHelper
            .compareExistingAndGeneratedPythonSource("ast/lists/ev3dev_lists_get_test.py", "/ast/lists/ev3dev_lists_get_test.xml", this.configuration);
    }

    @Test
    public void ev3DevSetListsTest() throws Exception {
        this.ev3DevHelper
            .compareExistingAndGeneratedPythonSource("ast/lists/ev3dev_lists_set_test.py", "/ast/lists/ev3dev_lists_set_test.xml", this.configuration);
    }

    @Test
    public void ev3DevMathOnListsTest() throws Exception {
        this.ev3DevHelper
            .compareExistingAndGeneratedPythonSource(
                "ast/lists/ev3dev_lists_math_on_list_test.py",
                "/ast/lists/ev3dev_lists_math_on_list_test.xml",
                this.configuration);
    }

    @Test
    public void ev3DevRepeatFindSublistListsTest() throws Exception {
        this.ev3DevHelper
            .compareExistingAndGeneratedPythonSource(
                "ast/lists/ev3dev_lists_repeat_find_sublist_test.py",
                "/ast/lists/ev3dev_lists_repeat_find_sublist_test.xml",
                this.configuration);
    }

}

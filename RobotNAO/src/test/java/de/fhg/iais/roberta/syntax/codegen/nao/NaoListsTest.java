package de.fhg.iais.roberta.syntax.codegen.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class NaoListsTest extends NaoAstTest {

    @Test
    public void listsGetSetTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/lists" + "/nao_lists_get_set_test.py", "/lists/nao_lists_get_set_test.xml");
    }

    @Test
    public void listsCreateFindTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/lists/nao_lists_create_find_test.py", "/lists/nao_lists_create_find_test.xml");
    }

    @Test
    public void listsMathOnListTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(testFactory, "/lists/nao_lists_math_on_list_test.py", "/lists/nao_lists_math_on_list_test.xml");
    }

    @Test
    public void listsSublistTest() throws Exception {
        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXml(testFactory, "/lists/nao_lists_sublist_test.py", "/lists/nao_lists_sublist_test.xml");
    }
}

package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListTest extends EdisonAstTest {

    private String insertIntoResult(String s) {
        return "BlockAST [project=" + s + "]";
    }

    //Lists

    @Test
    public void TestCreateWithEmpty() throws Exception {
        String expected = insertIntoResult("[[Location [x=442, y=211], ListCreate [NUMBER, ]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/create_with_empty.xml");
    }

    @Test
    public void TestCreateWith() throws Exception {
        String expected = insertIntoResult("[[Location [x=376, y=207], ListCreate [NUMBER, NumConst [345], NumConst [789], NumConst [234]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/create_with.xml");
    }

    @Test
    public void TestCreateRepeat() throws Exception {
        String expected = insertIntoResult("[[Location [x=321, y=188], ListRepeat [NUMBER, [EmptyExpr [defVal=ARRAY], NumConst [5]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/create_with_repeat.xml");
    }

    @Test
    public void TestLength() throws Exception {
        String expected = insertIntoResult("[[Location [x=443, y=202], LengthOfFunct [LIST_LENGTH, [EmptyExpr [defVal=STRING]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/length.xml");
    }

    @Test
    public void TestGet() throws Exception {
        String expected =
            insertIntoResult("[[Location [x=484, y=230], ListGetIndex [GET, FROM_START, [EmptyExpr [defVal=STRING], EmptyExpr [defVal=NUMBER_INT]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/get.xml");
    }

    @Test
    public void TestSet() throws Exception {
        String expected =
            insertIntoResult(
                "[[Location [x=417, y=215], ListSetIndex [SET, FROM_START, [EmptyExpr [defVal=STRING], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]]]]");
        UnitTestHelper.checkProgramAstEquality(testFactory, expected, "/ast/lists/set.xml");
    }
}

package de.fhg.iais.roberta.ast;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TextTest extends AstTest {

    @Test
    public void text1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-616, y=42], TextJoinFunct [StringConst [text1], EmptyExpr [defVal=STRING], StringConst [text2]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/text/text1.xml");
    }

    @Test
    public void reverseTransformatinText1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text1.xml");
    }

    @Test
    public void textAppend() throws Exception {
        String a = "BlockAST [project=[[Location [x=14, y=93], \nexprStmt Binary [TEXT_APPEND, Var [item], StringConst [text]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/text/text_append.xml");
    }

    @Test
    public void reverseTransformatinAppend() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_append.xml");
    }

    @Test
    public void textPrint() throws Exception {
        String a = "BlockAST [project=[[Location [x=-846, y=39], TextPrintFunct [[StringConst []]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/text/text_print.xml");
    }

    @Test
    public void reverseTransformatinPrint() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/text/text_print.xml");
    }

}

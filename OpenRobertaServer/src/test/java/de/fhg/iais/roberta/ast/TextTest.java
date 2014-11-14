package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TextTest {

    @Test
    public void text1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-616, y=42], TextJoinFunct [[StringConst [text1], EmptyExpr [defVal=class java.lang.String], StringConst [text2]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text1.xml"));
    }

    @Test
    public void reverseTransformatinText1() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text1.xml");
    }

    @Test
    public void text2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-610, y=217], IndexOfFunct [FIRST, [Var [text], StringConst [test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text2.xml"));
    }

    @Test
    public void reverseTransformatinText2() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text2.xml");
    }

    @Test
    public void textAppend() throws Exception {
        String a = "BlockAST [project=[[Location [x=14, y=93], Binary [TEXT_APPEND, Var [item], StringConst [text]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_append.xml"));
    }

    @Test
    public void reverseTransformatinAppend() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_append.xml");
    }

    @Test
    public void textLength() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=177], LenghtOfFunct [TEXT_LENGTH, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_length.xml"));
    }

    @Test
    public void reverseTransformatinLength() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_length.xml");
    }

    @Test
    public void textIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=96, y=220], LenghtOfFunct [IS_EMPTY, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_isEmpty.xml"));
    }

    @Test
    public void reverseTransformatinIsEmpty() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_isEmpty.xml");
    }

    @Test
    public void textIndexOf() throws Exception {
        String a = "BlockAST [project=[[Location [x=13, y=298], IndexOfFunct [FIRST, [Var [text], StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_indexOf.xml"));
    }

    @Test
    public void reverseTransformatinTextIndexOf() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_indexOf.xml");
    }

    @Test
    public void textCharAt() throws Exception {
        String a = "BlockAST [project=[[Location [x=8, y=156], TextCharAtFunct [FROM_START, [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_charAt.xml"));
    }

    @Test
    public void reverseTransformatinTextCharAt() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_charAt.xml");
    }

    @Test
    public void textCharAt1() throws Exception {
        String a = "BlockAST [project=[[Location [x=8, y=156], TextCharAtFunct [RANDOM, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_charAt1.xml"));
    }

    @Test
    public void reverseTransformatinTextCharAt1() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_charAt1.xml");
    }

    @Test
    public void textGetSubString() throws Exception {
        String a = "BlockAST [project=[[Location [x=-169, y=219], GetSubFunct [SUBSTRING, [FIRST, LAST], [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring.xml"));
    }

    @Test
    public void reverseTransformatinTextSubString() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_getSubstring.xml");
    }

    @Test
    public void textGetSubString1() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], GetSubFunct [SUBSTRING, [FROM_START, FROM_END], [Var [text], NumConst [0], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring1.xml"));
    }

    @Test
    public void reverseTransformatinTextSubString1() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_getSubstring1.xml");
    }

    @Test
    public void textGetSubString2() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], GetSubFunct [SUBSTRING, [FROM_START, LAST], [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring2.xml"));
    }

    @Test
    public void reverseTransformatinTextSubString2() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_getSubstring2.xml");
    }

    @Test
    public void textGetSubString3() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], GetSubFunct [SUBSTRING, [FIRST, FROM_START], [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring3.xml"));
    }

    @Test
    public void reverseTransformatinTextSubString3() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_getSubstring3.xml");
    }

    @Test
    public void textChangeCase() throws Exception {
        String a = "BlockAST [project=[[Location [x=22, y=324], TextChangeCaseFunct [UPPERCASE, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_changeCase.xml"));
    }

    @Test
    public void reverseTransformatinChangeCase() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_changeCase.xml");
    }

    @Test
    public void textChangeCase1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-824, y=30], TextChangeCaseFunct [LOWERCASE, [StringConst []]], Location [x=-804, y=70], TextChangeCaseFunct [UPPERCASE, [StringConst []]], Location [x=-784, y=110], TextChangeCaseFunct [TITLECASE, [StringConst []]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_changeCase1.xml"));
    }

    @Test
    public void reverseTransformatinChangeCase1() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_changeCase1.xml");
    }

    @Test
    public void textTrim() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=351], TextTrimFunct [LEFT, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_trim.xml"));
    }

    @Test
    public void reverseTransformatinTrim() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_trim.xml");
    }

    @Test
    public void textTrim1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-854, y=25], TextTrimFunct [BOTH, [StringConst []]], Location [x=-834, y=65], TextTrimFunct [LEFT, [StringConst []]], Location [x=-814, y=105], TextTrimFunct [RIGHT, [StringConst []]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_trim1.xml"));
    }

    @Test
    public void reverseTransformatinTrim1() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_trim1.xml");
    }

    @Test
    public void textPrint() throws Exception {
        String a = "BlockAST [project=[[Location [x=-846, y=39], TextPrintFunct [[StringConst []]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_print.xml"));
    }

    @Test
    public void reverseTransformatinPrint() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_print.xml");
    }

    @Test
    public void textPrompt() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=379], TextPromptFunct [TEXT, TEST]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_prompt.xml"));
    }

    @Test
    public void reverseTransformatinPrompt() throws Exception {
        Helper.assertTransformationIsOk("/ast/text/text_prompt.xml");
    }

}

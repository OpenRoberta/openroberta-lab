package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TextTest {

    @Test
    public void text1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-616, y=42], Funct [TEXT_JOIN, [StringConst [text1], EmptyExpr [defVal=class java.lang.String], StringConst [text2]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text1.xml"));
    }

    @Test
    public void text2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-610, y=217], Funct [FIRST, [Var [text], StringConst [test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text2.xml"));
    }

    @Test
    public void textAppend() throws Exception {
        String a = "BlockAST [project=[[Location [x=14, y=93], Binary [TEXT_APPEND, Var [item], StringConst [text]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_append.xml"));
    }

    @Test
    public void textLength() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=177], Funct [TEXT_LENGTH, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_length.xml"));
    }

    @Test
    public void textIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=96, y=220], Funct [IS_EMPTY, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_isEmpty.xml"));
    }

    @Test
    public void textIndexOf() throws Exception {
        String a = "BlockAST [project=[[Location [x=13, y=298], Funct [FIRST, [Var [text], StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_indexOf.xml"));
    }

    @Test
    public void textCharAt() throws Exception {
        String a = "BlockAST [project=[[Location [x=8, y=156], Funct [FROM_START, [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_charAt.xml"));
    }

    @Test
    public void textCharAt1() throws Exception {
        String a = "BlockAST [project=[[Location [x=8, y=156], Funct [RANDOM, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_charAt1.xml"));
    }

    @Test
    public void textGetSubString() throws Exception {
        String a = "BlockAST [project=[[Location [x=-169, y=219], Funct [SUBSTRING, [FIRST, LAST], [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring.xml"));
    }

    @Test
    public void textGetSubString1() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], Funct [SUBSTRING, [FROM_START, FROM_END], [Var [text], NumConst [0], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring1.xml"));
    }

    @Test
    public void textGetSubString2() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], Funct [SUBSTRING, [FROM_START, LAST], [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring2.xml"));
    }

    @Test
    public void textGetSubString3() throws Exception {
        String a = "BlockAST [project=[[Location [x=139, y=239], Funct [SUBSTRING, [FIRST, FROM_START], [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_getSubstring3.xml"));
    }

    @Test
    public void textChangeCase() throws Exception {
        String a = "BlockAST [project=[[Location [x=22, y=324], Funct [UPPERCASE, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_changeCase.xml"));
    }

    @Test
    public void textTrim() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=351], Funct [LEFT, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_trim.xml"));
    }

    @Test
    public void textPrompt() throws Exception {
        String a = "BlockAST [project=[[Location [x=-12, y=379], Funct [TEXT_PROMPT, [StringConst [TEST]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_prompt.xml"));
    }
}

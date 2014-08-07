package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class TextTest {

    @Test
    public void text1() throws Exception {
        String a = "BlockAST [project=[[Funct [TEXT_JOIN, [StringConst [text1], EmptyExpr [defVal=class java.lang.String], StringConst [text2]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text1.xml"));
    }

    @Test
    public void text2() throws Exception {
        String a = "BlockAST [project=[[Funct [FIRST, [Var [text], StringConst [test]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text2.xml"));
    }

    @Test
    public void textAppend() throws Exception {
        String a = "BlockAST [project=[[Binary [TEXT_APPEND, Var [item], StringConst [text]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_append.xml"));
    }

    @Test
    public void textLength() throws Exception {
        String a = "BlockAST [project=[[Funct [TEXT_LENGTH, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_length.xml"));
    }

    @Test
    public void textIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Funct [IS_EMPTY, [StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_isEmpty.xml"));
    }

    @Test
    public void textIndexOf() throws Exception {
        String a = "BlockAST [project=[[Funct [FIRST, [Var [text], StringConst [Test]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_indexOf.xml"));
    }

    @Test
    public void textCharAt() throws Exception {
        String a = "BlockAST [project=[[Funct [FROM_START, [Var [text], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_charAt.xml"));
    }

    @Test
    public void textCharAt1() throws Exception {
        String a = "BlockAST [project=[[Funct [RANDOM, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_charAt1.xml"));
    }

    @Ignore
    public void textGetSubString() throws Exception {
        String a = "BlockAST [project=[[Funct [RANDOM, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_getSubstring.xml"));
    }

    @Test
    public void textChangeCase() throws Exception {
        String a = "BlockAST [project=[[Funct [UPPERCASE, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_changeCase.xml"));
    }

    @Test
    public void textTrim() throws Exception {
        String a = "BlockAST [project=[[Funct [LEFT, [Var [text]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_trim.xml"));
    }

    @Test
    public void textPrompt() throws Exception {
        String a = "BlockAST [project=[[Funct [TEXT_PROMPT, [StringConst [TEST]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/text/text_prompt.xml"));
    }
}

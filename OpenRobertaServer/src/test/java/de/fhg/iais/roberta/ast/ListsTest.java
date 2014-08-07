package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class ListsTest {

    @Test
    public void listEmpty() throws Exception {
        String a = "BlockAST [project=[[EmptyExpr [defVal=interface java.util.List]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_empty.xml"));
    }

    @Test
    public void listRepeat() throws Exception {
        String a = "BlockAST [project=[[Funct [LISTS_REPEAT, [NumConst [4], NumConst [5]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_repeat.xml"));
    }

    @Test
    public void listLength() throws Exception {
        String a = "BlockAST [project=[[Funct [LISTS_LENGTH, [Var [item]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_length.xml"));
    }

    @Test
    public void listIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Funct [IS_EMPTY, [Var [item]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_isEmpty.xml"));
    }

    @Test
    public void listIndexOf() throws Exception {
        String a = "BlockAST [project=[[Funct [FIRST, [Var [liste], StringConst [T1]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_indexOf.xml"));
    }

    @Test
    public void listCreateWith() throws Exception {
        String a = "BlockAST [project=[[Funct [LISTS_REPEAT, [NumConst [10], NumConst [5]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/lists/list_createWith.xml"));
    }

}

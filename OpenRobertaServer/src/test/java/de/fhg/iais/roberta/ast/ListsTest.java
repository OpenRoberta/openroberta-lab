package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ListsTest {

    @Test
    public void listEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=10, y=63], EmptyExpr [defVal=interface java.util.List]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_empty.xml"));
    }

    @Test
    public void listRepeat() throws Exception {
        String a = "BlockAST [project=[[Location [x=-44, y=113], Funct [LISTS_REPEAT, [NumConst [4], NumConst [5]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_repeat.xml"));
    }

    @Test
    public void listLength() throws Exception {
        String a = "BlockAST [project=[[Location [x=14, y=197], Funct [LISTS_LENGTH, [Var [item]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_length.xml"));
    }

    @Test
    public void listIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=-7, y=230], Funct [IS_EMPTY, [Var [item]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_isEmpty.xml"));
    }

    @Test
    public void listIndexOf() throws Exception {
        String a = "BlockAST [project=[[Location [x=-97, y=351], Funct [FIRST, [Var [liste], StringConst [T1]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_indexOf.xml"));
    }

    @Test
    public void listCreateWith() throws Exception {
        String a = "BlockAST [project=[[Location [x=-80, y=150], Funct [LISTS_REPEAT, [NumConst [10], NumConst [5]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_createWith.xml"));
    }

}

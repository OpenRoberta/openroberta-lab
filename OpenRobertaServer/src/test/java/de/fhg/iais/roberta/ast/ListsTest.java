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

    @Test
    public void listGetIndex() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=245], Funct [GET_INDEX, [GET, FROM_START], [Var [liste], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getIndex.xml"));
    }

    @Test
    public void listGetIndex1() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=245], Funct [GET_INDEX, [GET, RANDOM], [Var [liste]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getIndex1.xml"));
    }

    @Test
    public void listGetIndex2() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=245], Funct [GET_INDEX, [REMOVE, LAST], [Var [liste]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getIndex2.xml"));
    }

    @Test
    public void listSetIndex() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=289], Funct [SET_INDEX, [SET, FROM_START], [Var [liste], NumConst [0], NumConst [12]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_setIndex.xml"));
    }

    @Test
    public void listSetIndex1() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=289], Funct [SET_INDEX, [SET, RANDOM], [Var [liste], NumConst [12]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_setIndex1.xml"));
    }

    @Test
    public void listSetIndex2() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=289], Funct [SET_INDEX, [INSERT, FROM_START], [Var [liste], NumConst [1], NumConst [12]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_setIndex2.xml"));
    }

    @Test
    public void listGetSublist() throws Exception {
        String a = "BlockAST [project=[[Location [x=-29, y=436], Funct [GET_SUBLIST, [FROM_START, FROM_START], [Var [liste], NumConst [0], NumConst [1]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getSublist.xml"));
    }

    @Test
    public void listGetSublist1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-29, y=436], Funct [GET_SUBLIST, [FROM_START, LAST], [Var [liste], NumConst [0]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getSublist1.xml"));
    }

    @Test
    public void listGetSublist2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-29, y=436], Funct [GET_SUBLIST, [FIRST, LAST], [Var [liste]]]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/lists/list_getSublist2.xml"));
    }

}

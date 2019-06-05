package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;

public class ListsTest<V> {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void listEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=10, y=63], EmptyList [STRING]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_empty.xml"));
    }

    @Test
    public void reverseTransformatinListEmpty() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_empty.xml");
    }

    @Test
    public void listRepeat() throws Exception {
        String a = "BlockAST [project=[[Location [x=-44, y=113], ListRepeat [NUMBER, [NumConst [4], NumConst [5]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_repeat.xml"));
    }

    @Test
    public void reverseTransformatinListRepeat() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_repeat.xml");
    }

    @Test
    public void listLength() throws Exception {
        String a = "BlockAST [project=[[Location [x=14, y=197], LengthOfFunct [LIST_LENGTH, [Var [item]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_length.xml"));
    }

    @Test
    public void reverseTransformatinListLength() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_length.xml");
    }

    @Test
    public void listIsEmpty() throws Exception {
        String a = "BlockAST [project=[[Location [x=-7, y=230], LengthOfFunct [LIST_IS_EMPTY, [Var [item]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_isEmpty.xml"));
    }

    @Test
    public void reverseTransformatinListIsEmpty() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_isEmpty.xml");
    }

    @Test
    public void listIndexOf() throws Exception {
        String a = "BlockAST [project=[[Location [x=-97, y=351], IndexOfFunct [FIRST, [Var [liste], StringConst [T1]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_indexOf.xml"));
    }

    @Test
    public void reverseTransformatinListIndexOf() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_indexOf.xml");
    }

    @Test
    public void listCreateWith() throws Exception {
        String a = "BlockAST [project=[[Location [x=-44, y=113], ListRepeat [NUMBER, [NumConst [4], NumConst [5]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_repeat.xml"));
    }

    @Test
    public void reverseTransformatinListCreateWith() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_repeat.xml");
    }

    @Test
    public void listGetIndex() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=245], ListGetIndex [GET, FROM_START, [Var [liste], NumConst [0]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getIndex.xml"));
    }

    @Test
    public void reverseTransformatinListGetIndex() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getIndex.xml");
    }

    @Test
    public void listGetIndex1() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=246], ListGetIndex [GET, LAST, [Var [liste]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getIndex1.xml"));
    }

    @Test
    public void reverseTransformatinListGetIndex1() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getIndex1.xml");
    }

    @Test
    public void listGetIndex2() throws Exception {
        String a = "BlockAST [project=[[Location [x=53, y=245], ListGetIndex [REMOVE, LAST, [Var [liste]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getIndex2.xml"));
    }

    @Test
    public void reverseTransformatinListGetIndex2() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getIndex2.xml");
    }

    @Test
    public void listSetIndex() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=289], ListSetIndex [SET, FROM_START, [Var [liste], NumConst [12], NumConst [0]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_setIndex.xml"));
    }

    @Test
    public void reverseTransformatinListSetIndex() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_setIndex.xml");
    }

    @Test
    public void listSetIndex1() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=288], ListSetIndex [SET, LAST, [Var [liste], NumConst [12]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_setIndex1.xml"));
    }

    @Test
    public void reverseTransformatinListSetIndex1() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_setIndex1.xml");
    }

    @Test
    public void listSetIndex2() throws Exception {
        String a = "BlockAST [project=[[Location [x=18, y=289], ListSetIndex [INSERT, FROM_START, [Var [liste], NumConst [12], NumConst [1]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_setIndex2.xml"));
    }

    @Test
    public void reverseTransformatinListSetIndex2() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_setIndex2.xml");
    }

    @Test
    public void listGetSublist() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-29, y=436], GetSubFunct [GET_SUBLIST, [FROM_START, FROM_START], [Var [liste], NumConst [0], NumConst [1]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getSublist.xml"));
    }

    @Test
    public void reverseTransformatinGetSubList() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getSublist.xml");
    }

    @Test
    public void listGetSublist1() throws Exception {
        String a = "BlockAST [project=[[Location [x=-29, y=436], GetSubFunct [GET_SUBLIST, [FROM_START, LAST], [Var [liste], NumConst [0]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getSublist1.xml"));
    }

    @Test
    public void reverseTransformatinGetSubList1() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getSublist1.xml");
    }

    @Test
    public void listGetSublist2() throws Exception {
        String a = "BlockAST [project=[[Location [x=-29, y=436], GetSubFunct [GET_SUBLIST, [FIRST, LAST], [Var [liste]]]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/lists/list_getSublist2.xml"));
    }

    @Test
    public void reverseTransformatinGetSubList2() throws Exception {
        this.h.assertTransformationIsOk("/ast/lists/list_getSublist2.xml");
    }
}

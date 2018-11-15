package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsSubListTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "ArrayList<Float>Element=newArrayList<>(Arrays.asList((float)0,(float)0,(float)0));publicvoidrun()throwsException{Element=newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).subList(0,0);Element=newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).subList((newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).size()-1)-0,(newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).size()-1)-0);Element=newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).subList(0,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).size());Element=newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).subList(0,0);}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_sub_list.xml");
    }

}

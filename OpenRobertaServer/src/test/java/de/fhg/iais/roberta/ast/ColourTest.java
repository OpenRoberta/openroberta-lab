package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class ColourTest {

    @Test
    public void test1() throws Exception {
        String a = "BlockAST [project=[[\nVar [item] := ColorConst [#585858]\n]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/colour/colour_const.xml"));
    }
}

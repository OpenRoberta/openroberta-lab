package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColourTest {

    @Test
    public void test1() throws Exception {
        String a = "BlockAST [project=[[\nVar [item] := ColorConst [#585858]\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/colour/colour_const.xml"));
    }
}

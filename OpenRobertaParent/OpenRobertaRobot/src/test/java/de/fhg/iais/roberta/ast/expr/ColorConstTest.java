package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.general.PickColor;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class ColorConstTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=116, y=139], ColorConst [BROWN]]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/colour/colour_const1.xml"));
    }

    @Test
    public void isValue() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(PickColor.BROWN, colorConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, colorConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, colorConst.getAssoc());
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/colour/colour_const1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        this.h.assertTransformationIsOk("/ast/colour/colour_const2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        this.h.assertTransformationIsOk("/ast/colour/colour_const3.xml");
    }

}

package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.GenericHelperForXmlTest;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;

public class StringConstTest {
    AbstractHelperForXmlTest h = new GenericHelperForXmlTest();

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=-616, y=42], StringConst [text2]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/text/text_string_const.xml"));
    }

    @Test
    public void getValue() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("text2", stringConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, stringConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, stringConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        this.h.assertTransformationIsOk("/ast/text/text_string_const.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        this.h.assertTransformationIsOk("/ast/text/text_string_const1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        this.h.assertTransformationIsOk("/ast/text/text_string_const2.xml");
    }

}

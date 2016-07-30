package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class BoolConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=171], BoolConst [true]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/logic/logic_boolConst.xml"));
    }

    @Test
    public void isValue() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/logic/logic_boolConst.xml");
        BoolConst<Void> boolConst = (BoolConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(true, boolConst.isValue());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/logic/logic_boolConst.xml");
        BoolConst<Void> boolConst = (BoolConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, boolConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/logic/logic_boolConst.xml");
        BoolConst<Void> boolConst = (BoolConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, boolConst.getAssoc());
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_boolConst.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_boolConst1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/logic/logic_boolConst2.xml");
    }
}

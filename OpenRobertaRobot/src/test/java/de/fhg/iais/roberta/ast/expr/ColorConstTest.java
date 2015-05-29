package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.Pickcolor;
import de.fhg.iais.roberta.syntax.expr.Assoc;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ColorConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=116, y=139], ColorConst [BROWN]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/colour/colour_const1.xml"));
    }

    @Test
    public void isValue() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Pickcolor.BROWN, colorConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, colorConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, colorConst.getAssoc());
    }

    @Test
    public void test1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=41, y=101], \nif Binary [EQ, ColorConst [WHITE], SensorExpr [ColorSensor [mode=RGB, port=S3]]]\n"
                + ",then\n"
                + "AktionStmt [DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/colour/colour_const.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/colour/colour_const.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/colour/colour_const1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/colour/colour_const2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        Helper.assertTransformationIsOk("/ast/colour/colour_const3.xml");
    }

}

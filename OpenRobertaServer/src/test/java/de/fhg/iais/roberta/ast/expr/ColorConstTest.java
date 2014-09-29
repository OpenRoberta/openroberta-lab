package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.PickColor;
import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class ColorConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ColorConst [BROWN]]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/colour/colour_const1.xml"));
    }

    @Test
    public void isValue() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(PickColor.BROWN, colorConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(999, colorConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/colour/colour_const1.xml");
        ColorConst<Void> colorConst = (ColorConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Assoc.NONE, colorConst.getAssoc());
    }

}

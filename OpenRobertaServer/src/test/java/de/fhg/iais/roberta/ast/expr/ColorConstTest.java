package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class ColorConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[ColorConst [#585858]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/colour/colour_const1.xml"));
    }

    @Test
    public void isValue() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/colour/colour_const1.xml");

        ColorConst colorConst = (ColorConst) transformer.getTree().get(0);

        Assert.assertEquals("#585858", colorConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/colour/colour_const1.xml");

        ColorConst colorConst = (ColorConst) transformer.getTree().get(0);

        Assert.assertEquals(999, colorConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/colour/colour_const1.xml");

        ColorConst colorConst = (ColorConst) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.NONE, colorConst.getAssoc());
    }
}

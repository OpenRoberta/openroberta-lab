package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;

public class StringConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[StringConst [text2]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/text/text_string_const.xml"));
    }

    @Test
    public void getValue() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals("text2", stringConst.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(999, stringConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer<Void> transformer = Helper.generateTransformer("/ast/text/text_string_const.xml");
        StringConst<Void> stringConst = (StringConst<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Assoc.NONE, stringConst.getAssoc());
    }
}

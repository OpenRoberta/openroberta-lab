package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class BoolConstTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[BoolConst [true]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/logic/logic_boolConst.xml"));
    }

    @Test
    public void isValue() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/logic/logic_boolConst.xml");

        BoolConst boolConst = (BoolConst) transformer.getTree().get(0);

        Assert.assertEquals(true, boolConst.isValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/logic/logic_boolConst.xml");

        BoolConst boolConst = (BoolConst) transformer.getTree().get(0);

        Assert.assertEquals(999, boolConst.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/logic/logic_boolConst.xml");

        BoolConst boolConst = (BoolConst) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.NONE, boolConst.getAssoc());
    }
}

package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.helper.Helper;

public class VariableTest {

    @Test
    public void variableSet() throws Exception {
        String a = "BlockAST [project=[[Var [item]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/variables/variable_set1.xml"));
    }

    @Test
    public void getValue() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/variables/variable_set1.xml");

        Var var = (Var) transformer.getTree().get(0);

        Assert.assertEquals("item", var.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/variables/variable_set1.xml");

        Var var = (Var) transformer.getTree().get(0);

        Assert.assertEquals(999, var.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/variables/variable_set1.xml");

        Var var = (Var) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.NONE, var.getAssoc());
    }
}

package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;

public class VariableTest {

    @Test
    public void variableSet() throws Exception {
        String a = "BlockAST [project=[[Var [item]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/variables/variable_set1.xml"));
    }

    @Test
    public void getValue() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0);
        Assert.assertEquals("item", var.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0);
        Assert.assertEquals(999, var.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0);
        Assert.assertEquals(Assoc.NONE, var.getAssoc());
    }
}

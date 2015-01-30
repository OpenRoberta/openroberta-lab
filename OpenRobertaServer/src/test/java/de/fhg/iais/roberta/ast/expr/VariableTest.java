package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;

public class VariableTest {

    @Test
    public void variableSet() throws Exception {
        String a = "BlockAST [project=[[Location [x=-23, y=-797], Var [item]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/variables/variable_set1.xml"));
    }

    @Test
    public void getValue() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals("item", var.getValue());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(999, var.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/variables/variable_set1.xml");
        Var<Void> var = (Var<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(Assoc.NONE, var.getAssoc());
    }

    @Ignore
    public void variableSet4() throws Exception {
        String a = "BlockAST [project=[[Location [x=-23, y=-797], Var [item]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/variables/variable_set4.xml"));
    }

    @Test
    public void reverseTransformatin() throws Exception {
        Helper.assertTransformationIsOk("/ast/variables/variable_set.xml");
    }

    @Test
    public void reverseTransformatin1() throws Exception {
        Helper.assertTransformationIsOk("/ast/variables/variable_set1.xml");
    }

    @Test
    public void reverseTransformatin2() throws Exception {
        Helper.assertTransformationIsOk("/ast/variables/variable_set2.xml");
    }

    @Test
    public void reverseTransformatin3() throws Exception {
        Helper.assertTransformationIsOk("/ast/variables/variable_set3.xml");
    }

}

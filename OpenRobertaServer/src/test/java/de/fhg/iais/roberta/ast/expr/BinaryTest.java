package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.dbc.DbcException;

public class BinaryTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Location [x=27, y=-575], Binary [ADD, NumConst [1], MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/math/math_arithmetic.xml"));
    }

    @Test
    public void getOp() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals(Binary.Op.ADD, binary.getOp());
    }

    @Test
    public void getLeft() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals("NumConst [1]", binary.getLeft().toString());
    }

    @Test
    public void getRight() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals("MathPowerFunct [POWER, [NumConst [5], NumConst [8]]]", binary.getRight().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals(100, binary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals(Assoc.LEFT, binary.getAssoc());
    }

    @Test
    public void getOpSymbol() throws Exception {
        JaxbBlocklyProgramTransformer<Void> transformer = Helper.generateTransformer("/ast/math/math_arithmetic.xml");
        Binary<Void> binary = (Binary<Void>) transformer.getTree().get(1);
        Assert.assertEquals("+", binary.getOp().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Binary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Binary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Binary.Op.get("asdf");
    }
}

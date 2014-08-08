package de.fhg.iais.roberta.ast.expr;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.Assoc;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.dbc.DbcException;
import de.fhg.iais.roberta.helper.Helper;

public class BinaryTest {

    @Test
    public void make() throws Exception {
        String a = "BlockAST [project=[[Binary [ADD, NumConst [1], Funct [POWER, [NumConst [5], NumConst [8]]]]]]]";

        Assert.assertEquals(a, Helper.generateASTString("/ast/math/math_arithmetic.xml"));
    }

    @Test
    public void getOp() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals(Binary.Op.ADD, binary.getOp());
    }

    @Test
    public void getLeft() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals("NumConst [1]", binary.getLeft().toString());
    }

    @Test
    public void getRight() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals("Funct [POWER, [NumConst [5], NumConst [8]]]", binary.getRight().toString());
    }

    @Test
    public void getPresedance() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals(100, binary.getPrecedence());
    }

    @Test
    public void getAssoc() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals(Assoc.LEFT, binary.getAssoc());
    }

    @Test
    public void getOpSymbol() throws Exception {
        JaxbTransformer transformer = Helper.generateAST("/ast/math/math_arithmetic.xml");

        Binary binary = (Binary) transformer.getTree().get(0);

        Assert.assertEquals("+", binary.getOp().getOpSymbol());
    }

    @Test(expected = DbcException.class)
    public void invalid() {
        Binary.Op op = Binary.Op.get("");
    }

    @Test(expected = DbcException.class)
    public void invalid1() {
        Binary.Op op = Binary.Op.get(null);
    }

    @Test(expected = DbcException.class)
    public void invalid2() {
        Binary.Op op = Binary.Op.get("asdf");
    }
}

package de.fhg.iais.roberta.codegen.lejos;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;

public class JavaVisitorTest {
    private static final BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();

    @Test
    public void getIndentaion() throws Exception {
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, 0);
        Assert.assertEquals(0, visitor.getIndentation());
    }

    // String code = AstToLejosJavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), true);
    @Test
    public void getSb() throws Exception {
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, 0);
        Assert.assertEquals("", visitor.getSb().toString());
    }

    @Test
    public void visitMathConst() throws Exception {
        MathConst mathConst = MathConst.make(Const.E);
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, 0);
        mathConst.visit(visitor);
        Assert.assertEquals(Const.E.toString(), visitor.getSb().toString());
    }

    @Test
    public void visitEmptyExpr() throws Exception {
        EmptyExpr emptyExpr = EmptyExpr.make(Double.class);
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, 0);
        emptyExpr.visit(visitor);
        Assert.assertEquals("[[EmptyExpr [defVal=class java.lang.Double]]]", visitor.getSb().toString());
    }
}

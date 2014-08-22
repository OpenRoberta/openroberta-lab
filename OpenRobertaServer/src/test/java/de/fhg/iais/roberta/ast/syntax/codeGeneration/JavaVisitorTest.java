package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.codegen.lejos.JavaVisitor;

public class JavaVisitorTest {

    @Test
    public void getIndentaion() throws Exception {
        StringBuilder sb = new StringBuilder();
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaVisitor visitor = new JavaVisitor(sb, 0, brickConfiguration);

        Assert.assertEquals(0, visitor.getIndentation());
    }

    @Test
    public void getSb() throws Exception {
        StringBuilder sb = new StringBuilder();
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaVisitor visitor = new JavaVisitor(sb, 0, brickConfiguration);

        Assert.assertEquals("", visitor.getSb().toString());
    }

    @Test
    public void visitMathConst() throws Exception {
        MathConst mathConst = MathConst.make(Const.E);
        StringBuilder sb = new StringBuilder();
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaVisitor visitor = new JavaVisitor(sb, 0, brickConfiguration);
        mathConst.accept(visitor);

        Assert.assertEquals(Const.E.toString(), visitor.getSb().toString());
    }

    @Test
    public void visitEmptyExpr() throws Exception {
        EmptyExpr emptyExpr = EmptyExpr.make(Double.class);
        StringBuilder sb = new StringBuilder();
        BrickConfiguration brickConfiguration = new BrickConfiguration.Builder().build();
        JavaVisitor visitor = new JavaVisitor(sb, 0, brickConfiguration);
        emptyExpr.accept(visitor);

        Assert.assertEquals("[[EmptyExpr [defVal=class java.lang.Double]]]", visitor.getSb().toString());
    }
}

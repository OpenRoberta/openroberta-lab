package de.fhg.iais.roberta.codegen.lejos;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.ev3.EV3BrickConfiguration;
import de.fhg.iais.roberta.ev3.EV3Sensors;

public class JavaVisitorTest {
    private static final EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder().build();
    private static final Set<EV3Sensors> usedSensors = new HashSet<EV3Sensors>();

    @Test
    public void getIndentaion() throws Exception {
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, usedSensors, 0);
        Assert.assertEquals(0, visitor.getIndentation());
    }

    // String code = AstToLejosJavaVisitor.generate("Test", brickConfiguration, transformer.getTree(), true);
    @Test
    public void getSb() throws Exception {
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, usedSensors, 0);
        Assert.assertEquals("", visitor.getSb().toString());
    }

    @Test
    public void visitMathConst() throws Exception {
        MathConst<Void> mathConst = MathConst.make(Const.E, null, null);
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, usedSensors, 0);
        mathConst.visit(visitor);
        Assert.assertEquals("Math.E", visitor.getSb().toString());
    }

    @Test
    public void visitEmptyExpr() throws Exception {
        EmptyExpr<Void> emptyExpr = EmptyExpr.make(Double.class);
        AstToLejosJavaVisitor visitor = new AstToLejosJavaVisitor("Test", brickConfiguration, usedSensors, 0);
        emptyExpr.visit(visitor);
        Assert.assertEquals("[[EmptyExpr [defVal=class java.lang.Double]]]", visitor.getSb().toString());
    }
}

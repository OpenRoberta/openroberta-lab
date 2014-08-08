package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst.Const;
import de.fhg.iais.roberta.codegen.lejos.JavaVisitor;
import de.fhg.iais.roberta.helper.Helper;

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

    @Test
    public void test3() throws Exception {

        String a =
            "import de.fhg.iais.roberta.codegen.lejos;\n"
                + "import de.fhg.iais.roberta.ast.syntax;\n\n"

                + "public class Test {\n"
                + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
                + "    .build();\n\n"

                + "    public static void main(String[] args) {\n"
                + "        Test.run();\n"
                + "    }\n\n"

                + "    public static void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        if ( hal.isPressed(S1) ) {\n"
                + "            hal.ledOn(GREEN, true);\n"
                + "        } else {\n"
                + "            if ( hal.isPressed(S1) ) {\n"
                + "                hal.ledOn(GREEN, true);\n"
                + "            } else if ( 0 == hal.getUltraSonicSensorValue(S4) ) {\n"
                + "                hal.drawPicture(\"SMILEY3\", 15, 15);\n"
                + "            } else {\n"
                + "                hal.setUltrasonicSensorMode(S4, DISTANCE);\n"
                + "                while ( !hal.isPressedAndReleased(UP) ) {\n\n"

                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, Helper.generateProgram("/syntax/code_generator/java_code_generator3.xml"));
    }
}

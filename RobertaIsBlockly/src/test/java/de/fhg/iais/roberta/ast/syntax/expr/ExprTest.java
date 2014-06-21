package de.fhg.iais.roberta.ast.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Binary.Op;

/**
 * tests absence of exceptions only :-)
 * 
 * @author rbudde
 */
public class ExprTest {

    @Test
    public void test() {
        NumConst n1 = NumConst.make("1");
        NumConst n2 = NumConst.make("2");
        NumConst n3 = NumConst.make("3");
        Binary add = Binary.make(Op.ADD, n1, n2);
        Binary mult1 = Binary.make(Op.MULTIPLY, add, n3);
        Binary mult2 = Binary.make(Op.MULTIPLY, add, add);
        generate(add);
        generate(mult1);
        generate(mult2);
    }

    private void generate(Phrase p) {
        StringBuilder sb = new StringBuilder();
        p.toStringBuilder(sb, 0);
        System.out.println(sb.toString());
    }

}
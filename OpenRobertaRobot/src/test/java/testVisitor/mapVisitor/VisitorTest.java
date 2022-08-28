package testVisitor.mapVisitor;

import org.junit.Test;

import testVisitor.ast.A;

public class VisitorTest {
    @Test
    public void testVoid() {
        System.out.println("Void");
        A av = new A();
        VoidVisitor xv = new VoidVisitor();
        av.accept(xv);
    }

    @Test
    public void testAcc() {
        System.out.println("Acc");
        A al = new A();
        AccVisitor xl = new AccVisitor();
        String generated = al.accept(xl);
        System.out.println(generated);
    }

}

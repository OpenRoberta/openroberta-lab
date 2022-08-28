package testVisitor;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public interface ITestVisitor<V> {
    V visitA(A a);

    V visitB(B b);

    V visitC(C c);
}

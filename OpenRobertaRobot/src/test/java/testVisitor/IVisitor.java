package testVisitor;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public interface IVisitor<V> {
    V visitA(A<V> a);

    V visitB(B<V> b);

    V visitC(C<V> c);
}

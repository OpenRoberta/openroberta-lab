package testVisitor.mapVisitor;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public class VoidVisitor implements AbstractVisitor<Void> {
    @Override
    public Void visitA(A<Void> a) {
        AbstractVisitor.super.visitAImpl(a);
        return null;
    }

    @Override
    public Void visitB(B<Void> b) {
        AbstractVisitor.super.visitBImpl(b);
        return null;
    }

    @Override
    public Void visitC(C<Void> c) {
        AbstractVisitor.super.visitCImpl(c);
        return null;
    }
}

package testVisitor.mapVisitor;

import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;

public class VoidVisitor implements AbstractVisitor<Void> {
    @Override
    public Void visitA(A a) {
        AbstractVisitor.super.visitAImpl(a);
        return null;
    }

    @Override
    public Void visitB(B b) {
        AbstractVisitor.super.visitBImpl(b);
        return null;
    }

    @Override
    public Void visitC(C c) {
        AbstractVisitor.super.visitCImpl(c);
        return null;
    }
}

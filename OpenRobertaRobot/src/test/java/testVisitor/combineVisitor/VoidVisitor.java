package testVisitor.combineVisitor;

import testVisitor.ITestVisitor;
import testVisitor.ast.A;
import testVisitor.ast.B;
import testVisitor.ast.C;
import testVisitor.ast.Phrase;

public class VoidVisitor implements ITestVisitor<Void>, IStructureRunner<Void> {
    @Override
    public Void visitA(A a) {
        return IStructureRunner.super.visitA(a, new VoidTraverser(), null);
    }

    @Override
    public Void visitB(B b) {
        return IStructureRunner.super.visitB(b, new VoidTraverser(), null);
    }

    @Override
    public Void visitC(C c) {
        return IStructureRunner.super.visitC(c, new VoidTraverser(), null);
    }

    private class VoidTraverser implements ITraverser<Void> {
        @Override
        public ITraverser<Void> step(Phrase p) {
            p.accept(VoidVisitor.this);
            return this;
        }

        @Override
        public Void yield(ICollector<Void> collector) {
            return null;
        }
    }
}

package testVisitor.ast;

import testVisitor.ITestVisitor;

public class B extends Phrase {
    public C c = new C();

    @Override
    public <V> V accept(ITestVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitB(this);
    }
}

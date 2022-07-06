package testVisitor.ast;

import testVisitor.ITestVisitor;

public class C extends Phrase {
    @Override
    public <V> V accept(ITestVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitC(this);
    }
}

package testVisitor.ast;

import testVisitor.ITestVisitor;

public class C<V> extends Phrase<V> {
    @Override
    public V accept(ITestVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitC(this);
    }
}

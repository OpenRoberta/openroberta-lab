package testVisitor.ast;

import testVisitor.IVisitor;

public class C<V> extends Phrase<V> {
    @Override
    public V accept(IVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitC(this);
    }
}

package testVisitor.ast;

import testVisitor.ITestVisitor;

public class B<V> extends Phrase<V> {
    public C<V> c = new C<>();

    @Override
    public V accept(ITestVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitB(this);
    }
}

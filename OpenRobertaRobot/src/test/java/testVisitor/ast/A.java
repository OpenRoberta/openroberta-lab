package testVisitor.ast;

import testVisitor.IVisitor;

public class A<V> extends Phrase<V> {
    public B<V> b = new B<>();
    public C<V> c = new C<>();
    @Override
    public V accept(IVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitA(this);
    }
}

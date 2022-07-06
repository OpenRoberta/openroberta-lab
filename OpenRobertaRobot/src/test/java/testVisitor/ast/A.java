package testVisitor.ast;

import testVisitor.ITestVisitor;

public class A extends Phrase {
    public B b = new B();
    public C c = new C();

    @Override
    public <V> V accept(ITestVisitor<V> x) {
        print("visitor " + x.getClass().getName());
        return x.visitA(this);
    }
}

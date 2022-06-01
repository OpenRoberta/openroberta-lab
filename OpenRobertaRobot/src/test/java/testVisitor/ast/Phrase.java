package testVisitor.ast;

import testVisitor.ITestVisitor;

public abstract class Phrase<V> {
    protected void print(String msg) {
        System.out.println(msg + " visiting " + this.getClass().getName());
    }

    public abstract V accept(ITestVisitor<V> x);
}

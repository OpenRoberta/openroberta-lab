package testVisitor.ast;

import testVisitor.IVisitor;

public abstract class Phrase<V> {
    protected void print(String msg) {
        System.out.println(msg + " visiting " + this.getClass().getName());
    }

    public abstract V accept(IVisitor<V> x);
}

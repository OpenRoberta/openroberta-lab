package testVisitor.ast;

import testVisitor.ITestVisitor;

public abstract class Phrase {
    protected void print(String msg) {
        System.out.println(msg + " visiting " + this.getClass().getName());
    }

    public abstract <V> V accept(ITestVisitor<V> x);
}
